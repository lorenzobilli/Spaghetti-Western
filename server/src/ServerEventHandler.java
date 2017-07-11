/**
 * ServerEventHandler class
 */
public class ServerEventHandler extends EventHandler {

    protected ServerEventHandler(Message message) {
        super(message);
    }

    @Override
    protected Message handleSession() {
        if (message.getMessageContent().equals("Start session request")) {
            if (UserManager.addUser(message.getMessageSender())) {
                return new Message(
                        MessageType.SESSION,
                        "SERVER",
                        message.getMessageSender(),
                        "ACCEPTED"
                );
            } else {
                return new Message(
                        MessageType.SESSION,
                        "SERVER",
                        message.getMessageSender(),
                        "REFUSED"
                );
            }
        } else if (message.getMessageContent().equals("Stop session request")) {
            //TODO: implement stop session request from a client
        }
        return null;
    }

    @Override
    protected Message handleChat() {
        //TODO: implement server-side chat
        return message;
    }
}
