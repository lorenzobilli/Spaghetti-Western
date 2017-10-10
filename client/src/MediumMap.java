import javax.swing.*;
import java.awt.*;

/**
 * MediumMap class
 */
public class MediumMap extends Map {

	private final Dimension buttonDimension = new Dimension(45, 45);
	private final int borderSize = 15;

	private JButton santaFeButton;
	private JButton sanRafaelButton;
	private JButton valverdeButton;
	private JButton watermillButton;
	private JButton desertButton;
	private JButton canyonDiabloButton;
	private JButton phoenixButton;
	private JButton tucsonButton;
	private JButton elPasoButton;
	private JButton unionPrisonButton;

	public MediumMap() {

		santaFeButton = new JButton();
		santaFeButton.addActionListener(e -> sendPlayerMove("Santa Fe"));

		sanRafaelButton = new JButton();
		sanRafaelButton.addActionListener(e -> sendPlayerMove("San Rafael"));

		valverdeButton = new JButton();
		valverdeButton.addActionListener(e -> sendPlayerMove("Valverde"));

		watermillButton = new JButton();
		watermillButton.addActionListener(e -> sendPlayerMove("Watermill"));

		desertButton = new JButton();
		desertButton.addActionListener(e -> sendPlayerMove("Desert"));

		canyonDiabloButton = new JButton();
		canyonDiabloButton.addActionListener(e -> sendPlayerMove("Canyon Diablo"));

		phoenixButton = new JButton();
		phoenixButton.addActionListener(e -> sendPlayerMove("Phoenix"));

		tucsonButton = new JButton();
		tucsonButton.addActionListener(e -> sendPlayerMove("Tucson"));

		elPasoButton = new JButton();
		elPasoButton.addActionListener(e -> sendPlayerMove("El Paso"));

		unionPrisonButton = new JButton();
		unionPrisonButton.addActionListener(e -> sendPlayerMove("Union prison"));

	}

	@Override
	protected void populate(MapWindow map) {

	}
}
