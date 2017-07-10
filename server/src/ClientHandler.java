import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ClientHandler class
 */
public class ClientHandler implements Runnable {

    private Socket connection;
    private PrintWriter sendStream;
    private BufferedReader receiveStream;
    private String connectedUser;
    private final String serverID = "SERVER";
    private volatile boolean keepAlive = true;

    private ExecutorService executor = Executors.newCachedThreadPool();

    public ClientHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            sendStream = new PrintWriter(connection.getOutputStream(), true);
            receiveStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        Server.consolePrintLine("[*] A client has connected to the server");
        initUserConnection();
        serveUserConnection();
    }

    private void _initUserConnection() {
        boolean isUsernameAccepted = false;
        while (!isUsernameAccepted) {
            Future<Message> receive = executor.submit(new Receiver(receiveStream));
            try {
                Message newUsername = receive.get();
                String newUser = newUsername.getMessageSender();
                assert newUser != null;
                Message confirmUsername;
                if (UserManager.addUser(newUser)) {
                    connectedUser = newUser;
                    confirmUsername = new Message(
                            MessageType.SESSION, serverID, connectedUser, "ACCEPTED"
                    );
                    Server.consolePrintLine("[*] New client registered as: " + newUser);
                    isUsernameAccepted = true;
                } else {
                    confirmUsername = new Message(
                            MessageType.SESSION, serverID, "", "REFUSED"
                    );
                }
                Future send = executor.submit(new Sender(confirmUsername, sendStream));
                try {
                    send.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.getMessage();
                    e.getCause();
                    e.printStackTrace();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    private void initUserConnection() {
        boolean isUsernameAccepted = false;
        while (!isUsernameAccepted) {
            Future<Message> receive = executor.submit(new Receiver(receiveStream));
            try {
                Message newUsername = receive.get();
                String newUser = newUsername.getMessageSender();
                Message confirmUsername;
                if (UserManager.addUser(newUser)) {
                    connectedUser = newUser;
                    confirmUsername = new Message(
                            MessageType.SESSION, serverID, connectedUser, "ACCEPTED"
                    );
                    Server.consolePrintLine("[*] New client registered as: " + newUser);
                    isUsernameAccepted = true;
                } else {
                    confirmUsername = new Message(
                            MessageType.SESSION, serverID, "", "REFUSED"
                    );
                }
                Future send = executor.submit(new Sender(confirmUsername, sendStream));
                try {
                    send.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.getMessage();
                    e.getCause();
                    e.printStackTrace();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    private void _serveUserConnection() {
        while (keepAlive) {
            // Receive message test
            Future<Message> receive = executor.submit(new Receiver(receiveStream));
            try {
                Message receivedMessage = receive.get();
                if (receivedMessage == null) {
                    break;
                }
                Server.consolePrintLine("Received message from " + receivedMessage.getMessageSender() + " : " +
                        receivedMessage.getMessageContent());
                // Send message test
                Future send = executor.submit(new Sender(new Message(MessageType.CHAT, this.serverID,
                        "Server has received: " + receivedMessage.getMessageContent()), sendStream));
                try {
                    send.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.getMessage();
                    e.getCause();
                    e.printStackTrace();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    private void serveUserConnection() {
        while (keepAlive) {
            // Wait for a message and pass it to the handler
            Future<Message> receive = executor.submit(new Receiver(receiveStream));
            try {
                Message receivedMessage = receive.get();
                if (receivedMessage == null) {
                    break;
                }
                Server.consolePrintLine("Message from " + receivedMessage.getMessageSender() + ": " +
                    receivedMessage.getMessageContent());
                Future<Boolean> handle = executor.submit(new ServerEventHandler(receivedMessage));
                try {
                    Boolean handleResult = handle.get();
                    if (!handleResult) {
                        Server.consolePrintLine("Server cannot handle a received message");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.getMessage();
                    e.getCause();
                    e.printStackTrace();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    public void terminateUserConnection() {
        Message notifyConnectionTerm = new Message(
                MessageType.SESSION, serverID, connectedUser, "DISCONNECTED"
        );
        Server.consolePrintLine("[*] Sending terminating message to: " + connectedUser);
            //TODO: Check if an async call is a better option...
        Future send = executor.submit(new Sender(notifyConnectionTerm, sendStream));
        keepAlive = false;
        try {
            connection.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
    }
}
