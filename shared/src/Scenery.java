import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * Scenery class
 */
public abstract class Scenery {

    private Graph<Place, Path> sceneryGraph = new SimpleWeightedGraph<>(Path.class);

}
