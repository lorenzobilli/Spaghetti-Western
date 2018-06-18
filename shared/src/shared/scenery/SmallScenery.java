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
 * Implementation of a small size scenery.
 * A small scenery is meant to be used when connected players number is below 10.
 */
public class SmallScenery extends Scenery {

	/**
	 * Creates a new small scenery.
	 */
	public SmallScenery() {
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

		// Adding defined places to corresponding hashmap
		namePlaces.put(santaFe.getPlaceName(), santaFe);
		namePlaces.put(sanRafael.getPlaceName(), sanRafael);
		namePlaces.put(valverde.getPlaceName(), valverde);
		namePlaces.put(watermill.getPlaceName(), watermill);
		namePlaces.put(desert.getPlaceName(), desert);

		// Adding defined ids to corresponding hashmap
		idPlaces.put(santaFe.getPlaceId(), santaFe);
		idPlaces.put(sanRafael.getPlaceId(), sanRafael);
		idPlaces.put(valverde.getPlaceId(), valverde);
		idPlaces.put(watermill.getPlaceId(), watermill);
		idPlaces.put(desert.getPlaceId(), desert);

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
