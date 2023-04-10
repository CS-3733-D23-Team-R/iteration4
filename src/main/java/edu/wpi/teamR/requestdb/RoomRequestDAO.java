package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;

class RoomRequestDAO {
    private Connection connection;

    RoomRequestDAO(Connection connection) {
        this.connection = connection;
    }

    RoomRequest addRoomRequest(String longName, Timestamp startTime, Timestamp endTime, String requesterName, String requestReason) throws SQLException {
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

    void deleteRoomRequest(int requestID){
        //TODO this function
    }

    ArrayList<RoomRequest> getRoomRequests(){return null;}
    RoomRequest getRoomRequestByID(int requestID){return null;}

    ArrayList<RoomRequest> getRoomRequestsByRequesterName(String requesterName){return null;}

    ArrayList<RoomRequest> getRoomRequestsByLocation(String location){return null;}

    ArrayList<RoomRequest> getRoomRequestsByStartTime(Timestamp startTime){return null;}

    ArrayList<RoomRequest> getRoomRequestsByEndTime(Timestamp endTime){return null;}
    ArrayList<RoomRequest> getRoomRequestsByRequestReason(String requestReason){return null;}

    ArrayList<RoomRequest> getRoomRequestsAfterTime(Timestamp time){return null;}

    ArrayList<RoomRequest> getRoomRequestsBeforeTime(Timestamp time){return null;}

    ArrayList<RoomRequest> getRoomRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime){return null;}
}
