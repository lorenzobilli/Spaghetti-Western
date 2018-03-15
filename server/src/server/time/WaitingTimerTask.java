package server.time;

import server.Server;
import shared.gaming.Player;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.utils.Randomizer;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * This timer implements the waiting timer.
 * The waiting timer controls how much time should pass before starting the gaming session. During this period, players
 * can join the next playing session.
 */
public class WaitingTimerTask implements Callable<Void> {

	/**
	 * Enables debugging mode.
	 */
	private final boolean DEBUG_MODE = true;

	/**
	 * Determines how much this timer should last.
	 */
	private final Duration WAIT = Duration.ofMinutes(5);

	/**
	 * Duration value used by the timer.
	 */
	private Duration waitDuration;

	/**
	 * Internal timer object.
	 */
	private Timer waitTimer;

	/**
	 * Internal timer task used for all timers operations.
	 */
	private TimerTask waitCountdown;

	/**
	 * Creates a new waiting timer.
	 */
	public WaitingTimerTask() {
		if (DEBUG_MODE) {
			waitDuration = Duration.ofSeconds(5);
		} else {
			waitDuration = WAIT;
		}
		waitTimer = new Timer();
		waitTimerRoutine();
	}

	/**
	 * Broadcasts a message with the left time amount, then when timer is up, it initializes the scenery and the
	 * scheduler.
	 */
	private void waitTimerRoutine() {
		Server.consolePrintLine("[*] Session wait timer started");
		waitCountdown = new TimerTask() {
			@Override
			public void run() {
				waitDuration = waitDuration.minusSeconds(1);
				MessageTable messageTable = new MessageTable();
				messageTable.put("header", "WAIT_REMAINING");
				messageTable.put("content", String.valueOf(waitDuration.getSeconds()));
				Server.connectionManager.broadcastMessage(new Message(
						Message.Type.TIME,
						new Player("SERVER", Player.Team.SERVER),
						MessageManager.createXML(messageTable)
				));
				if (waitDuration.isZero()) {
					this.cancel();
					Server.connectionManager.broadcastMessage(new Message(
							Message.Type.TIME,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(new MessageTable("header", "WAIT_TIMEOUT"))
					));
					Server.sessionManager.setSessionState(true);
					Server.consolePrintLine("[*] Session wait waitTimer expired");
					Server.consolePrintLine("[*] Choosing new scenery based on connected players...");
					Server.sessionManager.chooseScenery();
					Server.consolePrintLine("[*] Spawning players inside scenery graph...");
					Server.sessionManager.putPlayers();
					Server.consolePrintLine("[*] Initiating turn scheduler...");
					Server.turnScheduler.initializeScheduler(
							Randomizer.getRandomInteger() % 2 == 0 ? Player.Team.GOOD : Player.Team.BAD
					);
					TimeManager.launchPlayingTimer();
				}
			}
		};
	}

	/**
	 * Strokes the seconds used by the timer.
	 * @return This method returns no value.
	 */
	@Override
	public Void call() {
		// Repeat this every second
		waitTimer.scheduleAtFixedRate(waitCountdown, 0, 1000);
		return null;
	}
}
