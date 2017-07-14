/**
 * ClientEventHandler
 */
public class ClientEventHandler extends EventHandler {

    protected ClientEventHandler(Message message) {
        super(message);
    }

    @Override
    protected Message handleSession() {
        if (message.getMessageContent().equals("ACCEPTED")) {
            return new Message(
                    MessageType.SESSION,
                    message.getMessageSender(),
                    "Confirmed"
            );
        } else if (message.getMessageContent().equals("REFUSED")) {
            return null;
        }
        return null;
    }

    @Override
    protected Message handleChat() {
        Client.chatWindow.updateChat(message);
        return null;
    }
}
