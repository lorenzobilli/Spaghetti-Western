import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ClashManager class
 */
public class ClashManager {

	public enum Winners {
		ATTACK,
		DEFENSE,
	}

	private Dice attackDice = new Dice(Dice.Type.ATTACK);
	private Dice defenseDice = new Dice(Dice.Type.DEFENSE);
	private List<Integer> attackResult;
	private List<Integer> defenseResult;

	public Winners clash(List<Player> attackers, List<Player> defenders) {
		attackResult = new ArrayList<>();
		defenseResult = new ArrayList<>();

		int attackDiceNumber = attackers.size();
		int defenseDiceNumber = defenders.size();

		while (attackDiceNumber != 0) {
			attackDice.throwDice();
			attackResult.add(attackDice.getValue());
			attackDiceNumber--;
		}
		while (defenseDiceNumber != 0) {
			defenseDice.throwDice();
			defenseResult.add(defenseDice.getValue());
			defenseDiceNumber--;
		}

		attackResult.sort((o1, o2) -> Integer.compare(o2, o1));
		defenseResult.sort((o1, o2) -> Integer.compare(o2, o1));

		return getWinners();
	}

	private Winners getWinners() {
		int attackersPoints = 0;
		int defendersPoints = 0;

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

	public List<Integer> getAttackResult() {
		if (attackResult == null) {
			throw new NoSuchElementException("Clash has not been made yet");
		}
		return attackResult;
	}

	public List<Integer> getDefenseResult() {
		if (defenseResult == null) {
			throw new NoSuchElementException("Clash has not been made yet");
		}
		return defenseResult;
	}
}
