import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * ServerConnectionManager class
 */
public class ServerConnectionManager implements Runnable {

    private final int PORT_NUM = 10000;      //TODO: Handle this value with a proper user setting
    private ServerSocket socket;
    private volatile boolean keepAlive = true;
    private ArrayList<Thread> clients = new ArrayList<>();

    @Override
    public void run() {
        System.out.println("[*] Server is waiting for a client to connect...");
        try {
            socket = new ServerSocket(PORT_NUM);
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        while (keepAlive) {
            try {
                clients.add(new Thread(new ClientHandler(socket.accept())));
                for (Thread thread : clients) {     //TODO: Check if there is a better way to do this
                    if (!thread.isAlive()) {
                        thread.start();
                    }
                }
            } catch (IOException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        shutdownServer();
    }

    private void shutdownServer() {
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
