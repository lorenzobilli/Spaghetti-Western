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

package client.gui.map;

import client.Client;
import client.gui.MapWindow;
import shared.communication.Sender;
import shared.gaming.Player;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.scenery.Place;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Base class for all maps implementations.
 */
public abstract class Map {

	/**
	 * Internal flag used to check if all components inside a map are aligned correctly.
	 * For normal operations this boolean value should always be set to false.
	 */
	private final boolean checkAlignMode = false;

	/**
	 * Size of the border of the map.
	 * Represents the distance from window borders where graphical elements can be drawn.
	 */
	protected final int borderSize = 15;

	/**
	 * Number of labels for each cluster.
	 * This value should be equal to half the maximum allowed players per node.
	 */
	protected final int labelClusterSize = 3;

	private JLabel totalTimeLabel;

	private JLabel turnTimeLabel;

	/**
	 * Label used to show bullet number.
	 */
	private JLabel bulletLabel;

	/**
	 * Button used to send a clash request.
	 */
	private JButton clashButton;

	/**
	 * Hash map containing reference to all player labels.
	 * A player label is a label used to show the current player position in the map.
	 */
	protected HashMap<String, JLabel> playerLabels = new HashMap<>();

	/**
	 * Hash map containing references to all good-team player labels.
	 * These labels are treated as arrays so they can be positioned in the map as a single block of objects.
	 */
	protected HashMap<String, JLabel[]> goodLabels = new HashMap<>();

	/**
	 * Hash map containing references to all bad-team player labels.
	 * These labels are treated as arrays so they can be positioned in the map as a single block of objects.
	 */
	protected HashMap<String, JLabel[]> badLabels = new HashMap<>();

	/**
	 * Path to map background asset.
	 */
	protected String mapBackground;

	/**
	 * Path to the red hat graphic asset.
	 */
	protected String redHatIcon;

	/**
	 * Path to the white hat graphic asset.
	 */
	protected String whiteHatIcon;

	/**
	 * Path to the black hat graphic asset.
	 */
	protected String blackHatIcon;

	/**
	 * Dimension of the clickable, invisible button at the center of each map point of interest.
	 */
	protected Dimension buttonDimension;

	/**
	 * Dimension of each player label.
	 */
	protected Dimension labelDimension;

	/**
	 * Horizontal margin between player labels.
	 */
	protected int xLabelMargin;

	/**
	 * Vertical margin between player labels.
	 */
	protected int yLabelMargin;

	public String getMapBackground() {
		return mapBackground;
	}

	/**
	 * Converts a click on a button to a player move.
	 * @param position String representing the position of the clicked button in the map.
	 */
	protected void sendPlayerMove(String position) {
		MessageTable messageTable = new MessageTable();
		messageTable.put("header", "TRY_PLAYER_MOVE");
		messageTable.put("content", position);
		Client.globalThreadPool.submit(new Sender(new Message(
				Message.Type.MOVE,
				Client.getPlayer(),
				MessageManager.createXML(messageTable)
		), Client.connectionManager.getSendStream()));
	}

	/**
	 * Enables a button.
	 * @param button Button to be enabled.
	 */
	protected void enableButton(JButton button) {
		button.setEnabled(true);
	}

	/**
	 * Disables a button.
	 * @param button Button to be disabled.
	 */
	protected void disableButton(JButton button) {
		button.setEnabled(false);
	}

	/**
	 * Configures a button by making it invisible and assigning its action to the sendPlayerMove() method.
	 * @param button Button to be configured.
	 * @param position Corresponding position of the button in the map.
	 */
	protected void configureButton(JButton button, String position) {
		// Left the button visible if debugging mode is selected.
		if (!checkAlignMode) {
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
		}
		button.addActionListener(e -> sendPlayerMove(position));
		enableButton(button);   // Make sure that JButton is really enabled
	}

