package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;

class RoomRequestDAO {
    private Connection connection;

    RoomRequestDAO(Connection connection) {
        this.connection = connection;
    }

    static RoomRequest addRoomRequest(String longName, Timestamp startTime, Timestamp endTime, String requesterName, String requestReason) throws SQLException {
        Statement statement = connection.createStatement();
        PreparedStatement sqlInsert = connection.prepareStatement("INSERT INTO " + Configuration.schemaName + "." + Configuration.roomRequestTableName+"(requestername,starttime,endtime,location,requesterreason)" +
                "VALUES(?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS);
        sqlInsert.setString(1, requesterName);
        sqlInsert.setTimestamp(2, startTime);
        sqlInsert.setTimestamp(3, endTime);
        sqlInsert.setString(4, longName);
        sqlInsert.setString(5, requestReason);
        sqlInsert.executeUpdate();

        ResultSet rs = sqlInsert.getGeneratedKeys();
        int requestID = 0;
        if (rs.next()) {
            requestID = rs.getInt(1);
        }

        return(new RoomRequest(requestID, longName, startTime, endTime, requesterName, requestReason));
    }

    void deleteRoomRequest(int requestID) throws SQLException {
        PreparedStatement sqlDelete = connection.prepareStatement("DELETE FROM"+ Configuration.getRoomRequestSchemaNameTableName() + "WHERE requestID = " + requestID +";");
    }


    static ArrayList<RoomRequest> getRoomRequests() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+";");
        ArrayList<RoomRequest> rooms = new ArrayList<>();
        while (resultSet.next()){
            Integer requestID = resultSet.getInt("requestID");
            String location = resultSet.getString("location");
            Timestamp startTime = resultSet.getTimestamp("startTime");
            Timestamp endTime = resultSet.getTimestamp("endTime");
            String requesterName = resultSet.getString("requesterName");
            String requesterReason = resultSet.getString("requesterReason");

            rooms.add(new RoomRequest(requestID,location, startTime, endTime, requesterName, requesterReason));
        }
        return rooms;
    }
    RoomRequest getRoomRequestByID(int requestID) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE requestID="+requestID+";");
        resultSet.next();
        String requesterName = resultSet.getString("requestername");
        Timestamp startTime = resultSet.getTimestamp("starttime");
        Timestamp endTime = resultSet.getTimestamp("endtime");
        String location = resultSet.getString("location");
        String requestReason = resultSet.getString("requestreason");

        return new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason);
    }

    ArrayList<RoomRequest> getRoomRequestsByRequesterName(String requesterName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE requestername="+requesterName+";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            Timestamp startTime = resultSet.getTimestamp("starttime");
            Timestamp endTime = resultSet.getTimestamp("endtime");
            String location = resultSet.getString("location");
            String requestReason = resultSet.getString("requestreason");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }

    ArrayList<RoomRequest> getRoomRequestsByLocation(String location) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE location="+location+";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            String requesterName = resultSet.getString("requestername");
            Timestamp startTime = resultSet.getTimestamp("starttime");
            Timestamp endTime = resultSet.getTimestamp("endtime");
            String requestReason = resultSet.getString("requestreason");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }

    ArrayList<RoomRequest> getRoomRequestsByStartTime(Timestamp startTime) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE starttime="+startTime+";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            String requesterName = resultSet.getString("requestername");
            Timestamp endTime = resultSet.getTimestamp("endtime");
            String location = resultSet.getString("location");
            String requestReason = resultSet.getString("requestreason");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }

    ArrayList<RoomRequest> getRoomRequestsByEndTime(Timestamp endTime) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE endtime="+endTime+";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            String requesterName = resultSet.getString("requestername");
            Timestamp startTime = resultSet.getTimestamp("starttime");
            String location = resultSet.getString("location");
            String requestReason = resultSet.getString("requestreason");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }
    ArrayList<RoomRequest> getRoomRequestsByRequestReason(String requestReason) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE requestreason="+requestReason+";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            String requesterName = resultSet.getString("requestername");
            Timestamp startTime = resultSet.getTimestamp("starttime");
            Timestamp endTime = resultSet.getTimestamp("endtime");
            String location = resultSet.getString("location");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }

    ArrayList<RoomRequest> getRoomRequestsAfterTime(Timestamp time) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE starttime>"+time+";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            String requesterName = resultSet.getString("requestername");
            Timestamp startTime = resultSet.getTimestamp("starttime");
            Timestamp endTime = resultSet.getTimestamp("endtime");
            String location = resultSet.getString("location");
            String requestReason = resultSet.getString("requestreason");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }

    ArrayList<RoomRequest> getRoomRequestsBeforeTime(Timestamp time) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE starttime<"+time+";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            String requesterName = resultSet.getString("requestername");
            Timestamp startTime = resultSet.getTimestamp("starttime");
            Timestamp endTime = resultSet.getTimestamp("endtime");
            String location = resultSet.getString("location");
            String requestReason = resultSet.getString("requestreason");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }

    ArrayList<RoomRequest> getRoomRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getRoomRequestSchemaNameTableName()+" WHERE starttime>"+ firstTime +"AND starttime<" + secondTime + ";");
        ArrayList<RoomRequest> roomRequests = new ArrayList<>();
        while(resultSet.next()){
            Integer requestID = resultSet.getInt("requestid");
            String requesterName = resultSet.getString("requestername");
            Timestamp startTime = resultSet.getTimestamp("starttime");
            Timestamp endTime = resultSet.getTimestamp("endtime");
            String location = resultSet.getString("location");
            String requestReason = resultSet.getString("requestreason");
            roomRequests.add(new RoomRequest(requestID, location, startTime, endTime, requesterName, requestReason));
        }

        return roomRequests;
    }

    public void deleteAllRoomRequests() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeQuery("DELETE FROM "+Configuration.getRoomRequestSchemaNameTableName()+";");
    }
}
