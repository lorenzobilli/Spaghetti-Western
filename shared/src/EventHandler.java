import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * EventHandler class
 */
public abstract class EventHandler implements Callable<Boolean> {

    private Message message;

    protected EventHandler(Message message) {
        if (message == null) {
            throw new InvalidParameterException("Unable to handle a null message");
        }
        this.message = message;
    }

    @Override
    public Boolean call() {
        Boolean handled;
        switch (message.getMessageType()) {
            case SESSION:
                handleSession();
                handled = true;
                break;
            case CHAT:
                handleChat();
                handled = true;
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }

    protected abstract void handleSession();

    protected abstract void handleChat();

}
