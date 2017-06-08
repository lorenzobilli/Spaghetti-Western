import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;

/**
 * MainWindow class
 */
public class MainWindow {

    private JFrame window;
    private Container content;
    private JPanel upperPanel;
    private JPanel lowerPanel;
    private JButton startButton;
    private JButton stopButton;
    private static JTextArea consoleOutput;
    private JScrollPane consoleScroll;

    public MainWindow(String title) {
        if (title == null) {
            throw new InvalidParameterException("Window title unspecified");
        }

        // Window settings
        window = new JFrame(title);
        window.setSize(800, 600);

        // Setting JFrame main layout manager
        content = window.getContentPane();
        content.setLayout(new BorderLayout());

        // Configuring upper part of the window
        upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout());
        content.add(upperPanel, BorderLayout.CENTER);

        // Configuring lower part of the window
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout());
        content.add(lowerPanel, BorderLayout.PAGE_END);

        // Setting up the console
        consoleOutput = new JTextArea();
        consoleOutput.setBackground(Color.BLACK);
        consoleOutput.setForeground(Color.WHITE);
        consoleScroll = new JScrollPane(consoleOutput);
        upperPanel.add(consoleScroll);

        // Setting up buttons
        startButton = new JButton("START");
        startButton.addActionListener(e -> Server.startServer());
        stopButton = new JButton("STOP");
        stopButton.addActionListener(e -> Server.stopServer());
        lowerPanel.add(startButton);
        lowerPanel.add(stopButton);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    public void appendText(String message) {
        if (message == null) {
            throw new InvalidParameterException("Null message");
        }
        consoleOutput.append(message);
    }
}
