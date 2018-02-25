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
