package client;

import client.gui.*;
import client.gui.map.LargeMap;
import client.gui.map.MediumMap;
import client.gui.map.SmallMap;
import shared.*;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.LargeScenery;
import shared.scenery.MediumScenery;
import shared.scenery.Place;
import shared.scenery.SmallScenery;

import javax.swing.*;
import java.security.InvalidParameterException;

/**
 * client.Client implementation of the Event Handler
 */
public class ClientEventHandler extends EventHandler {

	/**
	 * Creates new client.Client Event Handler.
	 * @param message shared.messaging.Message to be handled.
	 */
    protected ClientEventHandler(Message message) {
        super(message);
    }

	/**
	 * Handle all session-related messages. In particular, these events are handled:
	 *  - ACCEPTED: Request has been accepted and client has been registered.
	 *  - ALREADY_CONNECTED: A client is already registered with the same username.
	 *  - MAX_NUM_REACHED: Maximum number of connected clients reached, no more clients accepted by the server.
	 *  - SESSION_RUNNING: A play session is already running, no client registrations allowed.
	 * @return The message containing the response result, a null message if the request is not recognised.
	 */
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

	/**
	 * Handle all time-related messages. In particular, these events are handled:
	 *  - WAIT_REMAINING: Updates about remaining time until a new session is started.
	 *  - WAIT_TIMEOUT: Remaining time is up, a session start is imminent.
	 *  - PLAY_SESSION_START: A session is starting up.
	 * @return A null message, since all events are handled inside this method.
	 */
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
			Client.mapWindow = new MapWindow(Client.getMap().getMapBackground());	// Loading map
			Client.getMap().populate(Client.mapWindow);
        	Client.chatWindow = new ChatWindow();	// Spawning chat window
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
	 * Handle all scenery-related messages. In particular, these events are handled:
	 *  - CHOOSEN_SCENERY: shared.messaging.Message containing which scenery has been chosen by the server.
	 *  - PLAYER_INSERTED: shared.Player has been correctly inserted in the active scenery by the server.
	 *  - PLAYER_MOVED: Another player has been moved correctly by the server.
	 * @return A null message.
	 */
	@Override
    protected Message handleScenery() {
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("CHOOSEN_SCENERY")) {
            String choosenScenery = MessageManager.convertXML("content", message.getMessageContent());
			switch (choosenScenery) {
				case "shared.scenery.SmallScenery":
					Client.setScenery(new SmallScenery());
					Client.setMap(new SmallMap());
					break;
				case "shared.scenery.MediumScenery":
					Client.setScenery(new MediumScenery());
					Client.setMap(new MediumMap());
					break;
				case "shared.scenery.LargeScenery":
					Client.setScenery(new LargeScenery());
					Client.setMap(new LargeMap());
					break;
				default:
					throw new InvalidParameterException("Unrecognized scenery found");
			}
        }
        if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_INSERTED")) {
        	Player player = new Player(
					MessageManager.convertXML("player_name", message.getMessageContent()),
					MessageManager.convertXML("player_team", message.getMessageContent())
			);
        	Place position = Client.getScenery().getNamePlaces().get(
        			MessageManager.convertXML("position", message.getMessageContent())
			);
        	Client.setPosition(position);
        	Client.getScenery().insertPlayer(
        			player, position
			);
			Client.getMap().updateMap(player, position);
			if (Client.getPosition().getClashStatus()) {
				Client.getMap().toggleClashButton();		//TODO: Add here turn-checking
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_MOVED")) {
        	//TODO: Consider changing "PLAYER_MOVED" with OTHER_PLAYER_MOVED
        	Player player = new Player(
        			MessageManager.convertXML("player_name", message.getMessageContent()),
					MessageManager.convertXML("player_team", message.getMessageContent())
			);
        	if (player.equals(Client.getPlayer())) {
        		return null;
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
        return null;
    }

	/**
	 * Handle all move-related messages. In particular, these events are handled:
	 *  - PLAYER_MOVED:
	 * @return A null message, since all operations are done inside the method.
	 */
	@Override
	protected Message handleMove() {
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_MOVED")) {
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
			if (Client.getPosition().getClashStatus()) {
				Client.getMap().toggleClashButton();
			}
		}
		return null;
	}

