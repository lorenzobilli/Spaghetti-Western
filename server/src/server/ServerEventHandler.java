package server;

import shared.*;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.Scenery;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * server.Server implementation of the Event Handler.
 */
public class ServerEventHandler extends EventHandler {

	/**
	 * Creates new server.Server Event Handler.
	 * @param message shared.messaging.Message to be handled.
	 */
    protected ServerEventHandler(Message message) {
        super(message);
    }

	/**
	 * Handle all session-related messages. In particular, these events are handled:
	 *  - SESSION_START_REQUEST: A client requesting a new connection to the server (with unique username).
	 *  - SESSION_STOP_REQUEST: A client requesting to end an existing connection with the server.
	 * @return A new message with the request result.
	 * Possible results for SESSION_START_REQUEST are:
	 *  - ACCEPTED: Request has been accepted and client has been registered.
	 *  - ALREADY_CONNECTED: A client is already registered with the same username.
	 *  - MAX_NUM_REACHED: Maximum number of connected clients reached, no more clients accepted by the server.
	 *  - SESSION_RUNNING: A play session is already running, no client registrations allowed.
	 *  Possible results for SESSION_STOP_REQUEST are:
	 *  - SHUTDOWN: Stop request accepted, client disconnected from the server.
	 */
	@Override
    protected Message handleSession() {
        if (MessageManager.convertXML("header",
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
        } else if (MessageManager.convertXML("header",
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

	/**
	 * Handle all time-related messages. In particular, these events are handled:
	 *  - WAIT_START_REQUEST: A client requesting to start the login timer.
	 * @return A null message, since all operations are handled directly by the time manager.
	 */
	@Override
    protected Message handleTime() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("WAIT_START_REQUEST")) {
            if (PlayerManager.getConnectedUsersNumber() == 1) {     // First client connected to the server
            	Server.timeManager = new TimeManager();
                Server.globalThreadPool.submit(Server.timeManager);     // Start time manager
            }
        }
        return null;
    }

	/**
	 * Handle all chat-related messages.
	 * Every received message is redirected to the other team members of the message sender.
	 * @return A null message.
	 */
	@Override
    protected Message handleChat() {
        Server.connectionManager.sendMessageToTeamMembers(message.getMessageSender(), message);
        return null;
    }

	/**
	 * Handle all scenery-related messages.
	 * No events are handled currently
	 * @return A null message.
	 */
	@Deprecated
	@Override
    protected Message handleScenery() {
        return null;
    }

	/**
	 * Handle all move-related messages. In particular, these events are handled:
	 *  - TRY_PLAYER_MOVE: A client requesting to be moved in the scenery.
	 * @return A new message with the request result.
	 * Possible results for TRY_PLAYER_MOVE are:
	 *  - PLAYER_MOVED: Client has been moved successfully in the scenery.
	 *  - DESTINATION_BUSY: The desired destination has reached the maximum capacity of the node.
	 *  - DESTINATION_UNREACHABLE: The desired destination is not reachable from current client position.
	 */
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

	/**
	 * Get the corresponding fighters in a place given the opposite players' team.
	 * @param player shared.Player used as reference for retrieving the fighting team.
	 * @param position shared.scenery.Scenery place where clash will be made.
	 * @return List of opponent players.
	 */
	private List<Player> getOppositeFighters(Player player, Place position) {
    	if (player == null) {
    		throw new InvalidParameterException("shared.Player cannot be null");
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

	/**
	 * Handle all clash-related messages. In particular, these events are handled:
	 *  - CLASH_REQUEST: A client requests a new clash.
	 *  - CLASH_ACCEPTED: A clash request has been accepted by the system.
	 *  - CLASH_REJECTED: A clash request has been rejected by the system.
	 *  - START_CLASH: Clash start request.
	 * @return A null message only, since all events are handled internally by the method.
	 */
	@Override
	protected Message handleClash() {
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REQUEST")) {
			Server.gameManager.acceptClashRequests();	// Make sure that requests are unlocked for future uses
			Place clashLocation = Server.connectionManager.getPlayerHandler(
					message.getMessageSender()).getConnectedPlayer().getPosition();
			List<Player> receivers = getOppositeFighters(message.getMessageSender(), clashLocation);
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
				List<Player> receivers = getOppositeFighters(message.getMessageSender(), clashLocation);
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
				List<Player> receivers = getOppositeFighters(message.getMessageSender(), clashLocation);
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
