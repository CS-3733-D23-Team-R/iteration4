package edu.wpi.teamR.login;
import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationDAO {
    public User addUser(String staffUsername, String password, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department, int imageID) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getUserTableSchemaNameTableName()+"(staffUsername,password,salt,name,email,jobTitle,phoneNum,joinDate,accessLevel,department,imageID) VALUES (?,?,?,?,?,?,?,?,?,?,?)");

        //Handle salting
        String saltString = generateSaltString();
        String saltedPassword = password + saltString;
        String hashedPassword;
        try {
            hashedPassword = hashPassword(saltedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        preparedStatement.setString(1, staffUsername);
        preparedStatement.setString(2, hashedPassword); //store password hashed with salt
        preparedStatement.setString(3, saltString); //store salt used with password
        preparedStatement.setString(4, name);
        preparedStatement.setString(5, email);
        preparedStatement.setString(6, jobTitle);
        preparedStatement.setString(7, phoneNum);
        preparedStatement.setDate(8, joinDate);
        preparedStatement.setString(9, accessLevel.toString());
        preparedStatement.setString(10, department);
        preparedStatement.setInt(11, imageID);
        preparedStatement.executeUpdate();
        return new User(staffUsername, hashedPassword, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
    }

    public void addUsers(List<User> users) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getUserTableSchemaNameTableName()+"(staffUsername,password,salt,name,email,jobTitle,phoneNum,joinDate,accessLevel,department,imageID) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        for (User u : users) {
            preparedStatement.setString(1, u.getStaffUsername());
            preparedStatement.setString(2, u.getPassword()); //store password hashed with salt
            preparedStatement.setString(3, u.getSalt()); //store salt used with password
            preparedStatement.setString(4, u.getName());
            preparedStatement.setString(5, u.getEmail());
            preparedStatement.setString(6, u.getJobTitle());
            preparedStatement.setString(7, u.getPhoneNum());
            preparedStatement.setDate(8, u.getJoinDate());
            preparedStatement.setString(9, u.getAccessLevel().toString());
            preparedStatement.setString(10, u.getDepartment());
            preparedStatement.setInt(11, u.getImageID());
            preparedStatement.executeUpdate();
        }
    }
    User modifyUserByUsername(String staffUsername, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department, int imageID) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getUserTableSchemaNameTableName()+" SET name=?, email=?, jobTitle=?, phoneNum=?, joinDate=?, accessLevel=?, department=?, imageID=? WHERE staffUsername=? RETURNING *;");

        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, jobTitle);
        preparedStatement.setString(4, phoneNum);
        preparedStatement.setDate(5, joinDate);
        preparedStatement.setString(6, accessLevel.toString());
        preparedStatement.setString(7, department);
        preparedStatement.setInt(8, imageID);
        preparedStatement.setString(9, staffUsername);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        if (!resultSet.next())
            throw new ItemNotFoundException();
        String hashedPassword = resultSet.getString("password");
        String saltString = resultSet.getString("salt");
        return new User(staffUsername, hashedPassword, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
    }

    User modifyUserPasswordByUsername(String staffUsername, String newPassword) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getUserTableSchemaNameTableName()+" SET password=?, salt=? WHERE staffUsername=? RETURNING *;");

        //Handle salting
        String saltString = generateSaltString();
        String saltedPassword = newPassword + saltString;
        String hashedPassword;
        try {
            hashedPassword = hashPassword(saltedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        preparedStatement.setString(1, hashedPassword);
        preparedStatement.setString(2, saltString);
        preparedStatement.setString(3, staffUsername);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        if (!resultSet.next())
            throw new ItemNotFoundException();

        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String jobTitle = resultSet.getString("jobTitle");
        String phoneNum = resultSet.getString("phoneNum");
        Date joinDate = resultSet.getDate("joinDate");
        AccessLevel accessLevel = AccessLevel.valueOf(resultSet.getString("accessLevel"));
        String department = resultSet.getString("department");
        int imageID = resultSet.getInt("imageID");

        return new User(staffUsername, hashedPassword, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
    }

    void deleteAllUsers() throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getUserTableSchemaNameTableName()+";");
    }

    void deleteUserByUsername(String staffUsername) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getUserTableSchemaNameTableName()+" WHERE staffUsername=?;");
        preparedStatement.setString(1, staffUsername);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
    }


    User getUserByUsername(String staffUsername) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getUserTableSchemaNameTableName()+" WHERE staffUsername=?;");
        preparedStatement.setString(1, staffUsername);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next())
            throw new ItemNotFoundException();

        String password = resultSet.getString("password");
        String saltString = resultSet.getString("salt");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String jobTitle = resultSet.getString("jobTitle");
        String phoneNum = resultSet.getString("phoneNum");
        Date joinDate = resultSet.getDate("joinDate");
        AccessLevel accessLevel = AccessLevel.valueOf(resultSet.getString("accessLevel"));
        String department = resultSet.getString("department");
        int imageID = resultSet.getInt("imageID");

        return new User(staffUsername, password, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID);
    }

    ArrayList<User> getUsers() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getUserTableSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<User> users = new ArrayList<>();

        while (resultSet.next()){
            String password = resultSet.getString("password");
            String saltString = resultSet.getString("salt");
            String username = resultSet.getString("staffUsername");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String jobTitle = resultSet.getString("jobTitle");
            String phoneNum = resultSet.getString("phoneNum");
            Date joinDate = resultSet.getDate("joinDate");
            AccessLevel accessLevel = AccessLevel.valueOf(resultSet.getString("accessLevel"));
            String department = resultSet.getString("department");
            int imageID = resultSet.getInt("imageID");

            users.add(new User(username, password, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department, imageID));
        }

        return users;
    }

    boolean verifyUser(String username, String password) throws SQLException, ItemNotFoundException {
        User user = this.getUserByUsername(username);

        //Handle salting
        String saltString = user.getSalt();
        String saltedPassword = password + saltString;
        String hashedPassword;
        try {
            hashedPassword = hashPassword(saltedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        return hashedPassword.equals(user.getPassword());
    }

    private static String generateSaltString() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        return bytesToHex(hashedPassword);
    }
}
