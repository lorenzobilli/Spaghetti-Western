import javax.swing.*;
import java.awt.*;

/**
 * LoginDialog class
 */
public class LoginDialog {

    private JDialog dialog;
    private JTextField usernameTextField;

    public LoginDialog(Frame parentWindow) {
        dialog = new JDialog(parentWindow, "Start new gaming session", true);

        JPanel upperPanel = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        layout.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel(" Please choose a username: ");
        layout.gridx = 0;
        layout.gridy = 0;
        layout.gridwidth = 1;
        upperPanel.add(usernameLabel, layout);

        usernameTextField = new JTextField(20);
        layout.gridx = 1;
        layout.gridy = 0;
        layout.gridwidth = 2;
        upperPanel.add(usernameTextField, layout);

        JPanel lowerPanel = new JPanel();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            Client.setUsername(usernameTextField.getText().trim());
            dialog.dispose();
        });
        lowerPanel.add(loginButton);

        dialog.getContentPane().add(upperPanel, BorderLayout.CENTER);
        dialog.getContentPane().add(lowerPanel, BorderLayout.PAGE_END);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentWindow);
        dialog.setVisible(true);
    }
}
