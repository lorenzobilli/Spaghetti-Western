package client;

import client.gui.ChatWindow;
import client.gui.MainWindow;
import client.gui.map.Map;
import client.gui.MapWindow;
import shared.Place;
import shared.Player;
import shared.scenery.Scenery;

import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the main entry point for the client.Client.
 */
public class Client {

	/**
	 * Defines the name of the game.
	 */
	public static final String GAME_NAME = "Spaghetti Western";

	/**
	 * Main window used for waiting and login process.
	 */
	public static MainWindow clientWindow;

	/**
	 * Chat window for the current player.
	 */
    public static ChatWindow chatWindow;

	/**
	 * Main window used for the map exploration.
	 */
	public static MapWindow mapWindow;

	/**
	 * Internal thread used for handling connections with the server.
	 */
	private static Thread connectionThread;

	/**
	 * Handles all connection-related routines.
	 */
    public static ClientConnectionManager connectionManager;

	/**
	 * Represents the current player.
	 */
	private static Player player;

	/**
	 * Stores current player position in the scenery.
	 */
    private static Place position;

	/**
	 * Stores the corresponding loaded scenery.
	 */
	private static Scenery scenery;

	/**
	 * Stores the corresponding loaded map.
	 */
    private static Map map;

	/**
	 * Stores the current number of bullets for this player.
	 */
	private static int bullets = 0;

	/**
	 * Global cached thread pool used by the client for most multithreaded operations.
	 */
    public static ExecutorService globalThreadPool;

	/**
	 * Main method of client.Client.
	 * Handles all internal objects initialisation, then it starts the client.
	 * @param args Main method's arguments. Not used here.
	 */
	public static void main(String[] args) {
        clientWindow = new MainWindow();
        connectionManager = new ClientConnectionManager();
        connectionThread = new Thread(connectionManager);
        globalThreadPool = Executors.newCachedThreadPool();
        startClient();
    }

	/**
	 * Starts up the client by executing the internal connection thread.
	 */
	private static void startClient() {
        connectionThread.start();
    }

	/**
	 * Get the current player.
	 * @return Current player.
	 */
	public static Player getPlayer() {
        return player;
    }

	/**
	 * Set the current player.
	 * @param player Current player to be set.
	 */
	public static void setPlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("client.Client player cannot be null");
        }
        Client.player = player;
    }

	/**
	 * Get the current player position in the scenery.
	 * @return Current player position.
	 */
	public static Place getPosition() {
    	return position;
	}

	/**
	 * Set the current player position in the scenery.
	 * @param position Position to be set.
	 */
	public static void setPosition(Place position) {
    	if (position == null) {
    		throw new InvalidParameterException("Current position cannot be null");
		}
		Client.position = position;
	}

	/**
	 * Get the scenery used in the current session.
	 * @return Used scenery.
	 */
	public static Scenery getScenery() {
    	return scenery;
	}

	/**
	 * Set the scenery to be used for the current session.
	 * @param scenery shared.scenery.Scenery to be used for the current session.
	 */
	public static void setScenery(Scenery scenery) {
    	if (scenery == null) {
    		throw new InvalidParameterException("shared.scenery.Scenery cannot be null");
		}
		Client.scenery = scenery;
	}

	/**
	 * Get the current map object used.
	 * @return Current map used.
	 */
	public static Map getMap() {
    	return map;
	}

	/**
	 * Set the current map object to be used.
	 * @param map client.gui.map.Map to be used for the current session.
	 */
	public static void setMap(Map map) {
    	if (map == null) {
    		throw new InvalidParameterException("client.gui.map.Map cannot be null");
		}
		Client.map = map;
	}

	/**
	 * Get current number of bullets of the player.
	 * @return Total number of bullets.
	 */
	public static int getBullets() {
    	return bullets;
	}

	/**
	 * Add to an existing number of bullets a certain amount of new bullets.
	 * @param bullets Bullets to be added to the total bullets number.
	 */
	public static void setBullets(int bullets) {
    	Client.bullets += bullets;
	}
}