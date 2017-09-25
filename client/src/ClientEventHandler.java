import java.security.InvalidParameterException;

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
                MessageManager.convertXML("header", message.getMessageContent()).equals("MAX_NUM_REACHED") ||
                MessageManager.convertXML("header", message.getMessageContent()).equals("SESSION_RUNNING")) {
            return message;
        } else {
            return null;
        }
    }

    @Override
    protected Message handleTime() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("WAIT_REMAINING")) {
            int secondsRemaining = Integer.parseInt(
                    MessageManager.convertXML("content", message.getMessageContent())
            );
            Client.clientWindow.updateWaitingCountdown((secondsRemaining / 60) + 1);   // +1 to avoid round down
        }
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("WAIT_TIMEOUT")) {
            Client.chatWindow = new ChatWindow();   // Spawning chat window
            Client.clientWindow.prepareSceneryLoad();
        }
        return null;
    }

    @Override
    protected Message handleChat() {
        Client.chatWindow.updateChat(message);
        return null;
    }

    @Override
    protected Message handleScenery() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("CHOOSEN_SCENERY")) {
            String choosenScenery = MessageManager.convertXML("content", message.getMessageContent());
            if (choosenScenery.equals("DefaultScenery")) {
                Client.connectionManager.setScenery(new DefaultScenery("shared/assets/default_scenery.jpg"));  //FIXME: DefaultScenery is temporary!
            } else {
                throw new InvalidParameterException("Unrecognized scenery found");
            }
        }
        return null;
    }
}
