package shared;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class MessageTable extends HashMap<String, String> {

	public MessageTable() {
		super();
	}

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
