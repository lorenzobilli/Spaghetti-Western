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

package server.connection;

import server.Server;
import shared.communication.Sender;
import shared.gaming.Player;
import shared.messaging.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class is responsible for all connection-related aspects of the server, such as:
 * - Accepting connections requests
 * - Managing connected clients
 * - Sending messages to single players
 * - Sending messages to whole teams
 * - Broadcasting messages to all connected clients
 */
public class ServerConnectionManager implements Runnable {

	/**
	 * Socket used by the server to accept incoming connections.
	 */
	private static ServerSocket socket;

	/**
	 * Handlers used by the server to manage connected clients.
	 */
	private static ArrayList<ConnectionHandler> connectionHandlers = new ArrayList<>();

	/**
	 * Threads used for connections with clients. Each client is associated with a separate thread. Each thread executes
	 * the corresponding server.connection.ConnectionHandler class.
	 */
	private static ArrayList<Thread> clientThreads = new ArrayList<>();

	static {
		try {
			socket = new ServerSocket(Server.PORT_NUMBER);
		} catch (IOException e) {
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the internal socket and start listening for incoming connections.
	 */
	@Override
	public void run() {
		acceptIncomingConnections();
	}

	/**
	 * Listens for new connections and accept them. Each new successfully connected client is managed with a dedicated
	 * thread and client handler.
	 */
	private void acceptIncomingConnections() {
			Server.consolePrintLine("Server is ready for connection requests");
		while (true) {
			try {
				connectionHandlers.add(new ConnectionHandler(socket.accept()));
				clientThreads.add(new Thread(connectionHandlers.get(connectionHandlers.size() - 1)));
				clientThreads.get(clientThreads.size() - 1).start();
			} catch (IOException e) {
				e.getMessage();
				e.getCause();
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get corresponding handler for a given player.
	 * @param player Player whose handler is wanted.
	 * @return Handler associated with the given player.
	 */
	public ConnectionHandler getPlayerHandler(Player player) {
		if (player == null) {
			throw new InvalidParameterException("A player is needed to obtain a handle");
		}
		for (ConnectionHandler handler : connectionHandlers) {
			if (handler.getConnectedPlayer().equals(player)) {
				return handler;
			}
		}
		throw new NoSuchElementException("Referenced player is not connected");
	}

	/**
	 * Send a message to a player.
	 * @param player Receiver of the message.
	 * @param message Message to be sent.
	 */
	public void sendMessageToPlayer(Player player, Message message) {
		if (player == null) {
			throw new InvalidParameterException("A player is needed to send a message");
		}
		if (message == null) {
			throw new InvalidParameterException("Cannot send a null message");
		}
		for (ConnectionHandler connectedPlayer : connectionHandlers) {
			if (connectedPlayer.getConnectedPlayer().getName().equals(player.getName())) {
				Server.globalThreadPool.submit(new Sender(message, connectedPlayer.getSendStream()));
			}
		}
	}

	/**
	 * Send a message to all team members of the given player
	 * @param player Player who is part of the team where message will be sent.
	 * @param message Message to be sent.
	 */
	public void sendMessageToTeamMembers(Player player, Message message) {
		if (player == null) {
			throw new InvalidParameterException("A player is needed to send a message");
		}
		if (message == null) {
			throw new InvalidParameterException("Cannot send a null message");
		}
		for (ConnectionHandler connectedPlayer : connectionHandlers) {
			if (connectedPlayer.getConnectedPlayer().getTeam().equals(player.getTeam())) {
				// Avoid sending message also to the original sender
				if (!connectedPlayer.getConnectedPlayer().getName().equals(player.getName())) {
					Server.globalThreadPool.submit(new Sender(message, connectedPlayer.getSendStream()));
				}
			}
		}
	}

	/**
	 * Send a message to all connected clients.
	 * @param message Message to be sent.
	 */
	public void broadcastMessage(Message message) {
		if (message == null) {
			throw new InvalidParameterException("Cannot send a null message");
		}
		for (ConnectionHandler connectedClient : connectionHandlers) {
			Server.globalThreadPool.submit(new Sender(message, connectedClient.getSendStream()));
		}
	}

	/**
	 * Resets all connections by killing connection threads and making sure that internal structures used to store
	 * connection threads and connection handlers are empty.
	 */
	public void resetAllConnections() {

		for (Thread connectionThread : clientThreads) {
			connectionThread.interrupt();
		}
		connectionHandlers.clear();
		clientThreads.clear();
	}
}
