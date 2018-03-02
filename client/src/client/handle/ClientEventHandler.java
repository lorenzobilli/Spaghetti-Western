package client.handle;

import client.Client;
import client.gui.*;
import client.gui.map.LargeMap;
import client.gui.map.MediumMap;
import client.gui.map.SmallMap;
import shared.gaming.Player;
import shared.handle.EventHandler;
import shared.handle.HandlerException;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.LargeScenery;
import shared.scenery.MediumScenery;
import shared.scenery.Place;
import shared.scenery.SmallScenery;

import javax.swing.*;

/**
 * client.Client implementation of the Event Handler
 */
public class ClientEventHandler extends EventHandler {

	/**
	 * Creates new client.Client Event Handler.
	 * @param message shared.messaging.Message to be handled.
	 */
	public ClientEventHandler(Message message) {
		super(message);
	}

	/**
	 * Handle all session-related messages. In particular, these events are handled:
	 * - ACCEPTED: Request has been accepted and client has been registered.
	 * - ALREADY_CONNECTED: A client is already registered with the same username.
	 * - MAX_NUM_REACHED: Maximum number of connected clients reached, no more clients accepted by the server.
	 * - SESSION_RUNNING: A play session is already running, no client registrations allowed.
	 * @return The message containing the response result.
	 */
	@Override
	protected Message handleSession() {
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "ACCEPTED":
			case "ALREADY_CONNECTED":
			case "MAX_NUM_REACHED":
			case "SESSION_RUNNING":
				return message;
			default:
				throw new HandlerException("Invalid message type encountered");
		}
	}

	/**
	 * Handle all time-related messages. In particular, these events are handled:
	 * - WAIT_REMAINING: Updates about remaining time until a new session is started.
	 * - WAIT_TIMEOUT: Remaining time is up, a session start is imminent.
	 * - PLAY_SESSION_START: A session is starting up.
	 * @return A null message, since all events are handled inside this method.
	 */
	@Override
	protected Message handleTime() {
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "WAIT_REMAINING":
				int waitSecondsRemaining = Integer.parseInt(
						MessageManager.convertXML("content", message.getMessageContent())
				);
				Client.clientWindow.updateWaitingCountdown(waitSecondsRemaining);
				break;
			case "WAIT_TIMEOUT":
				Client.clientWindow.showSessionReadyAdvice();
				break;
			case "PLAY_SESSION_START":
				Client.clientWindow.hide();
				Client.mapWindow = new MapWindow(Client.getMap().getMapBackground());    // Loading map
				Client.getMap().populate(Client.mapWindow);
				Client.chatWindow = new ChatWindow();    // Spawning chat window
				break;
			case "PLAY_REMAINING":
				int playSecondsRemaining = Integer.parseInt(
						MessageManager.convertXML("content", message.getMessageContent())
				);
				Client.getMap().updateTotalTimeLabel(Client.mapWindow, playSecondsRemaining);
				break;
			case "TURN_BEGIN":
				Client.getMap().enableUserCommands();
				Client.getMap().enableTurnTimeLabel();
				break;
			case "TURN_END":
				Client.getMap().disableUserCommands();
				Client.getMap().disableTurnTimeLabel();
				break;
			case "TURN_REMAINING":
				int turnSecondsRemaining = Integer.parseInt(
						MessageManager.convertXML("content", message.getMessageContent())
				);
				Client.getMap().updateTurnTimeLabel(Client.mapWindow, turnSecondsRemaining);
				break;
			case "PLAY_TIMEOUT":
				// This message is unused, for now...
				break;
			default:
				throw new HandlerException("Invalid message type encountered");
		}
		return null;
	}

	/**
	 * Handle all chat-related messages.
	 * Every received message is shown in the chat window.
	 * @return A null message.
	 */
	@Override
	protected Message handleChat() {
		Client.chatWindow.updateChat(message);
		return null;
	}

	/**
	 * Set corresponding scenery and map based upon the content of the message.
	 */
	private void setSelectedScenery() {
		switch (MessageManager.convertXML("content", message.getMessageContent())) {
			case "SmallScenery":
				Client.setScenery(new SmallScenery());
				Client.setMap(new SmallMap());
				break;
			case "MediumScenery":
				Client.setScenery(new MediumScenery());
				Client.setMap(new MediumMap());
				break;
			case "LargeScenery":
				Client.setScenery(new LargeScenery());
				Client.setMap(new LargeMap());
				break;
			default:
				throw new HandlerException("Invalid scenery token found");
		}
	}

	/**
	 * Insert a player in the scenery and in the map in a specific place.
	 */
	private void insertPlayer() {
		Player player = new Player(
				MessageManager.convertXML("player_name", message.getMessageContent()),
				MessageManager.convertXML("player_team", message.getMessageContent())
		);

		Place position = Client.getScenery().getNamePlaces().get(
				MessageManager.convertXML("position", message.getMessageContent())
		);
		Client.setPosition(position);

		Client.getScenery().insertPlayer(player, position);
		Client.getMap().updateMap(player, position);
		if (Client.getPosition().isClashEnabled()) {
			Client.getMap().toggleClashButton();        //TODO: Add here turn-checking
		}
	}

	/**
	 * Move a player in the scenery and in the map from one place to another.
	 */
	private void moveOtherPlayer() {
		Player player = new Player(
				MessageManager.convertXML("player_name", message.getMessageContent()),
				MessageManager.convertXML("player_team", message.getMessageContent())
		);
		if (player.equals(Client.getPlayer())) {
			return;
		}
		Place origin = Client.getScenery().getNamePlaces().get(
				MessageManager.convertXML("origin", message.getMessageContent())
		);
		Place destination = Client.getScenery().getNamePlaces().get(
				MessageManager.convertXML("destination", message.getMessageContent())
		);
		Client.getScenery().movePlayer(player, origin, destination);
		Client.getMap().updateMap(player, origin, destination);
	}

	/**
	 * Handle all scenery-related messages. In particular, these events are handled:
	 * - CHOSEN_SCENERY: shared.messaging.Message containing which scenery has been chosen by the server.
	 * - PLAYER_INSERTED: shared.gaming.Player has been correctly inserted in the active scenery by the server.
	 * - PLAYER_MOVED: Another player has been moved correctly by the server.
	 *
	 * @return A null message.
	 */
	@Override
	protected Message handleScenery() {
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "CHOSEN_SCENERY":
				setSelectedScenery();
				break;
			case "PLAYER_INSERTED":
				insertPlayer();
				break;
			case "PLAYER_MOVED":
				moveOtherPlayer();
				break;
			default:
				throw new HandlerException("Invalid message type encountered");
		}
		return null;
	}

	/**
	 * Move the current player from one place to another.
	 */
	private void moveCurrentPlayer() {
		Place origin = Client.getScenery().getNamePlaces().get(
				MessageManager.convertXML("origin", message.getMessageContent())
		);
		Place destination = Client.getScenery().getNamePlaces().get(
				MessageManager.convertXML("destination", message.getMessageContent())
		);
		Client.setPosition(destination);
		Client.getScenery().movePlayer(Client.getPlayer(), origin, destination);
		Client.getMap().updateMap(Client.getPlayer(), origin, destination);
		Client.setBullets(destination.pickBullets());
		Client.getMap().updateBulletLabel(Client.mapWindow, Client.getBullets());
		if (Client.getPosition().isClashEnabled()) {
			Client.getMap().toggleClashButton();
		}
	}

	/**
	 * Handle all move-related messages. In particular, these events are handled:
	 * - PLAYER_MOVED:
	 *
	 * @return A null message, since all operations are done inside the method.
	 */
	@Override
	protected Message handleMove() {
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "PLAYER_MOVED":
				moveCurrentPlayer();
				break;
			default:
				throw new HandlerException("Invalid message type encountered");
		}
		return null;
	}

	/**
	 * Show a prompt for choosing to accept or reject a clash to the user.
	 * @return A message configured with the selected result from the player.
	 */
	private Message selectClashRequest() {
		Object[] options = {"Accept", "Reject"};

		int selected = JOptionPane.showOptionDialog(Client.mapWindow.getWindow(),
				"Hey " + Client.getPlayer().getName() + "! " +
						message.getMessageSender().getName() + " has sent a clash request! Accept request?",
				"Clash request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[1]
		);

		MessageTable result = null;
		switch (selected) {
			case JOptionPane.YES_OPTION:    // "Accept" selected
				result = new MessageTable("header", "CLASH_ACCEPTED");
				break;
			case JOptionPane.NO_OPTION:
			case JOptionPane.CLOSED_OPTION:
				result = new MessageTable("header", "CLASH_REJECTED");
				break;
		}

		return new Message(Message.Type.CLASH, Client.getPlayer(), MessageManager.createXML(result));
	}

	/**
	 * Manages the case when the user accepts a clash.
	 * @return A formed message with the start clash request.
	 */
	private Message manageAcceptedClash() {
		JOptionPane.showMessageDialog(
				Client.mapWindow.getWindow(),
				message.getMessageSender().getName() + " has accepted the clash!",
				"Clash accepted!", JOptionPane.INFORMATION_MESSAGE
		);    //TODO: Consider auto closeable message option
		return new Message(
				Message.Type.CLASH,
				Client.getPlayer(),
				MessageManager.createXML(new MessageTable("header", "START_CLASH"))
		);
	}

	/**
	 * Manages the case when the user rejects a clash.
	 */
	private void manageRejectedClash() {
		JOptionPane.showMessageDialog(
				Client.mapWindow.getWindow(),
				message.getMessageSender().getName() + " has rejected the clash!",
				"Clash rejected!", JOptionPane.INFORMATION_MESSAGE
		);    //TODO: Consider auto closeable message option
	}

	/**
	 * Manages the case when a clash is won.
	 */
	private void manageWonClash() {
		String attackResult = MessageManager.convertXML("attack", message.getMessageContent());
		String defenseResult = MessageManager.convertXML("defense", message.getMessageContent());
		JOptionPane.showMessageDialog(
				Client.mapWindow.getWindow(),
				"Attack: " + attackResult + " - " +
						"Defense: " + defenseResult,
				"YOU WON!", JOptionPane.INFORMATION_MESSAGE
		);    //TODO: Consider auto closeable message option
		// Get prize and add corresponding points to the current user, since he has won
		int prize = Integer.parseInt(MessageManager.convertXML("prize", message.getMessageContent()));
		Client.getPlayer().addBullets(prize);
		System.err.println("Added " + prize + " bullets to current player!");
	}

	/**
	 * Manages the case when a clash is lost.
	 */
	private void manageLostClash() {
		String attackResult = MessageManager.convertXML("attack", message.getMessageContent());
		String defenseResult = MessageManager.convertXML("defense", message.getMessageContent());
		JOptionPane.showMessageDialog(
				Client.mapWindow.getWindow(),
				"Attack: " + attackResult + " - " +
						"Defense: " + defenseResult,
				"YOU LOOSE!", JOptionPane.INFORMATION_MESSAGE
		);    //TODO: Consider auto closeable message option
		// Remove points from current user, since he has lost
		Client.getPlayer().removeBullets();
		System.err.println("Removed present bullets from current player!");
	}

	/**
	 * Handle all clash-related messages. In particular, these events are handled:
	 * - CLASH_REQUEST: Another client has sent a clash request to the current client.
	 * - CLASH_ACCEPTED: Another client has accepted a clash request sent by the current client.
	 * - CLASH_REJECTED: Another client has rejected a clash request sent by the current client.
	 * - CLASH_WON: Current client has won a clash.
	 * - CLASH_LOST: Current client has lost a clash.
	 *
	 * @return A new message with the request result.
	 * Possible results for CLASH_REQUEST are:
	 * - CLASH_ACCEPTED: The client has accepted the clash request.
	 * - CLASH_REJECTED: The client has rejected the clash request.
	 * Possible results for CLASH_ACCEPTED are:
	 * - START_CLASH: Signal for clash starting.
	 * If no other options are recognized, a null message is returned.
	 */
	@Override
	protected Message handleClash() {
		switch (MessageManager.convertXML("header", message.getMessageContent())) {
			case "CLASH_REQUEST":
				return selectClashRequest();
			case "CLASH_ACCEPTED":
				return manageAcceptedClash();
			case "CLASH_REJECTED":
				manageRejectedClash();
				break;
			case "CLASH_WON":
				manageWonClash();
				break;
			case "CLASH_LOST":
				manageLostClash();
				break;
			default:
				throw new HandlerException("Invalid message type encountered");
		}
		return null;
	}
}
