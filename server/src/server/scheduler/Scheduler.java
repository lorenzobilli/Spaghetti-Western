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

import shared.gaming.Player;

/**
 * Common interface for all possible schedulers in the project.
 */
public interface Scheduler {

	/**
	 * Enables the scheduler.
	 */
	void enableScheduler();

	/**
	 * Disables the scheduler.
	 */
	void disableScheduler();

	/**
	 * Checks whether the scheduler is enabled.
	 * @return True if the scheduler is enabled, false if not.
	 */
	boolean isSchedulerEnabled();

	/**
	 * Initializes the scheduler.
	 * @param firstScheduledTeam Team that will be first scheduled.
	 */
	void initializeScheduler(Player.Team firstScheduledTeam);

	/**
	 * Adds a schedulable element to the scheduler. A schedulable element corresponds to a Player object.
	 * @param scheduledPlayer Player to be added to the scheduler.
	 */
	void addScheduledElement(Player scheduledPlayer);

	/**
	 * Removes a schedulable element from the scheduler. A schedulable element corresponds to a Player object.
	 * @param scheduledPlayer Player to be removed from the scheduler.
	 */
	void removeScheduledElement(Player scheduledPlayer);

	/**
	 * Schedules next selected element from the scheduler.
	 */
	void scheduleNext();

	/**
	 * Gets currently scheduled element.
	 * @return The currently active scheduled Player.
	 */
	Player getScheduledElement();
}
