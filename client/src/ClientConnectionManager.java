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
        test();
        shutdownClient();
    }

    private void initUserConnection() {
        while (true) {
            try {
                SwingUtilities.invokeAndWait(() -> Client.clientWindow.createLoginDialog());
            } catch (InterruptedException | InvocationTargetException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
            Message initCurrentSession = new Message(
                    MessageType.SESSION, Client.getUsername(), "Start session request"
            );
            Future send = executor.submit(new Sender(initCurrentSession, sendStream));
            try {
                send.get();     // We want this as an asynchronous call
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
            Future<Message> receive = executor.submit(new Receiver(receiveStream));
            try {
                Message confirmCurrentSession = receive.get();
                if (confirmCurrentSession.getMessageContent().equals("ACCEPTED")) {
                    JOptionPane.showMessageDialog(
                            null, "Successfully registered as: " + Client.getUsername(),
                            "Success!", JOptionPane.INFORMATION_MESSAGE
                    );
                    break;
                } else {
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

    private void shutdownClient() {
        System.out.println("[*] Terminating current client session...");
        try {
            socket.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] Session terminated correctly");
    }

    private void test() {

        while (true) {
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
            Message sendMessage = new Message(MessageType.CHAT, Client.getUsername(), input);
            // Send message test
            Future send = executor.submit(new Sender(sendMessage, sendStream));
            try {
                send.get();     // We want this as an asynchronous call
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
            // Receive message test
            Future<Message> receive = executor.submit(new Receiver(receiveStream));
            try {
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
}
