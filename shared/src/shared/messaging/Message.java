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

package shared.messaging;

import shared.gaming.Player;

import java.security.InvalidParameterException;

/**
 * Message object implementation.
 */
public class Message {

	/**
	 * Type of message.
	 */
	private Type type;

	/**
	 * Sender of the message.
	 */
	private Player messageSender;

	/**
	 * Receiver of the message.
	 */
	private Player messageReceiver;

	/**
	 * Content of the message.
	 */
	private String messageContent;

	/**
	 * Possible types of a message. Enabled types are:
	 *  - SESSION: Session-related messages.
	 *  - TIME: Time-related messages.
	 *  - CHAT: Chat-related messages.
	 *  - SCENERY: Scenery-related messages.
	 *  - MOVE: Move-related messages.
	 *  - CLASH: Clash-related messages.
	 */
	public enum Type {
		SESSION,
		TIME,
		CHAT,
		SCENERY,
		MOVE,
		CLASH
	}

	/**
	 * Creates a new message.
	 * @param type Type of message
	 * @param messageSender Sender of the message.
	 * @param messageReceiver Receiver of the message.
	 * @param messageContent Content of the message.
	 */
	public Message(Type type, Player messageSender, Player messageReceiver, String messageContent) {
		if (type == null) {
			throw new InvalidParameterException("Type cannot be null");
		}
		if (messageSender == null) {
			throw new InvalidParameterException("MessageSender cannot be null");
		}
		if (messageReceiver == null) {
			throw new InvalidParameterException("MessageReceiver cannot be null");
		}
		if (messageContent == null) {
			throw new InvalidParameterException("MessageContent cannot be null");
		}
		this.type = type;
		this.messageSender = messageSender;
		this.messageReceiver = messageReceiver;
		this.messageContent = messageContent;
	}

	/**
	 * Creates a new message with the server as message receiver.
	 * @param type Type of message.
	 * @param messageSender Sender of the message.
	 * @param messageContent Content of the message.
	 */
	public Message(Type type, Player messageSender, String messageContent) {
		if (type == null) {
			throw new InvalidParameterException("Type cannot be null");
		}
		if (messageSender == null) {
			throw new InvalidParameterException("MessageReceiver cannot be null");
		}
		if (messageContent == null) {
			throw new InvalidParameterException("MessageContent cannot be null");
		}
		this.type = type;
		this.messageSender = messageSender;
		this.messageReceiver = new Player("SERVER", Player.Team.SERVER);
		this.messageContent = messageContent;
	}

	/**
	 * Gets message type.
	 * @return Type of message.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Gets message sender.
	 * @return Sender of the message.
	 */
	public Player getMessageSender() {
		return this.messageSender;
	}

	/**
	 * Gets message receiver.
	 * @return Receiver of the message.
	 */
	public Player getMessageReceiver() {
		return this.messageReceiver;
	}

	/**
	 * Gets message content.
	 * @return Content of the message.
	 */
	public String getMessageContent() {
		return this.messageContent;
	}
}
