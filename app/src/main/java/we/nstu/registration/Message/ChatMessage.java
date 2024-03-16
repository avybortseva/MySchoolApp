package we.nstu.registration.Message;

public class ChatMessage
{
    private String messageSender;
    private String messageRecipient;
    private String messageText;
    private String messageTime;
    private boolean messageChecked;

    public ChatMessage(String messageSender, String messageRecipient, String messageText, String messageTime, boolean messageChecked) {
        this.messageSender = messageSender;
        this.messageRecipient = messageRecipient;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.messageChecked = messageChecked;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageRecipient() {
        return messageRecipient;
    }

    public void setMessageRecipient(String messageRecipient) {
        this.messageRecipient = messageRecipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isMessageChecked() {
        return messageChecked;
    }

    public void setMessageChecked(boolean messageChecked) {
        this.messageChecked = messageChecked;
    }
}
