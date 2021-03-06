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

package shared.gaming;

import shared.scenery.Place;

import java.security.InvalidParameterException;

/**
 * Player implementation.
 */
public class Player {

	/**
	 * Name of the player.
	 */
	private String name;

	/**
	 * Team of the player.
	 */
	private Team team;

	/**
	 * Current player position in the scenery.
	 */
	private Place position;

	/**
	 * Total number of bullets owned by the player.
	 */
	private int bullets;

	/**
	 * Possible team for the player.
	 * - SERVER: Only used for emulating the central server during some message passing operations.
	 * - GOOD: Player belongs to the "Good" team.
	 * - BAD: Player belongs to the "Bad" team.
	 * - UGLY: Only used internally for implementing "the ugly" player.
	 */
	public enum Team {
		SERVER,
		GOOD,
		BAD,
		UGLY,
	}

	/**
	 * Creates a new player.
	 * @param name Name of the player.
	 * @param team Team of the player.
	 */
	public Player(String name, Team team) {
		if (name == null) {
			throw new InvalidParameterException("shared.gaming.Player name cannot be null");
		}
		if (team == null) {
			throw new InvalidParameterException("shared.gaming.Player team cannot be null");
		}
		this.name = name;
		this.team = team;
		bullets = 0;
	}

	/**
	 * Creates a new player.
	 * @param name Name of the player.
	 * @param team String representing the team of the player. This value is parsed into the appropriate corresponding
	 *             team value.
	 */
	public Player(String name, String team) {
		if (name == null) {
			throw new InvalidParameterException("shared.gaming.Player name cannot be null");
		}
		if (team == null) {
			throw new InvalidParameterException("shared.gaming.Player team cannot be null");
		}
		if (team.equals("SERVER")) {
			this.name = name;
			this.team = Team.SERVER;
			bullets = 0;
		} else if (team.equals("GOOD")) {
			this.name = name;
			this.team = Team.GOOD;
			bullets = 0;
		} else if (team.equals("BAD")) {
			this.name = name;
			this.team = Team.BAD;
			bullets = 0;
		} else if (team.equals("UGLY")) {
			this.name = name;
			this.team = Team.UGLY;
			bullets = 0;
		} else {
			throw new InvalidParameterException("Unrecognized team string");
		}
	}

	/**
	 * Gets name of the player.
	 * @return Name of the player.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets team of the player.
	 * @return Team of the player.
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Gets current position of the player.
	 * @return Position of the player.
	 */
	public Place getPosition()
	{
		return position;
	}

	/**
	 * Sets current position of the player.
	 * @param position Position of the player.
	 */
	public void setPosition(Place position) {
		if (position == null) {
			throw new InvalidParameterException("shared.gaming.Player position cannot be null");
		}
		this.position = position;
	}

	/**
	 * Gets total number of bullets that player has.
	 * @return Number of bullets.
	 */
	public int getBullets() {
		return bullets;
	}

	/**
	 * Add some bullets to the current number of bullets of the player.
	 * @param bullets Bullets to be added to the player.
	 */
	public void addBullets(int bullets) {
		this.bullets += bullets;
	}

	/**
	 * Remove all bullets from the player.
	 */
	public void removeBullets() {
		bullets = 0;
	}

	/**
	 * Removes a single bullet from the player. The bullet is not removed if bullets number is zero.
	 */
	public void stoleBullet() {
		if (bullets > 0) {
			bullets--;
		}
	}

	/**
	 * Translates the current team as its string representation.
	 * @return Current team expressed as an uppercase string.
	 */
	public String getTeamAsString() {
		switch (team) {
			case SERVER:
				return "SERVER";
			case GOOD:
				return "GOOD";
			case BAD:
				return "BAD";
			case UGLY:
				return "UGLY";
		}
		return null;
	}

	/**
	 * Checks if two players are equal.
	 * @param object Player to be compared.
	 * @return True if the players are the same, false if not.
	 */
	@Override
	public final boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Player)) {
			return false;
		}
		return (name.equals(((Player) object).name) && team.equals(((Player) object).team));
	}
}
