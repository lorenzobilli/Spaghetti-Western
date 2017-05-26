import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * UserManager class
 */
public class UserManager {
    private static ArrayList<String> connectedUsers = new ArrayList<>();

    public static boolean addUser(String user) {
        if (user == null) {
            throw new InvalidParameterException("Invalid parameter given");
        }
        if (!isUserConnected(user)) {
            connectedUsers.add(user);
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
}
