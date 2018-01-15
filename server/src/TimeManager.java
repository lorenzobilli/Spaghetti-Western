import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * TimeManager class
 */
public class TimeManager implements Callable<Boolean> {

	//public final static Duration WAIT_TIME = Duration.ofMinutes(5);
	private final static Duration WAIT_TIME = Duration.ofSeconds(30);    //FIXME: Temporary value for testing purposes only
	private final static Duration PLAY_TIME = Duration.ofMinutes(10);
	private final static Duration TURN_TIME = Duration.ofSeconds(15);	//FIXME: Exact value T.B.D.

    private Duration waitDuration = WAIT_TIME;
    private Duration playDuration = PLAY_TIME;
    private Duration turnDuration = PLAY_TIME;
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
				Server.connectionManager.broadcastMessage(new Message(
						MessageType.TIME,
						new Player("SERVER", Player.Team.SERVER),
						MessageManager.createXML(
								new ArrayList<>(Arrays.asList(
										"header", "content"
								)),
								new ArrayList<>(Arrays.asList(
										"WAIT_REMAINING", String.valueOf(waitDuration.getSeconds())
								))
						)
				));
				if (waitDuration.isZero()) {
					this.cancel();
					Server.connectionManager.broadcastMessage(new Message(
							MessageType.TIME,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML("header", "WAIT_TIMEOUT")
					));
					Server.connectionManager.setSessionRunning(true);
					Server.consolePrintLine("[*] Session wait timer expired");
					Server.consolePrintLine("[*] Choosing new scenery based on connected players...");
					Server.connectionManager.chooseScenery();
					Server.consolePrintLine("[*] Spawning players inside scenery graph...");
					Server.connectionManager.putPlayers();
					playTimer();
				}
			}
		};
	}

	private void playTimer() {
		Server.consolePrintLine("[*] Starting new gaming session...");
		Server.connectionManager.broadcastMessage(new Message(
				MessageType.TIME,
				new Player("SERVER", Player.Team.SERVER),
				MessageManager.createXML("header", "PLAY_SESSION_START")
		));
		countdown = new TimerTask() {
			@Override
			public void run() {
				if (firstTurn) {
					firstTurn = false;
					//TODO: Handle turn change here
				}
				playDuration = playDuration.minusSeconds(1);
				Server.connectionManager.broadcastMessage(new Message(
						MessageType.TIME,
						new Player("SERVER", Player.Team.SERVER),
						MessageManager.createXML(
								new ArrayList<>(Arrays.asList(
										"header", "content"
								)),
								new ArrayList<>(Arrays.asList(
										"PLAY_REMAINING", String.valueOf(playDuration.getSeconds())
								))
						)
				));
				if (playDuration.isZero()) {
					this.cancel();
					Server.connectionManager.broadcastMessage(new Message(
							MessageType.TIME,
							new Player("SERVER", Player.Team.SERVER),
							MessageManager.createXML("header", "PLAY_TIMEOUT")
					));
					Server.connectionManager.setSessionRunning(false);
					Server.consolePrintLine("[*] Session play timer expired");
					//TODO: Add here winners declaration
				}
				if (turnDuration.minus(playDuration) == TURN_TIME) {
					turnDuration = playDuration;
					//TODO: Handle turn change here
				}
			}
		};
	}

    @Override
    public Boolean call() throws Exception {
        timer.scheduleAtFixedRate(countdown, 0, 1000);      // Repeat every second
        return false;
    }
}
