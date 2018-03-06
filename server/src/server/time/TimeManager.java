package server.time;

import server.Server;

/**
 * Handles all time-related operations.
 */
public class TimeManager {

	/**
	 * Creates a new waiting timer and passes it to the global thread pool.
	 */
	public static void launchWaitingTimer() {
		Server.globalThreadPool.submit(new WaitingTimerTask());
	}

	/**
	 * Creates a new playing timer and passes it to the global thread pool.
	 */
	public static void launchPlayingTimer() {
		Server.globalThreadPool.submit(new PlayingTimerTask());
	}
}
