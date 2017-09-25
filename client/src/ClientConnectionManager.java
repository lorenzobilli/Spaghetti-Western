import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * ClientConnectionManager
 */
public class ClientConnectionManager implements Runnable {

    private final String HOSTNAME = "localhost";
    private final int PORT_NUMBER = 10000;
    private Socket socket;
    private PrintWriter sendStream;
    private BufferedReader receiveStream;
    private SceneryManager sessionScenery;

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

    public synchronized PrintWriter getSendStream() {
        return sendStream;
    }

    public synchronized BufferedReader getReceiveStream() {
        return receiveStream;
    }

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
                Future send = Client.globalThreadPool.submit(new Sender(initCurrentSession, getSendStream()));
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
        Future send = Client.globalThreadPool.submit(new Sender(new Message(
                MessageType.TIME,
                Client.getPlayer(),
                MessageManager.createXML("header", "WAIT_START_REQUEST")
        ), getSendStream()));
        Client.clientWindow.createWaitingCountdown();
    }

    private void talkWithServer() {
        while (true) {
            try {
                // Wait for a message and pass it to the handler
                Future<Message> receive = Client.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Client.globalThreadPool.submit(new ClientEventHandler(receive.get()));
                // Retrieve generated message from handle and send it back
                Message message = handle.get();
                if (message != null) {
                    Future send = Client.globalThreadPool.submit(new Sender(message, getSendStream()));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        //shutdownClient();
    }

    public SceneryManager getSceneryManager() {
        return sessionScenery;
    }

    public void setScenery(Scenery scenery) {
        if (scenery == null) {
            throw new InvalidParameterException("Scenery cannot be null");
        }
        sessionScenery = new SceneryManager(scenery);
        Client.clientWindow.loadScenery(scenery);
    }

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
