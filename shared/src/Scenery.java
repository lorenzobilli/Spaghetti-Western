import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Scenery class
 */
public abstract class Scenery {

    private String sceneryBackground;
    protected Graph<Place, Path> sceneryGraph = new SimpleWeightedGraph<>(Path.class);
    protected HashMap<String, Place> namePlaces = new HashMap<>();
    protected HashMap<Integer, Place> idPlaces = new HashMap<>();

    public enum SceneryEvents {
    	PLAYER_INSERTED,
        PLAYER_MOVED,
        DESTINATION_BUSY
    }

    public String getSceneryBackground() {
        return sceneryBackground;
    }

    public void setSceneryBackground(String sceneryBackground) {
        if (sceneryBackground == null) {
            throw new InvalidParameterException("Scenery background cannot be null");
        }
        this.sceneryBackground = sceneryBackground;
    }

    protected abstract void setPlacesAndPaths();

	public HashMap<String, Place> getNamePlaces() {
		return namePlaces;
	}

	public HashMap<Integer, Place> getIdPlaces() {
		return idPlaces;
	}

	public int getPlacesNumber() {
		return namePlaces.size();
	}

	public SceneryEvents insertPlayer(Player player, int placeId) {
		for (Place place : namePlaces.values()) {
			if (place.getPlaceId() == placeId) {
				return insertPlayer(player, place);
			}
		}
		return SceneryEvents.DESTINATION_BUSY;
	}

	public SceneryEvents insertPlayer(Player player, Place place) {
		if (!sceneryGraph.containsVertex(place)) {
			throw new InvalidParameterException("Specified place doesn't exist inside scenery");
		}
		if (place.addPlayer(player)) {
			return SceneryEvents.PLAYER_INSERTED;
		} else {
			return SceneryEvents.DESTINATION_BUSY;
		}
	}

	public SceneryEvents movePlayer(Player player, Place origin, Place destination) {
        if (!sceneryGraph.containsVertex(origin)) {
            throw new InvalidParameterException("Specified origin doesn't exist inside scenery");
        }
        if (!sceneryGraph.containsVertex(destination)) {
            throw new InvalidParameterException("Specified destination doesn't exist inside scenery");
        }
        if (!origin.isPlayerPresent(player)) {
        	throw new InvalidParameterException("Specified player is not present inside scenery");
        }

        if (destination.addPlayer(player)) {
            origin.removePlayer(player);
            return SceneryEvents.PLAYER_MOVED;
        } else {
            return SceneryEvents.DESTINATION_BUSY;
        }
    }
}
