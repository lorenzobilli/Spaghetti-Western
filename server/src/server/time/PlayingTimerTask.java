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

public class PlayingTimerTask implements Callable<Void> {

	private final boolean DEBUG_MODE = false;

	private final Duration PLAY = Duration.ofMinutes(10);
	private final Duration TURN = Duration.ofSeconds(15);   //FIXME: Exact value to be defined

	private Duration playDuration;
	private Duration turnDuration;
	private Duration uglyMovement;
	private Timer playTimer;
	private TimerTask playCountdown;

	public PlayingTimerTask() {
		playDuration = PLAY;
		turnDuration = playDuration;
		selectRandomUglyDuration();
		playTimer = new Timer();
		playTimerRoutine();
	}

	private void selectRandomUglyDuration() {
		long randomSeconds = Randomizer.getRandomLong(turnDuration.getSeconds() * 3);
		uglyMovement = Duration.ofSeconds(randomSeconds);
	}

	private void playTimerRoutine() {
		Server.consolePrintLine("[*] Starting new gaming session...");
		Server.connectionManager.broadcastMessage(new Message(
				Message.Type.TIME,
				new Player("SERVER", Player.Team.SERVER),
				MessageManager.createXML(new MessageTable("header", "PLAY_SESSION_START"))
		));

		if (DEBUG_MODE) {
			Server.turnScheduler.disableScheduler();
			Server.consolePrintLine("[!] Debug mode selected, turn scheduler disabled");
		} else {
			Server.turnScheduler.enableScheduler();
		}

		playCountdown = new TimerTask() {
			@Override
			public void run() {
				MessageTable playRemainingMessageTable = new MessageTable();
				playRemainingMessageTable.put("header", "PLAY_REMAINING");
				playRemainingMessageTable.put("content", String.valueOf(playDuration.getSeconds()));
				Server.connectionManager.broadcastMessage(new Message(
						Message.Type.TIME,
						new Player("SERVER", Player.Team.SERVER),
						MessageManager.createXML(playRemainingMessageTable)
				));

				if (turnDuration.minus(playDuration).isZero() || turnDuration.minus(playDuration).equals(TURN)) {
					if (Server.turnScheduler.isSchedulerEnabled()) {
						Server.turnScheduler.scheduleNext();
					}
					turnDuration = playDuration;
				}

				if (uglyMovement.isZero()) {
					//TODO: Add here ugly player movement
					selectRandomUglyDuration();
				}

				if (Server.turnScheduler.isSchedulerEnabled()) {
					MessageTable turnRemainingMessageTable = new MessageTable();
					turnRemainingMessageTable.put("header", "TURN_REMAINING");
					turnRemainingMessageTable.put("content", String.valueOf(
							(TURN.minus(turnDuration.minus(playDuration))).getSeconds()
					));
					Server.connectionManager.sendMessageToPlayer(Server.turnScheduler.getScheduledElement(),
							new Message(
									Message.Type.TIME,
									new Player("SERVER", Player.Team.SERVER),
									MessageManager.createXML(turnRemainingMessageTable)
							)
					);
				}

				playDuration = playDuration.minusSeconds(1);
				uglyMovement = uglyMovement.minusSeconds(1);

				if (playDuration.isZero()) {
					this.cancel();
					Server.connectionManager.broadcastMessage(new Message(
							Message.Type.TIME,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(new MessageTable("header", "PLAY_TIMEOUT"))
					));
					Server.sessionManager.setSessionState(false);
					Server.consolePrintLine("[*] Session play waitTimer expired");
					//TODO: Add here winners declaration
				}
			}
		};
	}

	@Override
	public Void call() {
		// Repeat this every second
		playTimer.scheduleAtFixedRate(playCountdown, 0, 1000);
		return null;
	}
}
