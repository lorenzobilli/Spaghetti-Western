package server.scheduler;

import server.Server;
import server.connection.ConnectionHandler;
import shared.gaming.Player;

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
	private ArrayList<ConnectionHandler> goodScheduledPlayers = new ArrayList<>();

	/**
	 * List of schedulabel players belonging to the bad team.
	 */
	private ArrayList<ConnectionHandler> badScheduledPlayers = new ArrayList<>();

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
				goodScheduledPlayers.add(Server.connectionManager.getPlayerHandler(scheduledPlayer));
				break;
			case BAD:
				badScheduledPlayers.add(Server.connectionManager.getPlayerHandler(scheduledPlayer));
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
				goodScheduledPlayers.remove(Server.connectionManager.getPlayerHandler(scheduledPlayer));
				break;
			case BAD:
				badScheduledPlayers.remove(Server.connectionManager.getPlayerHandler(scheduledPlayer));
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
		}
	}

	/**
	 * Schedules next selected player belonging to the bad team.
	 */
	private void scheduleBad() {
		if (lastBadScheduledPlayerIndex == badScheduledPlayers.size() - 1) {
			lastBadScheduledPlayerIndex = 0;
		}
	}

	/**
	 * Schedules next selected element from the scheduler.
	 */
	@Override
	public void scheduleNext() {
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
}
