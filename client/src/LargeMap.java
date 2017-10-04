import javax.swing.*;

/**
 * LargeMap class
 */
public class LargeMap extends Map {

	public LargeMap() {

		JButton santaFeButton = new JButton();
		JButton sanRafaelButton = new JButton();
		JButton valverdeButton = new JButton();
		JButton watermillButton = new JButton();
		JButton desertButton = new JButton();
		JButton canyonDiabloButton = new JButton();
		JButton phoenixButton = new JButton();
		JButton tucsonButton = new JButton();
		JButton elPasoButton = new JButton();
		JButton santaAnaButton = new JButton();
		JButton langstoneBridgeButton = new JButton();
		JButton sadHillGraveyardButton = new JButton();
		JButton confederatePrisonButton = new JButton();
		JButton unionPrisonButton = new JButton();
		JButton saintAnthonyMissionButton = new JButton();

		santaFeButton.addActionListener(e -> sendPlayerMove("Santa Fe"));
		sanRafaelButton.addActionListener(e -> sendPlayerMove("San Rafael"));
		valverdeButton.addActionListener(e -> sendPlayerMove("Valverde"));
		watermillButton.addActionListener(e -> sendPlayerMove("Watermill"));
		desertButton.addActionListener(e -> sendPlayerMove("Desert"));
		canyonDiabloButton.addActionListener(e -> sendPlayerMove("Canyon Diablo"));
		phoenixButton.addActionListener(e -> sendPlayerMove("Phoenix"));
		tucsonButton.addActionListener(e -> sendPlayerMove("Tucson"));
		elPasoButton.addActionListener(e -> sendPlayerMove("El Paso"));
		santaAnaButton.addActionListener(e -> sendPlayerMove("Santa Ana"));
		langstoneBridgeButton.addActionListener(e -> sendPlayerMove("Langstone bridge"));
		sadHillGraveyardButton.addActionListener(e -> sendPlayerMove("Sad Hill graveyard"));
		confederatePrisonButton.addActionListener(e -> sendPlayerMove("Confederate prison"));
		unionPrisonButton.addActionListener(e -> sendPlayerMove("Union prison"));
		saintAnthonyMissionButton.addActionListener(e -> sendPlayerMove("Saint Anthony mission"));

	}
}
