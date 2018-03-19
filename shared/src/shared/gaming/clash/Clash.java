package shared.gaming.clash;

import shared.gaming.Player;
import shared.utils.Dice;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Manages all clash-related routines, such as clash creation, deciding winners and losers, and getting clash results.
 */
public class Clash {

	/**
	 * Enumeration used to determine which team is the winner, either attackers or defenders.
	 */
	public enum Winners {
		ATTACK,
		DEFENSE,
	}

	/**
	 * Dice of type attack used for attacking.
	 */
	private Dice attackDice = new Dice(Dice.Type.ATTACK);

	/**
	 * Dice of type defense used for defending.
	 */
	private Dice defenseDice = new Dice(Dice.Type.DEFENSE);

	/**
	 * Stores attack results.
	 */
	private List<Integer> attackResult = new ArrayList<>();

	/**
	 * Stores defense results.
	 */
	private List<Integer> defenseResult = new ArrayList<>();

	/**
	 * Creates a new clash and determines the winners by throwing the correct number of dice.
	 * @param attackers List of attacking players.
	 * @param defenders List of defending players.
	 * @return The winners, using a Winners enumeration.
	 */
	public Winners doClash(List<Player> attackers, List<Player> defenders) {

		int attackDiceNumber = attackers.size();
		int defenseDiceNumber = defenders.size();

		// Throw a dice for each attacker
		while (attackDiceNumber != 0) {
			attackDice.throwDice();
			try {
				attackResult.add(attackDice.getValue());
			} catch (Exception e) {
				e.getMessage();
			}
			attackDiceNumber--;
		}

		// Throw a dice for each defender
		while (defenseDiceNumber != 0) {
			defenseDice.throwDice();
			try {
				defenseResult.add(defenseDice.getValue());
			} catch (Exception e) {
				e.getCause();
			}
			defenseDiceNumber--;
		}

		return getWinners();
	}

	/**
	 * Internal method which determines who is the winning team of the clash.
	 * Points are given in the same manner as in the "Risiko" game:
	 *  - Dice are sorted in descending order.
	 *  - Each attack dice is compared in order to the corresponding defense dice, the higher dice gets the point.
	 *  - Defense has priority over attack, so if an attack dice has the same value of a defense dice, the point is
	 *    given to the defense.
	 *  - If there is a mismatch in attack and defense dice numbers, the missing dice number is automatically given to
	 *    the player with the higher number of dice (i.e. three defense dice vs. two attack dice: a base point is
	 *    assigned to defense).
	 * @return A Winners enumeration which contains the winning team.
	 */
	private Winners getWinners() {

		int attackersPoints = 0;
		int defendersPoints = 0;

		// Sort results for dice matching in descending order
		attackResult.sort((o1, o2) -> Integer.compare(o2, o1));
		defenseResult.sort((o1, o2) -> Integer.compare(o2, o1));

		// Same number of dice: no base points
		if (attackResult.size() == defenseResult.size()) {
			for (Integer attack : attackResult) {
				for (Integer defence : defenseResult) {
					if (defence >= attack) {
						defendersPoints++;
					} else {
						attackersPoints++;
					}
				}
			}
		}

		// More attack dice: assign base points to attackers, then compute remaining points.
		if (attackResult.size() > defenseResult.size()) {
			attackersPoints = attackResult.size() - defenseResult.size();
			for (int index = 0; index < defenseResult.size(); index++) {
				if (attackResult.get(index) > defenseResult.get(index)) {
					attackersPoints++;
				} else {
					defendersPoints++;
				}
			}
		}

		// More defense dice: assign base points to defenders, then compute remaining points.
		if (defenseResult.size() > attackResult.size()) {
			defendersPoints = defenseResult.size() - attackResult.size();
			for (int index = 0; index < attackResult.size(); index++) {
				if (defenseResult.get(index) >= attackResult.get(index)) {
					defendersPoints++;
				} else {
					attackersPoints++;
				}
			}
		}

		return defendersPoints >= attackersPoints ? Winners.DEFENSE : Winners.ATTACK;
	}

	/**
	 * Gets the attack results.
	 * @return A list of integers containing all attack results.
	 */
	public List<Integer> getAttackResult() {
		if (attackResult == null) {
			throw new NoSuchElementException("Clash has not been made yet");
		}
		return attackResult;
	}

	/**
	 * Gets the defense results.
	 * @return A list of integers containing all defense results.
	 */
	public List<Integer> getDefenseResult() {
		if (defenseResult == null) {
			throw new NoSuchElementException("Clash has not been made yet");
		}
		return defenseResult;
	}
}
