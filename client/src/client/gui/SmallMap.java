package client.gui;

import javax.swing.*;
import java.awt.*;

/**
 * client.gui.SmallMap class
 */
public class SmallMap extends Map {

	{
		super.redHatIcon = "shared/assets/red_hat_small.png";
		super.whiteHatIcon = "shared/assets/white_hat_small.png";
		super.blackHatIcon = "shared/assets/black_hat_small.png";
		super.buttonDimension = new Dimension(60, 60);
		super.labelDimension = new Dimension(60, 43);
		super.xLabelMargin = 5;
		super.yLabelMargin = 5;
	}

	// Buttons declaration
	private JButton santaFeButton = new JButton();
	private JButton sanRafaelButton = new JButton();
	private JButton valverdeButton = new JButton();
	private JButton watermillButton = new JButton();
	private JButton desertButton = new JButton();

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

	public SmallMap() {

		// Declaring used positions
		final String santaFePosition = "Santa Fe";
		final String sanRafaelPosition = "San Rafael";
		final String valverdePosition = "Valverde";
		final String watermillPosition = "Watermill";
		final String desertPosition = "Desert";

		// Configuring buttons
		configureButton(santaFeButton, santaFePosition);
		configureButton(sanRafaelButton, sanRafaelPosition);
		configureButton(valverdeButton, valverdePosition);
		configureButton(watermillButton, watermillPosition);
		configureButton(desertButton, desertPosition);

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
		}

		// Configuring labels
		configureLabels(santaFePlayerLabel, santaFeGoodLabels, santaFeBadLabels);
		configureLabels(sanRafaelPlayerLabel, sanRafaelGoodLabels, sanRafaelBadLabels);
		configureLabels(valverdePlayerLabel, valverdeGoodLabels, valverdeBadLabels);
		configureLabels(watermillPlayerLabel, watermillGoodLabels, watermillBadLabels);
		configureLabels(desertPlayerLabel, desertGoodLabels, desertBadLabels);

		// Populating internal playerLabels hashmap
		playerLabels.put(santaFePosition, santaFePlayerLabel);
		playerLabels.put(sanRafaelPosition, sanRafaelPlayerLabel);
		playerLabels.put(valverdePosition, valverdePlayerLabel);
		playerLabels.put(watermillPosition, watermillPlayerLabel);
		playerLabels.put(desertPosition, desertPlayerLabel);

		// Populating internal goodLabels hashmap
		goodLabels.put(santaFePosition, santaFeGoodLabels);
		goodLabels.put(sanRafaelPosition, sanRafaelGoodLabels);
		goodLabels.put(valverdePosition, valverdeGoodLabels);
		goodLabels.put(watermillPosition, watermillGoodLabels);
		goodLabels.put(desertPosition, desertGoodLabels);

		// Populating internal badLabels hashmap
		badLabels.put(santaFePosition, santaFeBadLabels);
		badLabels.put(sanRafaelPosition, sanRafaelBadLabels);
		badLabels.put(valverdePosition, valverdeBadLabels);
		badLabels.put(watermillPosition, watermillBadLabels);
		badLabels.put(desertPosition, desertBadLabels);
	}

	@Override
	public void populate(MapWindow map) {

		// Adding global button and labels
		super.configureBulletLabel(map);
		super.configureClashButton(map);

		// Configuring button positions
		Point santaFeButtonPosition = new Point(650, 71 - borderSize);
		Point sanRafaelButtonPosition = new Point(225, 145 - borderSize);
		Point valverdeButtonPosition = new Point(622, 318 - borderSize);
		Point watermillButtonPosition = new Point(1043, 185 - borderSize);
		Point desertButtonPosition = new Point(558, 646 - borderSize);

		// Adding buttons
		map.add(santaFeButton, buttonDimension, santaFeButtonPosition);
		map.add(sanRafaelButton, buttonDimension, sanRafaelButtonPosition);
		map.add(valverdeButton, buttonDimension, valverdeButtonPosition);
		map.add(watermillButton, buttonDimension, watermillButtonPosition);
		map.add(desertButton, buttonDimension, desertButtonPosition);

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
		map.add(watermillPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(watermillButtonPosition))
		);
		map.add(
				desertPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(desertButtonPosition))
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
		}
	}
}
