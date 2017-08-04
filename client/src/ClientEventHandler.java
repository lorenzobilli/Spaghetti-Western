/**
 * ClientEventHandler
 */
public class ClientEventHandler extends EventHandler {

    protected ClientEventHandler(Message message) {
        super(message);
    }

    @Override
    protected Message handleSession() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("ACCEPTED")) {
            return new Message(
                    MessageType.SESSION,
                    message.getMessageSender(),
                    MessageManager.createXML("header", "CONFIRMED")
            );
        } else if (MessageManager.convertXML("header", message.getMessageContent()).equals("REFUSED")) {
            return null;
        }
        return null;
    }

    @Override
    protected Message handleTime() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("WAIT_REMAINING")) {
            int secondsRemaining = Integer.parseInt(MessageManager.convertXML("content", message.getMessageContent()));
            Client.clientWindow.updateWaitingCountdown(secondsRemaining / 60);
        }
        return null;
    }

    @Override
    protected Message handleChat() {
        Client.chatWindow.updateChat(message);
        return null;
    }
}
