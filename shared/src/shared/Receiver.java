package shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * shared.Receiver class
 */
public class Receiver implements Callable<Message> {

    private BufferedReader receiveStream;

    public Receiver(BufferedReader receiveStream) {
        if (receiveStream == null) {
            throw new InvalidParameterException("Invalid receiveStream stream given");
        }
        this.receiveStream = receiveStream;
    }

    @Override
    public Message call() throws Exception {
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
