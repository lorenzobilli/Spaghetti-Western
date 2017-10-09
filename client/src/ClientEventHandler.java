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
            Client.clientWindow.showSessionReadyAdvice();
        }
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAY_SESSION_START")) {
        	Client.clientWindow.hide();
			Client.mapWindow = new MapWindow(Client.getCurrentScenery().getSceneryBackground());	// Loading map
			Client.getCurrentMap().populate(Client.mapWindow);
        	Client.chatWindow = new ChatWindow();	// Spawning chat window
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
			switch (choosenScenery) {
				case "SmallScenery":
					Client.setCurrentScenery(new SmallScenery());
					Client.setCurrentMap(new SmallMap());
					break;
				case "MediumScenery":
					Client.setCurrentScenery(new MediumScenery());
					Client.setCurrentMap(new MediumMap());
					break;
				case "LargeScenery":
					Client.setCurrentScenery(new LargeScenery());
					Client.setCurrentMap(new LargeMap());
					break;
				default:
					throw new InvalidParameterException("Unrecognized scenery found");
			}
        }
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_INSERTED")) {
        	Client.getCurrentScenery().insertPlayer(
        			new Player(
        					MessageManager.convertXML("player_name", message.getMessageContent()),
							MessageManager.convertXML("player_team", message.getMessageContent())
					),
					Integer.valueOf(MessageManager.convertXML("position", message.getMessageContent()))
			);
		}
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_MOVED")) {
			Place origin = Client.getCurrentPosition();
			Place destination = Client.getCurrentScenery().getSceneryPlaces().get(
					MessageManager.convertXML("position", message.getMessageContent())
			);
			Client.getCurrentScenery().movePlayer(Client.getPlayer(), origin, destination);
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_NOT_MOVED")) {
        	//TODO: Implement this
		}
        return null;
    }
}
