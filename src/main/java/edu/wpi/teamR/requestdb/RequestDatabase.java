package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RequestDatabase {
    public ItemRequest addItemRequest(RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException, ClassNotFoundException {
        return new ItemRequestDAO().addItemRequest(requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate);
    }

    public void addItemRequests(List<ItemRequest> itemRequests) throws SQLException {
        new ItemRequestDAO().addItemRequests(itemRequests);
    }

    public ItemRequest modifyItemRequestByID(int requestID, RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        return new ItemRequestDAO().modifyItemRequestByID(requestID, requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate);
    }

    public void deleteItemRequest(int requestID) throws SQLException, ItemNotFoundException {
        new ItemRequestDAO().deleteItemRequest(requestID);
    }

    public void deleteAllItemRequests() throws SQLException {
        new ItemRequestDAO().deleteAllItemRequests();
    }

    public ArrayList<ItemRequest> getItemRequests() throws SQLException {
        return new ItemRequestDAO().getItemRequests();
    }

    public ArrayList<ItemRequest> getItemRequestByAttributes(SearchList searchList) throws SQLException, ClassNotFoundException {
        return new ItemRequestDAO().getItemRequestByAttributes(searchList);
    }

    public ArrayList<AvailableItem> getAvailableItemsByTypeWithinRangeSortedByPrice(RequestType requestType, Double upperBound, Double lowerBound, SortOrder sortOrder) throws SQLException, ClassNotFoundException {
        return new AvailableItemDAO().getAvailableItemsByTypeWithinRangeSortedByPrice(requestType, upperBound, lowerBound, sortOrder);
    }

    public AvailableItem getAvailableItemByName(String itemName, RequestType requestType) throws SQLException, ItemNotFoundException {
        return new AvailableItemDAO().getAvailableItemByName(itemName, requestType);
    }

    public RoomRequest addRoomRequest(String longName, String staffUsername, Timestamp startTime, Timestamp endTime) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().addRoomRequest(longName, staffUsername, startTime, endTime);
    }

    public void addRoomRequests(List<RoomRequest> roomRequests) throws SQLException {
        new RoomRequestDAO().addRoomRequests(roomRequests);
    }
    public void deleteRoomRequest(int roomRequestID) throws SQLException, ClassNotFoundException {
        new RoomRequestDAO().deleteRoomRequest(roomRequestID);
    }

    public void deleteAllRoomRequests() throws SQLException {
        new RoomRequestDAO().deleteAllRoomRequests();
    }

    public ArrayList<RoomRequest> getRoomRequests() throws SQLException {
        return new RoomRequestDAO().getRoomRequests();
    }
    public RoomRequest getRoomRequestByID(int roomRequestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        return new RoomRequestDAO().getRoomRequestByID(roomRequestID);
    }

    public ArrayList<RoomRequest> getRoomRequestsByStaffUsername(String staffUsername) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().getRoomRequestsByStaffUsername(staffUsername);
    }

    public ArrayList<RoomRequest> getRoomRequestsByLongname(String longname) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().getRoomRequestsByLongName(longname);
    }

    public ArrayList<RoomRequest> getRoomRequestsByDate(LocalDate date) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().getRoomRequestsByDate(date);
    }
}
