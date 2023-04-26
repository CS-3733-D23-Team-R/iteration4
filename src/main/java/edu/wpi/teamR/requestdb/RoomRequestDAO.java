package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RoomRequestDAO {

    public RoomRequestDAO() {}

    RoomRequest addRoomRequest(String longname, String staffUsername, Timestamp startTime, Timestamp endTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement sqlInsert = connection.prepareStatement("INSERT INTO " + Configuration.getRoomRequestSchemaNameTableName()+"(longname,staffUsername,startTime,endTime) VALUES(?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
        sqlInsert.setString(1, longname);
        sqlInsert.setString(2, staffUsername);
        sqlInsert.setTimestamp(3, startTime);
        sqlInsert.setTimestamp(4, endTime);
        sqlInsert.executeUpdate();

        ResultSet rs = sqlInsert.getGeneratedKeys();
        int roomRequestID = 0;
        if (rs.next()) {
            roomRequestID = rs.getInt(1);
        }

        return(new RoomRequest(roomRequestID, longname, staffUsername, startTime, endTime));
    }

    void deleteRoomRequest(int roomRequestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement sqlDelete = connection.prepareStatement("DELETE FROM "+ Configuration.getRoomRequestSchemaNameTableName() + " WHERE roomRequestID=?;");
        sqlDelete.setInt(1, roomRequestID);
        sqlDelete.executeUpdate();
    }

    public void deleteAllRoomRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement sqlDelete = connection.prepareStatement("DELETE FROM "+ Configuration.getRoomRequestSchemaNameTableName() + ";");
        sqlDelete.executeUpdate();
    }


    ArrayList<RoomRequest> getRoomRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+";");
        ArrayList<RoomRequest> rooms = new ArrayList<>();
        while (resultSet.next()){
            int roomRequestID = resultSet.getInt("roomRequestID");
            String longname = resultSet.getString("longname");
            String staffUsername = resultSet.getString("staffUsername");
            Timestamp startTime = resultSet.getTimestamp("startTime");
            Timestamp endTime = resultSet.getTimestamp("endTime");

            rooms.add(new RoomRequest(roomRequestID, longname, staffUsername, startTime, endTime));
        }
        return rooms;
    }
    RoomRequest getRoomRequestByID(int roomRequestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE roomRequestID=?;");
        preparedStatement.setInt(1, roomRequestID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next())
            throw new ItemNotFoundException();
        String longname = resultSet.getString("longname");
        String staffUsername = resultSet.getString("staffUsername");
        Timestamp startTime = resultSet.getTimestamp("startTime");
        Timestamp endTime = resultSet.getTimestamp("endTime");

        return new RoomRequest(roomRequestID, longname, staffUsername, startTime, endTime);
    }

    ArrayList<RoomRequest> getRoomRequestsByStaffUsername(String staffUsername) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE staffUsername=?;");
        preparedStatement.setString(1, staffUsername);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<RoomRequest> rooms = new ArrayList<>();
        while (resultSet.next()){
            int roomRequestID = resultSet.getInt("roomRequestID");
            String longname = resultSet.getString("longname");
            Timestamp startTime = resultSet.getTimestamp("startTime");
            Timestamp endTime = resultSet.getTimestamp("endTime");

            rooms.add(new RoomRequest(roomRequestID, longname, staffUsername, startTime, endTime));
        }
        return rooms;
    }

    ArrayList<RoomRequest> getRoomRequestsByLongname(String longname) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE longname=?;");
        preparedStatement.setString(1, longname);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<RoomRequest> rooms = new ArrayList<>();
        while (resultSet.next()){
            int roomRequestID = resultSet.getInt("roomRequestID");
            String staffUsername = resultSet.getString("staffUsername");
            Timestamp startTime = resultSet.getTimestamp("startTime");
            Timestamp endTime = resultSet.getTimestamp("endTime");

            rooms.add(new RoomRequest(roomRequestID, longname, staffUsername, startTime, endTime));
        }
        return rooms;
    }

    ArrayList<RoomRequest> getRoomRequestsByDate(LocalDate date) throws SQLException, ClassNotFoundException {
        Timestamp startOfDay = Timestamp.valueOf(date.atStartOfDay());
        Timestamp endOfDay = Timestamp.valueOf(date.atTime(23, 59, 59, 999999999));
        return getRoomRequestsBetweenTimes(startOfDay, endOfDay);
    }

    ArrayList<RoomRequest> getRoomRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE NOT((startTime<? AND endTime<?) OR (startTime>? AND endTime>?));");
        preparedStatement.setTimestamp(1, firstTime);
        preparedStatement.setTimestamp(2, firstTime);
        preparedStatement.setTimestamp(3, secondTime);
        preparedStatement.setTimestamp(4, secondTime);

        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<RoomRequest> rooms = new ArrayList<>();
        while (resultSet.next()){
            int roomRequestID = resultSet.getInt("roomRequestID");
            String longname = resultSet.getString("longname");
            String staffUsername = resultSet.getString("staffUsername");
            Timestamp startTime = resultSet.getTimestamp("startTime");
            Timestamp endTime = resultSet.getTimestamp("endTime");

            rooms.add(new RoomRequest(roomRequestID, longname, staffUsername, startTime, endTime));
        }
        return rooms;
    }
}
