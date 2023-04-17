package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
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

    // Adds a room request and returns the created RoomRequest object
    public RoomRequest addRoomRequest(String longname, String staffUsername, String requestReason, Timestamp startTime, int duration) {
        // TODO: implementation code goes here
        return null;
    }

    // Deletes a room request with the given ID
    public void deleteRoomRequest(int roomRequestID) {
        // TODO: implementation code goes here
    }

    // Overloaded method to delete a room request with the given ID
    public void deleteRoomRequest(String roomRequestID) {
        // TODO: implementation code goes here
    }

    // Returns an ArrayList of all room requests
    public ArrayList<RoomRequest> getRoomRequests() {
        // TODO: implementation code goes here
        return null;
    }

    // Returns a RoomRequest object with the given ID
    public RoomRequest getRoomRequestByID(int roomRequestID) {
        // TODO: implementation code goes here
        return null;
    }

    // Returns an ArrayList of room requests made by the given requester name
    public ArrayList<RoomRequest> getRoomRequestsByRequesterName(String requesterName) {
        // TODO: implementation code goes here
        return null;
    }

    // Returns an ArrayList of room requests made for the given location
    public ArrayList<RoomRequest> getRoomRequestsByLocation(String location) {
        // TODO: implementation code goes here
        return null;
    }

    // Returns an ArrayList of room requests made for the given date
    public ArrayList<RoomRequest> getRoomRequestsForDate(LocalDate date) {
        // TODO: implementation code goes here
        return null;
    }
}
