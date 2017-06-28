import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ChatWindow class
 */
public class ChatWindow {

    private JTextArea chatView;
    private JTextArea chatField;

    public ChatWindow() {

        // Window settings
        JFrame window = new JFrame("Chat");
        window.setSize(400, 700);

        // Setting JFrame main layout manager
        Container content = window.getContentPane();
        content.setLayout(new BorderLayout());

        // Configuring central part of the window
        chatView = new JTextArea();
        chatView.setEditable(false);
        content.add(chatView, BorderLayout.CENTER);

        // Configuring lower part of the window
        JPanel chatEditor = new JPanel();
        chatEditor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatEditor.setLayout(new BorderLayout());
        content.add(chatEditor, BorderLayout.PAGE_END);

        // Setting up chat editor text field
        chatField = new JTextArea();
        chatField.setEditable(true);
        chatEditor.add(chatField, BorderLayout.CENTER);

        // Setting up send button
        JButton sendMessage = new JButton("Send >>>");
        sendMessage.addActionListener(e -> sendMessage());
        chatEditor.add(sendMessage, BorderLayout.EAST);

        // Setting latest JFrame options before launch
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setVisible(true);
    }

    private void sendMessage() {
        chatView.append(" " + Client.getUsername() + ": " + chatField.getText() + "\n");
        chatField.setText("");
    }
}