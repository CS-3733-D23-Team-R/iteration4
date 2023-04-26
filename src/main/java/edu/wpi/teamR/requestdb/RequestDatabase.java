package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class RequestDatabase {
    public ItemRequest addItemRequest(RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException, ClassNotFoundException {
        return new ItemRequestDAO().addItemRequest(requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate);
    }

    public ItemRequest modifyItemRequestByID(int requestID, RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        return new ItemRequestDAO().modifyItemRequestByID(requestID, requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate);
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

    public AvailableItem getAvailableItemByName(String itemName, RequestType requestType) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        return new AvailableItemDAO().getAvailableItemByName(itemName, requestType);
    }

    public RoomRequest addRoomRequest(String longname, String staffUsername, Timestamp startTime, Timestamp endTime) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().addRoomRequest(longname, staffUsername, startTime, endTime);
    }

    public void deleteRoomRequest(int roomRequestID) throws SQLException, ClassNotFoundException {
        new RoomRequestDAO().deleteRoomRequest(roomRequestID);
    }


    public ArrayList<RoomRequest> getRoomRequests() throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().getRoomRequests();
    }
    public RoomRequest getRoomRequestByID(int roomRequestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        return new RoomRequestDAO().getRoomRequestByID(roomRequestID);
    }

    public ArrayList<RoomRequest> getRoomRequestsByStaffUsername(String staffUsername) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().getRoomRequestsByStaffUsername(staffUsername);
    }

    public ArrayList<RoomRequest> getRoomRequestsByLongname(String longname) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().getRoomRequestsByLongname(longname);
    }

    public ArrayList<RoomRequest> getRoomRequestsByDate(LocalDate date) throws SQLException, ClassNotFoundException {
        return new RoomRequestDAO().getRoomRequestsByDate(date);
    }
}
