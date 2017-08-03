import java.util.ArrayList;
import java.util.Arrays;

/**
 * ServerEventHandler class
 */
public class ServerEventHandler extends EventHandler {

    protected ServerEventHandler(Message message) {
        super(message);
    }

    @Override
    protected Message handleSession() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("SESSION_START_REQUEST")) {
            if (UserManager.addUser(message.getMessageSender())) {
                return new Message(
                        MessageType.SESSION,
                        "SERVER",
                        message.getMessageSender(),
                        MessageManager.createXML("header", "ACCEPTED")
                );
            } else {
                return new Message(
                        MessageType.SESSION,
                        "SERVER",
                        message.getMessageSender(),
                        MessageManager.createXML("header", "REFUSED")
                );
            }
        } else if (MessageManager.convertXML("header", message.getMessageContent()).equals("SESSION_STOP_REQUEST")) {
            //TODO: implement stop session request from a client
            if (!UserManager.removeUser(message.getMessageSender())) {
                throw new RuntimeException("Error while trying to remove user: selected user doesn't exist");
            }
            return new Message(
                    MessageType.SESSION,
                    "SERVER",
                    message.getMessageSender(),
                    MessageManager.createXML("header", "SHUTDOWN")
            );
        }
        return null;
    }

    @Override
    protected Message handleTime() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("WAIT_START_REQUEST")) {
            return new Message(
                    MessageType.TIME,
                    "SERVER",
                    message.getMessageSender(),
                    MessageManager.createXML(
                            new ArrayList<String>(Arrays.asList(
                                    "header", "content"
                            )),
                            new ArrayList<String>(Arrays.asList(
                                    "WAIT_REMAINING", Server.remainingWaitTime.toString())
                            ))
            );
        }
        return null;
    }

    @Override
    protected Message handleChat() throws Exception {
        if (!Server.connectionManager.sendMessage(message.getMessageReceiver(), message)) {
            throw new Exception("handleChat() error: specified user isn't logged in");
        }
        return null;
    }
}
