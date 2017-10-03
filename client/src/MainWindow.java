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
        switch (Client.clientResolution) {
			case HD:
				window.setSize(1366, 768);
				break;
			case FULLHD:
				window.setSize(1920, 1080);
				break;
			case QUADHD:
				window.setSize(3840, 2160);
				break;
			default:
				throw new InvalidParameterException("Unrecognized screen resolution");
		}

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
        String label;
        if (minutes > 1) {
            label = "minutes";
        } else {
            label = "minute";
        }
        remainingWaitTime.setText(String.valueOf(minutes) + " " + label);
    }

    public void prepareSceneryLoad() {
        glass.remove(remainingWaitTime);
        glass.updateUI();
        remainingAdvice.setText("Get ready!");
    }

    public void loadScenery(Scenery scenery) {
        try {
            background.updateBackground(scenery.getSceneryBackground());
            glass.removeAll();
            glass.updateUI();
        } catch (IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
    }
}
