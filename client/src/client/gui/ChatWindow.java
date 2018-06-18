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

package client.gui;

import client.Client;
import shared.communication.Sender;
import shared.messaging.Message;
import shared.messaging.MessageManager;
import shared.messaging.MessageTable;

import javax.swing.*;
import java.awt.*;

/**
 * Class implementing chat window of each client.
 */
public class ChatWindow {

	/**
	 * Text area for displaying chat messages.
	 */
	private JTextArea chatView;

	/**
	 * Text area for writing user's chats.
	 */
	private JTextArea chatField;

	/**
	 * Spawns a new chat window.
	 */
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

	/**
	 * Sends a message to the other members of the current user's team.
	 */
	private void sendMessage() {
		chatView.append(" [" + Client.getPlayer().getName() + "]: " + chatField.getText() + "\n");
		Message chatMessage = new Message(
				Message.Type.CHAT,
				Client.getPlayer(),
				MessageManager.createXML(new MessageTable("content", chatField.getText()))
		);
		Client.globalThreadPool.submit(new Sender(chatMessage, Client.connectionManager.getSendStream()));
		chatField.setText("");
	}

	/**
	 * Update the chat window with a new received message.
	 * @param message Received message.
	 */
	public void updateChat(Message message) {
		chatView.append(" [" + message.getMessageSender().getName() + "]: " +
				MessageManager.convertXML("content", message.getMessageContent()) + "\n"
		);
	}
}