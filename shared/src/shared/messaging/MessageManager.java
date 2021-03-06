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

import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Set;

/**
 * Handle message translation to and from XML format.
 */
public class MessageManager {

	/**
	 * Manages JSON/XML translation.
	 */
	private static Gson manager = new Gson();

	/**
	 * Prepares to send the message by translating it from XML to JSON format.
	 * @param message Message to be translated.
	 * @return Translated message in JSON format.
	 */
	public static String prepareSend(Message message) {
		return manager.toJson(message);
	}

	/**
	 * Prepares to receive the message by translating it from JSON to XML format.
	 * @param message Message to be translated.
	 * @return Translated message in XML format.
	 */
	public static Message prepareReceive(String message) {
		return manager.fromJson(message, Message.class);
	}

	/**
	 * Create a XML message from a MessageTable object.
	 * @param messageTable MessageTable used for translation.
	 * @return Translated message in XML format.
	 */
	public static String createXML(MessageTable messageTable) {
		if (messageTable == null) {
			throw new InvalidParameterException("Message table cannot be null");
		}
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String begin = "<message>";
		String end = "</message>";
		String elem = "";

		Set<Map.Entry<String, String>> tableEntries = messageTable.entrySet();
		for (Map.Entry<String, String> entry : tableEntries) {
			elem = elem.concat("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">");
		}

		return header + begin + elem + end;
	}

	/**
	 * Gets corresponding string value inside a message given its tag value.
	 * @param tag Tag value corresponding to the retrieving value.
	 * @param message Message from which the value should be retrieved.
	 * @return Value retrieved from the message.
	 */
	public static String convertXML(String tag, String message) {
		if (tag == null) {
			throw new InvalidParameterException("Tag cannot be null");
		}
		if (message == null) {
			throw new InvalidParameterException("shared.messaging.Message cannot be null");
		}
		String result = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource input = new InputSource(new StringReader(message));
			Document document = builder.parse(input);
			result = document.getElementsByTagName(tag).item(0).getTextContent();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.getMessage();
			e.getCause();
			e.printStackTrace();
		}

		return result;
	}
}
