/*
 *  Project: "Spaghetti Western"
 *
 *
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2017-2018 Lorenzo Billi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *	documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *	rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *	permit persons to whom the Software is	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 *	the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *	WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *	OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *	OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package shared.scenery;

/**
 * Implementation of a large size scenery.
 * A large scenery is meant to be used when connected players number is over 20.
 */
public class LargeScenery extends Scenery {

	/**
	 * Creates a new large scenery.
	 */
	public LargeScenery() {
		setPlacesAndPaths();
	}

	/**
	 * Initializes the scenery by creating all places and linking them together.
	 */
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
