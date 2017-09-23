import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * PlayerManager class
 */
public class PlayerManager {

    private static ArrayList<Player> connectedPlayers = new ArrayList<>();

    public enum Status {
        SUCCESS,
        ALREADY_REGISTERED,
        MAX_NUM_REACHED,
        SESSION_RUNNING
    }

    public static Status addPlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Invalid parameter given");
        }
        if (Server.connectionManager.isSessionRunning()) {
            return Status.SESSION_RUNNING;
        }
        if (connectedPlayers.isEmpty() || !isUserConnected(player)) {
            if (connectedPlayers.size() >= Server.MAX_PLAYERS) {
                return Status.MAX_NUM_REACHED;
            }
            connectedPlayers.add(player);
            return Status.SUCCESS;
        }
        return Status.ALREADY_REGISTERED;
    }

    public static boolean removePlayer(Player player) {
        if (player == null) {
            throw new InvalidParameterException("Invalid parameter given");
        }
        if (isUserConnected(player)) {
            connectedPlayers.remove(player);
            return true;
        }
        return false;
    }

    private static boolean isUserConnected(Player player) {
        for (Player checkPlayer : connectedPlayers) {
            if (checkPlayer.equals(player)) {
                return true;
            }
        }
        return false;
    }

    public static int getConnectedUsersNumber() {
        return connectedPlayers.size();
    }
}
