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
	 * @param bound Maximum value considered in the random number generator.
	 * @return A random integer value between 1 (inclusive) and given bound (inclusive).
	 */
	public static int getRandomInteger(int bound) {
		return random.nextInt(bound) + 1;
	}

	/**
	 * Gets a random long value.
	 * @return A random long value.
	 */
	public static long getRandomLong() {
		return random.nextLong();
	}

	/**
	 * Gets a random long value between 1 (inclusive) and given bound (inclusive).
	 * @param bound Maximum value considered in the random number generator.
	 * @return A random long value between 1 (inclusive) and given bound (inclusive).
	 */
	public static long getRandomLong(long bound) {
		return (long) random.nextInt((int) bound) + 1;
	}

	/**
	 * Gets a random boolean value.
	 * @return A random boolean value.
	 */
	public static boolean getRandomBoolean() {
		return random.nextBoolean();
	}
}
