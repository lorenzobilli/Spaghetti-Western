import java.security.InvalidParameterException;
import java.time.Duration;
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

    public TimeManager(int seconds) {
        if (seconds < 0) {
            throw new InvalidParameterException("Seconds cannot be a negative value");
        }
        duration = Duration.ofSeconds(seconds);
        timer = new Timer();
        countdown = new TimerTask() {
            @Override
            public void run() {
                duration.minusSeconds(1);
                if (duration.isZero()) {
                    this.cancel();
                }
            }
        };
    }

    public TimeManager(int minutes, int seconds) {
        this(seconds);
        if (minutes < 0) {
            throw new InvalidParameterException("Minutes cannot be a negative value");
        }
        duration = Duration.ofMinutes(minutes);
    }

    public TimeManager(int hours, int minutes, int seconds) {
        this(seconds, minutes);
        if (hours < 0) {
            throw new InvalidParameterException("Hours cannot be a negative value");
        }
        duration = Duration.ofHours(hours);
    }

    @Override
    public Boolean call() throws Exception {
        timer.scheduleAtFixedRate(countdown, 0, 1000);      // Repeat every second
        return false;
    }
}
