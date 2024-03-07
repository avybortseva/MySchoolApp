package we.nstu.registration.Lesson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedule {
    private Map<String, List<Lesson>> schedule;

    public Schedule() {
        this.schedule = new HashMap<>();
    }

    public void addLesson(String date, Lesson lesson) {
        if (!schedule.containsKey(date)) {
            schedule.put(date, new ArrayList<>());
        }
        schedule.get(date).add(lesson);
    }

    public List<Lesson> getLessonsForDate(String localDate) {
        return schedule.get(localDate);
    }

    public String scheduleToJson() {
        Gson gson = new Gson();
        return gson.toJson(this.schedule);
    }

    public static Schedule fromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Lesson>>>(){}.getType();
        Map<String, List<Lesson>> scheduleMap = gson.fromJson(json, type);

        Schedule schedule = new Schedule();
        schedule.schedule = scheduleMap;
        return schedule;
    }

}
