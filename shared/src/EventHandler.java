import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * EventHandler class
 */
public abstract class EventHandler implements Callable<Message> {

    protected Message message;

    protected EventHandler(Message message) {
        if (message == null) {
            throw new InvalidParameterException("Unable to handle a null message");
        }
        this.message = message;
    }

    @Override
    public Message call() {
        Message result;
        switch (message.getMessageType()) {
            case SESSION:
                result = handleSession();
                break;
            case CHAT:
                result = handleChat();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    protected abstract Message handleSession();

    protected abstract Message handleChat();

}