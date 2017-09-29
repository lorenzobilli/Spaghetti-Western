/**
 * SmallScenery class
 */
public class SmallScenery extends Scenery {

    public SmallScenery() {

		final String background = "shared/assets/small_scenery.jpg";
		setSceneryBackground(background);

		// Defined scenery places
		Place santaFe = new Place("Santa Fe");
		Place sanRafael = new Place("San Rafael");
		Place valverde = new Place("Valverde");
		Place watermill = new Place("Watermill");
		Place desert = new Place("Desert");

		// Adding defined places to scenery graph
		sceneryGraph.addVertex(santaFe);
		sceneryGraph.addVertex(sanRafael);
		sceneryGraph.addVertex(valverde);
		sceneryGraph.addVertex(watermill);
		sceneryGraph.addVertex(desert);

		// Defined scenery paths
		Path santaFeWatermill = new Path();
		Path santaFeSanRafael = new Path();
		Path santaFeValverde = new Path();
		Path santaFeDesert = new Path();
		Path watermillValverde = new Path();
		Path sanRafaelValverde = new Path();
		Path sanRafaelDesert = new Path();
		Path valverdeDesert = new Path();

		// Adding defined paths to scenery graph
		sceneryGraph.addEdge(santaFe, watermill, santaFeWatermill);
		sceneryGraph.addEdge(santaFe, sanRafael, santaFeSanRafael);
		sceneryGraph.addEdge(santaFe, valverde, santaFeValverde);
		sceneryGraph.addEdge(santaFe, desert, santaFeDesert);
		sceneryGraph.addEdge(watermill, valverde, watermillValverde);
		sceneryGraph.addEdge(sanRafael, valverde, sanRafaelValverde);
		sceneryGraph.addEdge(sanRafael, desert, sanRafaelDesert);
		sceneryGraph.addEdge(valverde, desert, valverdeDesert);

    }
}
