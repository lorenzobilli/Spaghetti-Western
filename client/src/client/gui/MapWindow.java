package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.*;

/**
 * client.gui.MapWindow class
 */
public class MapWindow {

	public final Dimension size = new Dimension(1366, 768);
	public final Dimension margins = new Dimension(20, 45);

	private JFrame frame;
	private JLabel background;

	public MapWindow(String imagePath) {
		final Point position = new Point(0, 0);

		frame = new JFrame(Client.GAME_NAME);

		background = new JLabel();
		background.setIcon(new ImageIcon(imagePath));
		background.setSize(size);
		background.setLocation(position);
		background.setLayout(null);
		frame.add(background);

		frame.setSize(size);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public Component getFrame() {
		return frame;
	}

	public void add(Component component, Dimension size, Point location) {
		background.add(component);
		Insets insets = background.getInsets();
		component.setBounds(location.x + insets.left, location.y + insets.top, size.width, size.height);
	}

	public void update(Component component, Dimension size) {
		Insets insets = background.getInsets();
		Point location = component.getLocation();
		component.setBounds(location.x + insets.left, location.y + insets.top, size.width, size.height);
	}

	public void dispose() {
		frame.dispose();
	}
}
