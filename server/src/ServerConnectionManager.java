import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * ServerConnectionManager class
 */
public class ServerConnectionManager implements Runnable {

    private final int PORT_NUMBER = 10000;
    private ServerSocket socket;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ArrayList<Thread> clientThreads = new ArrayList<>();
    private volatile boolean keepServerAlive = true;

    @Override
    public void run() {
        try {
            socket = new ServerSocket(PORT_NUMBER);
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        acceptIncomingConnections();
    }

    private void acceptIncomingConnections() {
            Server.consolePrintLine("[*] Server is ready for connection requests");
        while (keepServerAlive) {
            try {
                clientHandlers.add(new ClientHandler(socket.accept()));
                clientThreads.add(new Thread(clientHandlers.get(clientHandlers.size() - 1)));
                clientThreads.get(clientThreads.size() - 1).start();
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
    }

    public boolean sendMessage(String client, Message message) {
        for (ClientHandler connectedClient : clientHandlers) {
            if (connectedClient.getConnectedUser().equals(client)) {
                Future send = Server.globalThreadPool.submit(new Sender(message, connectedClient.getSendStream()));
                return true;
            }
        }
        return false;
    }

    public void shutdown() {
        keepServerAlive = false;
        executeServerShutdown();
    }

    private void executeServerShutdown() {
        Server.consolePrintLine("[!] Initiating server shutdown");
        Server.consolePrintLine("[*] Sending terminating messages to all clients...");
        for (ClientHandler handler : clientHandlers) {
            handler.terminateUserConnection();
        }
        for (Thread thread : clientThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        Server.consolePrintLine("[*] Server is shutting down...");
        try {
            socket.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        Server.consolePrintLine("[*] Server has been shut down correctly");
    }
}
