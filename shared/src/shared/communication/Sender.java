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

package shared.communication;

import shared.messaging.Message;
import shared.messaging.MessageManager;

import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Sender class. Message sending routines are all abstracted inside this class.
 */
public class Sender implements Callable<Object> {

	/**
	 * Message to be sent.
	 */
	private Message message;

	/**
	 * Stream used for sending messages.
	 */
	private PrintWriter sendStream;

	/**
	 * Creates a new instance of Sender sending on the given stream.
	 * @param message Message to be sent.
	 * @param sendStream Sending stream on which the sender will send the message.
	 */
	public Sender(Message message, PrintWriter sendStream) {
		if (message == null) {
			throw new InvalidParameterException("Null message given");
		}
		if (sendStream == null) {
			throw new InvalidParameterException("Invalid sendStream stream given");
		}
		this.message = message;
		this.sendStream = sendStream;
	}

	/**
	 * Translate a Message object into its streaming form, then send it in a thread-safe manner via the given stream.
	 * @return Always returns a null object.
	 */
	@Override
	public Object call() {
		String translatedMessage = MessageManager.prepareSend(message);
		synchronized (this) {
			sendStream.println(translatedMessage);
		}
		return null;
	}
}
