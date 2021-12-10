package sample.models;

import java.io.Serializable;
import java.time.LocalDate;

public class User extends HrManager implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    private String role;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String location;
    private String position;
    private String family;
    private String department;
    private String acceptDay;
    private String fireDay;
    private String gender;
    private String birthDay;
    private String userPhoto;
    private String insuranceNumb;

    public User(String id, String firstName, String lastName, String patronymic, String birthDay) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthDay = birthDay;
    }

    public User() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAcceptDay() {
        return acceptDay;
    }

    public void setAcceptDay(String acceptDay) {
        this.acceptDay = acceptDay;
    }

    public String getFireDay() {
        return fireDay;
    }

    public void setFireDay(String fireDay) {
        this.fireDay = fireDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getInsuranceNumb() {
        return insuranceNumb;
    }

    public void setInsuranceNumb(String insuranceNumb) {
        this.insuranceNumb = insuranceNumb;
    }


}
