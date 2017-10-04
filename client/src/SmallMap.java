import javax.swing.*;

/**
 * SmallMap class
 */
public class SmallMap extends Map {

	public SmallMap() {

		JButton santaFeButton = new JButton();
		JButton sanRafaelButton = new JButton();
		JButton valverdeButton = new JButton();
		JButton watermillButton = new JButton();
		JButton desertButton = new JButton();

		santaFeButton.addActionListener(e -> sendPlayerMove("Santa Fe"));
		sanRafaelButton.addActionListener(e -> sendPlayerMove("San Rafael"));
		valverdeButton.addActionListener(e -> sendPlayerMove("Valverde"));
		watermillButton.addActionListener(e -> sendPlayerMove("Watermill"));
		desertButton.addActionListener(e -> sendPlayerMove("Desert"));
	}

}
