package client.gui;

import client.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Class implementing main window of each client.
 */
public class MainWindow {

	/**
	 * Frame used as basic gui element.
	 */
	private JFrame window;

	/**
	 * Label used for displaying a background image.
	 */
	private JLabel background;

	/**
	 * Label used for displaying user advices.
	 */
	private JLabel advice;

	/**
	 * Label used for showing remaining time.
	 */
	private JLabel countdown;

	/**
	 * Spawns new window with a background and initialize its components.
	 */
	public MainWindow() {
		final Dimension size = new Dimension(1366, 768);
		final Point position = new Point(0, 0);
		final String imagePath = "shared/assets/splashscreen.jpg";

		// Creating main window
		window = new JFrame(Client.GAME_NAME);

		// Setting background image
		background = new JLabel();
		background.setIcon(new ImageIcon(imagePath));
		background.setSize(size);
		background.setLocation(position);
		background.setLayout(new GridBagLayout());
		window.add(background);

		// Window setting
		window.setSize(size);
		window.setResizable(false);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	/**
	 * Spawn a new instance of LoginDialog.
	 */
	public void createLoginDialog() {
		new LoginDialog(window);
	}

	/**
	 * Initialize advice label and start displaying the countdown.
	 */
	public void createWaitingCountdown() {
		advice = new JLabel("New play session will begin in: ");
		countdown = new JLabel("minutes");
		advice.setFont(new Font(null, Font.BOLD, 50));
		advice.setBackground(new Color(0, 0, 0, 0));
		advice.setForeground(Color.WHITE);
		countdown.setFont(new Font(null, Font.BOLD, 50));
		countdown.setBackground(new Color(0, 0, 0, 0));
		countdown.setForeground(Color.WHITE);
		background.add(advice);
		background.add(countdown);
	}

	/**
	 * Updates the countdown with a new value.
	 * @param totalSeconds Seconds remaining of the countdown.
	 */
	public void updateWaitingCountdown(int totalSeconds) {
		int minutesRemaining = totalSeconds / 60;
		int secondsRemaining = totalSeconds - (minutesRemaining * 60);
		countdown.setText(
				(minutesRemaining < 10 ? "0" + minutesRemaining : minutesRemaining) +
				":" +
				(secondsRemaining < 10 ? "0" + secondsRemaining : secondsRemaining)
		);
	}

	/**
	 * Changes the advice label in order to notify the user about the imminent session start.
	 */
	public void showSessionReadyAdvice() {
		window.remove(countdown);
		advice.setText("Get ready!");
	}

	/**
	 * Makes the main window visible.
	 */
	public void show() {
		window.setVisible(true);
	}

	/**
	 * Makes the main window invisible.
	 */
	public void hide() {
		window.setVisible(false);
	}
}
