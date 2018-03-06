package shared.gaming;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Handles all points-related operations.
 */
public class PointsManager {

	/**
	 * Get total amount of prize of the doClash.
	 * @param losers List of players who had lost the doClash.
	 * @return Number of bullets representing the prize.
	 */
	public static int getPrize(List<Player> losers) {
		if (losers == null) {
			throw new InvalidParameterException("Loosers list cannot be null");
		}
		int totalPrize = 0;
		for (Player looser : losers) {
			totalPrize += looser.getBullets();
			looser.removeBullets();
		}
		switch (losers.size()) {
			case 1:
				return totalPrize;
			case 2:
				if (totalPrize % 2 != 0) {
					return (totalPrize - 1) / 2;
				} else {
					return totalPrize / 2;
				}
			case 3:
				if (totalPrize % 3 != 0) {
					while (totalPrize % 3 != 0) {
						totalPrize--;
					}
				}
				return totalPrize / 3;
		}
		return totalPrize;
	}

	/**
	 * Gives an equal part of the total prize to each winner.
	 * @param winners List of players who had won the doClash.
	 * @param totalPrize Total amount of prize of the doClash.
	 */
	public static void distributePrize(List<Player> winners, int totalPrize) {
		if (winners == null) {
			throw new InvalidParameterException("Winners list cannot be null");
		}
		if (totalPrize < 0) {
			throw new InvalidParameterException("Total prize cannot be negative");
		}
		if (totalPrize == 0) {
			return;
		}
		switch (winners.size()) {
			case 1:
				winners.get(0).addBullets(totalPrize);
				break;
			case 2:
				if (totalPrize % 2 != 0) {
					for (Player winner : winners) {
						winner.addBullets((totalPrize - 1) / 2);
					}
				} else {
					for (Player winner : winners) {
						winner.addBullets(totalPrize / 2);
					}
				}
				break;
			case 3:
				if (totalPrize % 3 != 0) {
					while (totalPrize % 3 != 0) {
						totalPrize--;
					}
				}
				for (Player winner : winners) {
					winner.addBullets(totalPrize / 3);
				}
				break;
		}
		return;
	}
}
