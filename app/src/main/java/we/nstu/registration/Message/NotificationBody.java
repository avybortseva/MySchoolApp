package we.nstu.registration.Message;

public class NotificationBody {
    private String title;
    private String body;

    public NotificationBody(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
