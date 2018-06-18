/*
 *  Project: "Spaghetti Western"
 *
 *
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2017-2018 Lorenzo Billi
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *	documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *	rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 *	permit persons to whom the Software is	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 *	the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *	WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 *	OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *	OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package client.gui.map;

import client.gui.MapWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Implementation of a medium size map.
 * A medium map is meant to be used when connected players number is between 10 and 20.
 */
public class MediumMap extends Map {

	// Setting up some generic settings such as paths to graphical assets and buttons/labels dimensions.
	{
		super.mapBackground = "shared/assets/medium_map.jpg";
		super.redHatIcon = "shared/assets/red_hat_medium.png";
		super.whiteHatIcon = "shared/assets/white_hat_medium.png";
		super.blackHatIcon = "shared/assets/black_hat_medium.png";
		super.buttonDimension = new Dimension(45, 45);
		super.labelDimension = new Dimension(45, 32);
		super.xLabelMargin = 3;
		super.yLabelMargin = 3;
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

	/**
	 * Button assigned to Canyon Diablo.
	 */
	private JButton canyonDiabloButton = new JButton();

	/**
	 * Button assigned to Phoenix.
	 */
	private JButton phoenixButton = new JButton();

	/**
	 * Button assigned to Tucson.
	 */
	private JButton tucsonButton = new JButton();

	/**
	 * Button assigned to El Paso.
	 */
	private JButton elPasoButton = new JButton();

	/**
	 * Button assigned to Union prison.
	 */
	private JButton unionPrisonButton = new JButton();

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
	 * Player labels assigned to Canyon Diablo.
	 */
	private JLabel canyonDiabloPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to Canyon Diablo.
	 */
	private JLabel[] canyonDiabloGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to Canyon Diablo.
	 */
	private JLabel[] canyonDiabloBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to Phoenix.
	 */
	private JLabel phoenixPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to Phoenix.
	 */
	private JLabel[] phoenixGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to Phoenix.
	 */
	private JLabel[] phoenixBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to Tucson.
	 */
	private JLabel tucsonPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to Tucson.
	 */
	private JLabel[] tucsonGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to Tucson.
	 */
	private JLabel[] tucsonBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to El Paso.
	 */
	private JLabel elPasoPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to El Paso.
	 */
	private JLabel[] elPasoGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to El Paso.
	 */
	private JLabel[] elPasoBadLabels = new JLabel[labelClusterSize];

	/**
	 * Player label assigned to Union prison.
	 */
	private JLabel unionPrisonPlayerLabel = new JLabel();

	/**
	 * Good-team player labels assigned to Union prison.
	 */
	private JLabel[] unionPrisonGoodLabels = new JLabel[labelClusterSize];

	/**
	 * Bad-team player labels assigned to Union prison.
	 */
	private JLabel[] unionPrisonBadLabels = new JLabel[labelClusterSize];

	/**
	 * Creates a new instance of a medium map.
	 */
	public MediumMap() {

		// Declaring used positions
		final String santaFePosition = "Santa Fe";
		final String sanRafaelPosition = "San Rafael";
		final String valverdePosition = "Valverde";
		final String watermillPosition = "Watermill";
		final String desertPosition = "Desert";
		final String canyonDiabloPosition = "Canyon Diablo";
		final String phoenixPosition = "Phoenix";
		final String tucsonPosition = "Tucson";
		final String elPasoPosition = "El Paso";
		final String unionPrisonPosition = "Union prison";

		// Configuring buttons
		configureButton(santaFeButton, santaFePosition);
		configureButton(sanRafaelButton, sanRafaelPosition);
		configureButton(valverdeButton, valverdePosition);
		configureButton(watermillButton, watermillPosition);
		configureButton(desertButton, desertPosition);
		configureButton(canyonDiabloButton, canyonDiabloPosition);
		configureButton(phoenixButton, phoenixPosition);
		configureButton(tucsonButton, tucsonPosition);
		configureButton(elPasoButton, elPasoPosition);
		configureButton(unionPrisonButton, unionPrisonPosition);

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

		// Populating internal playerLabels hashmap
		playerLabels.put(santaFePosition, santaFePlayerLabel);
		playerLabels.put(sanRafaelPosition, sanRafaelPlayerLabel);
		playerLabels.put(valverdePosition, valverdePlayerLabel);
		playerLabels.put(watermillPosition, watermillPlayerLabel);
		playerLabels.put(desertPosition, desertPlayerLabel);
		playerLabels.put(canyonDiabloPosition, canyonDiabloPlayerLabel);
		playerLabels.put(phoenixPosition, phoenixPlayerLabel);
		playerLabels.put(tucsonPosition, tucsonPlayerLabel);
		playerLabels.put(elPasoPosition, elPasoPlayerLabel);
		playerLabels.put(unionPrisonPosition, unionPrisonPlayerLabel);

		// Populating internal goodLabels hashmap
		goodLabels.put(santaFePosition, santaFeGoodLabels);
		goodLabels.put(sanRafaelPosition, sanRafaelGoodLabels);
		goodLabels.put(valverdePosition, valverdeGoodLabels);
		goodLabels.put(watermillPosition, watermillGoodLabels);
		goodLabels.put(desertPosition, desertGoodLabels);
		goodLabels.put(canyonDiabloPosition, canyonDiabloGoodLabels);
		goodLabels.put(phoenixPosition, phoenixGoodLabels);
		goodLabels.put(tucsonPosition, tucsonGoodLabels);
		goodLabels.put(elPasoPosition, elPasoGoodLabels);
		goodLabels.put(unionPrisonPosition, unionPrisonGoodLabels);

		// Populating internal badLabels hashmap
		badLabels.put(santaFePosition, santaFeBadLabels);
		badLabels.put(sanRafaelPosition, sanRafaelBadLabels);
		badLabels.put(valverdePosition, valverdeBadLabels);
		badLabels.put(watermillPosition, watermillBadLabels);
		badLabels.put(desertPosition, desertBadLabels);
		badLabels.put(canyonDiabloPosition, canyonDiabloBadLabels);
		badLabels.put(phoenixPosition, phoenixBadLabels);
		badLabels.put(tucsonPosition, tucsonBadLabels);
		badLabels.put(elPasoPosition, elPasoBadLabels);
		badLabels.put(unionPrisonPosition, unionPrisonBadLabels);
	}

	/**
	 * Inserts all map elements into a MapWindow instance.
	 * @param map Instance of MapWindow to be populated.
	 */
	@Override
	public void populate(MapWindow map) {

		// Adding global button and labels
		super.configureTotalTimeLabel(map);
		super.configureTurnTimeLabel(map);
		super.configureBulletLabel(map);
		super.configureClashButton(map);

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
		map.add(santaFePlayerLabel,
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
				unionPrisonPlayerLabel,
				labelDimension,
				new Point(calculatePlayerLabelPosition(unionPrisonButtonPosition))
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

	/**
	 * Quickly enables all clickable user commands.
	 */
	@Override
	public void enableUserCommands() {
		enableButton(santaFeButton);
		enableButton(sanRafaelButton);
		enableButton(valverdeButton);
		enableButton(watermillButton);
		enableButton(desertButton);
		enableButton(canyonDiabloButton);
		enableButton(phoenixButton);
		enableButton(tucsonButton);
		enableButton(elPasoButton);
		enableButton(unionPrisonButton);
	}

	/**
	 * Quickly disables all clickable user commands.
	 */
	@Override
	public void disableUserCommands() {
		disableButton(santaFeButton);
		disableButton(sanRafaelButton);
		disableButton(valverdeButton);
		disableButton(watermillButton);
		disableButton(desertButton);
		disableButton(canyonDiabloButton);
		disableButton(phoenixButton);
		disableButton(tucsonButton);
		disableButton(elPasoButton);
		disableButton(unionPrisonButton);
	}
}
