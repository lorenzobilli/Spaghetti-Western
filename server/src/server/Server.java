package server;

import server.connection.ServerConnectionManager;
import server.gaming.SessionManager;
import server.gui.MainWindow;
import server.scheduler.RoundRobinScheduler;
import server.scheduler.Scheduler;
import shared.gaming.Player;
import shared.scenery.Scenery;

import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the main entry point for the server.Server.
 */
public class Server {

	/**
	 * Defines the name of the game.
	 */
	public final String GAME_NAME = "Spaghetti Western";

	/**
	 * Defines maximum number of concurrently connected players.
	 */
    public final static int MAX_PLAYERS = 30;

	/**
	 * Main window used by the server.
	 */
	private static MainWindow serverWindow;

	/**
	 * Internal thread used for handling initial connections with the clients.
	 */
	private static Thread connectionThread;

	/**
	 * Handles all connection-related routines.
	 */
    public static ServerConnectionManager connectionManager;

    public static SessionManager sessionManager;

    public static Player uglyPlayer;

	/**
	 * Global cached thread pool used by the server for most multithreaded operations.
	 */
	public static ExecutorService globalThreadPool;

    public static Scheduler turnScheduler;

	/**
	 * Stores the corresponding loaded scenery
	 */
	private static Scenery scenery;

	/**
	 * Stores the current number of bullets for the "good" team.
	 */
    private static int goodTeamBullets = 0;

	/**
	 * Stores the current number of bullets for the "bad" team.
	 */
	private static int badTeamBullets = 0;

	/**
	 * Main method of server.Server.
	 * Handles all internal objects initialization.
	 * @param args Main method's arguments. Not used here.
	 */
    public static void main(String[] args) {
        serverWindow = new MainWindow("Spaghetti Western server");
        connectionManager = new ServerConnectionManager();
        sessionManager = new SessionManager();
        turnScheduler = new RoundRobinScheduler();
        connectionThread = new Thread(connectionManager);
        globalThreadPool = Executors.newCachedThreadPool();
    }

	/**
	 * Prints a message on the server's console.
	 * @param message message to be written in the console.
	 */
	public static void consolePrint(String message) {
        serverWindow.appendText(message);
    }

	/**
	 * Prints a message on the server's console, then adds a newline.
	 * @param message message to be written in the console.
	 */
	public static void consolePrintLine(String message) {
        serverWindow.appendText(message + "\n");
    }

	/**
	 * Starts the server by executing the internal connection thread.
	 */
	public static void startServer() {
        connectionThread.start();
    }

	/**
	 * Stop the server by calling shutdown() on the internal ConnectionManager
	 */
	//TODO: Implement correct server shutdown.
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

	/**
	 * Get the scenery used in the current session.
	 * @return the used scenery.
	 */
	public static Scenery getScenery() {
		return scenery;
	}

	/**
	 * Set the scenery to be used for the current session.
	 * @param scenery scenery to be used for the current session.
	 */
	public static void setScenery(Scenery scenery) {
    	if (scenery == null) {
    		throw new InvalidParameterException("shared.scenery.Scenery cannot be null");
		}
		Server.scenery = scenery;
	}

	/**
	 * Get current number of bullets of the "good" team.
	 * @return total number of bullets of the "good" team.
	 */
	public static int getGoodTeamBullets() {
    	return goodTeamBullets;
	}

	/**
	 * Add to an existing number of bullets for the "good" team a certain amount of new bullets.
	 * @param bullets bullets to be added to the total "good" team bullets number.
	 */
	public static void setGoodTeamBullets(int bullets) {
    	goodTeamBullets += bullets;
	}

	/**
	 * Get current number of bullets of the "bad" team.
	 * @return total number of bullets of the "bad" team.
	 */
	public static int getBadTeamBullets() {
    	return badTeamBullets;
	}

	/**
	 * Add to an existing number of bullets for the "bad" team a certain amount of new bullets.
	 * @param bullets bullets to be added to the total "bad" team bullets number.
	 */
	public static void setBadTeamBullets(int bullets) {
    	badTeamBullets += bullets;
	}
}
