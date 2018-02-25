package shared.utils;

import java.util.Random;

/**
 * Random number generator class. Mostly equal to the standard Java's Random class, this class encapsulates integer,
 * integer with bound and boolean random values generation.
 */
public class Randomizer {

	/**
	 * Internal random object used to generate random values.
	 */
	private static Random random = new Random();

	/**
	 * Gets a random integer value.
	 * @return A random integer value.
	 */
	public static int getRandomInteger() {
		return random.nextInt();
	}

	/**
	 * Gets a random integer value between 1 (inclusive) and given bound (inclusive).
	 * @param bound Maximum inclusive value considered in the randomic number generator.
	 * @return A random integer value between 1 (inclusive) and given bound (inclusive).
	 */
	public static int getRandomInteger(int bound) {
		return random.nextInt(bound) + 1;	// Zero exclusive, bound number inclusive
	}

	/**
	 * Gets a random boolean value.
	 * @return A random boolean value.
	 */
	public static boolean getRandomBoolean() {
		return random.nextBoolean();
	}
}
