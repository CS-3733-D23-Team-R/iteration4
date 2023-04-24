package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConferenceRoomDAO {
    ConferenceRoomDAO(){}
    ConferenceRoom addConferenceRoom(String longName, int capacity, boolean isAccessible, boolean hasOutlets, boolean hasScreen) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getConferenceRoomSchemaNameTableName()+"(longName,capacity,isAccessible,hasOutlets,hasScreen) VALUES (?,?,?,?,?);");
        preparedStatement.setString(1, longName);
        preparedStatement.setInt(2, capacity);
        preparedStatement.setBoolean(3, isAccessible);
        preparedStatement.setBoolean(4, hasOutlets);
        preparedStatement.setBoolean(5, hasScreen);
        preparedStatement.executeUpdate();
        return new ConferenceRoom(longName, capacity, isAccessible, hasOutlets, hasScreen);
    }

    void addConferenceRooms(List<ConferenceRoom> conferenceRooms) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getConferenceRoomSchemaNameTableName()+"(longName,capacity,isAccessible,hasOutlets,hasScreen) VALUES (?,?,?,?,?);");
        for (ConferenceRoom c : conferenceRooms) {
            preparedStatement.setString(1, c.getLongName());
            preparedStatement.setInt(2, c.getCapacity());
            preparedStatement.setBoolean(3, c.isAccessible());
            preparedStatement.setBoolean(4, c.isHasOutlets());
            preparedStatement.setBoolean(5, c.isHasScreen());
            preparedStatement.executeUpdate();
        }
    }
    void deleteConferenceRoom(String longName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getConferenceRoomSchemaNameTableName()+" WHERE longName=?;");
        preparedStatement.setString(1, longName);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
    }
    void deleteAllConferenceRooms() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getConferenceRoomSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    ArrayList<ConferenceRoom> getConferenceRooms() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getConferenceRoomSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ConferenceRoom> conferenceRooms = new ArrayList<>();

        while (resultSet.next()){
            String longName = resultSet.getString("longName");
            int capacity = resultSet.getInt("capacity");
            boolean isAccessible = resultSet.getBoolean("isAccessible");
            boolean hasOutlets = resultSet.getBoolean("hasOutlets");
            boolean hasScreen = resultSet.getBoolean("hasScreen");

            conferenceRooms.add(new ConferenceRoom(longName, capacity, isAccessible, hasOutlets, hasScreen));
        }
        return conferenceRooms;
    }

    ConferenceRoom getConferenceRoomByLongName(String longName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getConferenceRoomSchemaNameTableName()+" WHERE longName=?;");
        preparedStatement.setString(1, longName);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next())
            throw new ItemNotFoundException();

        int capacity = resultSet.getInt("capacity");
        boolean isAccessible = resultSet.getBoolean("isAccessible");
        boolean hasOutlets = resultSet.getBoolean("hasOutlets");
        boolean hasScreen = resultSet.getBoolean("hasScreen");

        return new ConferenceRoom(longName, capacity, isAccessible, hasOutlets, hasScreen);
    }
}
