import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
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

    public synchronized String getConnectedUser() {
        return connectedUser;
    }

    public synchronized PrintWriter getSendStream() {
        return sendStream;
    }

    public synchronized BufferedReader getReceiveStream() {
        return receiveStream;
    }

    private void initUserConnection() {
        boolean isUsernameAccepted = false;
        while (!isUsernameAccepted) {
            try {
                // Wait for first message from client and pass it to the handler
                Future<Message> receive = Server.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Server.globalThreadPool.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, check if username has been accepted and send it back
                Message message = handle.get();
                if (MessageManager.convertXML("header", message.getMessageContent()).equals("ACCEPTED")) {
                    connectedUser = message.getMessageReceiver();
                    Server.consolePrintLine("[*] New client registered as: " + connectedUser);
                    isUsernameAccepted = true;
                } else {
                    Server.consolePrint("[!] Attempt to log in from client " + message.getMessageReceiver() +
                    " has been refused: ");
                    if (MessageManager.convertXML(
                            "header",
                            message.getMessageContent()).equals("ALREADY_CONNECTED")) {
                        Server.consolePrintLine("username already in use");
                    } else if (MessageManager.convertXML(
                            "header",
                            message.getMessageContent()).equals("MAX_NUM_REACHED")) {
                        Server.consolePrintLine("max number of concurrent users reached");
                    }
                }
                Future send = Server.globalThreadPool.submit(new Sender(message, sendStream));
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
                Future<Message> receive = Server.globalThreadPool.submit(new Receiver(getReceiveStream()));
                Future<Message> handle = Server.globalThreadPool.submit(new ServerEventHandler(receive.get()));
                // Retrieve generated message from handle, print it on the server console and send it back
                Message message = handle.get();
                if (message != null) {
                    if (MessageManager.convertXML("header", message.getMessageContent()).equals("SHUTDOWN")) {
                        break;
                    }
                    Future send = Server.globalThreadPool.submit(new Sender(message, getSendStream()));
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
                MessageType.SESSION,
                "SERVER",
                connectedUser,
                MessageManager.createXML("header", "DISCONNECTED")
        );
        Server.consolePrintLine("[*] Sending terminating message to: " + connectedUser);
            //TODO: Check if an async call is a better option...
        Future send = Server.globalThreadPool.submit(new Sender(notifyConnectionTerm, getSendStream()));
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
