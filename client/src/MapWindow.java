import javax.swing.*;
import java.awt.*;

/**
 * MapWindow class
 */
public class MapWindow {

	private JFrame frame;
	private JLabel background;

	public MapWindow(String imagePath) {
		final Dimension size = new Dimension(1366, 768);
		final Point position = new Point(0, 0);

		frame = new JFrame(Client.gameName);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(size);
		frame.setLayout(new BorderLayout());

		background = new JLabel();
		background.setIcon(new ImageIcon(imagePath));
		background.setSize(size);
		background.setLocation(position);
		background.setLayout(null);
		frame.add(background);

		frame.setVisible(true);
	}

	public void add(Component component, Dimension size, Point location) {
		background.add(component);
		Insets insets = background.getInsets();
		component.setBounds(location.x + insets.left, location.y + insets.top, size.width, size.height);
	}
}
