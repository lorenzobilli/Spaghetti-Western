import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 */
public class Server {

    public final static int MAX_PLAYERS = 30;

    private static MainWindow serverWindow;
    public static ServerConnectionManager connectionManager;
    private static Thread connectionThread;
    public static ExecutorService globalThreadPool;
    public static TimeManager timeManager;
    private static Scenery currentScenery;
    private static int goodTeamBullets = 0;
    private static int badTeamBullets = 0;

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

	public static Scenery getCurrentScenery() {
		return currentScenery;
	}

	public static void setCurrentScenery(Scenery scenery) {
    	if (scenery == null) {
    		throw new InvalidParameterException("Scenery cannot be null");
		}
		currentScenery = scenery;
	}

	public static int getGoodTeamBullets() {
    	return goodTeamBullets;
	}

	public static void setGoodTeamBullets(int bullets) {
    	goodTeamBullets += bullets;
	}

	public static int getBadTeamBullets() {
    	return badTeamBullets;
	}

	public static void setBadTeamBullets(int bullets) {
    	badTeamBullets += bullets;
	}
}
