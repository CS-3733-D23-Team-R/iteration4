package edu.wpi.teamR.login;
import edu.wpi.teamR.Configuration;

import java.sql.*;

public class AuthenticationDAO {
    private static AuthenticationDAO instance;
    private String s;
    private AuthenticationDAO(){
        s = "loaded!";
    }
    public static AuthenticationDAO getInstance() {
        if(AuthenticationDAO.instance == null){
            AuthenticationDAO.instance = new AuthenticationDAO();
        }
        return instance;
    }
    public User addUser(String userID, String password, AccessLevel accessLevel) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO "+Configuration.getAuthenticationTableName()+"(username, password, accesslevel) VALUES ('"+userID+"', '"+password+"', '"+accessLevel.toString()+"');");
        connection.close();
        return new User(userID, password, accessLevel);
    }
    public User modifyUserAccessByID(String userID, AccessLevel accessLevel) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        Statement modify = connection.createStatement();
        modify.executeUpdate("UPDATE "+Configuration.getAuthenticationTableName()+" SET accesslevel = '"+accessLevel.toString()+"' WHERE username = '"+userID+"';");
        Statement getUpdated = connection.createStatement();
        ResultSet resultSet = getUpdated.executeQuery("SELECT * FROM "+Configuration.getAuthenticationTableName()+" WHERE username = '"+userID+"';");
        resultSet.next();
        String aUsername = resultSet.getString("username");
        String aPassword = resultSet.getString("password");
        AccessLevel anAccessLevel = AccessLevel.valueOf(resultSet.getString("accesslevel"));
        User temp = new User(aUsername, aPassword, anAccessLevel);
        connection.close();
        return temp;
    }
    public void removeUserByID(String userID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getAuthenticationTableName()+" WHERE username = '"+userID+"';");
        connection.close();
    }
    public void deleteALLUsers() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getAuthenticationTableName()+";");
        connection.close();
    }
}
