package edu.wpi.teamR.login;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDatabase {
    private final AuthenticationDAO authenticationDAO = new AuthenticationDAO();
    private final AlertDAO alertDAO = new AlertDAO();

    public User addUser(String staffUsername, String password, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department, int imageID) throws SQLException {
        return authenticationDAO.addUser(staffUsername, password, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
    }

    public void addUsers(List<User> users) throws SQLException {
        authenticationDAO.addUsers(users);
    }
    public User modifyUserByUsername(String staffUsername, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department, int imageID) throws SQLException, ItemNotFoundException {
        return authenticationDAO.modifyUserByUsername(staffUsername, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
    }

    public User modifyUserPasswordByUsername(String staffUsername, String newPassword) throws SQLException, ItemNotFoundException {
        return authenticationDAO.modifyUserPasswordByUsername(staffUsername, newPassword);
    }

    public void deleteAllUsers() throws SQLException {
        authenticationDAO.deleteAllUsers();
    }

    public void deleteUserByUsername(String staffUsername) throws SQLException, ItemNotFoundException {
        authenticationDAO.deleteUserByUsername(staffUsername);
    }


    public User getUserByUsername(String staffUsername) throws SQLException, ItemNotFoundException {
        return authenticationDAO.getUserByUsername(staffUsername);
    }

    public ArrayList<User> getUsers() throws SQLException {
        return authenticationDAO.getUsers();
    }

    public boolean verifyUser(String username, String password) throws SQLException, ItemNotFoundException {
        return authenticationDAO.verifyUser(username, password);
    }

    public Alert addAlert(String message, Date startDate, Date endDate) throws SQLException {
        return alertDAO.addAlert(message, startDate, endDate);
    }

    public void addAlerts(List<Alert> alerts) throws SQLException {
        alertDAO.addAlerts(alerts);
    }
    public void deleteAlert(String message, Date startDate) throws SQLException, ItemNotFoundException {
        alertDAO.deleteAlert(message, startDate);
    }
    public void deleteAllAlerts() throws SQLException {
        alertDAO.deleteAllAlerts();
    }
    public ArrayList<Alert> getAlerts() throws SQLException {
        return alertDAO.getAlerts();
    }
    public ArrayList<Alert> getAlertsInLastNumDaysDesc(int numDays) throws SQLException { //get all alerts between current time and currentTime - numDays days ordered desc by date
        return alertDAO.getAlertsInLastNumDaysDesc(numDays);
    }

    public List<Alert> getCurrentAlerts() throws SQLException {
        return alertDAO.getCurrentAlerts();
    }

    public static String getProfilePictureFromID(int profilePictureID) {
        String profilePictureLocation = "";
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
        return profilePictureLocation;
    }

}
