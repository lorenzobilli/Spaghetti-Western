/**
 * Dice class
 */
public class Dice {

	public final int FACES_NUMBER = 6;

	public enum Type {
		ATTACK,
		DEFENSE
	}

	private int value;
	private Type type;

	public Dice(Type type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	public void throwDice() {
		value = Randomizer.getRandomInteger(FACES_NUMBER);
	}
}
