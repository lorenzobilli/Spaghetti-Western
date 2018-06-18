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

import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Receiver class. Message receiving routines are all abstracted inside this class.
 */
public class Receiver implements Callable<Message> {

	/**
	 * Stream used for receiving messages.
	 */
	private BufferedReader receiveStream;

	/**
	 * Creates a new instance of Receiver listening for new messages on the given stream.
	 * @param receiveStream Receiving stream on which the receiver will listen for new messages.
	 */
	public Receiver(BufferedReader receiveStream) {
		if (receiveStream == null) {
			throw new InvalidParameterException("Invalid receiveStream stream given");
		}
		this.receiveStream = receiveStream;
	}

	/**
	 * Read a new incoming streaming message in a thread-safe manner and return the translated message
	 * @return The received message as a proper Message object.
	 */
	@Override
	public Message call() {
		String receivedMessage = "";
		try {
			synchronized (this) {
				receivedMessage = receiveStream.readLine();
			}
		} catch (IOException e) {
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}
		return MessageManager.prepareReceive(receivedMessage);
	}
}
