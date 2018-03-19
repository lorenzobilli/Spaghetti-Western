package shared.utils;

/**
 * Models the dice object. A dice can either be an attack dice or a defense dice. All dice have six faces.
 */
public class Dice {

	/**
	 * Determines the number of faces. This is a standard six faces cubic dice.
	 */
	private final int FACES_NUMBER = 6;

	/**
	 * Stores the type of the dice, either attack or defense.
	 */
	public enum Type {
		ATTACK,
		DEFENSE
	}

	/**
	 * Stores the type of the dice
	 */
	private Type type;

	/**
	 * Stores the value of the dice
	 */
	private int value;

	/**
	 * Checks if the dice has already been thrown or not.
	 */
	private boolean thrown;

	/**
	 * Creates a new dice of a specific type.
	 * @param type Type of the dice
	 */
	public Dice(Type type) {
		this.type = type;
	}

	/**
	 * Gets the value of the dice.
	 * @return Value of the dice.
	 */
	public int getValue() throws Exception {
		if (!thrown) {
			throw new Exception("Dice has not been thrown yet.");
		}
		thrown = false;
		return value;
	}

	/**
	 * Gets the type of the dice.
	 * @return Type of the dice.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Throws the dice.
	 */
	public void throwDice() {
		value = Randomizer.getRandomInteger(FACES_NUMBER);
		thrown = true;
	}
}
