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

package shared.handle;

import shared.messaging.Message;

import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Base class for event handling. All derived class must implement handling methods for each message type.
 * This is generated with an instance of a message. The message is then handled with the appropriate handler by the
 * server and the client implementations.
 */
public abstract class EventHandler implements Callable<Message> {

	/**
	 * Message to be handled.
	 */
	protected Message message;

	/**
	 * Creates a new Event Handler object.
	 * @param message Message to be handled.
	 */
	protected EventHandler(Message message) {
		if (message == null) {
			throw new InvalidParameterException("Unable to handle a null message");
		}
		this.message = message;
	}

	/**
	 * Call appropriate handlers based upon message type.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	@Override
	public Message call() throws HandlerException {
		Message result;
		switch (message.getType()) {
			case SESSION:
				result = handleSession();
				break;
			case TIME:
				result = handleTime();
				break;
			case CHAT:
				result = handleChat();
				break;
			case SCENERY:
				result = handleScenery();
				break;
			case MOVE:
				result = handleMove();
				break;
			case CLASH:
				result = handleClash();
				break;
			default:
				throw new HandlerException("Unrecognized message type");
		}
		return result;
	}

	/**
	 * Abstract declaration for session-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleSession() throws HandlerException;

	/**
	 * Abstract declaration for time-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleTime() throws HandlerException;

	/**
	 * Abstract declaration for chat-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleChat();

	/**
	 * Abstract declaration for scenery-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleScenery() throws HandlerException;

	/**
	 * Abstract declaration for move-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleMove() throws HandlerException;

	/**
	 * Abstract declaration for clash-related events handler.
	 * @return A new resulting message with further handling operations. Please note that the resulting message may be
	 * null if no further operations are required.
	 */
	protected abstract Message handleClash() throws HandlerException;

}
