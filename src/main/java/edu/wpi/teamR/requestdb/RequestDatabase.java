package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class RequestDatabase {
    private static RequestDatabase instance;

    private RequestDatabase() {}

    public static RequestDatabase getInstance() {
        if (instance == null)
            instance = new RequestDatabase();
        return instance;
    }

    public ItemRequest addItemRequest(RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException, ClassNotFoundException {
        return new ItemRequestDAO().addItemRequest(requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate);
    }

    public void deleteItemRequest(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        new ItemRequestDAO().deleteItemRequest(requestID);
    }

    public ArrayList<ItemRequest> getItemRequests() throws SQLException, ClassNotFoundException {
        return new ItemRequestDAO().getItemRequests();
    }

    public ArrayList<ItemRequest> getItemRequestByAttributes(SearchList searchList) throws SQLException, ClassNotFoundException {
        return new ItemRequestDAO().getItemRequestByAttributes(searchList);
    }

    public ArrayList<AvailableItem> getAvailableItemsByTypeWithinRangeSortedByPrice(RequestType requestType, Double upperBound, Double lowerBound, SortOrder sortOrder) throws SQLException, ClassNotFoundException {
        return new AvailableItemDAO().getAvailableItemsByTypeWithinRangeSortedByPrice(requestType, upperBound, lowerBound, sortOrder);
    }

    public RoomRequest addRoomRequest(String longName, Timestamp startTime, Timestamp endTime,String requesterName, String requestReason) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        RoomRequest output = Dao.addRoomRequest(longName, startTime, endTime, requesterName, requestReason);
        return output;
    }

    public void deleteRoomRequest(int requestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        Dao.deleteRoomRequest(requestID);
    }

    public ArrayList<RoomRequest> getRoomRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequests();
        return output;
    }

    public RoomRequest getRoomRequestByID(int requestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        RoomRequest output = Dao.getRoomRequestByID(requestID);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByRequesterName(String requesterName) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByRequesterName(requesterName);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByLocation(String location) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByLocation(location);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByStartTime(Timestamp startTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByStartTime(startTime);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByEndTime(Timestamp endTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByEndTime(endTime);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByRequestReason(String requestReason) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByRequestReason(requestReason);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsAfterTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsAfterTime(time);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsBeforeTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsBeforeTime(time);
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsBetweenTimes(firstTime, secondTime);
        return output;
    }
}
