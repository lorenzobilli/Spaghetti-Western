import java.util.ArrayList;
import java.util.Arrays;
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
        if (MessageManager.convertXML(
                "header",
                message.getMessageContent()).equals("SESSION_START_REQUEST")) {
            PlayerManager.Status userManagerStatus = PlayerManager.addPlayer(message.getMessageSender());
            switch (userManagerStatus) {
                case SUCCESS:
                    return new Message(
                            MessageType.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
                            MessageManager.createXML("header", "ACCEPTED")
                    );
                case ALREADY_REGISTERED:
                    return new Message(
                            MessageType.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
                            MessageManager.createXML("header", "ALREADY_CONNECTED")
                    );
                case MAX_NUM_REACHED:
                    return new Message(
                            MessageType.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
                            MessageManager.createXML("header", "MAX_NUM_REACHED")
                    );
                case SESSION_RUNNING:
                    return new Message(
                            MessageType.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
                            MessageManager.createXML("header", "SESSION_RUNNING")
                    );
                default:
                    return null;
            }
        } else if (MessageManager.convertXML(
                "header",
                message.getMessageContent()).equals("SESSION_STOP_REQUEST")) {
            //TODO: implement stop session request from a client
            if (!PlayerManager.removePlayer(message.getMessageSender())) {
                throw new RuntimeException("Error while trying to remove user: selected user doesn't exist");
            }
            return new Message(
                    MessageType.SESSION,
                    new Player("SERVER", Player.Team.SERVER),
                    message.getMessageSender(),
                    MessageManager.createXML("header", "SHUTDOWN")
            );
        }
        return null;
    }

    @Override
    protected Message handleTime() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("WAIT_START_REQUEST")) {
            if (PlayerManager.getConnectedUsersNumber() == 1) {
            	Server.timeManager = new TimeManager();
                Future<Boolean> enableTimeManager = Server.globalThreadPool.submit(Server.timeManager);
            }
        }
        return null;
    }

    @Override
    protected Message handleChat() {
        Server.connectionManager.sendMessageToTeam(message.getMessageSender(), message);
        return null;
    }

    @Override
    protected Message handleScenery() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("TRY_PLAYER_MOVE")) {
            Place origin = Server.connectionManager.getHandlerReference(
            		message.getMessageSender()).getCurrentPlayerPosition();
            Place destination = Server.getCurrentScenery().getSceneryPlaces().get(
            		MessageManager.convertXML("content", message.getMessageContent()));
            Scenery.SceneryEvents result = Server.getCurrentScenery().movePlayer(message.getMessageSender(), origin, destination);
            switch (result) {
				case PLAYER_MOVED:
					Server.connectionManager.broadcastMessage(new Message(
							MessageType.SCENERY,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(
									new ArrayList<>(Arrays.asList(
											"header", "position"
									)),
									new ArrayList<>(Arrays.asList(
											"PLAYER_MOVED", destination.getPlaceName()
									))
							)
					));
					return null;
				case DESTINATION_BUSY:
					return new Message(
							MessageType.SCENERY,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML("header", "PLAYER_NOT_MOVED")
					);
			}
        }
        return null;
    }
}
