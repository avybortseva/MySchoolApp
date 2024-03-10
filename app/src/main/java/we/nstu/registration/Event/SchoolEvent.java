package we.nstu.registration.Event;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SchoolEvent {

    private List<Event> eventList;

    public SchoolEvent(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public String eventToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static SchoolEvent eventFromJson(String json) {
        Gson gson = new Gson();
        Type schoolEventType = new TypeToken<SchoolEvent>(){}.getType();
        return gson.fromJson(json, schoolEventType);
    }



}
