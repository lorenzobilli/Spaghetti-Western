import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;

/**
 * ServerEventHandler class
 */
public class ServerEventHandler extends EventHandler {

	private final Object monitor = new Object();

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
                            Message.Type.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
		                    MessageManager.createXML(new MessageTable("header", "ACCEPTED"))
                    );
                case ALREADY_REGISTERED:
                    return new Message(
                            Message.Type.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
		                    MessageManager.createXML(new MessageTable("header", "ALREADY_CONNECTED"))
                    );
                case MAX_NUM_REACHED:
                    return new Message(
                            Message.Type.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
                            MessageManager.createXML(new MessageTable("header", "MAX_NUM_REACHED"))
                    );
                case SESSION_RUNNING:
                    return new Message(
                            Message.Type.SESSION,
                            new Player("SERVER", Player.Team.SERVER),
                            message.getMessageSender(),
                            MessageManager.createXML(new MessageTable("header", "SESSION_RUNNING"))
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
                    Message.Type.SESSION,
                    new Player("SERVER", Player.Team.SERVER),
                    message.getMessageSender(),
                    MessageManager.createXML(new MessageTable("header", "SHUTDOWN"))
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
        Server.connectionManager.sendMessageToTeamMembers(message.getMessageSender(), message);
        return null;
    }

    @Override
    protected Message handleScenery() {
        return null;
    }

	@Override
	protected Message handleMove() {
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("TRY_PLAYER_MOVE")) {
			Place origin = Server.connectionManager.getPlayerHandler(
					message.getMessageSender()).getConnectedPlayer().getPosition();
			Place destination = Server.getScenery().getNamePlaces().get(
					MessageManager.convertXML("content", message.getMessageContent()));
			Scenery.SceneryEvents result = Server.getScenery().movePlayer(message.getMessageSender(), origin, destination);
			switch (result) {
				case PLAYER_MOVED:
					Server.connectionManager.getPlayerHandler(message.getMessageSender()).getConnectedPlayer().setPosition(destination);
					MessageTable broadcastMessageTable = new MessageTable();
					broadcastMessageTable.put("header", "PLAYER_MOVED");
					broadcastMessageTable.put("player_name", message.getMessageSender().getName());
					broadcastMessageTable.put("player_team", message.getMessageSender().getTeamAsString());
					broadcastMessageTable.put("origin", origin.getPlaceName());
					broadcastMessageTable.put("destination", destination.getPlaceName());
					Server.connectionManager.broadcastMessage(new Message(
							Message.Type.SCENERY,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(broadcastMessageTable)
					));
					int takenBullets = destination.pickBullets();
					if (message.getMessageSender().getTeam() == Player.Team.GOOD) {
						Server.setGoodTeamBullets(takenBullets);
					} else if (message.getMessageSender().getTeam() == Player.Team.BAD) {
						Server.setBadTeamBullets(takenBullets);
					}
					Server.connectionManager.getPlayerHandler(message.getMessageSender()).getConnectedPlayer().addBullets(takenBullets);
					MessageTable messageTable = new MessageTable();
					messageTable.put("header", "PLAYER_MOVED");
					messageTable.put("origin", origin.getPlaceName());
					messageTable.put("destination", destination.getPlaceName());
					return new Message(
							Message.Type.MOVE,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(messageTable)
					);
				case DESTINATION_BUSY:
				case DESTINATION_UNREACHABLE:
					return new Message(
							Message.Type.MOVE,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(new MessageTable("header", "PLAYER_NOT_MOVED"))
					);
			}
		}
		return null;
	}

	private List<Player> getOppositeClashers(Player player, Place position) {
    	if (player == null) {
    		throw new InvalidParameterException("Player cannot be null");
		}
		if (position == null) {
    		throw new InvalidParameterException("Position cannot be null");
		}
		switch (player.getTeam()) {
			case GOOD:
				return position.getBadPlayers();
			case BAD:
				return position.getGoodPlayers();
			default:
				throw new NoSuchElementException("Requested player list cannot be found");
		}
	}

	@Override
	protected Message handleClash() {
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REQUEST")) {
			Server.gameManager.acceptClashRequests();	// Make sure that requests are unlocked for future uses
			Place clashLocation = Server.connectionManager.getPlayerHandler(
					message.getMessageSender()).getConnectedPlayer().getPosition();
			List<Player> receivers = getOppositeClashers(message.getMessageSender(), clashLocation);
			for (Player receiver : receivers) {
				Server.connectionManager.sendMessageToPlayer(receiver, new Message(
						Message.Type.CLASH,
						message.getMessageSender(),
						MessageManager.createXML(new MessageTable("header", "CLASH_REQUEST"))
				));
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_ACCEPTED")) {
			Server.gameManager.acceptAttackRequests();	// Make sure that requests are unlocked for future uses
			if (Server.gameManager.isClashRequestAccepted()) {
				Server.gameManager.denyClashRequests();
				Place clashLocation = Server.connectionManager.getPlayerHandler(
						message.getMessageSender()).getConnectedPlayer().getPosition();
				List<Player> receivers = getOppositeClashers(message.getMessageSender(), clashLocation);
				for (Player receiver : receivers) {
					Server.connectionManager.sendMessageToPlayer(receiver, new Message(
							Message.Type.CLASH,
							message.getMessageSender(),
							MessageManager.createXML(new MessageTable("header", "CLASH_ACCEPTED"))
					));
				}
			} else {
				return null;
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REJECTED")) {
			if (Server.gameManager.isClashRequestAccepted()) {
				Server.gameManager.denyClashRequests();
				Place clashLocation = Server.connectionManager.getPlayerHandler(
						message.getMessageSender()).getConnectedPlayer().getPosition();
				List<Player> receivers = getOppositeClashers(message.getMessageSender(), clashLocation);
				for (Player receiver : receivers) {
					Server.connectionManager.sendMessageToPlayer(receiver, new Message(
							Message.Type.CLASH,
							receiver,
							MessageManager.createXML(new MessageTable("header", "CLASH_REJECTED"))
					));
				}
			} else {
				return null;
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("START_CLASH")) {
			ClashManager currentClash = new ClashManager();
			Place clashLocation = Server.connectionManager.getPlayerHandler(
					message.getMessageSender()).getConnectedPlayer().getPosition();
			List<Player> attackers = null;
			List<Player> defenders = null;
			if (message.getMessageSender().getTeam().equals(Player.Team.GOOD)) {
				attackers = clashLocation.getGoodPlayers();
				defenders = clashLocation.getBadPlayers();
			} else if (message.getMessageSender().getTeam().equals(Player.Team.BAD)) {
				attackers = clashLocation.getBadPlayers();
				defenders = clashLocation.getGoodPlayers();
			}
			assert attackers != null;
			assert defenders != null;
			ClashManager.Winners winners = currentClash.clash(attackers, defenders);
			StringBuilder attackResults = new StringBuilder();
			for (Integer result : currentClash.getAttackResult()) {
				attackResults.append(String.valueOf(result));
			}
			StringBuilder defenseResult = new StringBuilder();
			for (Integer result : currentClash.getDefenseResult()) {
				defenseResult.append(String.valueOf(result));
			}
			if (winners.equals(ClashManager.Winners.ATTACK)) {
				for (Player winner : attackers) {
					MessageTable messageTable = new MessageTable();
					messageTable.put("header", "CLASH_WON");
					messageTable.put("attack", attackResults.toString());
					messageTable.put("defense", defenseResult.toString());
					messageTable.put("prize", String.valueOf(PointsManager.getPrize(defenders)));
					Server.connectionManager.sendMessageToPlayer(winner, new Message(
							Message.Type.CLASH,
							winner,
							MessageManager.createXML(messageTable)
					));
				}
				for (Player looser : defenders) {
					MessageTable messageTable = new MessageTable();
					messageTable.put("header", "CLASH_LOST");
					messageTable.put("attack", attackResults.toString());
					messageTable.put("defense", defenseResult.toString());
					Server.connectionManager.sendMessageToPlayer(looser, new Message(
						Message.Type.CLASH,
						looser,
						MessageManager.createXML(messageTable)
					));
				}
			}
			else if (winners.equals(ClashManager.Winners.DEFENSE)) {
				for (Player winner : defenders) {
					MessageTable messageTable = new MessageTable();
					messageTable.put("header", "CLASH_WON");
					messageTable.put("attack", attackResults.toString());
					messageTable.put("defense", defenseResult.toString());
					Server.connectionManager.sendMessageToPlayer(winner, new Message(
							Message.Type.CLASH,
							winner,
							MessageManager.createXML(messageTable)
					));
				}
				for (Player looser : attackers) {
					MessageTable messageTable = new MessageTable();
					messageTable.put("header", "CLASH_LOST");
					messageTable.put("attack", attackResults.toString());
					messageTable.put("defense", defenseResult.toString());
					messageTable.put("prize", String.valueOf(PointsManager.getPrize(attackers)));
					Server.connectionManager.sendMessageToPlayer(looser, new Message(
							Message.Type.CLASH,
							looser,
							MessageManager.createXML(messageTable)
					));
				}
			}
		}
		return null;
	}
}
