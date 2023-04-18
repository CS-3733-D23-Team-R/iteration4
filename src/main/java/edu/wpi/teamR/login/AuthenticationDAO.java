package edu.wpi.teamR.login;
import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class AuthenticationDAO {
    private static AuthenticationDAO instance;
    AuthenticationDAO(){

    }
    public static AuthenticationDAO getInstance() {
        if(AuthenticationDAO.instance == null){
            AuthenticationDAO.instance = new AuthenticationDAO();
        }
        return instance;
    }
    public User addUser(String staffUsername, String password, String name, String email, String jobTitle, int phoneNum, Date joinDate, AccessLevel accessLevel, String department) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getUserTableSchemaNameTableName()+"(staffUsername,password,name,email,jobTitle,phoneNum,joinDate,accessLevel,department) VALUES (?,?,?,?,?,?,?,?,?)");
        preparedStatement.setString(1, staffUsername);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5, jobTitle);
        preparedStatement.setInt(6, phoneNum);
        preparedStatement.setDate(7, joinDate);
        preparedStatement.setString(8, accessLevel.toString());
        preparedStatement.setString(9, department);
        preparedStatement.executeUpdate();
        return new User(staffUsername, password, name, email, jobTitle, phoneNum, joinDate, accessLevel, department);
    }
    public User modifyUserByUsername(String staffUsername, String password, String name, String email, String jobTitle, int phoneNum, Date joinDate, AccessLevel accessLevel, String department) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getUserTableSchemaNameTableName()+" SET password=?, name=?, email=?, jobTitle=?, phoneNum=?, joinDate=?, accessLevel=?, department=? WHERE staffUsername=?;");
        preparedStatement.setString(1, password);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, jobTitle);
        preparedStatement.setInt(5, phoneNum);
        preparedStatement.setDate(6, joinDate);
        preparedStatement.setString(7, accessLevel.toString());
        preparedStatement.setString(8, department);
        preparedStatement.setString(9, staffUsername);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
        return new User(staffUsername, password, name, email, jobTitle, phoneNum, joinDate, accessLevel, department);
    }

    public void deleteALLUsers() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getUserTableSchemaNameTableName()+";");
    }

    public void deleteUserByUsername(String staffUsername) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getUserTableSchemaNameTableName()+" WHERE staffUsername=?;");
        preparedStatement.setString(1, staffUsername);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
    }


    public User getUserByUsername(String staffUsername) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getUserTableSchemaNameTableName()+" WHERE staffUsername=?;");
        preparedStatement.setString(1, staffUsername);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next())
            throw new ItemNotFoundException();

        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String jobTitle = resultSet.getString("jobTitle");
        int phoneNum = resultSet.getInt("phoneNum");
        Date joinDate = resultSet.getDate("joinDate");
        AccessLevel accessLevel = AccessLevel.valueOf(resultSet.getString("accessLevel"));
        String department = resultSet.getString("department");

        return new User(staffUsername, password, name, email, jobTitle, phoneNum, joinDate, accessLevel, department);
    }

    public ArrayList<User> getUsers() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getUserTableSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<User> users = new ArrayList<>();

        while (resultSet.next()){
            String password = resultSet.getString("password");
            String username = resultSet.getString("staffUsername");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String jobTitle = resultSet.getString("jobTitle");
            int phoneNum = resultSet.getInt("phoneNum");
            Date joinDate = resultSet.getDate("joinDate");
            AccessLevel accessLevel = AccessLevel.valueOf(resultSet.getString("accessLevel"));
            String department = resultSet.getString("department");

            users.add(new User(username, password, name, email, jobTitle, phoneNum, joinDate, accessLevel, department));
        }

        return users;
    }
}
