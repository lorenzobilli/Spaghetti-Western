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
    public static Resolution clientResolution;
    public static ClientConnectionManager connectionManager;
    public static Thread connectionThread;
    private static Player player;   //TODO: Handle refactoring of this field
    private static Place currentPosition;
    private static Scenery currentScenery;

    public static ExecutorService globalThreadPool;

    public static void main(String[] args) {
        clientResolution = Resolution.HD;	//TODO: Consider a user setting for this value
        clientWindow = new MainWindow("Spaghetti Western");
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
}