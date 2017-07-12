import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Client class
 */
public class Client {

    public static MainWindow clientWindow;
    public static ChatWindow chatWindow;
    public static ClientConnectionManager connectionManager;
    public static Thread connectionThread;
    private static String username;

    public static ExecutorService globalExecutor;   //FIXME: Find a better solution than this

    public static void main(String[] args) {
        clientWindow = new MainWindow("Spaghetti Western");
        connectionManager = new ClientConnectionManager();
        connectionThread = new Thread(connectionManager);
        globalExecutor = Executors.newCachedThreadPool();
        startClient();
    }

    public static void startClient() {
        connectionThread.start();
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        if (username == null) {
            throw new InvalidParameterException("Client username cannot be null");
        }
        Client.username = username;
    }
}
