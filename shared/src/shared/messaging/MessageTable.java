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

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Object used to compose a message before its translation.
 */
public class MessageTable extends HashMap<String, String> {

	/**
	 * Creates a new MessageTable.
	 */
	public MessageTable() {
		super();
	}

	/**
	 * Creates a new MessageTable with a specified key and value.
	 * @param key Key of the MessageTable.
	 * @param value Value of the MessageTable.
	 */
	public MessageTable(String key, String value) {
		super();
		if (key == null) {
			throw new InvalidParameterException("Key cannot be null");
		}
		if (value == null) {
			throw new InvalidParameterException("Value cannot be null");
		}
		put(key, value);
	}
}
