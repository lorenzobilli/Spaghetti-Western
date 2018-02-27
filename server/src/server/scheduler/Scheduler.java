package server.scheduler;

import shared.gaming.Player;

/**
 * Common interface for all possible schedulers in the project.
 */
public interface Scheduler {

	/**
	 * Enables the scheduler.
	 */
	public void enableScheduler();

	/**
	 * Disables the scheduler.
	 */
	public void disableScheduler();

	/**
	 * Checks whether the scheduler is enabled.
	 * @return True if the scheduler is enabled, false if not.
	 */
	public boolean isSchedulerEnabled();

	/**
	 * Initializes the scheduler.
	 * @param firstScheduledTeam Team that will be first scheduled.
	 */
	public void initializeScheduler(Player.Team firstScheduledTeam);

	/**
	 * Adds a schedulable element to the scheduler. A schedulable element corresponds to a Player object.
	 * @param scheduledPlayer Player to be added to the scheduler.
	 */
	public void addScheduledElement(Player scheduledPlayer);

	/**
	 * Removes a schedulable element from the scheduler. A schedulable element corresponds to a Player object.
	 * @param scheduledPlayer Player to be removed from the scheduler.
	 */
	public void removeScheduledElement(Player scheduledPlayer);

	/**
	 * Schedules next selected element from the scheduler.
	 */
	public void scheduleNext();

	/**
	 * Gets currently scheduled element.
	 * @return The currently active scheduled Player.
	 */
	public Player getScheduledElement();
}
