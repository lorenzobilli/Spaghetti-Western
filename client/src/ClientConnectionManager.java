import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * ClientConnectionManager
 */
public class ClientConnectionManager implements Runnable {

    private final String HOSTNAME = "localhost";        //TODO: Handle this value with a proper user setting
    private final int PORT_NUM = 10000;     //TODO: Handle this value with a proper user setting
    private Socket socket;
    private PrintWriter sender;
    private BufferedReader receiver;
    private static BufferedReader userInput;    // Only for testing purposes

    @Override
    public void run() {
        System.out.println("[*] Launching client session...");
        try {
            socket = new Socket(HOSTNAME, PORT_NUM);
            sender = new PrintWriter(socket.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));       // Only for testing purposes
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] Successfully connected with the server");
        test();
        shutdownClient();
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
        boolean continueTesting = true;
        while (continueTesting) {
            try {
                String message = userInput.readLine();
                if (message.equals("exit") || message.equals("quit")) {
                    continueTesting = false;
                }
                sender.println(message);
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }
}
