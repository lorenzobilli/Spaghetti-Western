import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 */
public class Server {

    private static MainWindow serverWindow;
    public static ServerConnectionManager connectionManager;
    private static Thread connectionThread;

    public static ExecutorService globalThreadPool;

    public static void main(String[] args) {
        serverWindow = new MainWindow("Spaghetti Western server");
        connectionManager = new ServerConnectionManager();
        connectionThread = new Thread(connectionManager);
        globalThreadPool = Executors.newCachedThreadPool();
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
