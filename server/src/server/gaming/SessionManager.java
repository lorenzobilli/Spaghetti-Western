package server.gaming;

import server.Server;
import shared.gaming.Player;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.*;
import shared.utils.Randomizer;

/**
 * This class is responsible for basic game related controls and checks, such as:
 * - Marking a session as running
 * - Checking if a session is running
 * - Choosing the scenery based on number of connected clients
 * - Randomly putting players inside the scenery
 * - Handling in a thread-safe manner some time critical routines such as clash/attack requests synchronization
 */
public class SessionManager {

	/**
	 * Stores current state of the session
	 */
	private boolean sessionRunning = false;

	/**
	 * Internal field used for synchronizing clash requests.
	 */
	private boolean clashRequestAccepted;

	/**
	 * Internal field used for synchronizing attack requests.
	 */
	private boolean attackRequestAccepted;

	/**
	 * Get state of the current session.
	 * @return True if session is currently running, false if is not running.
	 */
	public boolean getSessionState() {
		return sessionRunning;
	}

	/**
	 * Set state of the current session.
	 * @param running State of the current session, true if session is running, false if not.
	 */
	public void setSessionState(boolean running) {
		sessionRunning = running;
	}

	/**
	 * Choose a type of scenery based on connected clients number, then broadcast the results.
	 * A scenery is selected with these criteria:
	 *  - Connected clients less or equal than 10: A small scenery map will be used.
	 *  - Connected clients between 11 and 20: A medium scenery map will be used.
	 *  - Connected clients between 21 and 30: A large scenery map will be used.
	 */
	public void chooseScenery() {
		if (PlayerManager.getConnectedPlayersNumber() <= 10) {
			Server.setScenery(new SmallScenery());
			Server.consolePrintLine("[*] shared.scenery.Scenery selected: shared.scenery.SmallScenery");
			MessageTable messageTable = new MessageTable();
			messageTable.put("header", "CHOSEN_SCENERY");
			messageTable.put("content", "SmallScenery");
			Server.connectionManager.broadcastMessage(new Message(
					Message.Type.SCENERY,
					new Player("SERVER", Player.Team.SERVER),
					MessageManager.createXML(messageTable)
			));
		} else if (PlayerManager.getConnectedPlayersNumber() > 10 && PlayerManager.getConnectedPlayersNumber() <= 20) {
			Server.setScenery(new MediumScenery());
			Server.consolePrintLine("[*] shared.scenery.Scenery selected: shared.scenery.MediumScenery");
			MessageTable messageTable = new MessageTable();
			messageTable.put("header", "CHOSEN_SCENERY");
			messageTable.put("content", "MediumScenery");
			Server.connectionManager.broadcastMessage(new Message(
					Message.Type.SCENERY,
					new Player("SERVER", Player.Team.SERVER),
					MessageManager.createXML(messageTable)
			));
		} else if (PlayerManager.getConnectedPlayersNumber() > 20 && PlayerManager.getConnectedPlayersNumber() <= 30) {
			Server.setScenery(new LargeScenery());
			Server.consolePrintLine("[*] shared.scenery.Scenery selected: shared.scenery.LargeScenery");
			MessageTable messageTable = new MessageTable();
			messageTable.put("header", "CHOSEN_SCENERY");
			messageTable.put("content", "LargeScenery");
			Server.connectionManager.broadcastMessage(new Message(
					Message.Type.SCENERY,
					new Player("SERVER", Player.Team.SERVER),
					MessageManager.createXML(messageTable)
			));
		}
	}

	/**
	 * Randomly put the ugly player in the chosen scenery.
	 */
	private void putUglyPlayer() {
		int randomId = Randomizer.getRandomInteger(Server.getScenery().getPlacesNumber());
		Server.uglyPlayer = new Player("UGLY", Player.Team.UGLY);
		Place randomPlace = Server.getScenery().getIdPlaces().get(randomId);
		Server.getScenery().insertPlayer(Server.uglyPlayer, randomPlace);    // No checks here since the ugly is always inserted
		MessageTable messageTable = new MessageTable();
		messageTable.put("header", "PLAYER_INSERTED");
		messageTable.put("player_name", Server.uglyPlayer.getName());
		messageTable.put("player_team", Server.uglyPlayer.getTeamAsString());
		messageTable.put("position", randomPlace.getPlaceName());
		Server.connectionManager.broadcastMessage(new Message(
				Message.Type.SCENERY,
				new Player("SERVER", Player.Team.SERVER),
				MessageManager.createXML(messageTable)
		));
	}

	/**
	 * Initialize the game by randomly putting players in the chosen scenery.
	 */
	public void putPlayers() {
		int totalPlayersNumber = PlayerManager.getConnectedPlayersNumber();
		int servedPlayersNumber = 0;
		while (servedPlayersNumber != totalPlayersNumber) {
			int randomId = Randomizer.getRandomInteger(Server.getScenery().getPlacesNumber());
			Player servedPlayer = PlayerManager.getPlayer(servedPlayersNumber);
			Place randomPlace = Server.getScenery().getIdPlaces().get(randomId);
			Scenery.SceneryEvent result = Server.getScenery().insertPlayer(servedPlayer, randomPlace);
			if (result == Scenery.SceneryEvent.PLAYER_INSERTED) {
				Server.connectionManager.getPlayerHandler(servedPlayer).getConnectedPlayer().setPosition(randomPlace);
				servedPlayersNumber++;
				MessageTable messageTable = new MessageTable();
				messageTable.put("header", "PLAYER_INSERTED");
				messageTable.put("player_name", servedPlayer.getName());
				messageTable.put("player_team", servedPlayer.getTeamAsString());
				messageTable.put("position", randomPlace.getPlaceName());
				Server.connectionManager.broadcastMessage(new Message(
						Message.Type.SCENERY,
						new Player("SERVER", Player.Team.SERVER),
						MessageManager.createXML(messageTable)
				));
			}
		}
		putUglyPlayer();
	}

	/**
	 * Accepts a new clash request in a thread-safe manner.
	 */
	public synchronized void acceptClashRequests() {
		clashRequestAccepted = true;
	}

	/**
	 * Denies a new clash request in a thread-safe manner.
	 */
	public synchronized void denyClashRequests() {
		clashRequestAccepted = false;
	}

	/**
	 * Checks if a clash request has been already accepted by another player in a thread-safe manner.
	 * @return True if request has been already accepted, false if not.
	 */
	public synchronized boolean isClashRequestAccepted() {
		return clashRequestAccepted;
	}

	/**
	 * Accepts a new attack request in a thread-safe manner.
	 */
	public synchronized void acceptAttackRequests() {
		attackRequestAccepted = true;
	}

	/**
	 * Denies a new attack request in a thread-safe manner.
	 */
	public synchronized void denyAttackRequests() {
		attackRequestAccepted = false;
	}

	/**
	 * Checks if an attack request has been already accepted by another player in a thread-safe manner.
	 * @return True if request has been already accepted, false if not.
	 */
	public synchronized boolean isAttackRequestAccepted() {
		return attackRequestAccepted;
	}
}
