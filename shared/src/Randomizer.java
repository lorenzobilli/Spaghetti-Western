import java.util.Random;

/**
 * Randomizer class
 */
public class Randomizer {

	private static Random random = new Random();

	public static int getRandomInteger() {
		return random.nextInt();
	}

	public static int getRandomInteger(int bound) {
		return random.nextInt(bound) + 1;	// Zero exclusive, bound number inclusive
	}

	public static boolean getRandomBoolean() {
		return random.nextBoolean();
	}
}
