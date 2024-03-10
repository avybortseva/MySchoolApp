package we.nstu.registration.Event;

public class Event {
    private String eventName;
    private String eventDescription;
    private String eventTime;
    private String eventID;



    public Event(String eventName, String eventDescription, String eventTime, String eventID) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventTime = eventTime;
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
