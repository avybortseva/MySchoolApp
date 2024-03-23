package we.nstu.registration.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

public class User
{
    private String firstName; //Имя
    private String secondName; //Фамилия
    private String surname; //Отчество
    private String email; //Почта
    private String password; //Пароля
    private int schoolID; //ID школы
    private int classroomID; //ID класса
    private int accessLevel;
    private String dialogs;
    private String notifyToken;


    public boolean checkFields(){

        if (
                firstName != null && !firstName.isEmpty() &&
                secondName != null && !secondName.isEmpty() &&
                surname != null && !surname.isEmpty() &&
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

    public User(String firstName, String secondName, String surname, String email) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.surname = surname;
        this.email = email;
    }

    public User(String firstName, String secondName, String surname, String email, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String accessLevelToText()
    {
        if (accessLevel == 0)
        {
            return "Учащийся";
        }
        else if (accessLevel == 1)
        {
            return "Староста";
        }
        else if (accessLevel == 2)
        {
            return "Учитель";
        }
        else
        {
            return "Администратор";
        }
    }

    public static String encryptPassword(String password)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User(String firstName, String secondName, String surname, String email, String password, int schoolID, int classroomID, int accessLevel, String dialogs) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.schoolID = schoolID;
        this.classroomID = classroomID;
        this.accessLevel = accessLevel;
        this.dialogs = dialogs;
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

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getDialogs() {
        return dialogs;
    }

    public void setDialogs(String dialogs) {
        this.dialogs = dialogs;
    }

    public String getNotifyToken() {
        return notifyToken;
    }

    public void setNotifyToken(String notifyToken) {
        this.notifyToken = notifyToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return schoolID == user.schoolID && classroomID == user.classroomID && accessLevel == user.accessLevel && Objects.equals(firstName, user.firstName) && Objects.equals(secondName, user.secondName) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(dialogs, user.dialogs) && Objects.equals(notifyToken, user.notifyToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, secondName, surname, email, password, schoolID, classroomID, accessLevel, dialogs, notifyToken);
    }
}
