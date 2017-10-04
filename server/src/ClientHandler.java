import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * ClientHandler class
 */
public class ClientHandler implements Runnable {

    private Socket connection;
    private PrintWriter sendStream;
    private BufferedReader receiveStream;
    private Player connectedPlayer;
    private Place currentPlayerPosition;
    private volatile boolean keepAlive = true;

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

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

    public synchronized Player getConnectedPlayer() {
        return connectedPlayer;
    }

    public Place getCurrentPlayerPosition() {
    	return currentPlayerPosition;
	}

	public void setCurrentPlayerPosition(Place currentPlayerPosition) {
    	if (currentPlayerPosition == null) {
    		throw new InvalidParameterException("Current player position cannot be null");
		}
		this.currentPlayerPosition = currentPlayerPosition;
	}

    public synchronized PrintWriter getSendStream() {
        return sendStream;
    }

    public synchronized BufferedReader getReceiveStream() {
        return receiveStream;
    }

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
                Future send = Server.globalThreadPool.submit(new Sender(message, sendStream));
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    private void serveUserConnection() {
        while (true) {
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
                    Future send = Server.globalThreadPool.submit(new Sender(message, getSendStream()));
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
                MessageType.SESSION,
                new Player("SERVER", Player.Team.SERVER),
                connectedPlayer,
                MessageManager.createXML("header", "DISCONNECTED")
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
