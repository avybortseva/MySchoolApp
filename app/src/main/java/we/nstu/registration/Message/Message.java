package we.nstu.registration.Message;

import we.nstu.registration.User.User;

public class Message {
    private String title;
    private String text;
    private String email;
    private String companionEmail; //Собеседник

    public Message(String title, String text, String email, String companionEmail) {
        this.title = title;
        this.text = text;
        this.email = email;
        this.companionEmail = companionEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanionEmail() {
        return companionEmail;
    }

    public void setCompanionEmail(String companionEmail) {
        this.companionEmail = companionEmail;
    }
}