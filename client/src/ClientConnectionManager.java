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
 * ClientConnectionManager
 */
public class ClientConnectionManager implements Runnable {

    private final String HOSTNAME = "localhost";
    private final int PORT_NUMBER = 10000;
    private Socket socket;
    private PrintWriter sendStream;
    private BufferedReader receiveStream;

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
                    Client.getUsername(),
                    MessageManager.createXML("header", "SESSION_START_REQUEST")
            );
            try {
                // Send request message to the server
                Future send = Client.globalThreadPool.submit(new Sender(initCurrentSession, getSendStream()));
                // Wait for response message and pass it to the handler
                Future<Message> receive = Client.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Client.globalThreadPool.submit(new ClientEventHandler(receive.get()));
                // Retrieve generated message from handler, check if server has accepted the choosen username
                Message message = handle.get();
                if (message != null) {
                    // Show success message dialog
                    JOptionPane.showMessageDialog(
                            null, "Successfully registered as: " + Client.getUsername(),
                            "Success!", JOptionPane.INFORMATION_MESSAGE
                    );
                    break;
                } else {
                    // Show error message dialog
                    JOptionPane.showMessageDialog(
                            null, "The choosen username already exist.",
                            "Failed!", JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        Client.chatWindow = new ChatWindow();   // Spawning chat window
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

    private void shutdownClient() {
        System.out.println("[*] Terminating current client session...");
        Message terminateCurrentSession = new Message(
                MessageType.SESSION,
                Client.getUsername(),
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
