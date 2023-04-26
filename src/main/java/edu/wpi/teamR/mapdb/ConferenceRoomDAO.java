package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConferenceRoomDAO {
    ConferenceRoomDAO(){}
    ConferenceRoom addConferenceRoom(String longname, int capacity, boolean isAccessible, boolean hasOutlets, boolean hasScreen) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getConferenceRoomSchemaNameTableName()+"(longname,capacity,isAccessible,hasOutlets,hasScreen) VALUES (?,?,?,?,?);");
        preparedStatement.setString(1, longname);
        preparedStatement.setInt(2, capacity);
        preparedStatement.setBoolean(3, isAccessible);
        preparedStatement.setBoolean(4, hasOutlets);
        preparedStatement.setBoolean(5, hasScreen);
        preparedStatement.executeUpdate();
        return new ConferenceRoom(longname, capacity, isAccessible, hasOutlets, hasScreen);
    }
    void deleteConferenceRoom(String longname) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getConferenceRoomSchemaNameTableName()+" WHERE longname=?;");
        preparedStatement.setString(1, longname);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
    }
    void deleteAllConferenceRooms() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getConferenceRoomSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    ArrayList<ConferenceRoom> getConferenceRooms() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getConferenceRoomSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ConferenceRoom> conferenceRooms = new ArrayList<>();

        while (resultSet.next()){
            String longname = resultSet.getString("longname");
            int capacity = resultSet.getInt("capacity");
            boolean isAccessible = resultSet.getBoolean("isAccessible");
            boolean hasOutlets = resultSet.getBoolean("hasOutlets");
            boolean hasScreen = resultSet.getBoolean("hasScreen");

            conferenceRooms.add(new ConferenceRoom(longname, capacity, isAccessible, hasOutlets, hasScreen));
        }
        return conferenceRooms;
    }

    ConferenceRoom getConferenceRoomByLongname(String longname) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getConferenceRoomSchemaNameTableName()+" WHERE longname=?;");
        preparedStatement.setString(1, longname);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next())
            throw new ItemNotFoundException();

        int capacity = resultSet.getInt("capacity");
        boolean isAccessible = resultSet.getBoolean("isAccessible");
        boolean hasOutlets = resultSet.getBoolean("hasOutlets");
        boolean hasScreen = resultSet.getBoolean("hasScreen");

        return new ConferenceRoom(longname, capacity, isAccessible, hasOutlets, hasScreen);
    }
}
