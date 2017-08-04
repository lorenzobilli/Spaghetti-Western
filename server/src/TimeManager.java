import java.security.InvalidParameterException;
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

    private Duration duration;
    private Timer timer;
    private TimerTask countdown;

    public TimeManager(Duration time) {
        if (time == null) {
            throw new InvalidParameterException("Duration cannot be null");
        }
        if (time.isZero()) {
            throw new InvalidParameterException("Duration cannot be zero");
        }
        duration = time;
        timer = new Timer();
        countdown = new TimerTask() {
            @Override
            public void run() {
                duration = duration.minusSeconds(1);
                Server.connectionManager.broadcastMessage(new Message(
                        MessageType.TIME,
                        "SERVER",
                        MessageManager.createXML(
                                new ArrayList<>(Arrays.asList(
                                       "header", "content"
                                )),
                                new ArrayList<>(Arrays.asList(
                                        "WAIT_REMAINING", String.valueOf(duration.getSeconds())
                                ))
                        )
                ));
                if (duration.isZero()) {
                    this.cancel();
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
