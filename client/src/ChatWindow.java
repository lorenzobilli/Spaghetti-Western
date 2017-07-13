import javafx.scene.input.KeyCode;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Future;

/**
 * ChatWindow class
 */
public class ChatWindow {

    private JTextArea chatView;
    private JTextArea chatField;
    private JTextArea chatSelectionField;

    public ChatWindow() {

        // Window settings
        JFrame window = new JFrame(Client.getUsername());
        window.setSize(400, 700);

        // Setting JFrame main layout manager
        Container content = window.getContentPane();
        content.setLayout(new BorderLayout());

        // Configuring upper part of the window
        JPanel chatSelector = new JPanel();
        chatSelector.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chatSelector.setLayout(new BorderLayout());
        content.add(chatSelector, BorderLayout.PAGE_START);


        chatSelectionField = new JTextArea();
        chatSelectionField.setEditable(true);
        chatSelector.add(chatSelectionField, BorderLayout.CENTER);

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
        System.out.println(Client.getUsername() + " is sending a message to " + chatSelectionField.getText());
        chatView.append(" [" + Client.getUsername() + "]: " + chatField.getText() + "\n");
        Message chatMessage = new Message(
                MessageType.CHAT, Client.getUsername(), chatSelectionField.getText(), chatField.getText()
        );
        Future sendMessage = Client.globalThreadPool.submit(new Sender(chatMessage, Client.connectionManager.getSendStream()));
        chatField.setText("");
    }

    public void updateChat(Message message) {
        chatView.append(" [" + message.getMessageSender() + "]: " + message.getMessageContent() + "\n");
    }
}