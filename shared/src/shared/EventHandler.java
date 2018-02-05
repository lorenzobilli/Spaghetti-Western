package shared;

import java.rmi.NoSuchObjectException;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Base class for event handling. All derived class must implement handling methods for each message type.
 * This is generated with an instance of a message. The message is then handled with the appropriate handler by the
 * server and the client implementations.
 */
public abstract class EventHandler implements Callable<Message> {

	/**
	 * shared.Message to be handled.
	 */
    protected Message message;

	/**
	 * Creates a new Event Handler object.
	 * @param message shared.Message to be handled.
	 */
	protected EventHandler(Message message) {
        if (message == null) {
            throw new InvalidParameterException("Unable to handle a null message");
        }
        this.message = message;
    }

	/**
	 * Call appropriate handlers based upon message type.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 * @throws NoSuchObjectException If received message has a wrong type.
	 */
	@Override
    public Message call() throws NoSuchObjectException {
        Message result = null;
        switch (message.getType()) {
            case SESSION:
                result = handleSession();
                break;
            case TIME:
                result = handleTime();
                break;
            case CHAT:
                result = handleChat();
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
            	throw new NoSuchObjectException("Wrong message received from handler");
        }
        return result;
    }

	/**
	 * Abstract declaration for session-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleSession();

	/**
	 * Abstract declaration for time-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
    protected abstract Message handleTime();

	/**
	 * Abstract declaration for chat-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleChat();

	/**
	 * Abstract declaration for scenery-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
    protected abstract Message handleScenery();

	/**
	 * Abstract declaration for move-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleMove();

	/**
	 * Abstract declaration for clash-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
    protected abstract Message handleClash();

}
