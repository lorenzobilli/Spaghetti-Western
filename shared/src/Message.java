import java.security.InvalidParameterException;

/**
 * Message class
 */
public class Message {

    private MessageType messageType;
    private Player messageSender;
    private Player messageReceiver;
    private String messageContent;

    public Message(MessageType messageType, Player messageSender, Player messageReceiver, String messageContent) {
        if (messageType == null) {
            throw new InvalidParameterException("MessageType cannot be null");
        }
        if (messageSender == null) {
            throw new InvalidParameterException("MessageSender cannot be null");
        }
        if (messageReceiver == null) {
            throw new InvalidParameterException("MessageReceiver cannot be null");
        }
        if (messageContent == null) {
            throw new InvalidParameterException("MessageContent cannot be null");
        }
        this.messageType = messageType;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        this.messageContent = messageContent;
    }

    public Message(MessageType messageType, Player messageSender, String messageContent) {
        if (messageType == null) {
            throw new InvalidParameterException("MessageType cannot be null");
        }
        if (messageSender == null) {
            throw new InvalidParameterException("MessageReceiver cannot be null");
        }
        if (messageContent == null) {
            throw new InvalidParameterException("MessageContent cannot be null");
        }
        this.messageType = messageType;
        this.messageSender = messageSender;
        this.messageReceiver = new Player("SERVER", Player.Team.SERVER);
        this.messageContent = messageContent;
    }

    public MessageType getMessageType() {
        return this.messageType;
    }

    public Player getMessageSender() {
        return this.messageSender;
    }

    public Player getMessageReceiver() {
        return this.messageReceiver;
    }

    public String getMessageContent() {
        return this.messageContent;
    }
}
