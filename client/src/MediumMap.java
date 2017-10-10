import javax.swing.*;
import java.awt.*;

/**
 * MediumMap class
 */
public class MediumMap extends Map {

	{
		super.redHatIcon = "shared/assets/red_hat_medium.png";
		super.whiteHatIcon = "shared/assets/white_hat_medium.png";
		super.blackHatIcon = "shared/assets/black_hat_medium.png";
		super.buttonDimension = new Dimension(45, 45);
		super.labelDimension = new Dimension(45, 32);
		super.xLabelMargin = 3;	//TODO: Check this value
		super.yLabelMargin = 3; //TODO: Check this value
	}

	private JButton santaFeButton = new JButton();
	private JButton sanRafaelButton = new JButton();
	private JButton valverdeButton = new JButton();
	private JButton watermillButton = new JButton();
	private JButton desertButton = new JButton();
	private JButton canyonDiabloButton = new JButton();
	private JButton phoenixButton = new JButton();
	private JButton tucsonButton = new JButton();
	private JButton elPasoButton = new JButton();
	private JButton unionPrisonButton = new JButton();

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

	private JLabel unionPrisonPlayerLabel = new JLabel();
	private JLabel[] unionPrisonGoodLabels = new JLabel[labelClusterSize];
	private JLabel[] unionPrisonBadLabels = new JLabel[labelClusterSize];

	public MediumMap() {
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
		configureButton(unionPrisonButton, "Union prison");

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
			unionPrisonGoodLabels[label] = new JLabel();
			unionPrisonBadLabels[label] = new JLabel();
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
		configureLabels(unionPrisonPlayerLabel, unionPrisonGoodLabels, unionPrisonBadLabels);
	}

	@Override
	protected void populate(MapWindow map) {

		// Configuring button positions
		Point santaFeButtonPosition = new Point(947, 42 - borderSize);
		Point sanRafaelButtonPosition = new Point(654, 93 - borderSize);
		Point valverdeButtonPosition = new Point(930, 214 - borderSize);
		Point watermillButtonPosition = new Point(1229, 124 - borderSize);
		Point desertButtonPosition = new Point(888, 445 - borderSize);
		Point canyonDiabloButtonPosition = new Point(281, 124 - borderSize);
		Point phoenixButtonPosition = new Point(114, 404 - borderSize);
		Point tucsonButtonPosition = new Point(269, 601 - borderSize);
		Point elPasoButtonPosition = new Point(874, 679 - borderSize);
		Point unionPrisonButtonPosition = new Point(1209, 388 - borderSize);

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
		map.add(unionPrisonButton, buttonDimension, unionPrisonButtonPosition);

		// Adding single labels
		map.add(santaFePlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(santaFeButtonPosition)));
		map.add(sanRafaelPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(sanRafaelButtonPosition)));
		map.add(valverdePlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(valverdeButtonPosition)));
		map.add(watermillPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(watermillButtonPosition)));
		map.add(desertPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(desertButtonPosition)));
		map.add(canyonDiabloPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(canyonDiabloButtonPosition)));
		map.add(phoenixPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(phoenixButtonPosition)));
		map.add(tucsonPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(tucsonButtonPosition)));
		map.add(elPasoPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(elPasoButtonPosition)));
		map.add(unionPrisonPlayerLabel, labelDimension, new Point(calculatePlayerLabelPosition(unionPrisonButtonPosition)));

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
			map.add(unionPrisonGoodLabels[label],
					labelDimension,
					new Point(calculateGoodLabelPosition(unionPrisonButtonPosition, label))
			);
			map.add(unionPrisonBadLabels[label],
					labelDimension,
					new Point(calculateBadLabelPosition(unionPrisonButtonPosition, label))
			);
		}
	}
}
