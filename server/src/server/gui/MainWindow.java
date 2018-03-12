package server.gui;

import server.Server;

import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;

/**
 * Class implementing main window of the server.
 */
public class MainWindow {

	private static JFrame window;

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
        window = new JFrame(title);
        window.setSize(800, 600);

        // Setting JFrame main layout manager
        Container content = window.getContentPane();
        content.setLayout(new BorderLayout());

        // Configuring central part of the window
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout());
        content.add(upperPanel, BorderLayout.CENTER);

        // Configuring lower part of the window
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout());
        content.add(lowerPanel, BorderLayout.PAGE_END);

        // Setting up the console
        consoleOutput = new JTextArea();
        consoleOutput.setBackground(Color.BLACK);
        consoleOutput.setForeground(Color.WHITE);
        JScrollPane consoleScroll = new JScrollPane(consoleOutput);
        upperPanel.add(consoleScroll);

        // Setting up buttons
        JButton startButton = new JButton("START");
        startButton.addActionListener(e -> Server.startServer());
        JButton stopButton = new JButton("STOP");
        stopButton.addActionListener(e -> Server.stopServer());
        lowerPanel.add(startButton);
        lowerPanel.add(stopButton);

        // Setting latest JFrame options before launch
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocation(10, 10);
        window.setVisible(true);
    }

	/**
	 * Enables the closing button of the server window.
	 */
	public void enableClosingButton() {
    	window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

	/**
	 * Disables the closing button of the server window.
	 */
	public void disableClosingButton() {
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
}
