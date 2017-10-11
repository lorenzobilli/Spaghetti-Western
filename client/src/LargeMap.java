import javax.swing.*;
import java.awt.*;

/**
 * LargeMap class
 */
public class LargeMap extends Map {

	{
		super.redHatIcon = "shared/assets/red_hat_large.png";
		super.whiteHatIcon = "shared/assets/white_hat_large.png";
		super.blackHatIcon = "shared/assets/black_hat_large.png";
		super.buttonDimension = new Dimension(37, 37);
		super.labelDimension = new Dimension(37, 26);
		super.xLabelMargin = 1;	//TODO: Check this value
		super.yLabelMargin = 1; //TODO: Check this value
	}

	// Buttons declaration
	private JButton santaFeButton = new JButton();
	private JButton sanRafaelButton = new JButton();
	private JButton valverdeButton = new JButton();
	private JButton watermillButton = new JButton();
	private JButton desertButton = new JButton();
	private JButton canyonDiabloButton = new JButton();
	private JButton phoenixButton = new JButton();
	private JButton tucsonButton = new JButton();
	private JButton elPasoButton = new JButton();
	private JButton santaAnaButton = new JButton();
	private JButton langstoneBridgeButton = new JButton();
	private JButton sadHillGraveyardButton = new JButton();
	private JButton confederatePrisonButton = new JButton();
	private JButton unionPrisonButton = new JButton();
	private JButton saintAnthonyMissionButton = new JButton();

