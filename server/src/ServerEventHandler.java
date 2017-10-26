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
        return null;
    }

	@Override
	protected Message handleMove() {
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("TRY_PLAYER_MOVE")) {
			Place origin = Server.connectionManager.getHandlerReference(
					message.getMessageSender()).getCurrentPlayerPosition();
			Place destination = Server.getCurrentScenery().getNamePlaces().get(
					MessageManager.convertXML("content", message.getMessageContent()));
			Scenery.SceneryEvents result = Server.getCurrentScenery().movePlayer(message.getMessageSender(), origin, destination);
			switch (result) {
				case PLAYER_MOVED:
					Server.connectionManager.getHandlerReference(message.getMessageSender()).setCurrentPlayerPosition(destination);
					Server.connectionManager.broadcastMessage(new Message(
							MessageType.SCENERY,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(
									new ArrayList<>(Arrays.asList(
											"header",
											"player_name",
											"player_team",
											"origin",
											"destination"
									)),
									new ArrayList<>(Arrays.asList(
											"PLAYER_MOVED",
											message.getMessageSender().getName(),
											message.getMessageSender().getTeamAsString(),
											origin.getPlaceName(),
											destination.getPlaceName()
									))
							)
					));
					int takenBullets = destination.pickBullets();
					if (message.getMessageSender().getTeam() == Player.Team.GOOD) {
						Server.setGoodTeamBullets(takenBullets);
					} else if (message.getMessageSender().getTeam() == Player.Team.BAD) {
						Server.setBadTeamBullets(takenBullets);
					}
					Server.connectionManager.getHandlerReference(message.getMessageSender()).setCurrentBullets(takenBullets);
					return new Message(
							MessageType.MOVE,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(
									new ArrayList<>(Arrays.asList(
											"header", "origin", "destination"
									)),
									new ArrayList<>(Arrays.asList(
											"PLAYER_MOVED", origin.getPlaceName(), destination.getPlaceName()
									))
							)
					);
				case DESTINATION_BUSY:
				case DESTINATION_UNREACHABLE:
					return new Message(
							MessageType.MOVE,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML("header", "PLAYER_NOT_MOVED")
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
			Server.connectionManager.acceptClashResponses();	// Make sure that requests are unlocked for future uses
			Place clashLocation = Server.connectionManager.getHandlerReference(
					message.getMessageSender()).getCurrentPlayerPosition();
			List<Player> receivers = getOppositeClashers(message.getMessageSender(), clashLocation);
			for (Player receiver : receivers) {
				Server.connectionManager.sendMessageToPlayer(receiver, new Message(
						MessageType.CLASH,
						message.getMessageSender(),
						MessageManager.createXML("header", "CLASH_REQUEST")
				));
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_ACCEPTED")) {
			Server.connectionManager.acceptAttackResponses();	// Make sure that requests are unlocked for future uses
			if (Server.connectionManager.areClashResponsesAccepted()) {
				Server.connectionManager.denyClashResponses();
				Place clashLocation = Server.connectionManager.getHandlerReference(
						message.getMessageSender()).getCurrentPlayerPosition();
				List<Player> receivers = getOppositeClashers(message.getMessageSender(), clashLocation);
				for (Player receiver : receivers) {
					Server.connectionManager.sendMessageToPlayer(receiver, new Message(
							MessageType.CLASH,
							message.getMessageSender(),
							MessageManager.createXML("header", "CLASH_ACCEPTED")
					));
				}
			} else {
				return null;
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REJECTED")) {
			if (Server.connectionManager.areClashResponsesAccepted()) {
				Server.connectionManager.denyClashResponses();
				Place clashLocation = Server.connectionManager.getHandlerReference(
						message.getMessageSender()).getCurrentPlayerPosition();
				List<Player> receivers = getOppositeClashers(message.getMessageSender(), clashLocation);
				for (Player receiver : receivers) {
					Server.connectionManager.sendMessageToPlayer(receiver, new Message(
							MessageType.CLASH,
							receiver,
							MessageManager.createXML("header", "CLASH_REJECTED")
					));
				}
			} else {
				return null;
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("START_CLASH")) {
			ClashManager currentClash = new ClashManager();
			Place clashLocation = Server.connectionManager.getHandlerReference(
					message.getMessageSender()).getCurrentPlayerPosition();
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
					Server.connectionManager.sendMessageToPlayer(winner, new Message(
							MessageType.CLASH,
							winner,
							MessageManager.createXML(
									new ArrayList<>(Arrays.asList(
											"header", "attack", "defense"
									)),
									new ArrayList<>(Arrays.asList(
											"CLASH_WON", attackResults.toString(), defenseResult.toString()
									))
							)
					));
				}
				for (Player looser : defenders) {
					Server.connectionManager.sendMessageToPlayer(looser, new Message(
						MessageType.CLASH,
						looser,
						MessageManager.createXML(
								new ArrayList<>(Arrays.asList(
										"header", "attack",	"defense"
								)),
								new ArrayList<>(Arrays.asList(
										"CLASH_LOST", attackResults.toString(), defenseResult.toString()
								))
						)
					));
				}
			}
			else if (winners.equals(ClashManager.Winners.DEFENSE)) {
				for (Player looser : defenders) {
					Server.connectionManager.sendMessageToPlayer(looser, new Message(
							MessageType.CLASH,
							looser,
							MessageManager.createXML(
									new ArrayList<>(Arrays.asList(
											"header", "attack", "defense"
									)),
									new ArrayList<>(Arrays.asList(
											"CLASH_LOST", attackResults.toString(), defenseResult.toString()
									))
							)
					));
				}
				for (Player winner : attackers) {
					Server.connectionManager.sendMessageToPlayer(winner, new Message(
							MessageType.CLASH,
							winner,
							MessageManager.createXML(
									new ArrayList<>(Arrays.asList(
											"header", "attack", "defense"
									)),
									new ArrayList<>(Arrays.asList(
											"CLASH_WON", attackResults.toString(), defenseResult.toString()
									))
							)
					));
				}
			}
		}
		return null;
	}
}
