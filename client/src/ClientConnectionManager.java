import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidParameterException;

/**
 * ClientConnectionManager
 */
public class ClientConnectionManager implements Runnable {

    private final String HOSTNAME = "localhost";        //TODO: Handle this value with a proper user setting
    private final int PORT_NUM = 10000;     //TODO: Handle this value with a proper user setting
    private Socket socket;
    private PrintWriter sender;
    private BufferedReader receiver;
    private BufferedReader userInput;    // Only for testing purposes

    @Override
    public void run() {
        System.out.println("[*] Launching client session...");
        try {
            socket = new Socket(HOSTNAME, PORT_NUM);
            sender = new PrintWriter(socket.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            userInput = new BufferedReader(new InputStreamReader(System.in));       // Only for testing purposes
            System.out.println("[*] Successfully connected with the server");
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        test();
        shutdownClient();
    }

    public void send(Message message) {
        String translatedMessage = MessageManager.prepareSend(message);
        if (translatedMessage == null) {
            throw new InvalidParameterException("Null message given");
        }
        sender.println(translatedMessage);
    }

    public Message receive() {
        String receivedMessage = "";
        try {
            receivedMessage = receiver.readLine();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        return MessageManager.prepareReceive(receivedMessage);
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
            Message sendMessage = new Message(input);
            // Send message test
            send(sendMessage);
            // Receive message test
            Message receivedMessage = receive();
            System.out.println(receivedMessage.getMessageContent());
        }
        shutdownClient();
    }
}
