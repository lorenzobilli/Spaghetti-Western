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

package shared.scenery;

import shared.gaming.Player;
import shared.gaming.clash.ClashManager;
import shared.utils.Randomizer;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Place object implementation.
 */
public class Place {

	/**
	 * Maximum number of good-team players that can be present in the current place at the same time.
	 */
	private final int MAX_GOOD_PLAYERS = 3;

	/**
	 * Maximum number of bad-team players that can be present in the current place at the same time.
	 */
	private final int MAX_BAD_PLAYERS = 3;

	/**
	 * Maximum number of bullets that can be present at the initialization time of an instance of a place.
	 */
	private final int MAX_BULLETS = 50;

	/**
	 * Name of the place.
	 */
	private String placeName;

	/**
	 * Unique ID of the place.
	 */
	private int placeId;

	/**
	 * List of currently present good-team players in the place.
	 */
	private List<Player> goodPlayers;

	/**
	 * List of currently present bad-team players in the place.
	 */
	private List<Player> badPlayers;

	/**
	 * Checks if the special ugly player is present in the current place.
	 */
	private boolean isUglyPresent;

	/**
	 * Handle all data-racing critical operation about clashes.
	 */
	private ClashManager clashManager;

	/**
	 * Number of bullets present in the place.
	 */
	private int bullets;

	/**
	 * Creates a new place with given name and ID.
	 * @param placeName Name of the place.
	 * @param placeId ID of the place
	 */
	public Place(String placeName, int placeId) {
		if (placeName == null) {
			throw new InvalidParameterException("shared.scenery.Place name cannot be null");
		}
		if (placeId <= 0) {
			throw new InvalidParameterException("shared.scenery.Place id cannot be zero or negative");
		}
		if (placeName.equals("")) {
			throw new InvalidParameterException("shared.scenery.Place name cannot be empty");
		}
		this.placeName = placeName;
		this.placeId = placeId;
		goodPlayers = new ArrayList<>();
		badPlayers = new ArrayList<>();
		clashManager = new ClashManager();
		bullets = Randomizer.getRandomInteger(MAX_BULLETS);
	}

	/**
	 * Gets name of the place.
	 * @return Name of the place.
	 */
	public String getPlaceName() {
		return placeName;
	}

	/**
	 * Gets ID of the place.
	 * @return ID of the place.
	 */
	public int getPlaceId() {
		return placeId;
	}

	/**
	 * Picks up an half of the present bullets.
	 * If bullets number is odd, total bullets number is first decreased by one.
	 * @return Number of picked bullets.
	 */
	public int pickBullets() {
		if (bullets == 0) {
			return 0;
		}
		if (bullets % 2 != 0) {
			bullets--;
		}
		int takenBullets = bullets / 2;
		bullets /= 2;
		return takenBullets;
	}

	/**
	 * Adds a player in the current place.
	 * @param player Player to be added.
	 * @return True if player has been correctly added, false if not.
	 */
	public boolean addPlayer(Player player) {
		if (player == null) {
			throw new InvalidParameterException("shared.gaming.Player to be added cannot be null");
		}

		if (clashManager.isClashRunning()) {
			return false;
		}

		boolean playerAdded = false;
		switch (player.getTeam()) {
			case GOOD:
				if (goodPlayers.size() < MAX_GOOD_PLAYERS) {
					goodPlayers.add(player);
					checkClash();
					playerAdded = true;
				}
				break;
			case BAD:
				if (badPlayers.size() < MAX_BAD_PLAYERS) {
					badPlayers.add(player);
					checkClash();
					playerAdded = true;
				}
				break;
			case UGLY:
				isUglyPresent = true;
				playerAdded = true;
				break;
		}
		return playerAdded;
	}

	/**
	 * Removes an existing player from the current place.
	 * @param player Player to be removed
	 * @return True if player has been correctly remove, false if not.
	 */
	public boolean removePlayer(Player player) {
		if (player == null) {
			throw new InvalidParameterException("shared.gaming.Player to be removed cannot be null");
		}

		if (clashManager.isClashRunning()) {
			return false;
		}

		boolean playerRemoved = false;
		switch (player.getTeam()) {
			case GOOD:
				if (goodPlayers.remove(player)) {
					checkClash();
					playerRemoved = true;
				}
				break;
			case BAD:
				if (badPlayers.remove(player)) {
					checkClash();
					playerRemoved = true;
				}
				break;
			case UGLY:
				isUglyPresent = false;
				playerRemoved = true;
				break;
		}
		return playerRemoved;
	}

	/**
	 * Checks if a player is present in the current place.
	 * @param player Player to be checked.
	 * @return True if the player is present, false if not.
	 */
	public boolean isPlayerPresent(Player player) {
		if (player == null) {
			throw new InvalidParameterException("shared.gaming.Player to be checked cannot be null");
		}

		boolean playerFound = false;
		switch (player.getTeam()) {
			case GOOD:
				if (goodPlayers.contains(player)) {
					playerFound = true;
				}
				break;
			case BAD:
				if (badPlayers.contains(player)) {
					playerFound = true;
				}
				break;
			case UGLY:
				return isUglyPresent;
		}
		return playerFound;
	}

	/**
	 * Gets all good-team players present in the current place.
	 * @return List of present good-team players
	 */
	public List<Player> getGoodPlayers() {
		return goodPlayers;
	}

	/**
	 * Gets all bad-team players present in the current place.
	 * @return List of present bad-team players.
	 */
	public List<Player> getBadPlayers() {
		return badPlayers;
	}

	/**
	 * Gets all players present in the current place.
	 * @return List of present players.
	 */
	public List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<>();
		players.addAll(goodPlayers);
		players.addAll(badPlayers);
		return players;
	}

	/**
	 * Gets reference to the current instance of the clash manager.
	 * @return Instance of the current clash manager.
	 */
	public ClashManager getClashManager() {
		return clashManager;
	}

	/**
	 * Checks wherever to enable or disable clashes.
	 */
	private void checkClash() {
		if (goodPlayers.size() > 0 && badPlayers.size() > 0) {
			clashManager.enableClash();
		} else {
			clashManager.disableClash();
		}
	}

	/**
	 * Checks if two places are equal.
	 * @param object Place to be compared.
	 * @return True if the places are the same, false if not.
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Place)) {
			return false;
		}
		return (placeName.equals(((Place) object).placeName) && placeId == ((Place) object).placeId);
	}
}
