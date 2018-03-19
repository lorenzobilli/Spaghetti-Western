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
