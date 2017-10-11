import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;

/**
 * Map class
 */
public abstract class Map {

	protected final int borderSize = 15;
	protected final int labelClusterSize = 3;

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
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.addActionListener(e -> sendPlayerMove(position));
	}

	protected void configureLabels(JLabel playerLabel, JLabel[] goodLabels, JLabel[] badLabels) {
		playerLabel.setIcon(new ImageIcon(redHatIcon));
		for (JLabel label : goodLabels) {
			label.setIcon(new ImageIcon(whiteHatIcon));
		}
		for (JLabel label : badLabels) {
			label.setIcon(new ImageIcon(blackHatIcon));
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

}
