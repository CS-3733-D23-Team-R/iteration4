package edu.wpi.teamR.login;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {
    Alert addAlert(String message, Timestamp time) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAlertSchemaNameTableName()+"(message,time) VALUES(?,?);");
        preparedStatement.setString(1, message);
        preparedStatement.setTimestamp(2, time);
        preparedStatement.executeUpdate();
        return new Alert(message, time);
    }

    void addAlerts(List<Alert> alerts) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAlertSchemaNameTableName()+"(message,time) VALUES(?,?);");
        for (Alert a : alerts) {
            preparedStatement.setString(1, a.getMessage());
            preparedStatement.setTimestamp(2, a.getTime());
            preparedStatement.executeUpdate();
        }
    }
    void deleteAlert(String message, Timestamp time) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAlertSchemaNameTableName()+" WHERE message=? AND time=?;");
        preparedStatement.setString(1, message);
        preparedStatement.setTimestamp(2, time);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
    }
    void deleteAllAlerts() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAlertSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }
    ArrayList<Alert> getAlerts() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getAlertSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Alert> alerts = new ArrayList<>();

        while (resultSet.next()){
            String message = resultSet.getString("message");
            Timestamp time = resultSet.getTimestamp("time");
            alerts.add(new Alert(message, time));
        }
        return alerts;
    }
    ArrayList<Alert> getAlertsInLastNumDaysDesc(int numDays) throws SQLException { //get all alerts between current time and currentTime - numDays days ordered desc by date
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getAlertSchemaNameTableName()+" WHERE time>CURRENT_DATE-? AND time<CURRENT_DATE ORDER BY time desc;");
        preparedStatement.setInt(1, numDays);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Alert> alerts = new ArrayList<>();

        while (resultSet.next()){
            String message = resultSet.getString("message");
            Timestamp time = resultSet.getTimestamp("time");
            alerts.add(new Alert(message, time));
        }
        return alerts;
    }
}
