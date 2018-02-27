package server.time;

import server.Server;

public class TimeManager {

	public static void launchWaitingTimer() {
		Server.globalThreadPool.submit(new WaitingTimerTask());
	}

	public static void launchPlayingTimer() {
		Server.globalThreadPool.submit(new PlayingTimerTask());
	}
}
