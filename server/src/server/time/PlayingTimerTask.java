package server.time;

import server.Server;
import shared.gaming.Player;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.Place;
import shared.scenery.Scenery;
import shared.utils.Randomizer;

import java.time.Duration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * This timers implements the playing timer.
 * The playing timer controls how much should the playing session lasts.
 */
public class PlayingTimerTask implements Callable<Void> {

	/**
	 * Enables debugging mode.
	 */
	private final boolean DEBUG_MODE = false;

	/**
	 * Determines how much this timer should last.
	 */
	//private final Duration PLAY = Duration.ofMinutes(10);
	private final Duration PLAY = Duration.ofMinutes(1);    //FIXME: To be removed!

	/**
	 * Determines turn interval time.
	 * This value must always assert this statement: PLAY % TURN == 0.
	 */
	private final Duration TURN = Duration.ofSeconds(15);   //FIXME: Exact value to be defined

	/**
	 * Duration value used by the timer.
	 */
	private Duration playDuration;

	/**
	 * Duration value used to determine the turns.
	 */
	private Duration turnDuration;

	/**
	 * Value of waiting before the ugly player can be moved in the scenery.
	 */
	private Duration uglyMovement;

	/**
	 * Internal timer object.
	 */
	private Timer playTimer;

	/**
	 * Internal timer task used for all timers operations.
	 */
	private TimerTask playCountdown;

	/**
	 * Creates a new playing timer.
	 */
	public PlayingTimerTask() {
		assert PLAY.getSeconds() % TURN.getSeconds() == 0;
		playDuration = PLAY;
		turnDuration = playDuration;
		selectRandomUglyDuration();
		playTimer = new Timer();
		playTimerRoutine();
	}

	/**
	 * Randomly choose a value for the uglyMovement duration.
	 */
	private void selectRandomUglyDuration() {
		long randomSeconds = Randomizer.getRandomLong(TURN.getSeconds() * 3);
		uglyMovement = Duration.ofSeconds(randomSeconds);
		Server.consolePrintLine("New random waiting value for ugly player selected: " + randomSeconds);
	}

	/**
	 * Moves the ugly player.
	 */
	private void moveUglyPlayer() {

		MessageTable messageTable = Server.sessionManager.chooseAndMoveUglyPlayer();
		Place origin = Server.getScenery().getNamePlaces().get(messageTable.get("origin"));
		Place destination = Server.getScenery().getNamePlaces().get(messageTable.get("destination"));

		Scenery.SceneryEvent result = Server.getScenery().movePlayer(Server.uglyPlayer, origin, destination);

		if (result == Scenery.SceneryEvent.PLAYER_MOVED) {

			Server.uglyPlayer.setPosition(destination);

			Server.consolePrintLine("Ugly player moved from scenery place \"" + origin.getPlaceName() +
					"\" to scenery place \"" + destination.getPlaceName() + "\"");

			messageTable.put("header", "PLAYER_MOVED");
			messageTable.put("player_name", Server.uglyPlayer.getName());
			messageTable.put("player_team", Server.uglyPlayer.getTeamAsString());

			Server.connectionManager.broadcastMessage(new Message(
					Message.Type.SCENERY,
					new Player("SERVER", Player.Team.SERVER),
					MessageManager.createXML(messageTable)
			));

			List<Player> victims = destination.getAllPlayers();
			for (Player player : victims) {
				Server.connectionManager.sendMessageToPlayer(player, new Message(
						Message.Type.CLASH,
						new Player("SERVER", Player.Team.SERVER),
						player,
						MessageManager.createXML(new MessageTable("header", "UGLY_VISIT"))
				));
				Server.connectionManager.getPlayerHandler(player).getConnectedPlayer().removeBullets();
			}
		}

		selectRandomUglyDuration();
	}

	/**
	 * Sends to the scheduled player a message containing the remaining time.
	 */
	private void sendRemainingTurnTime() {
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

	/**
	 * Broadcasts that the timer is up.
	 */
	private void sendTimeOutSignal() {
		Server.connectionManager.broadcastMessage(new Message(
				Message.Type.TIME,
				new Player("SERVER", Player.Team.SERVER),
				MessageManager.createXML(new MessageTable("header", "PLAY_TIMEOUT"))
		));
		Server.sessionManager.setSessionState(false);
		Server.consolePrintLine("Session play waitTimer expired");
		Server.sessionManager.declareWinners();
	}

	/**
	 * Broadcasts a message with the total left time amount, then when the turn period is up, schedules a new player
	 * for the turn.
	 */
	private void playTimerRoutine() {
		Server.consolePrintLine("Starting new gaming session...");
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
					Server.consolePrintLine("Ugly player selected for moving...");
					moveUglyPlayer();
				}

				if (Server.turnScheduler.isSchedulerEnabled()) {
					sendRemainingTurnTime();
				}

				playDuration = playDuration.minusSeconds(1);
				uglyMovement = uglyMovement.minusSeconds(1);

				if (playDuration.isZero()) {
					this.cancel();
					sendTimeOutSignal();
					Server.resetServer();
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
		playTimer.scheduleAtFixedRate(playCountdown, 0, 1000);
		return null;
	}
}
