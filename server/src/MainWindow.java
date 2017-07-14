import javax.swing.*;
import java.awt.*;
import java.security.InvalidParameterException;

/**
 * MainWindow class
 */
public class MainWindow {

    private static JTextArea consoleOutput;

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

    public void appendText(String message) {
        if (message == null) {
            throw new InvalidParameterException("Null message");
        }
        consoleOutput.append(message);
    }
}
