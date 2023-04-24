package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
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

    AvailableFlowers addAvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) throws SQLException {
        return new AvailableFlowersDAO().addAvailableFlowers(itemName, imageReference, description, itemPrice, isBouqet, hasCard);
    }

    void deleteAvailableFlowers(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableFlowersDAO().deleteAvailableFlowers(itemName);
    }

    void deleteAllAvailableFlowers() throws SQLException {
        new AvailableFlowersDAO().deleteAllAvailableFlowers();
    }

    AvailableFlowers modifyAvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) throws SQLException, ItemNotFoundException {
        return new AvailableFlowersDAO().modifyAvailableFlowers(itemName, imageReference, description, itemPrice, isBouqet, hasCard);
    }

    ArrayList<AvailableFlowers> getAvailableFlowers() throws SQLException {
        return new AvailableFlowersDAO().getAvailableFlowers();
    }

    ArrayList<AvailableFlowers> getAvailableFlowersByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isBouqet, Boolean hasCard, SortOrder sortOrder) throws SQLException {
        return new AvailableFlowersDAO().getAvailableFlowersByAttributes(itemName, imageReference, description, itemPrice, isBouqet, hasCard, sortOrder);
    }

    AvailableFurniture addAvailableFurniture(String itemName, String imageReference, String description, boolean isSeating, boolean isTable, boolean isPillow, boolean isStorage) throws SQLException {
        return new AvailableFurnitureDAO().addAvailableFurniture(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    void deleteAvailableFurniture(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableFurnitureDAO().deleteAvailableFurniture(itemName);
    }

    void deleteAllAvailableFurniture() throws SQLException {
        new AvailableFurnitureDAO().deleteAllAvailableFurniture();
    }

    AvailableFurniture modifyAvailableFurniture(String itemName, String imageReference, String description, boolean isSeating, boolean isTable, boolean isPillow, boolean isStorage) throws SQLException, ItemNotFoundException {
        return new AvailableFurnitureDAO().modifyAvailableFurniture(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    ArrayList<AvailableFurniture> getAvailableFurniture() throws SQLException {
        return new AvailableFurnitureDAO().getAvailableFurniture();
    }

    ArrayList<AvailableFurniture> getAvailableFurnitureByAttributes(String itemName, String imageReference, String description, Boolean isSeating, Boolean isTable, Boolean isPillow, Boolean isStorage) throws SQLException {
        return new AvailableFurnitureDAO().getAvailableFurnitureByAttributes(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    AvailableMeals addAvailableMeals(String itemName, String imageReference, String description, double itemPrice, boolean isVegan, boolean isVegetarian, boolean isDairyFree, boolean isPeanutFree, boolean isGlutenFree) throws SQLException {
        return new AvailableMealsDAO().addAvailableMeals(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree);
    }

    void deleteAvailableMeals(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableMealsDAO().deleteAvailableMeals(itemName);
    }

    void deleteAllAvailableMeals() throws SQLException {
        new AvailableMealsDAO().deleteAllAvailableMeals();
    }

    AvailableMeals modifyAvailableMeals(String itemName, String imageReference, String description, double itemPrice, boolean isVegan, boolean isVegetarian, boolean isDairyFree, boolean isPeanutFree, boolean isGlutenFree) throws SQLException, ItemNotFoundException {
        return new AvailableMealsDAO().modifyAvailableMeals(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree);
    }

    ArrayList<AvailableMeals> getAvailableMeals() throws SQLException {
        return new AvailableMealsDAO().getAvailableMeals();
    }

    ArrayList<AvailableMeals> getAvailableMealsByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isVegan, Boolean isVegetarian, Boolean isDairyFree, Boolean isPeanutFree, Boolean isGlutenFree, SortOrder sortOrder) throws SQLException {
        return new AvailableMealsDAO().getAvailableMealsByAttributes(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree, sortOrder);
    }

    AvailableSupplies addAvailableSupplies(String itemName, String imageReference, String description, double itemPrice, boolean isPaper, boolean isPen, boolean isOrganization, boolean isComputerAccessory) throws SQLException {
        return new AvailableSuppliesDAO().addAvailableSupplies(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory);
    }

    void deleteAvailableSupplies(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableSuppliesDAO().deleteAvailableSupplies(itemName);
    }

    void deleteAllAvailableSupplies() throws SQLException {
        new AvailableSuppliesDAO().deleteAllAvailableSupplies();
    }

    AvailableSupplies modifyAvailableSupplies(String itemName, String imageReference, String description, double itemPrice, boolean isPaper, boolean isPen, boolean isOrganization, boolean isComputerAccessory) throws SQLException, ItemNotFoundException {
        return new AvailableSuppliesDAO().modifyAvailableSupplies(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory);
    }

    ArrayList<AvailableSupplies> getAvailableSupplies() throws SQLException {
        return new AvailableSuppliesDAO().getAvailableSupplies();
    }

    ArrayList<AvailableSupplies> getAvailableSuppliesByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isPaper, Boolean isPen, Boolean isOrganization, Boolean isComputerAccessory, SortOrder sortOrder) throws SQLException {
        return new AvailableSuppliesDAO().getAvailableSuppliesByAttributes(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory, sortOrder);
    }
}
