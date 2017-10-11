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

    public Player(String name, String team) {
    	if (name == null) {
    		throw new InvalidParameterException("Player name cannot be null");
		}
		if (team == null) {
    		throw new InvalidParameterException("Player team cannot be null");
		}
		if (team.equals("SERVER")) {
    		this.name = name;
    		this.team = Team.SERVER;
		} else if (team.equals("GOOD")) {
    		this.name = name;
    		this.team = Team.GOOD;
		} else if (team.equals("BAD")) {
    		this.name = name;
    		this.team = Team.BAD;
		} else if (team.equals("UGLY")) {
    		this.name = name;
    		this.team = Team.UGLY;
		} else {
    		throw new InvalidParameterException("Unrecognized team string");
		}
	}

    public String getName() {
        return name;
    }

    public Team getTeam() {
        return team;
    }

    //TODO: Is there a better way?
    public String getTeamAsString() {
    	switch (team) {
			case SERVER:
				return "SERVER";
			case GOOD:
				return "GOOD";
			case BAD:
				return "BAD";
			case UGLY:
				return "UGLY";
		}
		return null;
	}

	@Override
	public final boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Player)) {
			return false;
		}
		return (name.equals(((Player) object).name) && team.equals(((Player) object).team));
	}
}
