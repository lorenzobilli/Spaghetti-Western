import java.security.InvalidParameterException;

/**
 * Player class
 */
public class Player {

    private String name;
    private Team team;
    private int bullets;

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
        bullets = 0;
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
    		bullets = 0;
		} else if (team.equals("GOOD")) {
    		this.name = name;
    		this.team = Team.GOOD;
    		bullets = 0;
		} else if (team.equals("BAD")) {
    		this.name = name;
    		this.team = Team.BAD;
    		bullets = 0;
		} else if (team.equals("UGLY")) {
    		this.name = name;
    		this.team = Team.UGLY;
    		bullets = 0;
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

    public int getBullets() {
    	return bullets;
	}

	public void addBullets(int bullets) {
    	this.bullets += bullets;
	}

	public void removeBullets() {
    	bullets = 0;
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

	public void takeBullets(Place place) {
    	bullets = place.pickBullets();
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
