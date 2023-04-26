package edu.wpi.teamR.userData;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

@Getter@Setter
public class CurrentUser {
    String username;
    AccessLevel accessLevel;
    String fullName;
    String email;
    String department;
    Date joinDate;
    Long phoneNum;
    String jobTitle;
    String profilePictureLocation;
    public CurrentUser(String username, AccessLevel accessLevel, String fullname, String email, String department, Date joinDate, Long phoneNum, String jobTitle, int profilePictureID){ //TODO: update this with alton's new user class
        this.username = username;
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
            case 5 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/5.png")).toExternalForm();
            case 6 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/6.png")).toExternalForm();
            case 7 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/7.png")).toExternalForm();
            case 8 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/8.png")).toExternalForm();
            case 9 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/9.png")).toExternalForm();
            case 10 ->
                    profilePictureLocation = Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/10.png")).toExternalForm();
        }
    }
    public void refreshUser() throws SQLException, ItemNotFoundException {
        UserData userData = UserData.getInstance();
        userData.logout();
        User aUser = new UserDatabase().getUserByUsername(this.username);
        CurrentUser refreshedUser = new CurrentUser(aUser.getStaffUsername(), aUser.getAccessLevel(), aUser.getName(), aUser.getEmail(), aUser.getDepartment(), aUser.getJoinDate(),Long.parseLong(aUser.getPhoneNum()), aUser.getJobTitle(), aUser.getImageID());
        userData.setLoggedIn(refreshedUser);
    }
}
