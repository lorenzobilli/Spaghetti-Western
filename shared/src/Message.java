/**
 * Message class
 */
public class Message {
    private String messageContent;

    public Message() {
        this.messageContent = "";
    }

    public Message(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
