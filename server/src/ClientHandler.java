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
    private String connectedUser;
    private final String serverID = "SERVER";

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
        Server.consolePrintLine("[*] A client has connected to the server");
        initUserConnection();
        test();
    }

    private void initUserConnection() {
        boolean isUsernameAccepted = false;
        while (!isUsernameAccepted) {
            Message newUsername = receive();
            Message confirmUsername;
            String newUser = newUsername.getMessageSender();
            assert newUser != null;
            if (UserManager.addUser(newUser)) {
                connectedUser = newUser;
                confirmUsername = new Message(MessageType.SESSION, serverID, connectedUser, "ACCEPTED");
                Server.consolePrintLine("[*] New client registered as: " + newUser);
                isUsernameAccepted = true;
            } else {
                confirmUsername = new Message(MessageType.SESSION, serverID, "REFUSED", "REFUSED");
            }
            send(confirmUsername);
        }
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
            Server.consolePrintLine("Received message from " + receivedMessage.getMessageSender() + " : " +
                    receivedMessage.getMessageContent());
            // Send message test
            send(new Message(MessageType.CHAT, this.serverID, "Server has received: " +
                    receivedMessage.getMessageContent()));
        }
    }
}
