package server.gaming;

import server.Server;
import server.scheduler.RoundRobinScheduler;
import server.scheduler.Scheduler;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.gaming.Player;
import shared.utils.Randomizer;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * server.gaming.TimeManager class
 */
public class TimeManager implements Callable<Boolean> {

	//public final static Duration WAIT_TIME = Duration.ofMinutes(5);
	private final static Duration WAIT_TIME = Duration.ofSeconds(30);    //FIXME: Temporary value for testing purposes only
	private final static Duration PLAY_TIME = Duration.ofMinutes(10);
	private final static Duration TURN_TIME = Duration.ofSeconds(15);	//FIXME: Exact value T.B.D.

	private final boolean DEBUG_MODE = false;

    private Duration waitDuration = WAIT_TIME;
    private Duration playDuration = PLAY_TIME;
    private Duration turnDuration = playDuration;
    private boolean firstTurn = true;
    private Timer timer;
    private TimerTask countdown;

    public TimeManager() {
        timer = new Timer();
        waitTimer();
    }

    private void waitTimer() {
		Server.consolePrintLine("[*] Session wait timer started");
    	countdown = new TimerTask() {
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
					Server.consolePrintLine("[*] Session wait timer expired");
					Server.consolePrintLine("[*] Choosing new scenery based on connected players...");
					Server.sessionManager.chooseScenery();
					Server.consolePrintLine("[*] Spawning players inside scenery graph...");
					Server.sessionManager.putPlayers();
					Server.consolePrintLine("[*] Initiating turn scheduler...");
					Server.turnScheduler.initializeScheduler(
							Randomizer.getRandomInteger() % 2 == 0 ? Player.Team.GOOD : Player.Team.BAD
					);
					playTimer();
				}
			}
		};
	}

	private void playTimer() {
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

		countdown = new TimerTask() {
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

				if (turnDuration.minus(playDuration).isZero() || turnDuration.minus(playDuration) == TURN_TIME) {
					if (Server.turnScheduler.isSchedulerEnabled()) {
						Server.turnScheduler.scheduleNext();
					}
					turnDuration = playDuration;
				}

				if (Server.turnScheduler.isSchedulerEnabled()) {
					MessageTable turnRemainingMessageTable = new MessageTable();
					turnRemainingMessageTable.put("header", "TURN_REMAINING");
					turnRemainingMessageTable.put("content", String.valueOf(
							(TURN_TIME.minus(turnDuration.minus(playDuration))).getSeconds()
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

				if (playDuration.isZero()) {
					this.cancel();
					Server.connectionManager.broadcastMessage(new Message(
							Message.Type.TIME,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML(new MessageTable("header", "PLAY_TIMEOUT"))
					));
					Server.sessionManager.setSessionState(false);
					Server.consolePrintLine("[*] Session play timer expired");
					//TODO: Add here winners declaration
				}
			}
		};
	}

    @Override
    public Boolean call() {
        timer.scheduleAtFixedRate(countdown, 0, 1000);      // Repeat every second
        return false;
    }
}
