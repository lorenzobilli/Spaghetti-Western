package client;

import shared.Player;

import javax.swing.*;
import java.awt.*;

/**
 * client.LoginDialog class
 */
public class LoginDialog {

    private String choosenName;
    private Player.Team choosenTeam;
    private boolean dataConfigured;

    public LoginDialog(Frame parentWindow) {

        // Dialog settings
        JDialog dialog = new JDialog(parentWindow, "Start new gaming session", true);

        // Setting JDialog main layout manager
        Container content = dialog.getContentPane();
        content.setLayout(new BorderLayout());

        // Configuring upper part of the dialog
        JPanel upperPanel = new JPanel(new GridBagLayout());
        GridBagConstraints upperPanelLayout = new GridBagConstraints();
        upperPanelLayout.fill = GridBagConstraints.HORIZONTAL;
        content.add(upperPanel, BorderLayout.CENTER);

        // Configuring lower part of the dialog
        JPanel lowerPanel = new JPanel();
        content.add(lowerPanel, BorderLayout.PAGE_END);

        // Setting up username label
        JLabel usernameLabel = new JLabel(" Please choose a player name: ");
        upperPanelLayout.gridx = 0;
        upperPanelLayout.gridy = 0;
        upperPanel.add(usernameLabel, upperPanelLayout);

        // Setting up username textfield
        JTextField usernameTextField = new JTextField(20);
        upperPanelLayout.gridx = 1;
        upperPanelLayout.gridy = 0;
        upperPanel.add(usernameTextField, upperPanelLayout);

        // Setting up team label
        JLabel teamLabel = new JLabel(" Please choose a team: ");
        upperPanelLayout.gridx = 0;
        upperPanelLayout.gridy = 1;
        upperPanel.add(teamLabel, upperPanelLayout);

        //Setting up team combobox
        String[] teams = {"Please select a team...", "GOOD", "BAD"};
        JComboBox teamComboBox = new JComboBox<>(teams);
        teamComboBox.setSelectedItem(teams[0]);
        teamComboBox.addActionListener(e -> {
            String selectedTeam = String.valueOf(teamComboBox.getSelectedItem());
            switch (selectedTeam) {
                case "GOOD":
                    choosenTeam = Player.Team.GOOD;
                    dataConfigured = true;
                    break;
                case "BAD":
                    choosenTeam = Player.Team.BAD;
                    dataConfigured = true;
                    break;
                default:
                    dataConfigured = false;
                    break;
            }
        });
        upperPanelLayout.gridx = 1;
        upperPanelLayout.gridy = 1;
        upperPanel.add(teamComboBox, upperPanelLayout);

        // Setting up login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            if (dataConfigured) {
                choosenName = usernameTextField.getText().trim();
                Client.setPlayer(new Player(choosenName, choosenTeam));
                dialog.dispose();
            }
        });
        lowerPanel.add(loginButton);

        // Setting latest JDialog options before launch
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setVisible(true);
    }
}
