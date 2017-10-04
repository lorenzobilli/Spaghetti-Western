import javax.swing.*;

/**
 * MediumMap class
 */
public class MediumMap extends Map {

	public MediumMap() {

		JButton santaFeButton = new JButton();
		JButton sanRafaelButton = new JButton();
		JButton valverdeButton = new JButton();
		JButton watermillButton = new JButton();
		JButton desertButton = new JButton();
		JButton canyonDiabloButton = new JButton();
		JButton phoenixButton = new JButton();
		JButton tucsonButton = new JButton();
		JButton elPasoButton = new JButton();

		santaFeButton.addActionListener(e -> sendPlayerMove("Santa Fe"));
		sanRafaelButton.addActionListener(e -> sendPlayerMove("San Rafael"));
		valverdeButton.addActionListener(e -> sendPlayerMove("Valverde"));
		watermillButton.addActionListener(e -> sendPlayerMove("Watermill"));
		desertButton.addActionListener(e -> sendPlayerMove("Desert"));
		canyonDiabloButton.addActionListener(e -> sendPlayerMove("Canyon Diablo"));
		phoenixButton.addActionListener(e -> sendPlayerMove("Phoenix"));
		tucsonButton.addActionListener(e -> sendPlayerMove("Tucson"));
		elPasoButton.addActionListener(e -> sendPlayerMove("El Paso"));

	}

}
