package we.nstu.registration.Message;

import we.nstu.registration.User.User;

public class Message {
    private String title;
    private String text;
    private int imageResource; // Идентификатор ресурса изображения
    private User companion; //Собеседник

    public Message(String title, String text, int imageResource, User companion) {
        this.title = title;
        this.text = text;
        this.imageResource = imageResource;
        this.companion = companion;
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

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public User getCompanion() {
        return companion;
    }

    public void setCompanion(User companion) {
        this.companion = companion;
    }
}