import javax.swing.*;
import java.awt.*;

/**
 * LoginDialog class
 */
public class LoginDialog {

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
        JLabel usernameLabel = new JLabel(" Please choose a username: ");
        upperPanelLayout.gridx = 0;
        upperPanelLayout.gridy = 0;
        upperPanelLayout.gridwidth = 1;
        upperPanel.add(usernameLabel, upperPanelLayout);

        // Setting up username textfield
        JTextField usernameTextField = new JTextField(20);
        upperPanelLayout.gridx = 1;
        upperPanelLayout.gridy = 0;
        upperPanelLayout.gridwidth = 2;
        upperPanel.add(usernameTextField, upperPanelLayout);

        // Setting up login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            Client.setUsername(usernameTextField.getText().trim());
            dialog.dispose();
        });
        lowerPanel.add(loginButton);

        // Setting latest JDialog options before launch
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setVisible(true);
    }
}
