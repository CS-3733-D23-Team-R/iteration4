package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class PatientMoveDAO {
    PatientMove addPatientMove(int patientID, Timestamp time, String longName, String staffUsername) throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("INSERT INTO "+Configuration.getPatientMoveSchemaNameTableName()+"(patientID,time,longName,staffUsername) VALUES(?,?,?,?);");
        preparedStatement.setInt(1, patientID);
        preparedStatement.setTimestamp(2, time);
        preparedStatement.setString(3, longName);
        preparedStatement.setString(4, staffUsername);
        preparedStatement.executeUpdate();
        return new PatientMove(patientID, time, longName, staffUsername);
    }

    //Matches on both patientID and time
    PatientMove modifyPatientMove(int patientID, Timestamp time, String longName, String staffUsername) throws SQLException, ItemNotFoundException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("UPDATE "+Configuration.getPatientMoveSchemaNameTableName()+" SET longName=?, staffUsername=? WHERE patientID=? AND time=?;");
        preparedStatement.setString(1, longName);
        preparedStatement.setString(2, staffUsername);
        preparedStatement.setInt(3, patientID);
        preparedStatement.setTimestamp(4, time);
        int rows = preparedStatement.executeUpdate();
        if (rows==0)
            throw new ItemNotFoundException();
        return new PatientMove(patientID, time, longName, staffUsername);
    }

    //Matches on both patientID and time
    void deletePatientMove(int patientID, Timestamp time) throws SQLException, ItemNotFoundException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getPatientMoveSchemaNameTableName()+" WHERE patientID=? AND time=?;");
        preparedStatement.setInt(1, patientID);
        preparedStatement.setTimestamp(2, time);
        int rows = preparedStatement.executeUpdate();
        if (rows==0)
            throw new ItemNotFoundException();
    }

    void deleteAllPatientMoves() throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getPatientMoveSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    ArrayList<PatientMove> getPatientMoves() throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("SELECT * FROM "+Configuration.getPatientMoveSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<PatientMove> patientMoves = new ArrayList<>();

        while (resultSet.next()){
            int patientID = resultSet.getInt("patientID");
            Timestamp time = resultSet.getTimestamp("time");
            String longName = resultSet.getString("longName");
            String staffUsername = resultSet.getString("staffUsername");
            patientMoves.add(new PatientMove(patientID, time, longName, staffUsername));
        }
        return patientMoves;
    }

    ArrayList<PatientMove> getPatientMovesByPatient(int patientID) throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("SELECT * FROM "+Configuration.getPatientMoveSchemaNameTableName()+" WHERE patientID=?;");
        preparedStatement.setInt(1, patientID);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<PatientMove> patientMoves = new ArrayList<>();

        while (resultSet.next()){
            Timestamp time = resultSet.getTimestamp("time");
            String longName = resultSet.getString("longName");
            String staffUsername = resultSet.getString("staffUsername");
            patientMoves.add(new PatientMove(patientID, time, longName, staffUsername));
        }
        return patientMoves;
    }

    PatientMove getCurrentPatientMove(int patientID) throws SQLException, ItemNotFoundException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("SELECT * FROM (SELECT patientID, max(time) as time FROM "+Configuration.getPatientMoveSchemaNameTableName()+" WHERE patientID=? AND time<(SELECT LOCALTIMESTAMP) GROUP BY patientID) as foo NATURAL JOIN "+Configuration.getPatientMoveSchemaNameTableName()+";");
        preparedStatement.setInt(1, patientID);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next())
            throw new ItemNotFoundException();

        Timestamp time = resultSet.getTimestamp("time");
        String longName = resultSet.getString("longName");
        String staffUsername = resultSet.getString("staffUsername");
        return new PatientMove(patientID, time, longName, staffUsername);
    }
}