	/**
	 * Handle all clash-related messages. In particular, these events are handled:
	 *  - CLASH_REQUEST: Another client has sent a clash request to the current client.
	 *  - CLASH_ACCEPTED: Another client has accepted a clash request sent by the current client.
	 *  - CLASH_REJECTED: Another client has rejected a clash request sent by the current client.
	 *  - CLASH_WON: Current client has won a clash.
	 *  - CLASH_LOST: Current client has lost a clash.
	 * @return A new message with the request result.
	 * Possible results for CLASH_REQUEST are:
	 *  - CLASH_ACCEPTED: The client has accepted the clash request.
	 *  - CLASH_REJECTED: The client has rejected the clash request.
	 * Possible results for CLASH_ACCEPTED are:
	 *  - START_CLASH: Signal for clash starting.
	 * If no other options are recognized, a null message is returned.
	 */
	@Override
	protected Message handleClash() {
    	if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REQUEST")) {
    		Object[] options = {"Accept", "Reject"};
			int selected = JOptionPane.showOptionDialog(
					Client.mapWindow.getWindow(),
					"Hey " + Client.getPlayer().getName() + "! " +
							message.getMessageSender().getName() + " has sent a clash request! Accept request?",
					"Clash request",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[1]);
			switch (selected) {
				case JOptionPane.YES_OPTION:	// "Accept" selected
					return new Message(
							Message.Type.CLASH,
							Client.getPlayer(),
							MessageManager.createXML(new MessageTable("header", "CLASH_ACCEPTED"))
					);
				case JOptionPane.NO_OPTION:
				case JOptionPane.CLOSED_OPTION:
					return new Message(
							Message.Type.CLASH,
							Client.getPlayer(),
							MessageManager.createXML(new MessageTable("header", "CLASH_REJECTED"))
					);
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_ACCEPTED")) {
			JOptionPane.showMessageDialog(
					Client.mapWindow.getWindow(),
					message.getMessageSender().getName() + " has accepted the clash!",
					"Clash accepted!", JOptionPane.INFORMATION_MESSAGE
			);	//TODO: Consider auto closeable message option
			return new Message(
					Message.Type.CLASH,
					Client.getPlayer(),
					MessageManager.createXML(new MessageTable("header", "START_CLASH"))
			);
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REJECTED")) {
			JOptionPane.showMessageDialog(
					Client.mapWindow.getWindow(),
					message.getMessageSender().getName() + " has rejected the clash!",
					"Clash rejected!", JOptionPane.INFORMATION_MESSAGE
			);	//TODO: Consider auto closeable message option
			return null;
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_WON")) {
			String attackResult = MessageManager.convertXML("attack", message.getMessageContent());
			String defenseResult = MessageManager.convertXML("defense", message.getMessageContent());
			JOptionPane.showMessageDialog(
					Client.mapWindow.getWindow(),
					"Attack: " + attackResult + " - " +
							"Defense: " + defenseResult,
					"YOU WON!", JOptionPane.INFORMATION_MESSAGE
			);	//TODO: Consider auto closeable message option
			// Get prize and add corresponding points to the current user, since he has won
			int prize = Integer.parseInt(MessageManager.convertXML("prize", message.getMessageContent()));
			Client.getPlayer().addBullets(prize);
			System.err.println("Added " + prize + " bullets to current player!");
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_LOST")) {
			String attackResult = MessageManager.convertXML("attack", message.getMessageContent());
			String defenseResult = MessageManager.convertXML("defense", message.getMessageContent());
			JOptionPane.showMessageDialog(
					Client.mapWindow.getWindow(),
					"Attack: " + attackResult + " - " +
							"Defense: " + defenseResult,
					"YOU LOOSE!", JOptionPane.INFORMATION_MESSAGE
			);	//TODO: Consider auto closeable message option
			// Remove points from current user, since he has lost
			Client.getPlayer().removeBullets();
			System.err.println("Removed present bullets from current player!");
		}
		return null;
	}
}
