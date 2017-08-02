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

    public static String createXML(List<String> tagList, List<String> valueList) {
        if (tagList == null) {
            throw new InvalidParameterException("tag list cannot be null");
        }
        if (valueList == null) {
            throw new InvalidParameterException("value list cannot be null");
        }
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String begin = "<message>";
        String end = "</message>";
        String elems = "";
        for (String tag : tagList) {
            for (String value : valueList) {
                elems = elems.concat("<" + tag + ">" + value + "</" + tag + ">");
            }
        }
        return header + begin + elems + end;
    }

    public static String convertXML(String tag, String content) {
        if (tag == null) {
            throw new InvalidParameterException("tag cannot be null");
        }
        if (content == null) {
            throw new InvalidParameterException("content cannot be null");
        }
        String result = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource input = new InputSource(new StringReader(content));
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
