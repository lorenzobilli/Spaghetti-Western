import javax.swing.*;
import java.awt.*;

/**
 * MainWindow class
 */
public class MainWindow {

	private JFrame frame;
	private JLabel background;
	private JLabel advice;
	private JLabel countdown;

	public MainWindow() {
		final Dimension size = new Dimension(1366, 768);
		final Point position = new Point(0, 0);
		final String imagePath = "shared/assets/splashscreen.jpg";

		frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(size);
		frame.setLayout(new BorderLayout());

		background = new JLabel();
		background.setIcon(new ImageIcon(imagePath));
		background.setSize(size);
		background.setLocation(position);
		background.setLayout(new GridBagLayout());
		frame.add(background);

		frame.setVisible(true);
	}

	public void createLoginDialog() {
		new LoginDialog(frame);
	}

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

	public void updateWaitingCountdown(int minutes) {
		String label;
		if (minutes > 1) {
			label = "minutes";
		} else {
			label = "minute";
		}
		countdown.setText(String.valueOf(minutes) + " " + label);
	}

	public void showSessionReadyAdvice() {
		frame.remove(countdown);
		advice.setText("Get ready!");
	}
}
