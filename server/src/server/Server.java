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

package server;

import server.connection.ServerConnectionManager;
import server.gaming.PlayerManager;
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
	public static final String GAME_NAME = "Spaghetti Western";

	/**
	 * Defines port number on which the server will listen for incoming connections.
	 */
	public static final int PORT_NUMBER = 10000;

	/**
	 * Defines maximum number of concurrently connected players.
	 */
	public static final int MAX_PLAYERS = 30;

	/**
	 * Defines duration of waiting before starting a new play session during normal use.
	 * This value is intended to be used as minutes.
	 */
	public static final int WAIT_TIME = 5;

	/**
	 * Defines duration of waiting before starting a new play session during debug mode.
	 * This value is intended to be used as seconds.
	 */
	public static final int WAIT_TIME_DEBUG = 30;

	/**
	 * Defines duration of each play session. This value is intended to be used as minutes.
	 */
	public static final int PLAY_TIME = 1;

	/**
	 * Defines duration of each turn. This value is intended to be used as seconds.
	 * Please note that this value MUST assert the following statement: (PLAY_TIME * 60) % TURN_TIME == 0.
	 */
	public static final int TURN_TIME = 15;

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

	/**
	 * Handles all session-related routines.
	 */
	public static SessionManager sessionManager;

	/**
	 * Global object used to represent the ugly player at server level.
	 */
	public static Player uglyPlayer;

	/**
	 * Global cached thread pool used by the server for most multithreaded operations.
	 */
	public static ExecutorService globalThreadPool;

	/**
	 * Scheduler used to schedule players' turns.
	 */
	public static Scheduler turnScheduler;

	/**
	 * Stores the corresponding loaded scenery
	 */
	private static Scenery scenery;

	/**
	 * Stores the current number of bullets for the "good" team.
	 */
	private static int goodTeamBullets;

	/**
	 * Stores the current number of bullets for the "bad" team.
	 */
	private static int badTeamBullets;

	/**
	 * Main method of server.Server.
	 * Handles all internal objects initialization.
	 * @param args Main method's arguments. Not used here.
	 */
	public static void main(String[] args) {
		serverWindow = new MainWindow(GAME_NAME);
		globalThreadPool = Executors.newCachedThreadPool();

		consolePrintLine("Welcome to " + GAME_NAME + "!\n");

		configureServerObjects();
		configureConnection();

		startServer();
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

	private static void configureServerObjects() {
		consolePrint("Configuring server objects...");
		sessionManager = new SessionManager();
		turnScheduler = new RoundRobinScheduler();
		badTeamBullets = 0;
		goodTeamBullets = 0;
		consolePrintLine("...Done");
	}

	private static void configureConnection() {
		consolePrint("Creating new connection thread...");
		connectionManager = new ServerConnectionManager();
		connectionThread = new Thread(connectionManager);
		consolePrintLine("...Done.");
	}

	/**
	 * Starts the server by executing the internal connection thread.
	 */
	private static void startServer() {
		consolePrintLine("[!] Server is starting up...");
		connectionThread.start();
	}

	public static void resetServer() {
		serverWindow.clear();
		consolePrintLine("[!] Initiating server reset...");

		consolePrint("Killing all connections...");
		connectionManager.resetAllConnections();
		consolePrintLine("...Done.");
		consolePrint("Killing connection thread...");
		connectionThread.interrupt();
		consolePrintLine("...Done.");

		configureServerObjects();
		configureConnection();
		PlayerManager.removeAllPlayers();

		startServer();
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
	 * Sets a new amount of bullets for the "good" team.
	 * @param bullets New amount of bullets to be set.
	 */
	public static void setGoodTeamBullets(int bullets) {
		goodTeamBullets = bullets;
	}

	/**
	 * Get current number of bullets of the "bad" team.
	 * @return total number of bullets of the "bad" team.
	 */
	public static int getBadTeamBullets() {
		return badTeamBullets;
	}

	/**
	 * Sets a new amount of bullets for the "bad" team.
	 * @param bullets New amount of bullets to be set.
	 */
	public static void setBadTeamBullets(int bullets) {
		badTeamBullets = bullets;
	}
}
