import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * MainWindow class
 */
public class MainWindow {

    private JFrame window;
    private JPanel glass;
    private JLabel remainingAdvice;
    private JLabel remainingWaitTime;
    private BackgroundPanel background;
    private Container content;

    public MainWindow(String title) {
        final String backgroundImagePath = "shared/assets/far_west.jpg";

        if (title == null) {
            throw new InvalidParameterException("Window title unspecified");
        }

        // Window settings
        window = new JFrame(title);
        window.setSize(1280, 800);

        // Setting JFrame main layout manager
        content = window.getContentPane();
        content.setLayout(new BorderLayout());

        // Configuring central part of the window
        try {
            background = new BackgroundPanel(backgroundImagePath);
            content.add(background, BorderLayout.CENTER);
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }

        // Setting latest JFrame options before launch
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public void createLoginDialog() {
        new LoginDialog(window);
    }

    public void createWaitingCountdown() {
        glass = (JPanel) window.getGlassPane();
        glass.setVisible(true);
        glass.setLayout(new GridBagLayout());   //TODO: Configure this layout
        remainingAdvice = new JLabel("New session will begin in: ");
        remainingWaitTime = new JLabel("minutes");
        remainingAdvice.setFont(new Font(null, Font.BOLD, 50));
        remainingAdvice.setBackground(new Color(0,0,0,0));
        remainingAdvice.setForeground(Color.WHITE);
        remainingWaitTime.setFont(new Font(null, Font.BOLD, 50));
        remainingWaitTime.setBackground(new Color(0,0,0,0));
        remainingWaitTime.setForeground(Color.WHITE);
        glass.add(remainingAdvice);
        glass.add(remainingWaitTime);
    }

    public void updateWaitingCountdown(int minutes) {
        remainingWaitTime.setText(String.valueOf(minutes) + " minutes");
    }
}
