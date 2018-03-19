package server.connection;

import server.Server;
import server.handle.ServerEventHandler;
import shared.communication.Receiver;
import shared.communication.Sender;
import shared.gaming.Player;
import shared.messaging.Message;
import shared.messaging.MessageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidParameterException;
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
	 * Creates new server.connection.ConnectionHandler by assigning the internal socket to another initialized socket.
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

        talkWithClient();
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

    private boolean checkConnectionResponse(Message message) {
		if (message == null) {
			throw new InvalidParameterException("Response message cannot be null");
		}

		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "ACCEPTED":
				connectedPlayer = message.getMessageReceiver();
				Server.consolePrintLine("[*] New client registered as: " + connectedPlayer.getName());
				Server.globalThreadPool.submit(new Sender(message, sendStream));
				return true;
			case "ALREADY_CONNECTED":
				Server.consolePrintLine("[!] Attempt to login from client " + message.getMessageReceiver().getName() +
						" has been refused: username already in use");
				Server.globalThreadPool.submit(new Sender(message, sendStream));
				return false;
			case "MAX_NUM_REACHED":
				Server.consolePrintLine("[!] Attempt to login from client " + message.getMessageReceiver().getName() +
						" has been refused: maximum number of concurrent users reached.");
				Server.globalThreadPool.submit(new Sender(message, sendStream));
				return false;
			case "SESSION_RUNNING":
				Server.consolePrintLine("[!] Attempt to login from client " + message.getMessageReceiver().getName() +
						" has been refused: a play session is already running.");
				Server.globalThreadPool.submit(new Sender(message, sendStream));
			default:
				throw new InvalidParameterException("Unknown message given");
		}
    }

	/**
	 * Initiate server connection with the user. An attempt is done until a valid username is inserted by the client.
	 * Connection is refused if a client is already connected with the same choosen username or if the maximum client
	 * number has been reached. At the end of this method the server will be connected with the client.
	 * If the client is the first to connect to this server, it will also cause the login timer to start.
	 */
	private void initUserConnection() {
        while (true) {
            try {
                // Wait for first message from client and pass it to the handler
                Future<Message> receive = Server.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Server.globalThreadPool.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, check if username has been accepted and send it back
	            if (checkConnectionResponse(handle.get())) {
	            	break;
	            }
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
	private void talkWithClient() {
        while (true) {
            try {
                // Wait for a message and pass it to the handler
                Future<Message> receive = Server.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Server.globalThreadPool.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, print it on the server console and send it back
                Message message = handle.get();
                if (message != null) {
                    Server.globalThreadPool.submit(new Sender(message, getSendStream()));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }
}