	/**
	 * Configures labels by setting corresponding graphical assets and making them visible in the map.
	 * @param playerLabel Label representing current player position.
	 * @param goodLabels Array of labels representing good-team players positions.
	 * @param badLabels Array of labels representing bad-team players positions.
	 */
	protected void configureLabels(JLabel playerLabel, JLabel[] goodLabels, JLabel[] badLabels) {
		playerLabel.setIcon(new ImageIcon(redHatIcon));
		// Left the label visible if debugging mode is selected.
		if (!checkAlignMode) {
			playerLabel.setVisible(false);
		}
		for (JLabel label : goodLabels) {
			label.setIcon(new ImageIcon(whiteHatIcon));
			// Left the label visible if debugging mode is selected.
			if (!checkAlignMode) {
				label.setVisible(false);
			}
		}
		for (JLabel label : badLabels) {
			label.setIcon(new ImageIcon(blackHatIcon));
			// Left the label visible if debugging mode is selected.
			if (!checkAlignMode) {
				label.setVisible(false);
			}
		}
	}

	/**
	 * Configures the total time remaining label, then puts it in the map.
	 * @param map Map on which the label should be placed.
	 */
	protected void configureTotalTimeLabel(MapWindow map) {
		totalTimeLabel = new JLabel("Total time remaining: XX:XX");
		totalTimeLabel.setForeground(Color.WHITE);
		Dimension totalTimeLabelDimension = totalTimeLabel.getPreferredSize();
		map.add(totalTimeLabel, totalTimeLabelDimension, new Point(
				map.margins.width, map.margins.height
		));
	}

	/**
	 * Updates the total time remaining label inside the given map with a new value.
	 * @param map Map on which the label that should be updated is.
	 * @param totalSeconds Value of the remaining time that the label should show.
	 */
	public void updateTotalTimeLabel(MapWindow map, int totalSeconds) {
		int minutesRemaining = totalSeconds / 60;
		int secondsRemaining = totalSeconds - (minutesRemaining * 60);
		totalTimeLabel.setText("Total time remaining: " +
				(minutesRemaining < 10 ? "0" + minutesRemaining : minutesRemaining) +
				":" +
				(secondsRemaining < 10 ? "0" + secondsRemaining : secondsRemaining));
		map.update(totalTimeLabel, totalTimeLabel.getPreferredSize());
	}

	/**
	 * Makes the turn time label visible.
	 */
	public void enableTurnTimeLabel() {
		turnTimeLabel.setVisible(true);
	}

	/**
	 * Makes the turn time label invisible.
	 */
	public void disableTurnTimeLabel() {
		turnTimeLabel.setVisible(false);
	}

	/**
	 * Configures the turn time label, then puts it in the map.
	 * @param map Map on which the label should be placed.
	 */
	protected void configureTurnTimeLabel(MapWindow map) {
		turnTimeLabel = new JLabel("Time remaining to move: XX:XX");
		turnTimeLabel.setForeground(Color.WHITE);
		Dimension turnTimeLabelDimension = turnTimeLabel.getPreferredSize();
		turnTimeLabel.setVisible(false);
		map.add(turnTimeLabel, turnTimeLabelDimension, new Point(
				map.size.width - map.margins.width - turnTimeLabelDimension.width, map.margins.height
		));
	}

	/**
	 * Updates the turn time label inside the given map with a new value.
	 * @param map Map on which the label that should be updated is.
	 * @param totalSeconds Value of the remaining time that the label should show.
	 */
	public void updateTurnTimeLabel(MapWindow map, int totalSeconds) {
		int minutesRemaining = totalSeconds / 60;
		int secondsRemaining = totalSeconds - (minutesRemaining * 60);
		turnTimeLabel.setText("Time remaining to move: " +
				(minutesRemaining < 10 ? "0" + minutesRemaining : minutesRemaining) +
				":" +
				(secondsRemaining < 10 ? "0" + secondsRemaining : secondsRemaining));
		map.update(turnTimeLabel, turnTimeLabel.getPreferredSize());
	}

	/**
	 * Configures the bullet number counter in the map.
	 * @param map Instance of MapWindow where the bullet number counter should be configured.
	 */
	protected void configureBulletLabel(MapWindow map) {
		bulletLabel = new JLabel("Bullets: " + String.valueOf(0));
		bulletLabel.setForeground(Color.WHITE);
		Dimension bulletLabelDimension = bulletLabel.getPreferredSize();
		map.add(bulletLabel, bulletLabelDimension, new Point(
				map.margins.width, map.size.height - map.margins.height - bulletLabelDimension.height
		));
	}

