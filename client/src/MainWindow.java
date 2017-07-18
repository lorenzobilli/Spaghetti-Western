import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * MainWindow class
 */
public class MainWindow {

    private JFrame window;
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

    public void showWaitingScreen() {
        final String waitingImagePath = "shared/assets/tumbleweed.jpg";
        try {
            content.remove(background);
            background = new BackgroundPanel(waitingImagePath);
            content.add(background, BorderLayout.CENTER);
            content.revalidate();
            content.repaint();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
    }
}
