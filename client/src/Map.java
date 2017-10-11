import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;

/**
 * Map class
 */
public abstract class Map {

	private final boolean checkAlignMode = false;
	protected final int borderSize = 15;
	protected final int labelClusterSize = 3;

	protected HashMap<String, JLabel> playerLabels = new HashMap<>();
	protected HashMap<String, JLabel[]> goodLabels = new HashMap<>();
	protected HashMap<String, JLabel[]> badLabels = new HashMap<>();
	protected String redHatIcon;
	protected String whiteHatIcon;
	protected String blackHatIcon;
	protected Dimension buttonDimension;
	protected Dimension labelDimension;
	protected int xLabelMargin;
	protected int yLabelMargin;

	protected void sendPlayerMove(String destination) {
		Future send = Client.globalThreadPool.submit(new Sender(new Message(
				MessageType.SCENERY,
				Client.getPlayer(),
				MessageManager.createXML(
						new ArrayList<>(Arrays.asList(
								"header", "content"
						)),
						new ArrayList<>(Arrays.asList(
								"TRY_PLAYER_MOVE", destination
						))
				)
		), Client.connectionManager.getSendStream()));
	}

	protected void configureButton(JButton button, String position) {
		if (!checkAlignMode) {
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
		}
		button.addActionListener(e -> sendPlayerMove(position));
	}

	protected void configureLabels(JLabel playerLabel, JLabel[] goodLabels, JLabel[] badLabels) {
		playerLabel.setIcon(new ImageIcon(redHatIcon));
		if (!checkAlignMode) {
			playerLabel.setVisible(false);
		}
		for (JLabel label : goodLabels) {
			label.setIcon(new ImageIcon(whiteHatIcon));
			if (!checkAlignMode) {
				label.setVisible(false);
			}
		}
		for (JLabel label : badLabels) {
			label.setIcon(new ImageIcon(blackHatIcon));
			if (!checkAlignMode) {
				label.setVisible(false);
			}
		}
	}

	protected Point calculatePlayerLabelPosition(Point buttonPosition) {
		return new Point(buttonPosition.x, buttonPosition.y - labelDimension.height - yLabelMargin);
	}

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

	protected abstract void populate(MapWindow map);

	protected void updateMap(Player movingPlayer, Place origin, Place destination) {
		if (movingPlayer.equals(Client.getPlayer())) {
			updatePlayerLabels(origin, destination);
		} else if (movingPlayer.getTeam().equals(Player.Team.GOOD)) {
			updateGoodLabels(origin, destination);
		} else if (movingPlayer.getTeam().equals(Player.Team.BAD)) {
			updateBadLabels(origin, destination);
		}
	}

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

	private void updateGoodLabels(Place origin, Place destination) {
		JLabel oldPlace[] = goodLabels.get(origin.getPlaceName());
		for (short index = labelClusterSize; index > 0; index--) {
			if (oldPlace[index].isVisible()) {
				oldPlace[index].setVisible(false);
			}
		}
		JLabel newPlace[] = goodLabels.get(destination.getPlaceName());
		for (short index = 0; index < labelClusterSize; index++) {
			if (!newPlace[index].isVisible()) {
				newPlace[index].setVisible(true);
			}
		}
	}

	private void updateBadLabels(Place origin, Place destination) {
		JLabel oldPlace[] = badLabels.get(origin.getPlaceName());
		for (short index = labelClusterSize; index > 0; index--) {
			if (oldPlace[index].isVisible()) {
				oldPlace[index].setVisible(false);
			}
		}
		JLabel newPlace[] = badLabels.get(destination.getPlaceName());
		for (short index = 0; index < labelClusterSize; index++) {
			if (!newPlace[index].isVisible()) {
				newPlace[index].setVisible(true);
			}
		}
	}

}
