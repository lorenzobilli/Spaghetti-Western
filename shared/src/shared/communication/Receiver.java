package shared.communication;

import shared.messaging.Message;
import shared.messaging.MessageManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Receiver class. Message receiving routines are all abstracted inside this class.
 */
public class Receiver implements Callable<Message> {

	/**
	 * Stream used for receiving messages.
	 */
	private BufferedReader receiveStream;

	/**
	 * Creates a new instance of Receiver listening for new messages on the given stream.
	 * @param receiveStream Receiving stream on which the receiver will listen for new messages.
	 */
    public Receiver(BufferedReader receiveStream) {
        if (receiveStream == null) {
            throw new InvalidParameterException("Invalid receiveStream stream given");
        }
        this.receiveStream = receiveStream;
    }

	/**
	 * Read a new incoming streaming message in a thread-safe manner and return the translated message
	 * @return The received message as a proper Message object.
	 */
	@Override
    public Message call() {
        String receivedMessage = "";
        try {
            synchronized (this) {
                receivedMessage = receiveStream.readLine();
            }
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        return MessageManager.prepareReceive(receivedMessage);
    }
}
