package shared.scenery;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.security.InvalidParameterException;

/**
 * Path implementation. A path is defined as a link between two Place objects.
 */
public class Path extends DefaultWeightedEdge {

	/**
	 * Cost of the path. If the cost is set to 0, the path is considered as non-weighted.
	 */
	private int cost;

	/**
	 * Creates a new non-weighted path.
	 */
    public Path() {
        setCost(0);
    }

	/**
	 * Creates a new weighted path with the given cost.
	 * @param cost Cost of the path.
	 */
	public Path(int cost) {
        setCost(cost);
    }

	/**
	 * Gets cost of the path.
	 * @return Cost of the path.
	 */
	public int getCost() {
        return cost;
    }

	/**
	 * Sets cost of the path.
	 * @param cost Cost of the path.
	 */
	public void setCost(int cost) {
        if (cost < 0) {
            throw new InvalidParameterException("shared.scenery.Path weight cannot be less than zero");
        }
        this.cost = cost;
    }

}
