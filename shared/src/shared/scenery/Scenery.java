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

import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleWeightedGraph;
import shared.gaming.Player;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Set;

/**
 * Base class for all sceneries implementations.
 */
public abstract class Scenery {

	/**
	 * Simple weighted graph representing the scenery.
	 */
	protected Graph<Place, Path> sceneryGraph = new SimpleWeightedGraph<>(Path.class);

	/**
	 * Hash map containing names of scenery places.
	 */
	protected HashMap<String, Place> namePlaces = new HashMap<>();

	/**
	 * Hash map containing numeric id of scenery places.
	 */
	protected HashMap<Integer, Place> idPlaces = new HashMap<>();

	/**
	 * Enumeration representing possible events happening in the scenery.
	 * Possible events are:
	 *  - PLAYER_INSERTED: A player has been correctly inserted inside the scenery.
	 *  - PLAYER_MOVED: A player has been correctly moved from one place to another.
	 *  - DESTINATION_BUSY: Place where the player is trying to move is full.
	 *  - DESTINATION_UNREACHABLE: Place where the player is trying to move is not linked to the current place.
	 */
	public enum SceneryEvent {
		PLAYER_INSERTED,
		PLAYER_MOVED,
		DESTINATION_BUSY,
		DESTINATION_UNREACHABLE
	}

	/**
	 * Initializes the scenery by creating all places and linking them together.
	 */
	protected abstract void setPlacesAndPaths();

	/**
	 * Gets hash map containing all places names.
	 * @return Hash map containing all places names.
	 */
	public HashMap<String, Place> getNamePlaces() {
		return namePlaces;
	}

	/**
	 * Gets hash map containing all places ids.
	 * @return Hash map containing all places ids.
	 */
	public HashMap<Integer, Place> getIdPlaces() {
		return idPlaces;
	}

	/**
	 * Gets number of places in the scenery.
	 * @return Number of places in the scenery.
	 */
	public int getPlacesNumber() {
		return namePlaces.size();
	}

	/**
	 * Gets all paths starting from given place.
	 * @param departure Place where paths start.
	 * @return A set containing all selected paths that start from given place.
	 */
	public Set<Path> getAllPaths(Place departure) {
		if (departure == null) {
			throw new InvalidParameterException("Departure place cannot be null");
		}
		return sceneryGraph.outgoingEdgesOf(departure);
	}

	/**
	 * Gets the starting point of a given path.
	 * @param path Path whose departure point should be calculated.
	 * @return Departure place of the given path.
	 */
	@Deprecated
	public Place getDeparture(Path path) {
		if (path == null) {
			throw new InvalidParameterException("Given path cannot be null");
		}
		return sceneryGraph.getEdgeSource(path);
	}

	/**
	 * Gets the arriving point of a given path.
	 * @param path Path whose destination point should be calculated.
	 * @return Destination place of the given path.
	 */
	@Deprecated
	public Place getDestination(Path path) {
		if (path == null) {
			throw new InvalidParameterException("Given path cannot be null");
		}
		return sceneryGraph.getEdgeTarget(path);
	}

	public Place getPathTarget(Place origin, Path path) {
		if (origin == null) {
			throw new InvalidParameterException("Given origin place cannot be null");
		}
		if (path == null) {
			throw new InvalidParameterException("Given path cannot be null");
		}
		Place target = sceneryGraph.getEdgeTarget(path);
		if (target.equals(origin)) {
			target = sceneryGraph.getEdgeSource(path);
		}
		return target;
	}

	/**
	 * Insert a player in the scenery.
	 * @param player Player to be added to the scenery.
	 * @param place Place where the player should be added.
	 * @return A SceneryEvent containing the result of the operation.
	 */
	public SceneryEvent insertPlayer(Player player, Place place) {
		if (player == null) {
			throw new InvalidParameterException("Player cannot be null");
		}
		if (place == null) {
			throw new InvalidParameterException("Place cannot be null");
		}

		if (!sceneryGraph.containsVertex(place)) {
			throw new InvalidParameterException("Specified place doesn't exist inside scenery");
		}
		if (place.addPlayer(player)) {
			return SceneryEvent.PLAYER_INSERTED;
		} else {
			return SceneryEvent.DESTINATION_BUSY;
		}
	}

	/**
	 * Move a player from one place to another.
	 * @param player Player to be moved.
	 * @param origin Current player position.
	 * @param destination Place where the player should be moved.
	 * @return A SceneryEvent containing the result of the operation.
	 */
	public SceneryEvent movePlayer(Player player, Place origin, Place destination) {
		if (player == null) {
			throw new InvalidParameterException("Player cannot be null");
		}
		if (origin == null) {
			throw new InvalidParameterException("Origin place cannot be null");
		}
		if (destination == null) {
			throw new InvalidParameterException("Destination place cannot be null");
		}

		if (!origin.isPlayerPresent(player)) {
			throw new InvalidParameterException("Specified player is not present at the given origin place");
		}
		if (!sceneryGraph.containsVertex(origin)) {
			throw new InvalidParameterException("Specified origin doesn't exist inside scenery");
		}
		if (!sceneryGraph.containsVertex(destination)) {
			throw new InvalidParameterException("Specified destination doesn't exist inside scenery");
		}

		if (!sceneryGraph.containsEdge(origin, destination)) {
			return SceneryEvent.DESTINATION_UNREACHABLE;
		}
		if (destination.addPlayer(player)) {
			origin.removePlayer(player);
			return SceneryEvent.PLAYER_MOVED;
		} else {
			return SceneryEvent.DESTINATION_BUSY;
		}
	}
}
