/**
 * ClientEventHandler
 */
public class ClientEventHandler extends EventHandler {

    protected ClientEventHandler(Message message) {
        super(message);
    }

    @Override
    protected Message handleSession() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("ACCEPTED") ||
                MessageManager.convertXML("header", message.getMessageContent()).equals("ALREADY_CONNECTED") ||
                MessageManager.convertXML("header", message.getMessageContent()).equals("MAX_NUM_REACHED")) {
            return message;
        } else {
            return null;
        }
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
