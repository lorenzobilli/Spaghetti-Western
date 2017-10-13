import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Client class
 */
public class Client {

	public final static String gameName = "Spaghetti Western";
	public static MainWindow clientWindow;
    public static ChatWindow chatWindow;
    public static MapWindow mapWindow;
    public static ClientConnectionManager connectionManager;
    public static Thread connectionThread;
    private static Player player;   //TODO: Handle refactoring of this field
    private static Place currentPosition;
    private static Scenery currentScenery;
    private static Map currentMap;
    private static int currentBullets = 0;

    public static ExecutorService globalThreadPool;

    public static void main(String[] args) {
        clientWindow = new MainWindow();
        connectionManager = new ClientConnectionManager();
        connectionThread = new Thread(connectionManager);
        globalThreadPool = Executors.newCachedThreadPool();
        startClient();
    }

    public static void startClient() {
        connectionThread.start();
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Client player cannot be null");
        }
        Client.player = player;
    }

    public static Place getCurrentPosition() {
    	return currentPosition;
	}

	public static void setCurrentPosition(Place position) {
    	if (position == null) {
    		throw new InvalidParameterException("Current position cannot be null");
		}
		currentPosition = position;
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

	public static Map getCurrentMap() {
    	return currentMap;
	}

	public static void setCurrentMap(Map map) {
    	if (map == null) {
    		throw new InvalidParameterException("Map cannot be null");
		}
		currentMap = map;
	}

	public static int getCurrentBullets() {
    	return currentBullets;
	}

	public static void setCurrentBullets(int bullets) {
    	currentBullets += bullets;
	}
}