package we.nstu.registration.Lesson;


public class Lesson {
    private String lessonNumber;
    private String lessonName;
    private String classroom;
    private String teacher;
    private String lessonStartTime;
    private String lessonEndTime;

    public Lesson(String lessonNumber, String lessonName, String classroom, String teacher, String lessonStartTime, String lessonEndTime) {
        this.lessonNumber = lessonNumber;
        this.lessonName = lessonName;
        this.classroom = classroom;
        this.teacher = teacher;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLessonStartTime() {
        return lessonStartTime;
    }

    public void setLessonStartTime(String lessonStartTime) {
        this.lessonStartTime = lessonStartTime;
    }

    public String getLessonEndTime() {
        return lessonEndTime;
    }

    public void setLessonEndTime(String lessonEndTime) {
        this.lessonEndTime = lessonEndTime;
    }
}