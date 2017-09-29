import org.jgrapht.graph.DefaultWeightedEdge;

import java.security.InvalidParameterException;

/**
 * Path class
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
            throw new InvalidParameterException("Path weight cannot be less than zero");
        }
        this.cost = cost;
    }

}
