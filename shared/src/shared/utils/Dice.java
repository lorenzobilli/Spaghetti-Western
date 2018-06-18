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
