package edu.wpi.teamR.login;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {
    Alert addAlert(String message, Date startDate, Date endDate) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAlertSchemaNameTableName()+"(message,time,duration) VALUES(?,?,?);");
        preparedStatement.setString(1, message);
        preparedStatement.setDate(2, startDate);
        preparedStatement.setDate(3, endDate);
        preparedStatement.executeUpdate();
        return new Alert(message, startDate, endDate);
    }

    void addAlerts(List<Alert> alerts) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAlertSchemaNameTableName()+"(message,startDate,endDate) VALUES(?,?,?);");
        for (Alert a : alerts) {
            preparedStatement.setString(1, a.getMessage());
            preparedStatement.setDate(2, a.getStartDate());
            preparedStatement.setDate(3, a.getEndDate());
            preparedStatement.executeUpdate();
        }
    }
    void deleteAlert(String message, Date startDate) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAlertSchemaNameTableName()+" WHERE message=? AND startDate=?;");
        preparedStatement.setString(1, message);
        preparedStatement.setDate(2, startDate);
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
            Date startDate = resultSet.getDate("startDate");
            Date endDate = resultSet.getDate("endDate");
            alerts.add(new Alert(message, startDate, endDate));
        }
        return alerts;
    }

    @Deprecated
    ArrayList<Alert> getAlertsInLastNumDaysDesc(int numDays) throws SQLException { //get all alerts between current time and currentTime - numDays days ordered desc by date
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getAlertSchemaNameTableName()+" WHERE time > cast((CURRENT_DATE - ?) as timestamp) AND time <= CURRENT_TIMESTAMP ORDER BY time desc;");
        //preparedStatement.setString(1, numDays + " DAYS");
        preparedStatement.setInt(1, numDays);
        //System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Alert> alerts = new ArrayList<>();

        while (resultSet.next()){
            String message = resultSet.getString("message");
            Date time = resultSet.getDate("startDate");
            alerts.add(new Alert(message, time, time));
        }
        return alerts;
    }

    List<Alert> getCurrentAlerts() throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet =  statement.executeQuery("SELECT * FROM "+Configuration.getAlertSchemaNameTableName()+" WHERE startDate <= CURRENT_DATE AND endDate >= CURRENT_DATE ORDER BY startDate desc;");
        List<Alert> alerts = new ArrayList<>();

        while (resultSet.next()) {
            String message = resultSet.getString("message");
            Date startDate = resultSet.getDate("startDate");
            Date endDate = resultSet.getDate("endDate");
            alerts.add(new Alert(message, startDate, endDate));
        }
        return alerts;
    }
}
