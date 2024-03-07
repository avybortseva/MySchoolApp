package we.nstu.registration.User;

public class User
{
    private String firstName; //Имя
    private String secondName; //Фамилия
    private String surname; //Отчество
    private String phoneNumber; //Телефон
    private String email; //Почта
    private String password; //Пароля
    private int schoolID; //ID школы
    private int classroomID; //ID класса


    public boolean checkFields(){

        if (
                firstName != null && !firstName.isEmpty() &&
                secondName != null && !secondName.isEmpty() &&
                surname != null && !surname.isEmpty() &&
                phoneNumber != null && !phoneNumber.isEmpty() &&
                email != null && !email.isEmpty() &&
                password != null && !password.isEmpty())
        {
            return true;
        }

        return false;
    }

    public boolean checkPassword()
    {
        boolean upperCase = false;
        boolean lowerCase = false;
        boolean digit = false;

        for (char c : password.toCharArray())
        {
            if (Character.isUpperCase(c))
            {
                upperCase = true;
            }
            else if(Character.isDigit(c))
            {
                digit = true;
            }
            else {
                lowerCase = true;
            }
        }

        if(password.length() > 8 && lowerCase && upperCase && digit)
        {
            return true;
        }

        return false;
    }

    public User() {
    }

    public User(String firstName, String secondName, String surname, String phoneNumber, String email, String password, int schoolID, int classroomID) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.schoolID = schoolID;
        this.classroomID = classroomID;
    }

    public User(String firstName, String secondName, String surname, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
