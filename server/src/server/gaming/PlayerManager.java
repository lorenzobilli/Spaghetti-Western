package server.gaming;

import server.Server;
import shared.gaming.Player;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * This classes handles all player-related routines.
 */
public class PlayerManager {

	/**
	 * List of connected players.
	 */
	private static ArrayList<Player> connectedPlayers = new ArrayList<>();

	/**
	 * Represents all possible internal status for player operations:
	 *  - SUCCESS: Operation successfully.
	 *  - ALREADY_REGISTERED: A player with the same chosen name is already logged in the system.
	 *  - MAX_NUM_REACHED: Maximum number of concurrent connected players has been reached.
	 *  - SESSION_RUNNING: A gaming session is currently running.
	 */
    public enum Status {
        SUCCESS,
        ALREADY_REGISTERED,
        MAX_NUM_REACHED,
        SESSION_RUNNING
    }

	/**
	 * Adds a player to the system.
	 * @param player Player to be added.
	 * @return Status indicating the result of the operation.
	 */
	public static Status addPlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Invalid parameter given");
        }
        if (Server.sessionManager.getSessionState()) {
            return Status.SESSION_RUNNING;
        }
        if (connectedPlayers.isEmpty() || !isPlayerConnected(player)) {
            if (connectedPlayers.size() >= Server.MAX_PLAYERS) {
                return Status.MAX_NUM_REACHED;
            }
            connectedPlayers.add(player);
            Server.turnScheduler.addScheduledElement(player);
            return Status.SUCCESS;
        }
        return Status.ALREADY_REGISTERED;
    }

	/**
	 * Removes a player from the system.
	 * @param player Player to be removed.
	 * @return True if the used was connected and has been removed successfully, false if the user is not connected.
	 */
	public static boolean removePlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Invalid parameter given");
        }
        if (isPlayerConnected(player)) {
            connectedPlayers.remove(player);
            Server.turnScheduler.removeScheduledElement(player);
            return true;
        }
        return false;
    }

	/**
	 * Removes all players from the system.
	 */
	public static void removeAllPlayers() {
		connectedPlayers.clear();
    }

	/**
	 * Gets player with the given index in the connected player list.
	 * @param index Index of the player.
	 * @return Player corresponding to the given index in the list.
	 */
	public static Player getPlayer(int index) {
    	if (index < 0 || index > connectedPlayers.size()) {
    		throw new ArrayIndexOutOfBoundsException("Invalid index given");
		}
    	return connectedPlayers.get(index);
	}

	/**
	 * Checks if the given player is connected to the system.
	 * @param player Player to be checked.
	 * @return True if the player is connected to the system, false if not.
	 */
    private static boolean isPlayerConnected(Player player) {
        for (Player checkPlayer : connectedPlayers) {
            if (checkPlayer.equals(player)) {
                return true;
            }
        }
        return false;
    }

	/**
	 * Gets total number of connected players.
	 * @return Number of connected players.
	 */
	public static int getConnectedPlayersNumber() {
        return connectedPlayers.size();
    }
}
