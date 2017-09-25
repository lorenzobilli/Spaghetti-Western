/**
 * DefaultScenery class
 */
public class DefaultScenery extends Scenery {

    /*
    This is only for testing purposes. Actual sceneries are still missing from the source tree
     */
    public DefaultScenery(String sceneryBackgroundImage) {

        super(sceneryBackgroundImage);

        Place tucson = new Place("Tucson");
        Place jefferson = new Place("Jefferson");
        Place elpaso = new Place("El Paso");
        Place calico = new Place("Calico");
        Place bodie = new Place("Bodie");
        Place canyondiablo = new Place("Canyon Diablo");

        sceneryGraph.addVertex(tucson);
        sceneryGraph.addVertex(jefferson);
        sceneryGraph.addVertex(elpaso);
        sceneryGraph.addVertex(calico);
        sceneryGraph.addVertex(bodie);
        sceneryGraph.addVertex(canyondiablo);

        sceneryGraph.addEdge(tucson, canyondiablo);
        sceneryGraph.addEdge(canyondiablo, bodie);
        sceneryGraph.addEdge(bodie, calico);
        sceneryGraph.addEdge(calico, elpaso);
        sceneryGraph.addEdge(elpaso, jefferson);
        sceneryGraph.addEdge(jefferson, tucson);
        sceneryGraph.addEdge(canyondiablo, elpaso);

    }
}
