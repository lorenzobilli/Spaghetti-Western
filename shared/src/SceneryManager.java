import java.security.InvalidParameterException;

/**
 * SceneryManager class
 */
public class SceneryManager {

    private Scenery scenery;

    public SceneryManager(Scenery scenery) {
        if (scenery == null) {
            throw new InvalidParameterException("Choosen scenery cannot be null");
        }
        this.scenery = scenery;
    }

}
