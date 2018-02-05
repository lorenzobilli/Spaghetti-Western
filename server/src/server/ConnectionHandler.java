package server;

import shared.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Handle communication routines with each client.
 */
public class ConnectionHandler implements Runnable {

	/**
	 * Internal socket used for the connection with the client
	 */
	private Socket connection;

	/**
	 * Stream used by the server to send messages.
	 */
    private PrintWriter sendStream;

	/**
	 * Stream used by the server to receive messages.
	 */
	private BufferedReader receiveStream;

	/**
	 * Stores a copy of the connected player.
	 */
    private Player connectedPlayer;

	/**
	 * Stores status of the connection
	 */
	private volatile boolean keepAlive = true;

	/**
	 * Creates new server.ConnectionHandler by assigning the internal socket to another initialized socket.
	 * @param connection Socket that will be used for the connection. An active client must be already assigned to this
	 *                   socket prior run() call.
	 */
    public ConnectionHandler(Socket connection) {
        this.connection = connection;
    }

	/**
	 * Initializes the internal sending/receiving streams, then passes control to methods for actual connection
	 * initialization and enters in the "big loop" (listens for messages and sends responses accordingly).
	 */
	@Override
    public void run() {
        try {
            sendStream = new PrintWriter(connection.getOutputStream(), true);
            receiveStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        Server.consolePrintLine("[*] A client has connected to the server");
        initUserConnection();
        serveUserConnection();
    }

	/**
	 * Gets the current connected player in thread-safe manner.
	 * @return Reference to the connected player.
	 */
	public synchronized Player getConnectedPlayer() {
        return connectedPlayer;
    }

	/**
	 * Gets the sending stream in a thread-safe manner.
	 * @return Reference to the send stream.
	 */
	public synchronized PrintWriter getSendStream() {
        return sendStream;
    }

	/**
	 * Gets the receiving stream in a thread-safe manner.
	 * @return Reference to the receive stream.
	 */
	public synchronized BufferedReader getReceiveStream() {
        return receiveStream;
    }

	/**
	 * Initiate server connection with the user. An attempt is done until a valid username is inserted by the client.
	 * Connection is refused if a client is already connected with the same choosen username or if the maximum client
	 * number has been reached. At the end of this method the server will be connected with the client.
	 * If the client is the first to connect to this server, it will also cause the login timer to start.
	 */
	private void initUserConnection() {
        boolean isUsernameAccepted = false;
        while (!isUsernameAccepted) {
            try {
                // Wait for first message from client and pass it to the handler
                Future<Message> receive = Server.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Server.globalThreadPool.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, check if username has been accepted and send it back
                Message message = handle.get();
                if (MessageManager.convertXML("header", message.getMessageContent()).equals("ACCEPTED")) {
                    connectedPlayer = message.getMessageReceiver();
                    Server.consolePrintLine("[*] New client registered as: " + connectedPlayer.getName());
                    isUsernameAccepted = true;
                } else {
                    Server.consolePrint("[!] Attempt to log in from client " + message.getMessageReceiver().getName() +
                    " has been refused: ");
                    if (MessageManager.convertXML(
                            "header",
                            message.getMessageContent()).equals("ALREADY_CONNECTED")) {
                        Server.consolePrintLine("username already in use");
                    } else if (MessageManager.convertXML(
                            "header",
                            message.getMessageContent()).equals("MAX_NUM_REACHED")) {
                        Server.consolePrintLine("max number of concurrent users reached");
                    }
                }
                Server.globalThreadPool.submit(new Sender(message, sendStream));
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

	/**
	 * Listens for messages coming from the client synchronously. When a message comes from the client, it passes it
	 * synchronously to an handler. Then the response message is retrieved synchronously from the handler and sent
	 * asynchronously to the client. Finally, the server enters the listening state again, and the loop repeats.
	 */
	private void serveUserConnection() {
        while (true) {  //TODO: implement here loop exit for correct connection shutdown
            try {
                // Wait for a message and pass it to the handler
                Future<Message> receive = Server.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Server.globalThreadPool.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, print it on the server console and send it back
                Message message = handle.get();
                if (message != null) {
                    if (MessageManager.convertXML("header", message.getMessageContent()).equals("SHUTDOWN")) {
                        break;
                    }
                    Server.globalThreadPool.submit(new Sender(message, getSendStream()));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        shutdownConnection();
    }

    private void shutdownConnection() {
        Server.consolePrintLine("[*] Shutting down connection with " + connectedPlayer.getName() + "...");
        try {
            connection.shutdownInput();
            connection.shutdownOutput();
            connection.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        Server.consolePrintLine("[*] Connection with " + connectedPlayer.getName() + " closed");
    }

    //TODO: Implement this the proper way
    public void terminateUserConnection() {
        Message notifyConnectionTerm = new Message(
                Message.Type.SESSION,
                new Player("SERVER", Player.Team.SERVER),
                connectedPlayer,
		        MessageManager.createXML(new MessageTable("header", "DISCONNECTED"))
        );
        Server.consolePrintLine("[*] Sending terminating message to: " + connectedPlayer.getName());
            //TODO: Check if an async call is a better option...
        Future send = Server.globalThreadPool.submit(new Sender(notifyConnectionTerm, getSendStream()));
        keepAlive = false;
        try {
            connection.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
    }
}
