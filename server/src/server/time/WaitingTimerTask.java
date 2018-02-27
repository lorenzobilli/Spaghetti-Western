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

public class WaitingTimerTask implements Callable<Void> {

	private final boolean DEBUG_MODE = true;

	private final Duration WAIT = Duration.ofMinutes(5);

	private Duration waitDuration;
	private Timer waitTimer;
	private TimerTask waitCountdown;

	public WaitingTimerTask() {
		if (DEBUG_MODE) {
			waitDuration = Duration.ofSeconds(30);
		} else {
			waitDuration = WAIT;
		}
		waitTimer = new Timer();
		waitTimerRoutine();
	}

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

	@Override
	public Void call() {
		// Repeat this every second
		waitTimer.scheduleAtFixedRate(waitCountdown, 0, 1000);
		return null;
	}
}
