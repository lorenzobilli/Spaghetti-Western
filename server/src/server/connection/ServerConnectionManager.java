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
	 * Defines port number on which the server will listen for incoming connections.
	 */
    private static final int PORT_NUMBER = 10000;

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
			socket = new ServerSocket(PORT_NUMBER);
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
	 * Listen for new connections and accept them. Each new successfully connected client is managed with a dedicated
	 * thread and client handler.
	 */
	private void acceptIncomingConnections() {
            Server.consolePrintLine("[*] server.Server is ready for connection requests");
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
	 * @param player shared.gaming.Player whose handler is wanted.
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
	 * @param player shared.communication.Receiver of the message.
	 * @param message shared.messaging.Message to be sent.
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
	 * @param player shared.gaming.Player who is part of the team where message will be sent.
	 * @param message shared.messaging.Message to be sent.
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
	 * @param message shared.messaging.Message to be sent.
	 */
	public void broadcastMessage(Message message) {
		if (message == null) {
			throw new InvalidParameterException("Cannot send a null message");
		}
        for (ConnectionHandler connectedClient : connectionHandlers) {
            Server.globalThreadPool.submit(new Sender(message, connectedClient.getSendStream()));
        }
    }

    public void resetAllConnections() {

		for (Thread connectionThread : clientThreads) {
			connectionThread.interrupt();
		}
		connectionHandlers.clear();
		clientThreads.clear();
    }
}
