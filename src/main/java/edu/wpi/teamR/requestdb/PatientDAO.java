package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import oracle.ucp.common.FailoverStats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    Patient addPatient(String patientName) throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("INSERT INTO "+Configuration.getPatientSchemaNameTableName()+"(patientName) VALUES(?);", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, patientName);
        preparedStatement.executeUpdate();

        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int patientID = 0;
        if (resultSet.next())
            patientID = resultSet.getInt("patientID");
        return new Patient(patientID, patientName);
    }

    void addPatients(List<Patient> patients) throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("INSERT INTO "+Configuration.getPatientSchemaNameTableName()+" VALUES(?,?);");
        for (Patient p : patients) {
            preparedStatement.setInt(1, p.getPatientID());
            preparedStatement.setString(2, p.getPatientName());
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();
    }

    Patient modifyPatient(int patientID, String patientName) throws SQLException, ItemNotFoundException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("UPDATE "+Configuration.getPatientSchemaNameTableName()+" SET patientName=? WHERE patientID=?;");
        preparedStatement.setString(1, patientName);
        preparedStatement.setInt(2, patientID);
        int rows = preparedStatement.executeUpdate();
        if (rows==0)
            throw new ItemNotFoundException();

        return new Patient(patientID, patientName);
    }

    void deletePatient(int patientID) throws SQLException, ItemNotFoundException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getPatientSchemaNameTableName()+" WHERE patientID=?;");
        preparedStatement.setInt(1, patientID);
        int rows = preparedStatement.executeUpdate();

        if (rows==0)
            throw new ItemNotFoundException();
    }

    void deleteAllPatients() throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getPatientSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    ArrayList<Patient> getPatients() throws SQLException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("SELECT * FROM "+Configuration.getPatientSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Patient> patients = new ArrayList<>();

        while (resultSet.next()){
            int patientID = resultSet.getInt("patientID");
            String patientName = resultSet.getString("patientName");
            patients.add(new Patient(patientID, patientName));
        }
        return patients;
    }

    Patient getPatientByID(int patientID) throws SQLException, ItemNotFoundException {
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("SELECT * FROM "+Configuration.getPatientSchemaNameTableName()+" WHERE patientID=?;");
        preparedStatement.setInt(1, patientID);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next())
            throw new ItemNotFoundException();
        String patientName = resultSet.getString("patientName");
        return new Patient(patientID, patientName);
    }
}
