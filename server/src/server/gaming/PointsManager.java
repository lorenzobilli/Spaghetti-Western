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

package server.gaming;

import server.connection.ConnectionHandler;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Handles all points-related operations.
 */
public class PointsManager {

	/**
	 * Gives an equal amount of points to each winner, by removing all points of the losers.
	 * @param winners List of winning players references.
	 * @param losers List of losing players references.
	 * @return Amount of prize given to each winner.
	 */
	public static int distributePrize(List<ConnectionHandler> winners, List<ConnectionHandler> losers) {
		if (winners == null) {
			throw new InvalidParameterException("Winners list cannot be null");
		}
		if (losers == null) {
			throw new InvalidParameterException("Losers list cannot be null");
		}

		int totalPrize = 0;
		for (ConnectionHandler loser : losers) {
			totalPrize += loser.getConnectedPlayer().getBullets();
			loser.getConnectedPlayer().removeBullets();
		}

		if (totalPrize == 0) {
			return 0;
		}

		int partialPrize = 0;

		switch (winners.size()) {
			case 1:
				partialPrize = totalPrize;
				winners.get(0).getConnectedPlayer().addBullets(partialPrize);
				break;
			case 2:
				if (totalPrize % 2 != 0) {
					partialPrize = (totalPrize - 1) / 2;
				} else {
					partialPrize = totalPrize / 2;
				}
				for (ConnectionHandler winner : winners) {
					winner.getConnectedPlayer().addBullets(partialPrize);
				}
				break;
			case 3:
				while (totalPrize % 3 != 0) {
					totalPrize--;
				}
				partialPrize = totalPrize / 3;
				for (ConnectionHandler winner : winners) {
					winner.getConnectedPlayer().addBullets(partialPrize);
				}
				break;
		}

		return partialPrize;
	}
}
