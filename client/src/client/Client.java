/*
 *  Project: "Spaghetti Western"
 *
 *
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2017-2018 Lorenzo Billi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *	documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *	rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *	permit persons to whom the Software is	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 *	the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *	WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *	OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *	OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package client;

import client.connection.ClientConnectionManager;
import client.gui.ChatWindow;
import client.gui.MainWindow;
import client.gui.MapWindow;
import client.gui.map.Map;
import shared.gaming.Player;
import shared.scenery.Place;
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
	 * Defines server address on which the client will attempt to connect.
	 */
	public static final String SERVER_ADDRESS = "localhost";

	/**
	 * Defines port number on which the client will attempt to connect.
	 */
	public static final int PORT_NUMBER = 10000;

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
	 * Global cached thread pool used by the client for most multithreaded operations.
	 */
	public static ExecutorService globalThreadPool;

	/**
	 * Main method of Client.
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
	 * @param scenery Scenery to be used for the current session.
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
	 * @param map Map to be used for the current session.
	 */
	public static void setMap(Map map) {
		if (map == null) {
			throw new InvalidParameterException("client.gui.map.Map cannot be null");
		}
		Client.map = map;
	}
}