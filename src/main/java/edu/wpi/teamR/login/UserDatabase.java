package edu.wpi.teamR.login;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class UserDatabase {
    private final AuthenticationDAO authenticationDAO = new AuthenticationDAO();
    private final AlertDAO alertDAO = new AlertDAO();

    public User addUser(String staffUsername, String password, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department, int imageID) throws SQLException {
        return authenticationDAO.addUser(staffUsername, password, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
    }
    public User modifyUserByUsername(String staffUsername, String password, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department, int imageID) throws SQLException, ItemNotFoundException {
        return authenticationDAO.modifyUserByUsername(staffUsername, password, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
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

    Alert addAlert(String message, Timestamp time) throws SQLException {
        return alertDAO.addAlert(message, time);
    }
    void deleteAlert(String message, Timestamp time) throws SQLException, ItemNotFoundException {
        alertDAO.deleteAlert(message, time);
    }
    void deleteAllAlerts() throws SQLException {
        alertDAO.deleteAllAlerts();
    }
    public ArrayList<Alert> getAlerts() throws SQLException {
        return alertDAO.getAlerts();
    }
    ArrayList<Alert> getAlertsInLastNumDaysDesc(int numDays) throws SQLException { //get all alerts between current time and currentTime - numDays days ordered desc by date
        return alertDAO.getAlertsInLastNumDaysDesc(numDays);
    }

}
