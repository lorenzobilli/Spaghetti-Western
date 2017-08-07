import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * UserManager class
 */
public class UserManager {
    private static ArrayList<String> connectedUsers = new ArrayList<>();

    public enum Status {
        SUCCESS,
        ALREADY_REGISTERED,
        MAX_NUM_REACHED
    }

    public static Status addUser(String user) {
        if (user == null) {
            throw new InvalidParameterException("Invalid parameter given");
        }
        if (connectedUsers.isEmpty() || !isUserConnected(user)) {
            if (connectedUsers.size() >= Server.MAX_PLAYERS) {
                return Status.MAX_NUM_REACHED;
            }
            connectedUsers.add(user);
            return Status.SUCCESS;
        }
        return Status.ALREADY_REGISTERED;
    }

    public static boolean removeUser(String user) {
        if (user == null) {
            throw new InvalidParameterException("Invalid parameter given");
        }
        if (isUserConnected(user)) {
            connectedUsers.remove(user);
            return true;
        }
        return false;
    }

    private static boolean isUserConnected(String user) {
        for (String checkUser : connectedUsers) {
            if (checkUser.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public static int getConnectedUsersNumber() {
        return connectedUsers.size();
    }
}
