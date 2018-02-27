package server.handle;

import server.Server;
import server.gaming.PlayerManager;
import server.time.TimeManager;
import shared.gaming.ClashManager;
import shared.gaming.Player;
import shared.gaming.PointsManager;
import shared.handle.EventHandler;
import shared.handle.HandlerException;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.Place;
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
    public ServerEventHandler(Message message) {
        super(message);
    }

	/**
	 * Manages session start conditions.
	 * @return A message with all the correct result.
	 */
	private Message manageSessionStart() {
	    PlayerManager.Status userManagerStatus = PlayerManager.addPlayer(message.getMessageSender());
	    MessageTable result;
	    switch (userManagerStatus) {
		    case SUCCESS:
			    result = new MessageTable("header", "ACCEPTED");
			    break;
		    case ALREADY_REGISTERED:
			    result = new MessageTable("header", "ALREADY_CONNECTED");
			    break;
		    case MAX_NUM_REACHED:
			    result = new MessageTable("header", "MAX_NUM_REACHED");
			    break;
		    case SESSION_RUNNING:
			    result = new MessageTable("header", "SESSION_RUNNING");
			    break;
		    default:
			    throw new HandlerException("Invalid message state encountered");
	    }
	    return new Message(Message.Type.SESSION,
			    new Player("SERVER", Player.Team.SERVER),
			    message.getMessageSender(),
			    MessageManager.createXML(result)
	    );
    }

	/**
	 * Manages session stop condition.
	 * @return A message with the configured shutdown request.
	 */
	private Message manageSessionStop() {
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
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "SESSION_START_REQUEST":
				return manageSessionStart();
			case "SESSION_STOP_REQUEST":
				return manageSessionStop();
			default:
				throw new HandlerException("Invalid message type encountered");
		}
    }

	/**
	 * Handle all time-related messages. In particular, these events are handled:
	 *  - WAIT_START_REQUEST: A client requesting to start the login timer.
	 * @return A null message, since all operations are handled directly by the time manager.
	 */
	@Override
    protected Message handleTime() {
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "WAIT_START_REQUEST":
				if (PlayerManager.getConnectedPlayersNumber() == 1) {     // First client connected to the server
					TimeManager.launchWaitingTimer();
				}
				break;
			default:
				throw new HandlerException("Invalid message state encountered");
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
	@Override
    protected Message handleScenery() {
        return null;
    }

	/**
	 * Manages all possible player moves.
	 * @return A configured message with the result of the operation.
	 */
	private Message managePlayerMovement() {
	    Place origin = Server.connectionManager.getPlayerHandler(
	    		message.getMessageSender()).getConnectedPlayer().getPosition();
	    Place destination = Server.getScenery().getNamePlaces().get(
			    MessageManager.convertXML("content", message.getMessageContent()));
	    Scenery.SceneryEvent result = Server.getScenery().movePlayer(message.getMessageSender(), origin, destination);

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
			default:
				throw new HandlerException("Invalid message found");
	    }
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
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "TRY_PLAYER_MOVE":
				return managePlayerMovement();
			default:
				throw new HandlerException("Invalid message type encountered");
		}
	}

	/**
	 * Get the corresponding fighters in a place given the opposite players' team.
	 * @param player shared.gaming.Player used as reference for retrieving the fighting team.
	 * @param position shared.scenery.Scenery place where clash will be made.
	 * @return List of opponent players.
	 */
	private List<Player> getOppositeFighters(Player player, Place position) {
    	if (player == null) {
    		throw new InvalidParameterException("shared.gaming.Player cannot be null");
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
	 * Manages received clash requests.
	 */
	private void manageClashRequest() {
		Server.sessionManager.acceptClashRequests();	// Make sure that requests are unlocked for future uses
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

	/**
	 * Manages accepted clash requests.
	 */
	private void manageAcceptedClash() {
		Server.sessionManager.acceptAttackRequests();	// Make sure that requests are unlocked for future uses
		if (Server.sessionManager.isClashRequestAccepted()) {
			Server.sessionManager.denyClashRequests();
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
		}
	}

	/**
	 * Manages rejected clash requests.
	 */
	private void manageRejectedClash() {
		if (Server.sessionManager.isClashRequestAccepted()) {
			Server.sessionManager.denyClashRequests();
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
		}
	}

	/**
	 * Sends a message to the winners of a clash.
	 * @param winners List of winning players.
	 * @param attack Total attack score.
	 * @param defense Total defense score.
	 * @param prize Prize quantity for each player.
	 */
	private void sendWinningMessage(List<Player> winners, String attack, String defense, String prize) {
		if (winners == null) {
			throw new InvalidParameterException("Winners players list cannot be null");
		}
		if (attack == null) {
			throw new InvalidParameterException("Attack value cannot be null");
		}
		if (defense == null) {
			throw new InvalidParameterException("Defense value cannot be null");
		}
		if (prize == null) {
			throw new InvalidParameterException("Prize value cannot be null");
		}

		MessageTable messageTable = new MessageTable();
		messageTable.put("header", "CLASH_WON");
		messageTable.put("attack", attack);
		messageTable.put("defense", defense);
		messageTable.put("prize", prize);

		for (Player player : winners) {
			Server.connectionManager.sendMessageToPlayer(player, new Message(
					Message.Type.CLASH,
					player,
					MessageManager.createXML(messageTable)
			));
		}
	}

	/**
	 * Send a message to the losers of a clash.
	 * @param losers List of losing players.
	 * @param attack Total attack score.
	 * @param defense Total defense score.
	 */
	private void sendLoosingMessage(List<Player> losers, String attack, String defense) {
		if (losers == null) {
			throw new InvalidParameterException("Losers players list cannot be null");
		}
		if (attack == null) {
			throw new InvalidParameterException("Attack value cannot be null");
		}
		if (defense == null) {
			throw new InvalidParameterException("Defense value cannot be null");
		}

		MessageTable messageTable = new MessageTable();
		messageTable.put("header", "CLASH_LOST");
		messageTable.put("attack", attack);
		messageTable.put("defense", defense);

		for (Player player : losers) {
			Server.connectionManager.sendMessageToPlayer(player, new Message(
					Message.Type.CLASH,
					player,
					MessageManager.createXML(messageTable)
			));
		}
	}

	/**
	 * Manages start of a clash.
	 */
	private void manageClashStart() {

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
			sendWinningMessage(
					attackers,
					attackResults.toString(),
					defenseResult.toString(),
					String.valueOf(PointsManager.getPrize(defenders))
			);
			sendLoosingMessage(
					defenders,
					attackResults.toString(),
					defenseResult.toString()
			);
		}
		else if (winners.equals(ClashManager.Winners.DEFENSE)) {
			sendWinningMessage(
					defenders,
					attackResults.toString(),
					defenseResult.toString(),
					String.valueOf(PointsManager.getPrize(attackers))
			);
			sendLoosingMessage(
					attackers,
					attackResults.toString(),
					defenseResult.toString()
			);
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
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "CLASH_REQUEST":
				manageClashRequest();
				break;
			case "CLASH_ACCEPTED":
				manageAcceptedClash();
				break;
			case "CLASH_REJECTED":
				manageRejectedClash();
				break;
			case "START_CLASH":
				manageClashStart();
				break;
			default:
				throw new HandlerException("Invalid message type encountered");
		}
		return null;
	}
}