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
    private final int PORT_NUMBER = 10000;

	/**
	 * Socket used by the server to accept incoming connections.
	 */
	private ServerSocket socket;

	/**
	 * Handlers used by the server to manage connected clients.
	 */
    private ArrayList<ConnectionHandler> connectionHandlers = new ArrayList<>();

	/**
	 * Threads used for connections with clients. Each client is associated with a separate thread. Each thread executes
	 * the corresponding ConnectionHandler class.
	 */
	private ArrayList<Thread> clientThreads = new ArrayList<>();

	/**
	 * Flag used to shutdown the connection
	 */
    private volatile boolean keepServerAlive = true;    //TODO: Reimplement shutdown procedure

	/**
	 * Initializes the internal socket and start listening for incoming connections.
	 */
    @Override
    public void run() {
        try {
            socket = new ServerSocket(PORT_NUMBER);
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        acceptIncomingConnections();
    }

	/**
	 * Listen for new connections and accept them. Each new successfully connected client is managed with a dedicated
	 * thread and client handler.
	 */
	private void acceptIncomingConnections() {
            Server.consolePrintLine("[*] Server is ready for connection requests");
        while (keepServerAlive) {
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
	 * Shuts down the server.
	 */
    public void shutdown() {
        keepServerAlive = false;
        executeServerShutdown();
    }

	/**
	 * Sends a notification to all connected clients about server shutting down procedure and disconnects them, finally
	 * it closes the socket and shuts down the server.
	 */
	private void executeServerShutdown() {
        Server.consolePrintLine("[!] Initiating server shutdown");
        Server.consolePrintLine("[*] Sending terminating messages to all clients...");
        for (ConnectionHandler handler : connectionHandlers) {
            handler.terminateUserConnection();
        }
        for (Thread thread : clientThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.getMessage();
                e.getCause();
                e.printStackTrace();
            }
        }
        Server.consolePrintLine("[*] Server is shutting down...");
        try {
            socket.close();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        Server.consolePrintLine("[*] Server has been shut down correctly");
    }
}
