import com.google.gson.Gson;

/**
 * MessageManager class
 */
public class MessageManager {
    private static Gson manager = new Gson();

    public static String prepareSend(Message message) {
        return manager.toJson(message);
    }

    public static Message prepareReceive(String message) {
        return manager.fromJson(message, Message.class);
    }
}
