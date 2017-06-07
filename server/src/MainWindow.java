import javax.swing.*;
import java.awt.*;
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
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 600);

        content = window.getContentPane();
        content.setLayout(new BorderLayout());

        upperPanel = new JPanel();
        lowerPanel = new JPanel();
        content.add(upperPanel, BorderLayout.CENTER);
        content.add(lowerPanel, BorderLayout.PAGE_END);

        upperPanel.setLayout(new GridLayout());
        consoleOutput = new JTextArea();
        consoleOutput.setBackground(Color.BLACK);
        consoleOutput.setForeground(Color.WHITE);
        consoleScroll = new JScrollPane(consoleOutput);
        upperPanel.add(consoleScroll);

        lowerPanel.setLayout(new FlowLayout());
        startButton = new JButton("START");
        stopButton = new JButton("STOP");
        lowerPanel.add(startButton);
        lowerPanel.add(stopButton);

        window.setVisible(true);
    }

    public void appendText(String message) {
        if (message == null) {
            throw new InvalidParameterException("Null message");
        }
        consoleOutput.append(message);
    }
}
