package we.nstu.registration.School;

public class Lesson {
    public String lessonName; //Название урока
    public String lessonTeacher; //Учитель
    public int classroomNumber; //Номер кабинета
    public int lessonStartTime; //Начало урока (в минутах с начала дня)
    public int lessonEndTime; //Конец урока (в минутах с начала дня)

    public Lesson(String lessonName, String lessonTeacher, int classroomNumber, int lessonStartTime, int lessonEndTime) {
        this.lessonName = lessonName;
        this.lessonTeacher = lessonTeacher;
        this.classroomNumber = classroomNumber;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonTeacher() {
        return lessonTeacher;
    }

    public void setLessonTeacher(String lessonTeacher) {
        this.lessonTeacher = lessonTeacher;
    }

    public int getClassroomNumber() {
        return classroomNumber;
    }

    public void setClassroomNumber(int classroomNumber) {
        this.classroomNumber = classroomNumber;
    }

    public int getLessonStartTime() {
        return lessonStartTime;
    }

    public void setLessonStartTime(int lessonStartTime) {
        this.lessonStartTime = lessonStartTime;
    }

    public int getLessonEndTime() {
        return lessonEndTime;
    }

    public void setLessonEndTime(int lessonEndTime) {
        this.lessonEndTime = lessonEndTime;
    }
}
