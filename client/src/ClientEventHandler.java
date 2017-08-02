/**
 * ClientEventHandler
 */
public class ClientEventHandler extends EventHandler {

    protected ClientEventHandler(Message message) {
        super(message);
    }

    @Override
    protected Message handleSession() {
        String header = MessageManager.convertXML(message.getMessageContent()).getElementsByTagName("header").item(0).getTextContent();
        if (header.equals("ACCEPTED")) {
            return new Message(
                    MessageType.SESSION,
                    message.getMessageSender(),
                    MessageManager.createXML("header", "CONFIRMED")
            );
        } else if (header.equals("REFUSED")) {
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
