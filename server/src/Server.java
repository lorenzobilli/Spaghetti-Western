import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server class
 */
public class Server {

    private static final int PORT_NUM = 10000;      //TODO: Handle this value with a proper user setting
    private static ServerSocket socket;
    private static volatile boolean keepAlive = true;       //TODO: Handle server shutdown with a GUI command

    public static void main(String[] args) {
        System.out.println("[*] Server is starting up...");
        System.out.println("[*] Server is waiting for a client to connect...");
        try {
            socket = new ServerSocket(PORT_NUM);
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        while (keepAlive) {
            ConnectionManager connection;
            try {
                connection = new ConnectionManager(socket.accept());
                Thread connectionThread = new Thread(connection);
                connectionThread.start();
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        shutdownServer();
    }

    private static void shutdownServer() {
        System.out.println("[*] Server is shutting down...");
        try {
            socket.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        System.out.println("[*] Server has been shut down correctly");
    }
}
