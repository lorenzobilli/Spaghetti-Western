import java.util.concurrent.Future;

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
            UserManager.Status userManagerStatus = UserManager.addUser(message.getMessageSender());
            switch (userManagerStatus) {
                case SUCCESS:
                    return new Message(
                            MessageType.SESSION,
                            "SERVER",
                            message.getMessageSender(),
                            MessageManager.createXML("header", "ACCEPTED")
                    );
                case ALREADY_REGISTERED:
                    return new Message(
                            MessageType.SESSION,
                            "SERVER",
                            message.getMessageSender(),
                            MessageManager.createXML("header", "ALREADY_CONNECTED")
                    );
                case MAX_NUM_REACHED:
                    return new Message(
                            MessageType.SESSION,
                            "SERVER",
                            message.getMessageSender(),
                            MessageManager.createXML("header", "MAX_NUM_REACHED")
                    );
                default:
                    return null;
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
            if (UserManager.getConnectedUsersNumber() == 1) {
                Future<Boolean> runWaitTime = Server.globalThreadPool.submit(Server.remainingWaitTime);
                Server.consolePrintLine("[*] Session wait timer started");
            }
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
