package we.nstu.registration.Message;

public class Message {
    private String title;
    private String text;
    private int imageResource; // Идентификатор ресурса изображения

    public Message(String title, String text, int imageResource) {
        this.title = title;
        this.text = text;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getImageResource() {
        return imageResource;
    }
}