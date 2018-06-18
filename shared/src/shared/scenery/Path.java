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
