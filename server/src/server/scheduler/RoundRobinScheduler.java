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

package server.scheduler;

import server.Server;
import shared.gaming.Player;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * A scheduler implementation using the round-robin scheduling algorithm.
 */
public class RoundRobinScheduler implements Scheduler {

	/**
	 * Internal flag indicating whether the scheduler is enabled.
	 */
	private boolean enabled;

	/**
	 * List of schedulable players belonging to the good team.
	 */
	private ArrayList<Player> goodScheduledPlayers = new ArrayList<>();

	/**
	 * List of schedulable players belonging to the bad team.
	 */
	private ArrayList<Player> badScheduledPlayers = new ArrayList<>();

	/**
	 * Keeps track of the last scheduled team by the scheduler.
	 */
	private Player.Team lastScheduledTeam;

	/**
	 * Keeps track of the last scheduled player by the scheduler belonging to the good team.
	 */
	private int lastGoodScheduledPlayerIndex;

	/**
	 * Keeps track of the last scheduled player by the scheduler belonging to the bad team.
	 */
	private int lastBadScheduledPlayerIndex;

	/**
	 * Latest scheduled player.
	 */
	private Player previouslyScheduledPlayer;

	/**
	 * Currently scheduled player.
	 */
	private Player currentlyScheduledPlayer;

	/**
	 * Enables the scheduler.
	 */
	@Override
	public void enableScheduler() {
		enabled = true;
	}

	/**
	 * Disables the scheduler.
	 */
	@Override
	public void disableScheduler() {
		enabled = false;
	}

	/**
	 * Checks whether the scheduler is enabled.
	 * @return True if the scheduler is enabled, false if not.
	 */
	@Override
	public boolean isSchedulerEnabled() {
		return enabled;
	}

	/**
	 * Initializes the scheduler.
	 * @param firstScheduledTeam Team that will be first scheduled.
	 */
	@Override
	public void initializeScheduler(Player.Team firstScheduledTeam) {
		if (firstScheduledTeam == null) {
			throw new InvalidParameterException("Scheduled team cannot be null");
		}
		switch (firstScheduledTeam) {
			case GOOD:
				lastScheduledTeam = Player.Team.BAD;
				break;
			case BAD:
				lastScheduledTeam = Player.Team.GOOD;
				break;
			default:
				throw new InvalidParameterException("Invalid team given to the scheduler");
		}
		lastGoodScheduledPlayerIndex = lastBadScheduledPlayerIndex = 0;
		previouslyScheduledPlayer = currentlyScheduledPlayer = null;
	}

	/**
	 * Adds a schedulable element to the scheduler. A schedulable element corresponds to a Player object.
	 * @param scheduledPlayer Player to be added to the scheduler.
	 */
	@Override
	public void addScheduledElement(Player scheduledPlayer) {
		if (scheduledPlayer == null) {
			throw new InvalidParameterException("Scheduled player cannot be null");
		}
		switch (scheduledPlayer.getTeam()) {
			case GOOD:
				goodScheduledPlayers.add(scheduledPlayer);
				break;
			case BAD:
				badScheduledPlayers.add(scheduledPlayer);
				break;
			default:
				throw new InvalidParameterException("Bad scheduled player given");
		}
	}

	/**
	 * Removes a schedulable element from the scheduler. A schedulable element corresponds to a Player object.
	 * @param scheduledPlayer Player to be removed from the scheduler.
	 */
	@Override
	public void removeScheduledElement(Player scheduledPlayer) {
		if (scheduledPlayer == null) {
			throw new InvalidParameterException("Scheduled player cannot be null");
		}
		switch (scheduledPlayer.getTeam()) {
			case GOOD:
				goodScheduledPlayers.remove(scheduledPlayer);
				break;
			case BAD:
				badScheduledPlayers.remove(scheduledPlayer);
				break;
			default:
				throw new InvalidParameterException("Bad scheduled player given");
		}
	}

	/**
	 * Schedules next selected player belonging to the good team.
	 */
	private void scheduleGood() {
		if (lastGoodScheduledPlayerIndex == goodScheduledPlayers.size() - 1) {
			lastGoodScheduledPlayerIndex = 0;
		} else {
			lastGoodScheduledPlayerIndex++;
		}
		if (currentlyScheduledPlayer != null) {     // Null only on first run
			previouslyScheduledPlayer = currentlyScheduledPlayer;
		}
		currentlyScheduledPlayer = goodScheduledPlayers.get(lastGoodScheduledPlayerIndex);
	}

	/**
	 * Schedules next selected player belonging to the bad team.
	 */
	private void scheduleBad() {
		if (lastBadScheduledPlayerIndex == badScheduledPlayers.size() - 1) {
			lastBadScheduledPlayerIndex = 0;
		} else {
			lastBadScheduledPlayerIndex++;
		}
		if (currentlyScheduledPlayer != null) {     // Null only on first run
			previouslyScheduledPlayer = currentlyScheduledPlayer;
		}
		currentlyScheduledPlayer = badScheduledPlayers.get(lastBadScheduledPlayerIndex);
	}

	/**
	 * Sends a message to the new scheduled player that his turn has begun.
	 */
	private void notifyTurnBegin() {
		if (currentlyScheduledPlayer == null) {
			throw new IllegalStateException("Currently scheduled player still set as null");
		}
		Server.connectionManager.sendMessageToPlayer(currentlyScheduledPlayer, new Message(
				Message.Type.TIME,
				new Player("SERVER", Player.Team.SERVER),
				MessageManager.createXML(new MessageTable("header", "TURN_BEGIN"))
		));
	}

	/**
	 * Sends a message to the previously scheduled player that his turn has ended.
	 */
	private void notifyTurnEnd() {
		if (previouslyScheduledPlayer == null) {
			throw new IllegalStateException("Previously scheduled player still set as null");
		}
		Server.connectionManager.sendMessageToPlayer(previouslyScheduledPlayer, new Message(
				Message.Type.TIME,
				new Player("SERVER", Player.Team.SERVER),
				MessageManager.createXML(new MessageTable("header", "TURN_END"))
		));
	}

	/**
	 * Schedules next selected element.
	 */
	@Override
	public void scheduleNext() {
		if (goodScheduledPlayers.size() + badScheduledPlayers.size() == 1) {    // Single user mode
			currentlyScheduledPlayer = goodScheduledPlayers.isEmpty() ?
					badScheduledPlayers.get(0) : goodScheduledPlayers.get(0);
		} else {
			switch (lastScheduledTeam) {
				case GOOD:
					scheduleBad();
					lastScheduledTeam = Player.Team.BAD;
					break;
				case BAD:
					scheduleGood();
					lastScheduledTeam = Player.Team.GOOD;
					break;
			}
		}
		if (previouslyScheduledPlayer != null) {
			notifyTurnEnd();
		}
		notifyTurnBegin();
	}

	/**
	 * Gets currently scheduled element.
	 * @return Latest scheduled element.
	 */
	@Override
	public Player getScheduledElement() {
		return currentlyScheduledPlayer;
	}
}