	/**
	 * Updates the bullet number counter in the map.
	 * @param map Instance of MapWindow where the bullet number counter should be updated.
	 * @param bullets New number of bullets to be shown in the counter.
	 */
	public void updateBulletLabel(MapWindow map, int bullets) {
		bulletLabel.setText("Bullets: " + String.valueOf(bullets));
		map.update(bulletLabel, bulletLabel.getPreferredSize());
	}

	/**
	 * Configures the clash button.
	 * @param map Instance of MapWindow where the clash button should be configured.
	 */
	protected void configureClashButton(MapWindow map) {
		clashButton = new JButton("CLASH!");
		clashButton.setForeground(Color.RED);
		clashButton.setVisible(false);
		clashButton.addActionListener(e -> {
			Client.globalThreadPool.submit(new Sender(
					new Message(
							Message.Type.CLASH,
							Client.getPlayer(),
							MessageManager.createXML(new MessageTable("header", "CLASH_REQUEST"))
					), Client.connectionManager.getSendStream()
			));
		});
		Dimension clashButtonDimension = clashButton.getPreferredSize();
		map.add(clashButton, clashButtonDimension, new Point(
				map.size.width - map.margins.width - clashButtonDimension.width,
				map.size.height - map.margins.height - clashButtonDimension.height
		));
	}

	/**
	 * Toggles clash button visibility/invisibility.
	 */
	@Deprecated
	public void toggleClashButton() {
		clashButton.setVisible(!clashButton.isVisible());
	}

	/**
	 * Enables the clash button by making it visible on the map.
	 */
	public void enableClashButton() {
		if (!clashButton.isVisible()) {
			clashButton.setVisible(true);
		}
	}

	/**
	 * Disables the clash button by making it invisible on the map.
	 */
	public void disableClashButton() {
		if (clashButton.isVisible()) {
			clashButton.setVisible(false);
		}
	}

	/**
	 * Calculates the position of the player label by using the location button as reference.
	 * @param buttonPosition Position of the location button.
	 * @return Exact position where player label should be placed as a Point object.
	 */
	protected Point calculatePlayerLabelPosition(Point buttonPosition) {
		return new Point(buttonPosition.x, buttonPosition.y - labelDimension.height - yLabelMargin);
	}

	/**
	 * Calculates the position of a good-team player label by using the location button and other good-team player
	 * labels as reference.
	 * @param buttonPosition Position of the location button.
	 * @param label Label of which the position should be calculated.
	 * @return Exact position where player label should be placed as a Point object.
	 */
	protected Point calculateGoodLabelPosition(Point buttonPosition, int label) {
		switch (label) {
			case 0:
				return new Point(
						buttonPosition.x - (xLabelMargin + labelDimension.width),
						buttonPosition.y - labelDimension.height - yLabelMargin
				);
			case 1:
				return new Point(
						buttonPosition.x - (xLabelMargin + labelDimension.width),
						buttonPosition.y + (buttonDimension.height / 2 - labelDimension.height / 2)
				);
			case 2:
				return new Point(
						buttonPosition.x - (xLabelMargin + labelDimension.width),
						buttonPosition.y + buttonDimension.height + yLabelMargin
				);
			default:
				throw new NoSuchElementException("Bad index given");
		}
	}

	/**
	 * Calculates the position of a bad-team player label by using the location button and other bad-player labels as
	 * reference.
	 * @param buttonPosition Position of the location button.
	 * @param label Label of which the position should be calculated.
	 * @return Exact position where player should be placed as a Point object.
	 */
	protected Point calculateBadLabelPosition(Point buttonPosition, int label) {
		switch (label) {
			case 0:
				return new Point(
						buttonPosition.x + labelDimension.width + xLabelMargin,
						buttonPosition.y - labelDimension.height - yLabelMargin
				);
			case 1:
				return new Point(
						buttonPosition.x + labelDimension.width + xLabelMargin,
						buttonPosition.y + (buttonDimension.height / 2 - labelDimension.height / 2)
				);
			case 2:
				return new Point(
						buttonPosition.x + labelDimension.width + xLabelMargin,
						buttonPosition.y + buttonDimension.height + yLabelMargin
				);
			default:
				throw new NoSuchElementException("Bad index given");
		}
	}

	/**
	 * Inserts all map elements into a MapWindow instance.
	 * @param map Instance of MapWindow to be populated.
	 */
	public abstract void populate(MapWindow map);

	/**
	 * Quickly enables all clickable user commands.
	 */
	public abstract void enableUserCommands();

