import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.security.InvalidParameterException;

/**
 * Scenery class
 */
public abstract class Scenery {

    private Graph<Place, Path> sceneryGraph = new SimpleWeightedGraph<>(Path.class);

    private enum SceneryEvents {
        PLAYER_MOVED,
        PLAYER_NOT_FOUND,
        DESTINATION_BUSY
    }

    public SceneryEvents movePlayer(Player player, Place origin, Place destination) {
        if (!sceneryGraph.containsVertex(origin)) {
            throw new InvalidParameterException("Specified origin doesn't exist inside scenery");
        }
        if (!sceneryGraph.containsVertex(destination)) {
            throw new InvalidParameterException("Specified destination doesn't exist inside scenery");
        }

        if (!origin.isPlayerPresent(player)) {
            return SceneryEvents.PLAYER_NOT_FOUND;
        }
        if (destination.addPlayer(player)) {
            origin.removePlayer(player);
            return SceneryEvents.PLAYER_MOVED;
        } else {
            return SceneryEvents.DESTINATION_BUSY;
        }
    }
}
