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

    private void test() {
        while (true) {
            // Receive message test
            Message receivedMessage = receive();
            if (receivedMessage == null) {
                break;
            }
            System.out.println("Received message: " + receivedMessage.getMessageContent());
            // Send message test
            send(new Message("Server has received: " + receivedMessage.getMessageContent()));
        }
    }
}
