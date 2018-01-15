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
        Message result = null;
        switch (message.getMessageType()) {
            case SESSION:
                result = handleSession();
                break;
            case TIME:
                result = handleTime();
                break;
            case CHAT:
                try {
                    result = handleChat();
                } catch (Exception e) {
                    e.getMessage();
                    e.getCause();
                    e.printStackTrace();
                }
                break;
            case SCENERY:
                result = handleScenery();
                break;
			case MOVE:
				result = handleMove();
				break;
			case CLASH:
				result = handleClash();
				break;
            default:
                result = null;
                break;
        }
        return result;
    }

    protected abstract Message handleSession();

    protected abstract Message handleTime();

    protected abstract Message handleChat() throws Exception;

    protected abstract Message handleScenery();

    protected abstract Message handleMove();

    protected abstract Message handleClash();

}
