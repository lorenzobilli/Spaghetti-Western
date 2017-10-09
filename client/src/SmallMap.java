import javax.swing.*;
import java.awt.*;

/**
 * SmallMap class
 */
public class SmallMap extends Map {

	private final Dimension buttonDimension = new Dimension(60, 60);
	private final int borderSize = 15;

	private JButton santaFeButton;
	private JButton sanRafaelButton;
	private JButton valverdeButton;
	private JButton watermillButton;
	private JButton desertButton;

	public SmallMap() {

		santaFeButton = new JButton();
		santaFeButton.setOpaque(false);
		santaFeButton.setContentAreaFilled(false);
		santaFeButton.setBorderPainted(false);
		santaFeButton.addActionListener(e -> sendPlayerMove("Santa Fe"));

		sanRafaelButton = new JButton();
		sanRafaelButton.setOpaque(false);
		sanRafaelButton.setContentAreaFilled(false);
		sanRafaelButton.setBorderPainted(false);
		sanRafaelButton.addActionListener(e -> sendPlayerMove("San Rafael"));

		valverdeButton = new JButton();
		valverdeButton.setOpaque(false);
		valverdeButton.setContentAreaFilled(false);
		valverdeButton.setBorderPainted(false);
		valverdeButton.addActionListener(e -> sendPlayerMove("Valverde"));

		watermillButton = new JButton();
		watermillButton.setOpaque(false);
		watermillButton.setContentAreaFilled(false);
		watermillButton.setBorderPainted(false);
		watermillButton.addActionListener(e -> sendPlayerMove("Watermill"));

		desertButton = new JButton();
		desertButton.setOpaque(false);
		desertButton.setContentAreaFilled(false);
		desertButton.setBorderPainted(false);
		desertButton.addActionListener(e -> sendPlayerMove("Desert"));

	}

	@Override
	protected void populate(MapWindow map) {
		map.add(santaFeButton, buttonDimension, new Point(650, 71 - borderSize));
		map.add(sanRafaelButton, buttonDimension, new Point(225, 145 - borderSize));
		map.add(valverdeButton, buttonDimension, new Point(622, 318 - borderSize));
		map.add(watermillButton, buttonDimension, new Point(1043, 185 - borderSize));
		map.add(desertButton, buttonDimension, new Point(558, 646 - borderSize));
	}
}
