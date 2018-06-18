/*
 *  Project: "Spaghetti Western"
 *
 *
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2017-2018 Lorenzo Billi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *	documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *	rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *	permit persons to whom the Software is	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 *	the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *	WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *	OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *	OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package server.gaming;

import server.Server;
import shared.gaming.Player;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.*;
import shared.utils.Randomizer;

import java.util.Set;

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
	 * Gets state of the current session.
	 * @return True if session is currently running, false if is not running.
	 */
	public boolean getSessionState() {
		return sessionRunning;
	}

	/**
	 * Sets state of the current session.
	 * @param running State of the current session, true if session is running, false if not.
	 */
	public void setSessionState(boolean running) {
		sessionRunning = running;
	}

	/**
	 * Chooses a type of scenery based on connected clients number, then broadcast the results.
	 * A scenery is selected with these criteria:
	 *  - Connected clients less or equal than 10: A small scenery map will be used.
	 *  - Connected clients between 11 and 20: A medium scenery map will be used.
	 *  - Connected clients between 21 and 30: A large scenery map will be used.
	 */
	public void chooseScenery() {
		if (PlayerManager.getConnectedPlayersNumber() <= 10) {
			Server.setScenery(new SmallScenery());
			Server.consolePrintLine("Scenery selected: SmallScenery");
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
			Server.consolePrintLine("Scenery selected: MediumScenery");
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
			Server.consolePrintLine("Scenery selected: LargeScenery");
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
		Server.uglyPlayer.setPosition(randomPlace);
		Server.consolePrintLine("Ugly player inserted inside scenery place \"" + randomPlace.getPlaceName() + "\"");
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
	 * Selects a proper destination then moves the ugly player randomly inside the scenery.
	 * The ugly player can be moved only between two linked places, as any other player in the scenery.
	 * @return A MessageTable containing ugly player's place origin and final destination.
	 */
	public MessageTable chooseAndMoveUglyPlayer() {
		Place origin = Server.uglyPlayer.getPosition();
		Set<Path> destinationPaths = Server.getScenery().getAllPaths(origin);
		Path selectedPath = null;

		if (destinationPaths.size() == 1) {
			selectedPath = (Path) destinationPaths.toArray()[0];
		} else {
			int randomValue = Randomizer.getRandomInteger(destinationPaths.size());
			int current = 1;
			for (Path path : destinationPaths) {
				if (current == randomValue) {
					selectedPath = path;
					break;
				}
				current++;
			}
		}

		MessageTable messageTable = new MessageTable();
		messageTable.put("origin", origin.getPlaceName());
		messageTable.put("destination", Server.getScenery().getPathTarget(origin, selectedPath).getPlaceName());
		return messageTable;
	}

	public void declareWinners() {
		MessageTable messageTable = new MessageTable();
		messageTable.put("header", "SESSION_ENDED");
		if (Server.getGoodTeamBullets() == Server.getBadTeamBullets()) {
			messageTable.put("winners", "DRAW");
		} else if (Server.getGoodTeamBullets() > Server.getBadTeamBullets()) {
			messageTable.put("winners", "GOOD");
		} else if (Server.getGoodTeamBullets() < Server.getBadTeamBullets()) {
			messageTable.put("winners", "BAD");
		}
		Server.connectionManager.broadcastMessage(new Message(
				Message.Type.SESSION,
				new Player("SERVER", Player.Team.SERVER),
				MessageManager.createXML(messageTable)
		));
		sessionRunning = false;
	}
}
