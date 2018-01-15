import javax.swing.*;
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
        	Player player = new Player(
					MessageManager.convertXML("player_name", message.getMessageContent()),
					MessageManager.convertXML("player_team", message.getMessageContent())
			);
        	Place position = Client.getCurrentScenery().getNamePlaces().get(
        			MessageManager.convertXML("position", message.getMessageContent())
			);
        	Client.setCurrentPosition(position);
        	Client.getCurrentScenery().insertPlayer(
        			player, position
			);
			Client.getCurrentMap().updateMap(player, position);
			if (Client.getCurrentPosition().getClashStatus()) {
				Client.getCurrentMap().toggleClashButton();		//TODO: Add here turn-checking
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_MOVED")) {
        	Player player = new Player(
        			MessageManager.convertXML("player_name", message.getMessageContent()),
					MessageManager.convertXML("player_team", message.getMessageContent())
			);
        	if (player.equals(Client.getPlayer())) {
        		return null;
			}
        	Place origin = Client.getCurrentScenery().getNamePlaces().get(
        			MessageManager.convertXML("origin", message.getMessageContent())
			);
        	Place destination = Client.getCurrentScenery().getNamePlaces().get(
        			MessageManager.convertXML("destination", message.getMessageContent())
			);
        	Client.getCurrentScenery().movePlayer(player, origin, destination);
        	Client.getCurrentMap().updateMap(player, origin, destination);
		}
        return null;
    }

	@Override
	protected Message handleMove() {
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("PLAYER_MOVED")) {
			Place origin = Client.getCurrentScenery().getNamePlaces().get(
					MessageManager.convertXML("origin", message.getMessageContent())
			);
			Place destination = Client.getCurrentScenery().getNamePlaces().get(
					MessageManager.convertXML("destination", message.getMessageContent())
			);
			Client.setCurrentPosition(destination);
			Client.getCurrentScenery().movePlayer(Client.getPlayer(), origin, destination);
			Client.getCurrentMap().updateMap(Client.getPlayer(), origin, destination);
			Client.setCurrentBullets(destination.pickBullets());
			Client.getCurrentMap().updateBulletLabel(Client.mapWindow, Client.getCurrentBullets());
			if (Client.getCurrentPosition().getClashStatus()) {
				Client.getCurrentMap().toggleClashButton();
			}
		}
		return null;
	}

	@Override
	protected Message handleClash() {
    	if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REQUEST")) {
    		Object[] options = {"Accept", "Reject"};
			int selected = JOptionPane.showOptionDialog(
					Client.mapWindow.getFrame(),
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
							MessageType.CLASH,
							Client.getPlayer(),
							MessageManager.createXML("header", "CLASH_ACCEPTED")
					);
				case JOptionPane.NO_OPTION:
				case JOptionPane.CLOSED_OPTION:
					return new Message(
							MessageType.CLASH,
							Client.getPlayer(),
							MessageManager.createXML("header", "CLASH_REJECTED")
					);
			}
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_ACCEPTED")) {
			JOptionPane.showMessageDialog(
					Client.mapWindow.getFrame(),
					message.getMessageSender().getName() + " has accepted the clash!",
					"Clash accepted!", JOptionPane.INFORMATION_MESSAGE
			);	//TODO: Consider auto closeable message option
			return new Message(
					MessageType.CLASH,
					Client.getPlayer(),
					MessageManager.createXML("header", "START_CLASH")
			);
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_REJECTED")) {
			JOptionPane.showMessageDialog(
					Client.mapWindow.getFrame(),
					message.getMessageSender().getName() + " has rejected the clash!",
					"Clash rejected!", JOptionPane.INFORMATION_MESSAGE
			);	//TODO: Consider auto closeable message option
			return null;
		}
		if (MessageManager.convertXML("header", message.getMessageContent()).equals("CLASH_WON")) {
			String attackResult = MessageManager.convertXML("attack", message.getMessageContent());
			String defenseResult = MessageManager.convertXML("defense", message.getMessageContent());
			JOptionPane.showMessageDialog(
					Client.mapWindow.getFrame(),
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
					Client.mapWindow.getFrame(),
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
