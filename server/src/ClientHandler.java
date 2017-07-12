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

    private void initUserConnection() {
        boolean isUsernameAccepted = false;
        while (!isUsernameAccepted) {
            try {
                // Wait for first message from client and pass it to the handler
                Future<Message> receive = executor.submit(new Receiver(receiveStream));
                Future<Message> handle = executor.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, check if username has been accepted and send it back
                Message message = handle.get();
                if (message.getMessageContent().equals("ACCEPTED")) {
                    connectedUser = message.getMessageReceiver();
                    Server.consolePrintLine("[*] New client registered as: " + connectedUser);
                    isUsernameAccepted = true;
                }
                Future send = executor.submit(new Sender(message, sendStream));
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    private void serveUserConnection() {
        while (true) {
            try {
                // Wait for a message and pass it to the handler
                Future<Message> receive = executor.submit(new Receiver(receiveStream));
                Future<Message> handle = executor.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, print it on the server console and send it back
                Message message = handle.get();
                if (!message.getMessageContent().equals("SHUTDOWN")) {
                    Server.consolePrintLine("Message from " + message.getMessageSender() + ": " +
                            message.getMessageContent());
                    Future send = executor.submit(new Sender(message, sendStream));
                } else {
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        shutdownConnection();
    }

    private void shutdownConnection() {
        Server.consolePrintLine("[*] Shutting down connection with " + connectedUser + "...");
        try {
            connection.shutdownInput();
            connection.shutdownOutput();
            connection.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        Server.consolePrintLine("[*] Connection with " + connectedUser + " closed");
    }

    //TODO: Implement this the proper way
    public void terminateUserConnection() {
        Message notifyConnectionTerm = new Message(
                MessageType.SESSION, "SERVER", connectedUser, "DISCONNECTED"
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
