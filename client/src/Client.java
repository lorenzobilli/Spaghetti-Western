import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Client class
 */
public class Client {

    public final static Duration waitTime = Duration.ofMinutes(5);
    public final static Duration playTime = Duration.ofMinutes(10);

    public static MainWindow clientWindow;
    public static ChatWindow chatWindow;
    public static ClientConnectionManager connectionManager;
    public static Thread connectionThread;
    private static String username;
    public static TimeManager remainingWaitTime;
    public static TimeManager remainingPlayTime;

    public static ExecutorService globalThreadPool;

    public static void main(String[] args) {
        clientWindow = new MainWindow("Spaghetti Western");
        connectionManager = new ClientConnectionManager();
        connectionThread = new Thread(connectionManager);
        globalThreadPool = Executors.newCachedThreadPool();
        remainingWaitTime = new TimeManager(waitTime);
        remainingPlayTime = new TimeManager(playTime);
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
