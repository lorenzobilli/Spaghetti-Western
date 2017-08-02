/**
 * ServerEventHandler class
 */
public class ServerEventHandler extends EventHandler {

    protected ServerEventHandler(Message message) {
        super(message);
    }

    @Override
    protected Message handleSession() {
        String header = MessageManager.convertXML(message.getMessageContent()).getElementsByTagName("header").item(0).getTextContent();
        if (header.equals("SESSION_START_REQUEST")) {
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
        } else if (header.equals("SESSION_STOP_REQUEST")) {
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
    protected Message handleChat() throws Exception {
        if (!Server.connectionManager.sendMessage(message.getMessageReceiver(), message)) {
            throw new Exception("handleChat() error: specified user isn't logged in");
        }
        return null;
    }
}