	// Labels declaration and arrays initialization
	private JLabel santaFePlayerLabel = new JLabel();
	private JLabel[] santaFeGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] santaFeBadLabels = new JLabel[labelClusterSize];
	private JLabel sanRafaelPlayerLabel = new JLabel();
	private JLabel[] sanRafaelGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] sanRafaelBadLabels = new JLabel[labelClusterSize];
	private JLabel valverdePlayerLabel = new JLabel();
	private JLabel[] valverdeGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] valverdeBadLabels = new JLabel[labelClusterSize];
	private JLabel watermillPlayerLabel = new JLabel();
	private JLabel[] watermillGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] watermillBadLabels = new JLabel[labelClusterSize];
	private JLabel desertPlayerLabel = new JLabel();
	private JLabel[] desertGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] desertBadLabels = new JLabel[labelClusterSize];
	private JLabel canyonDiabloPlayerLabel = new JLabel();
	private JLabel[] canyonDiabloGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] canyonDiabloBadLabels = new JLabel[labelClusterSize];
	private JLabel phoenixPlayerLabel = new JLabel();
	private JLabel[] phoenixGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] phoenixBadLabels = new JLabel[labelClusterSize];
	private JLabel tucsonPlayerLabel = new JLabel();
	private JLabel[] tucsonGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] tucsonBadLabels = new JLabel[labelClusterSize];
	private JLabel elPasoPlayerLabel = new JLabel();
	private JLabel[] elPasoGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] elPasoBadLabels = new JLabel[labelClusterSize];
	private JLabel santaAnaPlayerLabel = new JLabel();
	private JLabel[] santaAnaGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] santaAnaBadLabels = new JLabel[labelClusterSize];
	private JLabel langstoneBridgePlayerLabel = new JLabel();
	private JLabel[] langstoneBridgeGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] langstoneBridgeBadLabels = new JLabel[labelClusterSize];
	private JLabel sadHillGraveyardPlayerLabel = new JLabel();
	private JLabel[] sadHillGraveyardGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] sadHillGraveyardBadLabels = new JLabel[labelClusterSize];
	private JLabel confederatePrisonPlayerLabel = new JLabel();
	private JLabel[] confederatePrisonGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] confederatePrisonBadLabels = new JLabel[labelClusterSize];
	private JLabel unionPrisonPlayerLabel = new JLabel();
	private JLabel[] unionPrisonGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] unionPrisonBadLabels = new JLabel[labelClusterSize];
	private JLabel saintAnthonyMissionPlayerLabel = new JLabel();
	private JLabel[] saintAnthonyMissionGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] saintAnthonyMissionBadLabels = new JLabel[labelClusterSize];

	public LargeMap() {

		// Configuring buttons
		configureButton(santaFeButton, "Santa Fe");
		configureButton(sanRafaelButton, "San Rafael");
		configureButton(valverdeButton, "Valverde");
		configureButton(watermillButton, "Watermill");
		configureButton(desertButton, "Desert");
		configureButton(canyonDiabloButton, "Canyon Diablo");
		configureButton(phoenixButton, "Phoenix");
		configureButton(tucsonButton, "Tucson");
		configureButton(elPasoButton, "El Paso");
		configureButton(santaAnaButton, "Santa Ana");
		configureButton(langstoneBridgeButton, "Langstone bridge");
		configureButton(sadHillGraveyardButton, "Sad Hill graveyard");
		configureButton(confederatePrisonButton, "Confederate prison");
		configureButton(unionPrisonButton, "Union prison");
		configureButton(saintAnthonyMissionButton, "Saint Anthony mission");

		// Initiating label arrays
		for (short label = 0; label < labelClusterSize; label++) {
			santaFeGoodLabels[label] = new JLabel();
			santaFeBadLabels[label] = new JLabel();
			sanRafaelGoodLabels[label] = new JLabel();
			sanRafaelBadLabels[label] = new JLabel();
			valverdeGoodLabels[label] = new JLabel();
			valverdeBadLabels[label] = new JLabel();
			watermillGoodLabels[label] = new JLabel();
			watermillBadLabels[label] = new JLabel();
			desertGoodLabels[label] = new JLabel();
			desertBadLabels[label] = new JLabel();
			canyonDiabloGoodLabels[label] = new JLabel();
			canyonDiabloBadLabels[label] = new JLabel();
			phoenixGoodLabels[label] = new JLabel();
			phoenixBadLabels[label] = new JLabel();
			tucsonGoodLabels[label] = new JLabel();
			tucsonBadLabels[label] = new JLabel();
			elPasoGoodLabels[label] = new JLabel();
			elPasoBadLabels[label] = new JLabel();
			santaAnaGoodLabels[label] = new JLabel();
			santaAnaBadLabels[label] = new JLabel();
			langstoneBridgeGoodLabels[label] = new JLabel();
			langstoneBridgeBadLabels[label] = new JLabel();
			sadHillGraveyardGoodLabels[label] = new JLabel();
			sadHillGraveyardBadLabels[label] = new JLabel();
			confederatePrisonGoodLabels[label] = new JLabel();
			confederatePrisonBadLabels[label] = new JLabel();
			unionPrisonGoodLabels[label] = new JLabel();
			unionPrisonBadLabels[label] = new JLabel();
			saintAnthonyMissionGoodLabels[label] = new JLabel();
			saintAnthonyMissionBadLabels[label] = new JLabel();
		}

		// Configuring labels
		configureLabels(santaFePlayerLabel, santaFeGoodLabels, santaFeBadLabels);
		configureLabels(sanRafaelPlayerLabel, sanRafaelGoodLabels, sanRafaelBadLabels);
		configureLabels(valverdePlayerLabel, valverdeGoodLabels, valverdeBadLabels);
		configureLabels(watermillPlayerLabel, watermillGoodLabels, watermillBadLabels);
		configureLabels(desertPlayerLabel, desertGoodLabels, desertBadLabels);
		configureLabels(canyonDiabloPlayerLabel, canyonDiabloGoodLabels, canyonDiabloBadLabels);
		configureLabels(phoenixPlayerLabel, phoenixGoodLabels, phoenixBadLabels);
		configureLabels(tucsonPlayerLabel, tucsonGoodLabels, tucsonBadLabels);
		configureLabels(elPasoPlayerLabel, elPasoGoodLabels, elPasoBadLabels);
		configureLabels(santaAnaPlayerLabel, santaAnaGoodLabels, santaAnaBadLabels);
		configureLabels(langstoneBridgePlayerLabel, langstoneBridgeGoodLabels, langstoneBridgeBadLabels);
		configureLabels(sadHillGraveyardPlayerLabel, sadHillGraveyardGoodLabels, sadHillGraveyardBadLabels);
		configureLabels(confederatePrisonPlayerLabel, confederatePrisonGoodLabels, confederatePrisonBadLabels);
		configureLabels(unionPrisonPlayerLabel, unionPrisonGoodLabels, unionPrisonBadLabels);
		configureLabels(saintAnthonyMissionPlayerLabel, saintAnthonyMissionGoodLabels, saintAnthonyMissionBadLabels);
	}

	@Override
	protected void populate(MapWindow map) {

		// Configuring button positions
		Point santaFeButtonPosition = new Point(838, 42 - borderSize);
		Point sanRafaelButtonPosition = new Point(611, 80 - borderSize);
		Point valverdeButtonPosition = new Point(826, 176 - borderSize);
		Point watermillButtonPosition = new Point(1059, 106 - borderSize);
		Point desertButtonPosition = new Point(791, 352 - borderSize);
		Point canyonDiabloButtonPosition = new Point(323, 102 - borderSize);
		Point phoenixButtonPosition = new Point(191, 323 - borderSize);
		Point tucsonButtonPosition = new Point(314, 475 - borderSize);
		Point elPasoButtonPosition = new Point(778, 533 - borderSize);
		Point santaAnaButtonPosition = new Point(296, 684 - borderSize);
		Point langstoneBridgeButtonPosition = new Point(850, 604 - borderSize);
		Point sadHillGraveyardButtonPosition = new Point(808, 676 - borderSize);
		Point unionPrisonButtonPosition = new Point(1041, 308 - borderSize);
		Point confederatePrisonButtonPosition = new Point(1041, 484 - borderSize);
		Point saintAnthonyMissionButtonPosition = new Point(1198, 585 - borderSize);

		// Adding buttons
		map.add(santaFeButton, buttonDimension, santaFeButtonPosition);
		map.add(sanRafaelButton, buttonDimension, sanRafaelButtonPosition);
		map.add(valverdeButton, buttonDimension, valverdeButtonPosition);
		map.add(watermillButton, buttonDimension, watermillButtonPosition);
		map.add(desertButton, buttonDimension, desertButtonPosition);
		map.add(canyonDiabloButton, buttonDimension, canyonDiabloButtonPosition);
		map.add(phoenixButton, buttonDimension, phoenixButtonPosition);
		map.add(tucsonButton, buttonDimension, tucsonButtonPosition);
		map.add(elPasoButton, buttonDimension, elPasoButtonPosition);
		map.add(santaAnaButton, buttonDimension, santaAnaButtonPosition);
		map.add(langstoneBridgeButton, buttonDimension, langstoneBridgeButtonPosition);
		map.add(sadHillGraveyardButton, buttonDimension, sadHillGraveyardButtonPosition);
		map.add(unionPrisonButton, buttonDimension, unionPrisonButtonPosition);
		map.add(confederatePrisonButton, buttonDimension, confederatePrisonButtonPosition);
		map.add(saintAnthonyMissionButton, buttonDimension, saintAnthonyMissionButtonPosition);

		// Adding single labels
		map.add(
				santaFePlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(santaFeButtonPosition))
		);
		map.add(
				sanRafaelPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(sanRafaelButtonPosition))
		);
		map.add(
				valverdePlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(valverdeButtonPosition))
		);
		map.add(
				watermillPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(watermillButtonPosition))
		);
		map.add(
				desertPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(desertButtonPosition))
		);
		map.add(
				canyonDiabloPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(canyonDiabloButtonPosition))
		);
		map.add(
				phoenixPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(phoenixButtonPosition))
		);
		map.add(
				tucsonPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(tucsonButtonPosition))
		);
		map.add(
				elPasoPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(elPasoButtonPosition))
		);
		map.add(
				santaAnaPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(santaAnaButtonPosition))
		);
		map.add(
				langstoneBridgePlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(langstoneBridgeButtonPosition))
		);
		map.add(
				sadHillGraveyardPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(sadHillGraveyardButtonPosition))
		);
		map.add(
				unionPrisonPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(unionPrisonButtonPosition))
		);
		map.add(
				confederatePrisonPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(confederatePrisonButtonPosition))
		);
		map.add(
				saintAnthonyMissionPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(saintAnthonyMissionButtonPosition))
		);

		// Adding arrays of labels
		for (short label = 0; label < labelClusterSize; label++) {
			map.add(santaFeGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(santaFeButtonPosition, label))
			);
			map.add(santaFeBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(santaFeButtonPosition, label))
			);
			map.add(sanRafaelGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(sanRafaelButtonPosition, label))
			);
			map.add(sanRafaelBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(sanRafaelButtonPosition, label))
			);
			map.add(valverdeGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(valverdeButtonPosition, label))
			);
			map.add(valverdeBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(valverdeButtonPosition, label))
			);
			map.add(watermillGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(watermillButtonPosition, label))
			);
			map.add(watermillBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(watermillButtonPosition, label))
			);
			map.add(desertGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(desertButtonPosition, label))
			);
			map.add(desertBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(desertButtonPosition, label))
			);
			map.add(canyonDiabloGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(canyonDiabloButtonPosition, label))
			);
			map.add(canyonDiabloBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(canyonDiabloButtonPosition, label))
			);
			map.add(phoenixGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(phoenixButtonPosition, label))
			);
			map.add(phoenixBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(phoenixButtonPosition, label))
			);
			map.add(tucsonGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(tucsonButtonPosition, label))
			);
			map.add(tucsonBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(tucsonButtonPosition, label))
			);
			map.add(elPasoGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(elPasoButtonPosition, label))
			);
			map.add(elPasoBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(elPasoButtonPosition, label))
			);
			map.add(santaAnaGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(santaAnaButtonPosition, label))
			);
			map.add(santaAnaBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(santaAnaButtonPosition, label))
			);
			map.add(langstoneBridgeGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(langstoneBridgeButtonPosition, label))
			);
			map.add(langstoneBridgeBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(langstoneBridgeButtonPosition, label))
			);
			map.add(sadHillGraveyardGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(sadHillGraveyardButtonPosition, label))
			);
			map.add(sadHillGraveyardBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(sadHillGraveyardButtonPosition, label))
			);
			map.add(unionPrisonGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(unionPrisonButtonPosition, label))
			);
			map.add(unionPrisonBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(unionPrisonButtonPosition, label))
			);
			map.add(confederatePrisonGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(confederatePrisonButtonPosition, label))
			);
			map.add(confederatePrisonBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(confederatePrisonButtonPosition, label))
			);
			map.add(saintAnthonyMissionGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(saintAnthonyMissionButtonPosition, label))
			);
			map.add(saintAnthonyMissionBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(saintAnthonyMissionButtonPosition, label))
			);
		}
	}
}
