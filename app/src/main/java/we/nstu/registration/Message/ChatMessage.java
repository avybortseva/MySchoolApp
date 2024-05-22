package we.nstu.registration.Message;

import java.util.Objects;

public class ChatMessage
{
    private String messageSender; //Отправитель
    private String messageRecipient; //Получатель
    private String messageText; //Сообщение
    private String messageTime; //Время
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return messageChecked == that.messageChecked && Objects.equals(messageSender, that.messageSender) && Objects.equals(messageRecipient, that.messageRecipient) && Objects.equals(messageText, that.messageText) && Objects.equals(messageTime, that.messageTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageSender, messageRecipient, messageText, messageTime, messageChecked);
    }
}
