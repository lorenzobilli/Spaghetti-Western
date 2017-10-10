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

		canyonDiabloButton = new JButton();
		canyonDiabloButton.setOpaque(false);
		canyonDiabloButton.setContentAreaFilled(false);
		canyonDiabloButton.setBorderPainted(false);
		canyonDiabloButton.addActionListener(e -> sendPlayerMove("Canyon Diablo"));

		phoenixButton = new JButton();
		phoenixButton.setOpaque(false);
		phoenixButton.setContentAreaFilled(false);
		phoenixButton.setBorderPainted(false);
		phoenixButton.addActionListener(e -> sendPlayerMove("Phoenix"));

		tucsonButton = new JButton();
		tucsonButton.setOpaque(false);
		tucsonButton.setContentAreaFilled(false);
		tucsonButton.setBorderPainted(false);
		tucsonButton.addActionListener(e -> sendPlayerMove("Tucson"));

		elPasoButton = new JButton();
		elPasoButton.setOpaque(false);
		elPasoButton.setContentAreaFilled(false);
		elPasoButton.setBorderPainted(false);
		elPasoButton.addActionListener(e -> sendPlayerMove("El Paso"));

		unionPrisonButton = new JButton();
		unionPrisonButton.setOpaque(false);
		unionPrisonButton.setContentAreaFilled(false);
		unionPrisonButton.setBorderPainted(false);
		unionPrisonButton.addActionListener(e -> sendPlayerMove("Union prison"));

	}

	@Override
	protected void populate(MapWindow map) {
		map.add(santaFeButton, buttonDimension, new Point(947, 42 - borderSize));
		map.add(sanRafaelButton, buttonDimension, new Point(654, 93 - borderSize));
		map.add(valverdeButton, buttonDimension, new Point(930, 214 - borderSize));
		map.add(watermillButton, buttonDimension, new Point(1229, 124 - borderSize));
		map.add(desertButton, buttonDimension, new Point(888, 445 - borderSize));
		map.add(canyonDiabloButton, buttonDimension, new Point(281, 124 - borderSize));
		map.add(phoenixButton, buttonDimension, new Point(114, 404 - borderSize));
		map.add(tucsonButton, buttonDimension, new Point(269, 601 - borderSize));
		map.add(elPasoButton, buttonDimension, new Point(874, 679 - borderSize));
		map.add(unionPrisonButton, buttonDimension, new Point(1209, 388 - borderSize));
	}
}
