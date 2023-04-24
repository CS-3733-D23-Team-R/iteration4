package edu.wpi.teamR.login;
import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;

public class AuthenticationDAO {
    public User addUser(String staffUsername, String password, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getUserTableSchemaNameTableName()+"(staffUsername,password,salt,name,email,jobTitle,phoneNum,joinDate,accessLevel,department) VALUES (?,?,?,?,?,?,?,?,?,?)");

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
        preparedStatement.executeUpdate();
        return new User(staffUsername, password, name, saltString, email, jobTitle, phoneNum, joinDate, accessLevel, department);
    }
    User modifyUserByUsername(String staffUsername, String password, String name, String email, String jobTitle, String phoneNum, Date joinDate, AccessLevel accessLevel, String department) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getUserTableSchemaNameTableName()+" SET password=?, salt=?, name=?, email=?, jobTitle=?, phoneNum=?, joinDate=?, accessLevel=?, department=? WHERE staffUsername=?;");

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

        preparedStatement.setString(1, hashedPassword);
        preparedStatement.setString(2, saltString);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, jobTitle);
        preparedStatement.setString(6, phoneNum);
        preparedStatement.setDate(7, joinDate);
        preparedStatement.setString(8, accessLevel.toString());
        preparedStatement.setString(9, department);
        preparedStatement.setString(10, staffUsername);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
        return new User(staffUsername, password, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department);
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

        return new User(staffUsername, password, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department);
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

            users.add(new User(username, password, saltString, name, email, jobTitle, phoneNum, joinDate, accessLevel, department));
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
