import java.security.InvalidParameterException;

/**
 * Player class
 */
public class Player {

    private String name;
    private Team team;

    public enum Team {
        SERVER,
        GOOD,
        BAD,
        UGLY,
    }

    public Player(String name, Team team) {
        if (name == null) {
            throw new InvalidParameterException("Player name cannot be null");
        }
        if (team == null) {
            throw new InvalidParameterException("Player team cannot be null");
        }
        this.name = name;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }
}
