import java.security.InvalidParameterException;

/**
 * Message class
 */
public class Message {

    private Type type;
    private Player messageSender;
    private Player messageReceiver;
    private String messageContent;

	public enum Type {
		SESSION,
		TIME,
		CHAT,
		SCENERY,
		MOVE,
		CLASH
	}

    public Message(Type type, Player messageSender, Player messageReceiver, String messageContent) {
        if (type == null) {
            throw new InvalidParameterException("Type cannot be null");
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
        this.type = type;
        this.messageSender = messageSender;
        this.messageReceiver = messageReceiver;
        this.messageContent = messageContent;
    }

    public Message(Type type, Player messageSender, String messageContent) {
        if (type == null) {
            throw new InvalidParameterException("Type cannot be null");
        }
        if (messageSender == null) {
            throw new InvalidParameterException("MessageReceiver cannot be null");
        }
        if (messageContent == null) {
            throw new InvalidParameterException("MessageContent cannot be null");
        }
        this.type = type;
        this.messageSender = messageSender;
        this.messageReceiver = new Player("SERVER", Player.Team.SERVER);
        this.messageContent = messageContent;
    }

    public Type getType() {
        return this.type;
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
