import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 */
public class Server {

    public final static int MAX_PLAYERS = 30;
    public final static Duration WAIT_TIME = Duration.ofMinutes(5);
    public final static Duration PLAY_TIME = Duration.ofMinutes(10);

    private static MainWindow serverWindow;
    public static ServerConnectionManager connectionManager;
    private static Thread connectionThread;
    public static ExecutorService globalThreadPool;
    public static TimeManager remainingWaitTime;
    public static TimeManager remainingPlayTime;

    public static void main(String[] args) {
        serverWindow = new MainWindow("Spaghetti Western server");
        connectionManager = new ServerConnectionManager();
        connectionThread = new Thread(connectionManager);
        globalThreadPool = Executors.newCachedThreadPool();
        remainingWaitTime = new TimeManager(WAIT_TIME);
        remainingPlayTime = new TimeManager(PLAY_TIME);
    }

    public static void consolePrint(String message) {
        serverWindow.appendText(message);
    }

    public static void consolePrintLine(String message) {
        serverWindow.appendText(message + "\n");
    }

    public static void startServer() {
        connectionThread.start();
    }

    public static void stopServer() {
        connectionManager.shutdown();
        try {
            connectionThread.join();
        } catch (InterruptedException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
    }
}
