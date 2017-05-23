import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidParameterException;

/**
 * ClientHandler class
 */
public class ClientHandler implements Runnable {

    private Socket connection;
    private PrintWriter sender;
    private BufferedReader receiver;

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            sender = new PrintWriter(connection.getOutputStream(), true);
            receiver = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] A client has connected to the server");
        test();
    }

    public void send(String message) {
        if (message == null) {
            throw new InvalidParameterException("Null message given");
        }
        sender.println(message);    //TODO: Check if other methods are more suited for sending messages
    }

    public String receive() {
        String message = "";
        try {
            message = receiver.readLine();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        return message;
    }

    private void test() {
        while (true) {
            // Receive message test
            String receivedMessage = receive();
            if (receivedMessage == null) {
                break;
            }
            System.out.println("Received message: " + receivedMessage);
            // Send message test
            send("Server has received: " + receivedMessage);
        }
    }
}
