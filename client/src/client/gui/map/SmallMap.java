package client.gui.map;

import client.gui.MapWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Implementation of a small size map.
 * A small map is meant to be used when connected players number is below 10.
 */
public class SmallMap extends Map {

	// Setting up some generic settings such as paths to graphical assets and buttons/labels dimensions
	{
		super.mapBackground = "shared/assets/small_map.jpg";
		super.redHatIcon = "shared/assets/red_hat_small.png";
		super.whiteHatIcon = "shared/assets/white_hat_small.png";
		super.blackHatIcon = "shared/assets/black_hat_small.png";
		super.buttonDimension = new Dimension(60, 60);
		super.labelDimension = new Dimension(60, 43);
		super.xLabelMargin = 5;
		super.yLabelMargin = 5;
	}

	// Buttons declaration:

	/**
	 * Button assigned to Santa Fe.
	 */
	private JButton santaFeButton = new JButton();

	/**
	 * Button assigned to San Rafael.
	 */
	private JButton sanRafaelButton = new JButton();

	/**
	 * Button assigned to Valverde.
	 */
	private JButton valverdeButton = new JButton();

	/**
	 * Button assigned to the watermill.
	 */
	private JButton watermillButton = new JButton();

	/**
	 * Button assigned to the desert.
	 */
	private JButton desertButton = new JButton();

	// Labels declaration and arrays initialization:

	/**
	 * Player label assigned to Santa Fe.
	 */
	private JLabel santaFePlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to Santa Fe.
	 */
	private JLabel[] santaFeGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to Santa Fe.
	 */
	private JLabel[] santaFeBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to San Rafael.
	 */
	private JLabel sanRafaelPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to San Rafael.
	 */
	private JLabel[] sanRafaelGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to San Rafael.
	 */
	private JLabel[] sanRafaelBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to Valverde.
	 */
	private JLabel valverdePlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to Valverde.
	 */
	private JLabel[] valverdeGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to Valverde.
	 */
	private JLabel[] valverdeBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to the watermill.
	 */
	private JLabel watermillPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to the watermill.
	 */
	private JLabel[] watermillGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to the watermill.
	 */
	private JLabel[] watermillBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to the desert.
	 */
	private JLabel desertPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to the desert.
	 */
	private JLabel[] desertGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to the desert.
	 */
	private JLabel[] desertBadLabels = new JLabel[labelClusterSize];

	/**
	 * Creates a new instance of a small map.
	 */
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

	/**
	 * Inserts all map elements into a MapWindow instance.
	 * @param map Instance of MapWindow to be populated.
	 */
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
