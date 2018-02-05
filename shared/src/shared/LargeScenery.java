package shared;

/**
 * shared.LargeScenery class
 */
public class LargeScenery extends Scenery {

    public LargeScenery() {

    	final String background = "shared/assets/large_scenery.jpg";
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
		Place santaAna = new Place("Santa Ana", 10);
		Place langstoneBridge = new Place("Langstone bridge", 11);
		Place sadHillGraveyard = new Place("Sad Hill graveyard", 12);
		Place confederatePrison = new Place("Confederate prison", 13);
		Place unionPrison = new Place("Union prison", 14);
		Place saintAnthonyMission = new Place("Saint Anthony mission", 15);

		// Adding defined places to corresponding hashmap
		namePlaces.put(santaFe.getPlaceName(), santaFe);
		namePlaces.put(sanRafael.getPlaceName(), sanRafael);
		namePlaces.put(valverde.getPlaceName(), valverde);
		namePlaces.put(watermill.getPlaceName(), watermill);
		namePlaces.put(desert.getPlaceName(), desert);
		namePlaces.put(canyonDiablo.getPlaceName(), canyonDiablo);
		namePlaces.put(phoenix.getPlaceName(), phoenix);
		namePlaces.put(tucson.getPlaceName(), tucson);
		namePlaces.put(elPaso.getPlaceName(), elPaso);
		namePlaces.put(santaAna.getPlaceName(), santaAna);
		namePlaces.put(langstoneBridge.getPlaceName(), langstoneBridge);
		namePlaces.put(sadHillGraveyard.getPlaceName(), sadHillGraveyard);
		namePlaces.put(confederatePrison.getPlaceName(), confederatePrison);
		namePlaces.put(unionPrison.getPlaceName(), unionPrison);
		namePlaces.put(saintAnthonyMission.getPlaceName(), saintAnthonyMission);

		// Adding defined ids to corresponding hashmap
		idPlaces.put(santaFe.getPlaceId(), santaFe);
		idPlaces.put(sanRafael.getPlaceId(), sanRafael);
		idPlaces.put(valverde.getPlaceId(), valverde);
		idPlaces.put(watermill.getPlaceId(), watermill);
		idPlaces.put(desert.getPlaceId(), desert);
		idPlaces.put(canyonDiablo.getPlaceId(), canyonDiablo);
		idPlaces.put(phoenix.getPlaceId(), phoenix);
		idPlaces.put(tucson.getPlaceId(), tucson);
		idPlaces.put(elPaso.getPlaceId(), elPaso);
		idPlaces.put(santaAna.getPlaceId(), santaAna);
		idPlaces.put(langstoneBridge.getPlaceId(), langstoneBridge);
		idPlaces.put(sadHillGraveyard.getPlaceId(), sadHillGraveyard);
		idPlaces.put(confederatePrison.getPlaceId(), confederatePrison);
		idPlaces.put(unionPrison.getPlaceId(), unionPrison);
		idPlaces.put(saintAnthonyMission.getPlaceId(), saintAnthonyMission);

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
		sceneryGraph.addVertex(santaAna);
		sceneryGraph.addVertex(langstoneBridge);
		sceneryGraph.addVertex(sadHillGraveyard);
		sceneryGraph.addVertex(confederatePrison);
		sceneryGraph.addVertex(unionPrison);
		sceneryGraph.addVertex(saintAnthonyMission);

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
		Path tucsonSantaAna = new Path();
		Path elPasoDesert = new Path();
		Path elPasoLangstoneBridge = new Path();
		Path langstoneBridgeSadHillGraveyard = new Path();
		Path desertUnionPrison = new Path();
		Path desertConfederatePrison = new Path();
		Path unionPrisonConfederatePrison = new Path();
		Path confederatePrisonSaintAnthonyMission = new Path();

		// Adding defined graphs to scenery graph
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
		sceneryGraph.addEdge(tucson, santaAna, tucsonSantaAna);
		sceneryGraph.addEdge(elPaso, desert, elPasoDesert);
		sceneryGraph.addEdge(elPaso, langstoneBridge, elPasoLangstoneBridge);
		sceneryGraph.addEdge(langstoneBridge, sadHillGraveyard, langstoneBridgeSadHillGraveyard);
		sceneryGraph.addEdge(desert, unionPrison, desertUnionPrison);
		sceneryGraph.addEdge(desert, confederatePrison, desertConfederatePrison);
		sceneryGraph.addEdge(unionPrison, confederatePrison, unionPrisonConfederatePrison);
		sceneryGraph.addEdge(confederatePrison, saintAnthonyMission, confederatePrisonSaintAnthonyMission);
	}
}
