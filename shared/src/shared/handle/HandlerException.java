package shared.handle;

/**
 * Exception thrown by the EventHandler if critical errors are encountered.
 */
public class HandlerException extends Exception {

	/**
	 * Creates a new HandlerException with a standard generic error message.
	 */
	public HandlerException() {
		super("Event handler has encountered a critical error.");
	}

	/**
	 * Creates a new HandlerException with a custom error message.
	 * @param message Message to be shown.
	 */
	public HandlerException(String message) {
		super("Event handler has encountered the following critical error: " + message);
	}

}
