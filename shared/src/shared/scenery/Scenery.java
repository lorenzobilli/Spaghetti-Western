package shared.scenery;

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleWeightedGraph;
import shared.gaming.Player;
import shared.messaging.MessageTable;
import shared.utils.Randomizer;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Set;

/**
 * Base class for all sceneries implementations.
 */
public abstract class Scenery {

	/**
	 * Simple weighted graph representing the scenery.
	 */
    protected Graph<Place, Path> sceneryGraph = new SimpleWeightedGraph<>(Path.class);

	/**
	 * Hash map containing names of scenery places.
	 */
	protected HashMap<String, Place> namePlaces = new HashMap<>();

	/**
	 * Hash map containing numeric id of scenery places.
	 */
    protected HashMap<Integer, Place> idPlaces = new HashMap<>();

	/**
	 * Enumeration representing possible events happening in the scenery.
	 * Possible events are:
	 *  - PLAYER_INSERTED: A player has been correctly inserted inside the scenery.
	 *  - PLAYER_MOVED: A player has been correctly moved from one place to another.
	 *  - DESTINATION_BUSY: Place where the player is trying to move is full.
	 *  - DESTINATION_UNREACHABLE: Place where the player is trying to move is not linked to the current place.
	 */
	public enum SceneryEvent {
    	PLAYER_INSERTED,
        PLAYER_MOVED,
        DESTINATION_BUSY,
		DESTINATION_UNREACHABLE
    }

	/**
	 * Initializes the scenery by creating all places and linking them together.
	 */
	protected abstract void setPlacesAndPaths();

	/**
	 * Gets hash map containing all places names.
	 * @return Hash map containing all places names.
	 */
	public HashMap<String, Place> getNamePlaces() {
		return namePlaces;
	}

	/**
	 * Gets hash map containing all places ids.
	 * @return Hash map containing all places ids.
	 */
	public HashMap<Integer, Place> getIdPlaces() {
		return idPlaces;
	}

	/**
	 * Gets number of places in the scenery.
	 * @return Number of places in the scenery.
	 */
	public int getPlacesNumber() {
		return namePlaces.size();
	}

	/**
	 * Gets all paths starting from given place.
	 * @param departure Place where paths start.
	 * @return A set containing all selected paths that start from given place.
	 */
	private Set<Path> getAllPaths(Place departure) {
		return sceneryGraph.edgesOf(departure);
	}

	/**
	 * Insert a player in the scenery.
	 * @param player Player to be added to the scenery.
	 * @param place Place where the player should be added.
	 * @return A SceneryEvent containing the result of the operation.
	 */
	public SceneryEvent insertPlayer(Player player, Place place) {
		if (!sceneryGraph.containsVertex(place)) {
			throw new InvalidParameterException("Specified place doesn't exist inside scenery");
		}
		if (place.addPlayer(player)) {
			return SceneryEvent.PLAYER_INSERTED;
		} else {
			return SceneryEvent.DESTINATION_BUSY;
		}
	}

	/**
	 * Move a player from one place to another.
	 * @param player Player to be moved.
	 * @param origin Current player position.
	 * @param destination Place where the player should be moved.
	 * @return A SceneryEvent containing the result of the operation.
	 */
	public SceneryEvent movePlayer(Player player, Place origin, Place destination) {
		if (!origin.isPlayerPresent(player)) {
			throw new InvalidParameterException("Specified player is not present inside scenery");
		}
        if (!sceneryGraph.containsVertex(origin)) {
            throw new InvalidParameterException("Specified origin doesn't exist inside scenery");
        }
        if (!sceneryGraph.containsVertex(destination)) {
            throw new InvalidParameterException("Specified destination doesn't exist inside scenery");
        }

        if (!sceneryGraph.containsEdge(origin, destination)) {
        	return SceneryEvent.DESTINATION_UNREACHABLE;
		}
        if (destination.addPlayer(player)) {
            origin.removePlayer(player);
            return SceneryEvent.PLAYER_MOVED;
        } else {
            return SceneryEvent.DESTINATION_BUSY;
        }
    }

	/**
	 * Move the ugly player randomly inside the scenery.
	 * The ugly player can be moved only between two linked places, as any other player in the scenery.
	 * @param uglyPlayer Reference to the moving ugly player.
	 * @return A MessageTable containing ugly player's place origin and final destination.
	 */
	public MessageTable moveUglyPlayer(Player uglyPlayer) {
		if (uglyPlayer == null) {
			throw new InvalidParameterException("Ugly player cannot be null");
		}
		if (!uglyPlayer.equals(new Player("UGLY", Player.Team.UGLY))) {
			throw new InvalidParameterException("Only an ugly player reference is allowed");
		}

		Set<Path> destinationPaths = getAllPaths(uglyPlayer.getPosition());
		int randomValue = Randomizer.getRandomInteger(destinationPaths.size());
		Path selectedPath = null;
		int current = 1;
		for (Path path : destinationPaths) {
			if (current == randomValue) {
				selectedPath = path;
				break;
			}
			current++;
		}
		movePlayer(uglyPlayer, sceneryGraph.getEdgeSource(selectedPath), sceneryGraph.getEdgeTarget(selectedPath));
		MessageTable messageTable = new MessageTable();
		messageTable.put("origin", sceneryGraph.getEdgeSource(selectedPath).toString());
		messageTable.put("destination", sceneryGraph.getEdgeTarget(selectedPath).toString());
		return messageTable;
    }
}
