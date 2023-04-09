package edu.wpi.teamR.login;
import edu.wpi.teamR.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class AuthenticationDAO {
    private static AuthenticationDAO instance = null;
    private AuthenticationDAO(){

    }
    public AuthenticationDAO getInstance() {
        if(AuthenticationDAO.instance == null){
            AuthenticationDAO.instance = new AuthenticationDAO();
        }
        return instance;
    }
    public User addUser(String userID, String password, AccessLevel accessLevel) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO ?(username, password, accesslevel) VALUES (?, ?, ?);");
        statement.setString(1, Configuration.getAuthenticationTableName());
        statement.setString(2, userID);
        statement.setString(3, password);
        statement.setString(4, accessLevel.toString());
        statement.execute();
        connection.close();
        return new User(userID, password, accessLevel);
    }
    public User modifyUserAccessByID(String userID, AccessLevel accessLevel) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE ? SET accesslevel = ? WHERE username = ?;");
        statement.setString(1, Configuration.getAuthenticationTableName());
        statement.setString(2, accessLevel.toString());
        statement.setString(3, userID);
        statement.execute();
        PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM ? WHERE username = ?;");
        statement.setString(1, Configuration.getAuthenticationTableName());
        statement2.setString(2, userID);
        statement.execute();
        ResultSet resultSet = statement2.getResultSet();
        connection.close();
        return new User(resultSet.getString("username"), resultSet.getString("password"), AccessLevel.valueOf(resultSet.getString("accessLevel")));
    }
    //input null to remove entire table
    public void removeUserByID(String userID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        if(userID != null){
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ? WHERE username = ?;");
            statement.setString(1, Configuration.getAuthenticationTableName());
            statement.setString(2, userID);
            statement.execute();
        } else{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ?;");
            statement.setString(1, Configuration.getAuthenticationTableName());
            statement.execute();
        }
        connection.close();
    }
}
