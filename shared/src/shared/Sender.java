package shared;

import shared.messaging.Message;
import shared.messaging.MessageManager;

import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * shared.Sender class
 */
public class Sender implements Callable<Object> {

    private Message message;
    private PrintWriter sendStream;

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

    @Override
    public Object call() throws Exception {
        String translatedMessage = MessageManager.prepareSend(message);
        synchronized (this) {
            sendStream.println(translatedMessage);
        }
        return null;
    }
}
