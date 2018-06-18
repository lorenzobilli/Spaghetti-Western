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

package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Class implementing map window of each client.
 */
public class MapWindow {

	/**
	 * Dimension of the map.
	 */
	public final Dimension size = new Dimension(1366, 768);

	/**
	 * Margins of the map.
	 */
	public final Dimension margins = new Dimension(20, 45);

	/**
	 * Internal base window where map is shown.
	 */
	private JFrame window;

	/**
	 * Window background containing a picture of the choosen map.
	 */
	private JLabel background;

	/**
	 * Spawns new map window
	 * @param imagePath Path to the map picture.
	 */
	public MapWindow(String imagePath) {
		final Point position = new Point(0, 0);

		// Creates the window
		window = new JFrame(Client.GAME_NAME);

		// Setting map background
		background = new JLabel();
		background.setIcon(new ImageIcon(imagePath));
		background.setSize(size);
		background.setLocation(position);
		background.setLayout(null);
		window.add(background);

		// Setting window
		window.setSize(size);
		window.setResizable(false);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	/**
	 * Gets reference to the internal frame.
	 * @return Reference to the internal frame.
	 */
	public Component getWindow() {
		return window;
	}

	/**
	 * Adds a graphic component to the map window.
	 * @param component Component to be added to the window.
	 * @param size Size of the component.
	 * @param location Position of the component in the window.
	 */
	public void add(Component component, Dimension size, Point location) {
		background.add(component);
		Insets insets = background.getInsets();
		component.setBounds(location.x + insets.left, location.y + insets.top, size.width, size.height);
	}

	/**
	 * Updates the status of a component in the window.
	 * @param component Component to be updated.
	 * @param size New size of the component.
	 */
	public void update(Component component, Dimension size) {
		Insets insets = background.getInsets();
		Point location = component.getLocation();
		component.setBounds(location.x + insets.left, location.y + insets.top, size.width, size.height);
	}

	/**
	 * Destroys current instance of the map window.
	 */
	public void dispose() {
		window.dispose();
	}
}
