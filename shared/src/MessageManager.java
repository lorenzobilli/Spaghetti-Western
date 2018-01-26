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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * MessageManager class
 */
public class MessageManager {
    private static Gson manager = new Gson();

    public static String prepareSend(Message message) {
        return manager.toJson(message);
    }

    public static Message prepareReceive(String message) {
        return manager.fromJson(message, Message.class);
    }

    public static String createXML(String tag, String value) {
        if (tag == null) {
            throw new InvalidParameterException("tag cannot be null");
        }
        if (value == null) {
            throw new InvalidParameterException("value cannot be null");
        }
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String begin = "<message>";
        String end = "</message>";
        String elem = "<" + tag + ">" + value + "</" + tag + ">";
        return header + begin + elem + end;
    }

    public static String createXML(MessageTable messageTable) {
    	if (messageTable == null) {
    		throw new InvalidParameterException("Message table cannot be null");
	    }
	    String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    	String begin = "<message>";
    	String end = "</message>";
    	String elem = "";

    	for (String value : messageTable.values()) {
    		String key = null;
		    Set<Map.Entry<String, String>> entries = messageTable.entrySet();
    		for (Map.Entry<String, String> entry : entries) {
    			if (entry.getValue().equals(value)) {
    				key = entry.getKey();
			    }
		    }
		    if (key == null) {  // A key should always be found, otherwise the hash table may not be correctly created
    			throw new NoSuchElementException("Key-value mismatching error");
		    }
		    elem = elem.concat("<" + key + ">" + value + "</" + key + ">");
	    }

    	return header + begin + elem + end;
    }

    public static String convertXML(String tag, String message) {
        if (tag == null) {
            throw new InvalidParameterException("Tag cannot be null");
        }
        if (message == null) {
            throw new InvalidParameterException("Message cannot be null");
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
