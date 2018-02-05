package client.gui;

import client.Client;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;
import shared.Sender;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Future;

/**
 * client.gui.ChatWindow class
 */
public class ChatWindow {

    private JTextArea chatView;
    private JTextArea chatField;

    public ChatWindow() {

        // Window settings
        JFrame window = new JFrame(Client.getPlayer().getName());
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
        chatView.append(" [" + Client.getPlayer().getName() + "]: " + chatField.getText() + "\n");
        Message chatMessage = new Message(
                Message.Type.CHAT,
                Client.getPlayer(),
		        MessageManager.createXML(new MessageTable("content", chatField.getText()))
        );
        Future sendMessage = Client.globalThreadPool.submit(
                new Sender(chatMessage, Client.connectionManager.getSendStream())
        );
        chatField.setText("");
    }

    public void updateChat(Message message) {
        chatView.append(" [" + message.getMessageSender().getName() + "]: " +
                MessageManager.convertXML("content", message.getMessageContent()) + "\n"
        );
    }
}