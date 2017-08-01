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

    public static Document convertXML(String contentXML) {
        Document xml = null;
        if (contentXML == null) {
            throw new InvalidParameterException("Content passed cannot be null");
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource input = new InputSource(new StringReader(contentXML));
            xml = builder.parse(input);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
        }
        return xml;
    }

    public static String createXML(String tag, String value) {
        if (tag == null || value == null) {
            throw new InvalidParameterException("Tag/Value cannot be null");
        }
        return "<" + tag + ">" + value + "</" + tag + ">";
    }
}
