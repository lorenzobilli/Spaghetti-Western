package shared.scenery;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.security.InvalidParameterException;

/**
 * shared.scenery.Path class
 */
public class Path extends DefaultWeightedEdge {

    private int cost;

    public Path() {
        setCost(0);
    }

    public Path(int cost) {
        setCost(cost);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        if (cost < 0) {
            throw new InvalidParameterException("shared.scenery.Path weight cannot be less than zero");
        }
        this.cost = cost;
    }

}