	/**
	 * Quickly disables all clickable user commands.
	 */
	public abstract void disableUserCommands();

	public void updateMap(Player player, Place place) {
		if (player.equals(Client.getPlayer())) {
			populatePlayerLabel(place);
		} else {
			switch (player.getTeam()) {
				case GOOD:
					populateGoodLabel(place);
					break;
				case BAD:
					populateBadLabel(place);
					break;
				case UGLY:
					break;
			}
		}
	}

	/**
	 * Updates the position of a player in the map.
	 * @param movingPlayer Player to be moved.
	 * @param origin Current position of the player in the map.
	 * @param destination New position of the player in the map.
	 */
	public void updateMap(Player movingPlayer, Place origin, Place destination) {
		if (movingPlayer.equals(Client.getPlayer())) {
			updatePlayerLabels(origin, destination);
		} else if (movingPlayer.getTeam().equals(Player.Team.GOOD)) {
			updateGoodLabels(origin, destination);
		} else if (movingPlayer.getTeam().equals(Player.Team.BAD)) {
			updateBadLabels(origin, destination);
		}
	}

	/**
	 * Puts player labels around a specific place in the map.
	 * @param place Place where labels should be placed.
	 */
	private void populatePlayerLabel(Place place) {
		JLabel label = playerLabels.get(place.getPlaceName());
		if (!label.isVisible()) {
			label.setVisible(true);
		}
	}

	/**
	 * Puts good-team player labels around a specific place in the map.
	 * @param place Place where labels should be placed.
	 */
	private void populateGoodLabel(Place place) {
		JLabel labels[] = goodLabels.get(place.getPlaceName());
		for (short index = 0; index < labelClusterSize; index++) {
			if (!labels[index].isVisible()) {
				labels[index].setVisible(true);
				break;
			}
		}
	}

	/**
	 * Puts bad-team player labels around a specific place in the map.
	 * @param place Place where labels should be placed.
	 */
	private void populateBadLabel(Place place) {
		JLabel labels[] = badLabels.get(place.getPlaceName());
		for (short index = 0; index < labelClusterSize; index++) {
			if (!labels[index].isVisible()) {
				labels[index].setVisible(true);
				break;
			}
		}
	}

	/**
	 * Updates player labels in order to make a player move visible in the map.
	 * @param origin Place where the label will be made invisible.
	 * @param destination Place where the label will be made visible.
	 */
	private void updatePlayerLabels(Place origin, Place destination) {
		JLabel oldPlace = playerLabels.get(origin.getPlaceName());
		if (oldPlace.isVisible()) {
			oldPlace.setVisible(false);
		}
		JLabel newPlace = playerLabels.get(destination.getPlaceName());
		if (!newPlace.isVisible()) {
			newPlace.setVisible(true);
		}
	}

	/**
	 * Updates good-team labels in order to make a good-team player move visible in the map.
	 * @param origin Place where a label will be made invisible.
	 * @param destination Place where a label will be made visible.
	 */
	private void updateGoodLabels(Place origin, Place destination) {
		JLabel oldPlace[] = goodLabels.get(origin.getPlaceName());
		for (short index = labelClusterSize - 1; index >= 0; index--) {
			if (oldPlace[index].isVisible()) {
				oldPlace[index].setVisible(false);
				break;
			}
		}
		JLabel newPlace[] = goodLabels.get(destination.getPlaceName());
		for (short index = 0; index < labelClusterSize; index++) {
			if (!newPlace[index].isVisible()) {
				newPlace[index].setVisible(true);
				break;
			}
		}
	}

	/**
	 * Updates bad-team labels in order to make a bad-team player move visible in the map.
	 * @param origin Place where a label will be made invisible.
	 * @param destination Place where a label will be made visible.
	 */
	private void updateBadLabels(Place origin, Place destination) {
		JLabel oldPlace[] = badLabels.get(origin.getPlaceName());
		for (short index = labelClusterSize - 1; index >= 0; index--) {
			if (oldPlace[index].isVisible()) {
				oldPlace[index].setVisible(false);
				break;
			}
		}
		JLabel newPlace[] = badLabels.get(destination.getPlaceName());
		for (short index = 0; index < labelClusterSize; index++) {
			if (!newPlace[index].isVisible()) {
				newPlace[index].setVisible(true);
				break;
			}
		}
	}

}
