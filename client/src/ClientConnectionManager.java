import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * ClientConnectionManager class
 */
public class ClientConnectionManager implements Runnable {

	/**
	 * Defines hostname on which the client will attempt to connect.
	 */
	private final String HOSTNAME = "localhost";

	/**
	 * Defines port number on which the client will attempt to connect.
	 */
    private final int PORT_NUMBER = 10000;

	/**
	 * Socket used by the client to establish connection with the server.
	 */
	private Socket socket;

	/**
	 * Stream used by the client to send messages.
	 */
    private PrintWriter sendStream;

	/**
	 * Stream used by the client to receive messages.
	 */
	private BufferedReader receiveStream;

	/**
	 * Initializes the internal socket and sending/receiving streams, then passes control to methods for actual
	 * connection initialization and enters in the "big loop" (listens for messages and sends responses accordingly).
	 * When the client exists from the loop, this method will initiate the shutdown routine.
	 */
    @Override
    public void run() {
        try {
            socket = new Socket(HOSTNAME, PORT_NUMBER);
            sendStream = new PrintWriter(socket.getOutputStream(), true);
            receiveStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        initUserConnection();
        talkWithServer();
        shutdownClient();
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
	 * @return Reference to the receiving stream.
	 */
	public synchronized BufferedReader getReceiveStream() {
        return receiveStream;
    }

	/**
	 * Initiate used connection with the server. An attempt is done until a username is accepted by the server.
	 * At the end of this function the client will be connected with the server with a unique username choosen by the
	 * user. If the client is the first to connect to the server, it will also cause the login timer to start.
	 */
	private void initUserConnection() {
        while (true) {
            // Generate the login dialog for username retrieving
            try {
                SwingUtilities.invokeAndWait(() -> Client.clientWindow.createLoginDialog());
            } catch (InterruptedException | InvocationTargetException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
            // In this phase we still have to compose a message without the handler
            Message initCurrentSession = new Message(
                    MessageType.SESSION,
                    Client.getPlayer(),
                    MessageManager.createXML("header", "SESSION_START_REQUEST")
            );
            try {
                // Send request message to the server
                Client.globalThreadPool.submit(new Sender(initCurrentSession, getSendStream()));
                Message response = null;
                boolean sessionResponse = false;
                do {
                    Future<Message> receive = Client.globalThreadPool.submit(new Receiver(getReceiveStream()));
                    response = receive.get();
                    if (response.getMessageType() == MessageType.SESSION) {     // Accept only session related messages
                        sessionResponse = true;
                    }
                } while (!sessionResponse);
                // Wait for response message and pass it to the handler
                Future<Message> handle = Client.globalThreadPool.submit(new ClientEventHandler(response));
                // Retrieve generated message from handler, check if server has accepted the chosen username
                Message message = handle.get();
                if (message == null) {
                    throw new RuntimeException("Invalid message received");
                }
                if (MessageManager.convertXML(
                        "header",
                        message.getMessageContent()).equals("ACCEPTED")) {
                    // Show success message dialog
                    JOptionPane.showMessageDialog(
                            null, "Successfully registered as: " + Client.getPlayer().getName(),
                            "Success!", JOptionPane.INFORMATION_MESSAGE
                    );
                    break;
                } else if (MessageManager.convertXML(
                        "header",
                        message.getMessageContent()).equals("ALREADY_CONNECTED")) {
                    // Show error message dialog
                    JOptionPane.showMessageDialog(
                            null, "The choosen username already exist.",
                            "Failed!", JOptionPane.ERROR_MESSAGE
                    );
                } else if (MessageManager.convertXML(
                        "header",
                        message.getMessageContent()).equals("MAX_NUM_REACHED")) {
                    // Show max number of users reached advice
                    JOptionPane.showMessageDialog(
                            null, "Max number of players reached. Please try again later.",
                            "Failed!", JOptionPane.ERROR_MESSAGE
                    );
                    System.exit(0);
                } else if (MessageManager.convertXML(
                        "header",
                        message.getMessageContent()).equals("SESSION_RUNNING")) {
                    // Show session running message dialog
                    JOptionPane.showMessageDialog(
                            null, "A play session is already running. Please try again later.",
                            "Failed!", JOptionPane.ERROR_MESSAGE
                    );
                    System.exit(0);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        Client.globalThreadPool.submit(new Sender(new Message(
                MessageType.TIME,
                Client.getPlayer(),
                MessageManager.createXML("header", "WAIT_START_REQUEST")
        ), getSendStream()));
        Client.clientWindow.createWaitingCountdown();
    }

	/**
	 * Listens for messages coming from the server synchronously. When a messages comes from the server, it passes it
	 * synchronously to an handler. Then the response message is retrieved synchronously from the handler and sended
	 * asynchronously to the server. Finally, the client enters the listening state again, and the loop repeats.
	 */
	private void talkWithServer() {
        while (true) {  //TODO: implement here loop exit for correct client shutdown
            try {
                // Wait for a message and pass it to the handler
                Future<Message> receive = Client.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Client.globalThreadPool.submit(new ClientEventHandler(receive.get()));
                // Retrieve generated message from handler and send it back
                Message message = handle.get();
                if (message != null) {
                    Client.globalThreadPool.submit(new Sender(message, getSendStream()));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

	/**
	 * Sends a message to the server to notify that the current client will disconnect from the system, then shuts down
	 * the socket manually if auto-closing system fails.
	 */
	private void shutdownClient() {
        System.out.println("[*] Terminating current client session...");
        Message terminateCurrentSession = new Message(
                MessageType.SESSION,
                Client.getPlayer(),
                MessageManager.createXML("header", "SESSION_STOP_REQUEST")
        );
        try {
            Future send = Client.globalThreadPool.submit(new Sender(terminateCurrentSession, getSendStream()));
            send.get();
        } catch (InterruptedException | ExecutionException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        if (!socket.isClosed()) {   // Never trust autocloseable objects :D
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        System.out.println("[*] Session terminated correctly");
    }
}
