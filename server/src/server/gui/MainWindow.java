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

package server.gui;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;

/**
 * Class implementing main window of the server.
 */
public class MainWindow {

	/**
	 * Text area used for showing console output.
	 */
	private static JTextArea consoleOutput;

	/**
	 * Spawns new window with given title.
	 * @param title Title of the window.
	 */
	public MainWindow(String title) {
		if (title == null) {
			throw new InvalidParameterException("Window title unspecified");
		}

		// Window settings
		JFrame window = new JFrame(title);
		window.setSize(800, 600);

		// Setting JFrame main layout manager
		Container content = window.getContentPane();
		content.setLayout(new BorderLayout());

		// Configuring central part of the window
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout());
		content.add(contentPanel, BorderLayout.CENTER);

		// Setting up the console
		consoleOutput = new JTextArea();
		consoleOutput.setBackground(Color.BLACK);
		consoleOutput.setForeground(Color.WHITE);
		JScrollPane consoleScroll = new JScrollPane(consoleOutput);
		contentPanel.add(consoleScroll);

		// Setting latest JFrame options before launch
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setLocation(10, 10);
		window.setVisible(true);
	}

	/**
	 * Insert new text in the console output area.
	 * @param message Message to be shown in the console area.
	 */
	public void appendText(String message) {
		if (message == null) {
			throw new InvalidParameterException("Null message");
		}
		consoleOutput.append(message);
	}

	public void clear() {
		consoleOutput.setText("");
	}
}
