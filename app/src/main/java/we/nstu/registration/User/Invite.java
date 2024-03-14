package we.nstu.registration.User;

public class Invite {
    private int schoolID;
    private int classroomID;
    private int numOfUses;
    private int accessLevel;

    public Invite() {
    }

    public Invite(int schoolID, int classroomID, int numOfUses, int accessLevel) {
        this.schoolID = schoolID;
        this.classroomID = classroomID;
        this.numOfUses = numOfUses;
        this.accessLevel = accessLevel;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public int getClassroomID() {
        return classroomID;
    }

    public void setClassroomID(int classroomID) {
        this.classroomID = classroomID;
    }

    public int getNumOfUses() {
        return numOfUses;
    }

    public void setNumOfUses(int numOfUses) {
        this.numOfUses = numOfUses;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
