import javax.swing.*;
import java.awt.*;

/**
 * LargeMap class
 */
public class LargeMap extends Map {

	private final Dimension buttonDimension = new Dimension(37, 37);
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
	private JButton santaAnaButton;
	private JButton langstoneBridgeButton;
	private JButton sadHillGraveyardButton;
	private JButton confederatePrisonButton;
	private JButton unionPrisonButton;
	private JButton saintAnthonyMissionButton;

	public LargeMap() {

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

		santaAnaButton = new JButton();
		santaAnaButton.setOpaque(false);
		santaAnaButton.setContentAreaFilled(false);
		santaAnaButton.setBorderPainted(false);
		santaAnaButton.addActionListener(e -> sendPlayerMove("Santa Ana"));

		langstoneBridgeButton = new JButton();
		langstoneBridgeButton.setOpaque(false);
		langstoneBridgeButton.setContentAreaFilled(false);
		langstoneBridgeButton.setBorderPainted(false);
		langstoneBridgeButton.addActionListener(e -> sendPlayerMove("Langstone bridge"));

		sadHillGraveyardButton = new JButton();
		sadHillGraveyardButton.setOpaque(false);
		sadHillGraveyardButton.setContentAreaFilled(false);
		sadHillGraveyardButton.setBorderPainted(false);
		sadHillGraveyardButton.addActionListener(e -> sendPlayerMove("Sad Hill graveyard"));

		confederatePrisonButton = new JButton();
		confederatePrisonButton.setOpaque(false);
		confederatePrisonButton.setContentAreaFilled(false);
		confederatePrisonButton.setBorderPainted(false);
		confederatePrisonButton.addActionListener(e -> sendPlayerMove("Confederate prison"));

		unionPrisonButton = new JButton();
		unionPrisonButton.setOpaque(false);
		unionPrisonButton.setContentAreaFilled(false);
		unionPrisonButton.setBorderPainted(false);
		unionPrisonButton.addActionListener(e -> sendPlayerMove("Union prison"));

		saintAnthonyMissionButton = new JButton();
		saintAnthonyMissionButton.setOpaque(false);
		saintAnthonyMissionButton.setContentAreaFilled(false);
		saintAnthonyMissionButton.setBorderPainted(false);
		saintAnthonyMissionButton.addActionListener(e -> sendPlayerMove("Saint Anthony mission"));

	}

	@Override
	protected void populate(MapWindow map) {
		map.add(santaFeButton, buttonDimension, new Point(838, 42 - borderSize));
		map.add(sanRafaelButton, buttonDimension, new Point(611, 80 - borderSize));
		map.add(valverdeButton, buttonDimension, new Point(826, 176 - borderSize));
		map.add(watermillButton, buttonDimension, new Point(1059, 106 - borderSize));
		map.add(desertButton, buttonDimension, new Point(791, 352 - borderSize));
		map.add(canyonDiabloButton, buttonDimension, new Point(323, 102 - borderSize));
		map.add(phoenixButton, buttonDimension, new Point(191, 323 - borderSize));
		map.add(tucsonButton, buttonDimension, new Point(314, 475 - borderSize));
		map.add(elPasoButton, buttonDimension, new Point(778, 533 - borderSize));
		map.add(santaAnaButton, buttonDimension, new Point(296, 684 - borderSize));
		map.add(langstoneBridgeButton, buttonDimension, new Point(850, 604 - borderSize));
		map.add(sadHillGraveyardButton, buttonDimension, new Point(808, 676 - borderSize));
		map.add(unionPrisonButton, buttonDimension, new Point(1041, 308 - borderSize));
		map.add(confederatePrisonButton, buttonDimension, new Point(1041, 484 - borderSize));
		map.add(saintAnthonyMissionButton, buttonDimension, new Point(1198, 585 - borderSize));
	}
}
