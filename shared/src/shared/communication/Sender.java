package shared.communication;

import shared.messaging.Message;
import shared.messaging.MessageManager;

import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Sender class. Message sending routines are all abstracted inside this class.
 */
public class Sender implements Callable<Object> {

	/**
	 * Message to be sent.
	 */
	private Message message;

	/**
	 * Stream used for sending messages.
	 */
    private PrintWriter sendStream;

	/**
	 * Creates a new instance of Sender sending on the given stream.
	 * @param message Message to be sent.
	 * @param sendStream Sending stream on which the sender will send the message.
	 */
	public Sender(Message message, PrintWriter sendStream) {
        if (message == null) {
            throw new InvalidParameterException("Null message given");
        }
        if (sendStream == null) {
            throw new InvalidParameterException("Invalid sendStream stream given");
        }
        this.message = message;
        this.sendStream = sendStream;
    }

	/**
	 * Translate a Message object into its streaming form, then send it in a thread-safe manner via the given stream.
	 * @return Always returns a null object.
	 */
	@Override
    public Object call() {
        String translatedMessage = MessageManager.prepareSend(message);
        synchronized (this) {
            sendStream.println(translatedMessage);
        }
        return null;
    }
}
