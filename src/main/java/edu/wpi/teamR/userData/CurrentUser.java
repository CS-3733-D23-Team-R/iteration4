package edu.wpi.teamR.userData;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

@Getter@Setter
public class CurrentUser {
    String username;
    private String password;
    AccessLevel accessLevel;
    String fullName;
    String email;
    String department;
    Date joinDate;
    int phoneNum;
    String jobTitle;
    String profilePictureLocation;
    public CurrentUser(String username, String password, AccessLevel accessLevel, String fullname, String email, String department, Date joinDate, int phoneNum, String jobTitle, int profilePictureID){ //TODO: update this with alton's new user class
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.fullName = fullname;
        this.email = email;
        this.department = department;
        this.joinDate = joinDate;
        this.phoneNum = phoneNum;
        this.jobTitle = jobTitle;
        switch (profilePictureID) {
            case 0 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/0.png")).toExternalForm();
            case 1 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/1.png")).toExternalForm();
            case 2 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/2.png")).toExternalForm();
            case 3 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/3.png")).toExternalForm();
            case 4 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/4.png")).toExternalForm();
            case 5 -> profilePictureLocation = "";
            case 6 -> profilePictureLocation = "";
            case 7 -> profilePictureLocation = "";
            case 8 -> profilePictureLocation = "";
            case 9 -> profilePictureLocation = "";
        }
    }
    public boolean comparePass(String passwordAttempt){
        return this.password.equals(passwordAttempt);
    }
}
