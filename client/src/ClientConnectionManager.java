import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private BufferedReader userInput;    // Only for testing purposes

    private ExecutorService executor = Executors.newSingleThreadExecutor();     //TODO: Use a better threadpool

    @Override
    public void run() {
        System.out.println("[*] Launching client session...");
        try {
            socket = new Socket(HOSTNAME, PORT_NUMBER);
            sendStream = new PrintWriter(socket.getOutputStream(), true);
            receiveStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));       // Only for testing purposes
            System.out.println("[*] Successfully connected with the server");
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        initUserConnection();
        talkWithServer();
        shutdownClient();
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
                    MessageType.SESSION, Client.getUsername(), "Start session request"
            );
            try {
                // Send request message to the server
                Future send = executor.submit(new Sender(initCurrentSession, sendStream));
                // Wait for response message and pass it to the handler
                Future<Message> receive = executor.submit(new Receiver(receiveStream));
                Future<Message> handle = executor.submit(new ClientEventHandler(receive.get()));
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
    }

    private void talkWithServer() {
        while (true) {
            //NOTE: This first part is only for testing
            String input = "";
            // Getting user input
            try {
                input = userInput.readLine();
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
            if (input.equals("exit")) {
                break;
            }
            // We are still in command-line mode, compose a simple chat message to test the system
            Message chat = new Message(MessageType.CHAT, Client.getUsername(), "", input);
            // Send message and receive the answer from the server
            try {
                Future send = executor.submit(new Sender(chat, sendStream));
                Future<Message> receive = executor.submit(new Receiver(receiveStream));
                Message receivedMessage = receive.get();
                System.out.println(receivedMessage.getMessageContent());
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        shutdownClient();
    }

    private void shutdownClient() {
        System.out.println("[*] Terminating current client session...");
        Message terminateCurrentSession = new Message(
                MessageType.SESSION, Client.getUsername(), "Stop session request"
        );
        try {
            Future send = executor.submit(new Sender(terminateCurrentSession, sendStream));
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
