/**
 * MediumScenery class
 */
public class MediumScenery extends Scenery {

    public MediumScenery() {

    	final String background = "shared/assets/medium_scenery.jpg";
    	setSceneryBackground(background);
		setPlacesAndPaths();
    }

	@Override
	protected void setPlacesAndPaths() {
		// Defined scenery places
		Place santaFe = new Place("Santa Fe", 1);
		Place sanRafael = new Place("San Rafael", 2);
		Place valverde = new Place("Valverde", 3);
		Place watermill = new Place("Watermill", 4);
		Place desert = new Place("Desert", 5);
		Place canyonDiablo = new Place("Canyon Diablo", 6);
		Place phoenix = new Place("Phoenix", 7);
		Place tucson = new Place("Tucson", 8);
		Place elPaso = new Place("El Paso", 9);
		Place unionPrison = new Place("Union prison", 10);

		// Adding defined places to scenery hashmap
		sceneryPlaces.put(santaFe.getPlaceName(), santaFe);
		sceneryPlaces.put(sanRafael.getPlaceName(), sanRafael);
		sceneryPlaces.put(valverde.getPlaceName(), valverde);
		sceneryPlaces.put(watermill.getPlaceName(), watermill);
		sceneryPlaces.put(desert.getPlaceName(), desert);
		sceneryPlaces.put(canyonDiablo.getPlaceName(), canyonDiablo);
		sceneryPlaces.put(phoenix.getPlaceName(), phoenix);
		sceneryPlaces.put(tucson.getPlaceName(), tucson);
		sceneryPlaces.put(elPaso.getPlaceName(), elPaso);
		sceneryPlaces.put(unionPrison.getPlaceName(), unionPrison);

		// Adding defined places to scenery graph
		sceneryGraph.addVertex(santaFe);
		sceneryGraph.addVertex(sanRafael);
		sceneryGraph.addVertex(valverde);
		sceneryGraph.addVertex(watermill);
		sceneryGraph.addVertex(desert);
		sceneryGraph.addVertex(canyonDiablo);
		sceneryGraph.addVertex(phoenix);
		sceneryGraph.addVertex(tucson);
		sceneryGraph.addVertex(elPaso);
		sceneryGraph.addVertex(unionPrison);

		// Defined scenery paths
		Path santaFeWatermill = new Path();
		Path santaFeSanRafael = new Path();
		Path santaFeValverde = new Path();
		Path santaFeDesert = new Path();
		Path santaFeElPaso = new Path();
		Path santaFeTucson = new Path();
		Path watermillValverde = new Path();
		Path watermillUnionPrison = new Path();
		Path sanRafaelValverde = new Path();
		Path sanRafaelDesert = new Path();
		Path sanRafaelCanyonDiablo = new Path();
		Path sanRafaelElPaso = new Path();
		Path sanRafaelTucson = new Path();
		Path valverdeDesert = new Path();
		Path valverdeDesertLong = new Path();
		Path valverdeTucson = new Path();
		Path valverdeElPaso = new Path();
		Path valverdeUnionPrison = new Path();
		Path canyonDiabloPhoenix = new Path();
		Path phoenixTucson = new Path();
		Path tucsonDesert = new Path();
		Path tucsonElPaso = new Path();
		Path elPasoDesert = new Path();
		Path desertUnionPrison = new Path();

		// Adding defined paths to scenery graph
		sceneryGraph.addEdge(santaFe, watermill, santaFeWatermill);
		sceneryGraph.addEdge(santaFe, sanRafael, santaFeSanRafael);
		sceneryGraph.addEdge(santaFe, valverde, santaFeValverde);
		sceneryGraph.addEdge(santaFe, desert, santaFeDesert);
		sceneryGraph.addEdge(santaFe, elPaso, santaFeElPaso);
		sceneryGraph.addEdge(santaFe, tucson, santaFeTucson);
		sceneryGraph.addEdge(watermill, valverde, watermillValverde);
		sceneryGraph.addEdge(watermill, unionPrison, watermillUnionPrison);
		sceneryGraph.addEdge(sanRafael, valverde, sanRafaelValverde);
		sceneryGraph.addEdge(sanRafael, desert, sanRafaelDesert);
		sceneryGraph.addEdge(sanRafael, canyonDiablo, sanRafaelCanyonDiablo);
		sceneryGraph.addEdge(sanRafael, elPaso, sanRafaelElPaso);
		sceneryGraph.addEdge(sanRafael, tucson, sanRafaelTucson);
		sceneryGraph.addEdge(valverde, desert, valverdeDesert);
		sceneryGraph.addEdge(valverde, desert, valverdeDesertLong);
		sceneryGraph.addEdge(valverde, tucson, valverdeTucson);
		sceneryGraph.addEdge(valverde, elPaso, valverdeElPaso);
		sceneryGraph.addEdge(valverde, unionPrison, valverdeUnionPrison);
		sceneryGraph.addEdge(canyonDiablo, phoenix, canyonDiabloPhoenix);
		sceneryGraph.addEdge(phoenix, tucson, phoenixTucson);
		sceneryGraph.addEdge(tucson, desert, tucsonDesert);
		sceneryGraph.addEdge(tucson, elPaso, tucsonElPaso);
		sceneryGraph.addEdge(elPaso, desert, elPasoDesert);
		sceneryGraph.addEdge(desert, unionPrison, desertUnionPrison);
	}
}
