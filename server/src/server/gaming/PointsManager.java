package server.gaming;

import server.connection.ConnectionHandler;
import shared.gaming.Player;

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
