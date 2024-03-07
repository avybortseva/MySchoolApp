package we.nstu.registration.User;

public class Invite {
    private int schoolID;
    private int classroomID;
    private int numOfUses;

    public Invite() {
    }

    public Invite(int schoolID, int classroomID, int numOfUses) {
        this.schoolID = schoolID;
        this.classroomID = classroomID;
        this.numOfUses = numOfUses;
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
}
