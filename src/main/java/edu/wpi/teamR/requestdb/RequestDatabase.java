package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RequestDatabase {

    public ItemRequest addItemRequest(RequestType requestType, RequestStatus requestStatus, String longName, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate, int quantity) throws SQLException {
        return new ItemRequestDAO().addItemRequest(requestType, requestStatus, longName, staffUsername, itemType, requesterName, additionalNotes, requestDate, quantity);
    }

    public void addItemRequests(List<ItemRequest> itemRequests) throws SQLException {
        new ItemRequestDAO().addItemRequests(itemRequests);
    }

    public void deleteItemRequestsByUser(String staffUsername) throws SQLException {
        new ItemRequestDAO().deleteItemRequestsByUser(staffUsername);
    }

    public ItemRequest modifyItemRequestByID(int requestID, RequestType requestType, RequestStatus requestStatus, String longName, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate, int quantity) throws SQLException, ItemNotFoundException {
        return new ItemRequestDAO().modifyItemRequestByID(requestID, requestType, requestStatus, longName, staffUsername, itemType, requesterName, additionalNotes, requestDate, quantity);
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

    public ArrayList<ItemRequest> getItemRequestByAttributes(SearchList searchList) throws SQLException {
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
    public void deleteRoomRequest(int roomRequestID) throws SQLException {
        new RoomRequestDAO().deleteRoomRequest(roomRequestID);
    }

    public void deleteRoomRequestByUser(String staffUsername) throws SQLException {
        new RoomRequestDAO().deleteRoomRequestByUser(staffUsername);
    }

    public void deleteAllRoomRequests() throws SQLException {
        new RoomRequestDAO().deleteAllRoomRequests();
    }

    public ArrayList<RoomRequest> getRoomRequests() throws SQLException {
        return new RoomRequestDAO().getRoomRequests();
    }
    public RoomRequest getRoomRequestByID(int roomRequestID) throws SQLException, ItemNotFoundException {
        return new RoomRequestDAO().getRoomRequestByID(roomRequestID);
    }

    public ArrayList<RoomRequest> getRoomRequestsByStaffUsername(String staffUsername) throws SQLException {
        return new RoomRequestDAO().getRoomRequestsByStaffUsername(staffUsername);
    }

    public ArrayList<RoomRequest> getRoomRequestsByLongName(String longName) throws SQLException {
        return new RoomRequestDAO().getRoomRequestsByLongName(longName);
    }

    public ArrayList<RoomRequest> getRoomRequestsByDate(LocalDate date) throws SQLException {
        return new RoomRequestDAO().getRoomRequestsByDate(date);
    }

    public AvailableFlowers addAvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) throws SQLException {
        return new AvailableFlowersDAO().addAvailableFlowers(itemName, imageReference, description, itemPrice, isBouqet, hasCard);
    }

    public void addAvailableFlowers(List<AvailableFlowers> availableFlowers) throws SQLException {
        new AvailableFlowersDAO().addAvailableFlowers(availableFlowers);
    }

    public void deleteAvailableFlowers(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableFlowersDAO().deleteAvailableFlowers(itemName);
    }

    public void deleteAllAvailableFlowers() throws SQLException {
        new AvailableFlowersDAO().deleteAllAvailableFlowers();
    }

    public AvailableFlowers modifyAvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) throws SQLException, ItemNotFoundException {
        return new AvailableFlowersDAO().modifyAvailableFlowers(itemName, imageReference, description, itemPrice, isBouqet, hasCard);
    }

    public ArrayList<AvailableFlowers> getAvailableFlowers() throws SQLException {
        return new AvailableFlowersDAO().getAvailableFlowers();
    }

    public ArrayList<AvailableFlowers> getAvailableFlowersByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isBouqet, Boolean hasCard, SortOrder sortOrder) throws SQLException {
        return new AvailableFlowersDAO().getAvailableFlowersByAttributes(itemName, imageReference, description, itemPrice, isBouqet, hasCard, sortOrder);
    }

    public AvailableFurniture addAvailableFurniture(String itemName, String imageReference, String description, boolean isSeating, boolean isTable, boolean isPillow, boolean isStorage) throws SQLException {
        return new AvailableFurnitureDAO().addAvailableFurniture(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    public void addAvailableFurniture(List<AvailableFurniture> availableFurniture) throws SQLException {
        new AvailableFurnitureDAO().addAvailableFurniture(availableFurniture);
    }

    public void deleteAvailableFurniture(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableFurnitureDAO().deleteAvailableFurniture(itemName);
    }

    public void deleteAllAvailableFurniture() throws SQLException {
        new AvailableFurnitureDAO().deleteAllAvailableFurniture();
    }

    public AvailableFurniture modifyAvailableFurniture(String itemName, String imageReference, String description, boolean isSeating, boolean isTable, boolean isPillow, boolean isStorage) throws SQLException, ItemNotFoundException {
        return new AvailableFurnitureDAO().modifyAvailableFurniture(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    public ArrayList<AvailableFurniture> getAvailableFurniture() throws SQLException {
        return new AvailableFurnitureDAO().getAvailableFurniture();
    }

    public ArrayList<AvailableFurniture> getAvailableFurnitureByAttributes(String itemName, String imageReference, String description, Boolean isSeating, Boolean isTable, Boolean isPillow, Boolean isStorage) throws SQLException {
        return new AvailableFurnitureDAO().getAvailableFurnitureByAttributes(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    public AvailableMeals addAvailableMeal(String itemName, String imageReference, String description, double itemPrice, boolean isVegan, boolean isVegetarian, boolean isDairyFree, boolean isPeanutFree, boolean isGlutenFree) throws SQLException {
        return new AvailableMealsDAO().addAvailableMeals(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree);
    }

    public void addAvailableMeals(List<AvailableMeals> availableMeals) throws SQLException {
        new AvailableMealsDAO().addAvailableMeals(availableMeals);
    }

    public void deleteAvailableMeal(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableMealsDAO().deleteAvailableMeals(itemName);
    }

    public void deleteAllAvailableMeals() throws SQLException {
        new AvailableMealsDAO().deleteAllAvailableMeals();
    }

    public AvailableMeals modifyAvailableMeal(String itemName, String imageReference, String description, double itemPrice, boolean isVegan, boolean isVegetarian, boolean isDairyFree, boolean isPeanutFree, boolean isGlutenFree) throws SQLException, ItemNotFoundException {
        return new AvailableMealsDAO().modifyAvailableMeals(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree);
    }

    public ArrayList<AvailableMeals> getAvailableMeals() throws SQLException {
        return new AvailableMealsDAO().getAvailableMeals();
    }

    public ArrayList<AvailableMeals> getAvailableMealsByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isVegan, Boolean isVegetarian, Boolean isDairyFree, Boolean isPeanutFree, Boolean isGlutenFree, SortOrder sortOrder) throws SQLException {
        return new AvailableMealsDAO().getAvailableMealsByAttributes(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree, sortOrder);
    }

    public AvailableSupplies addAvailableSupplies(String itemName, String imageReference, String description, double itemPrice, boolean isPaper, boolean isPen, boolean isOrganization, boolean isComputerAccessory) throws SQLException {
        return new AvailableSuppliesDAO().addAvailableSupplies(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory);
    }

    public void addAvailableSupplies(List<AvailableSupplies> availableSupplies) throws SQLException {
        new AvailableSuppliesDAO().addAvailableSupplies(availableSupplies);
    }

    public void deleteAvailableSupplies(String itemName) throws SQLException, ItemNotFoundException {
        new AvailableSuppliesDAO().deleteAvailableSupplies(itemName);
    }

    public void deleteAllAvailableSupplies() throws SQLException {
        new AvailableSuppliesDAO().deleteAllAvailableSupplies();
    }

    public AvailableSupplies modifyAvailableSupplies(String itemName, String imageReference, String description, double itemPrice, boolean isPaper, boolean isPen, boolean isOrganization, boolean isComputerAccessory) throws SQLException, ItemNotFoundException {
        return new AvailableSuppliesDAO().modifyAvailableSupplies(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory);
    }

    public ArrayList<AvailableSupplies> getAvailableSupplies() throws SQLException {
        return new AvailableSuppliesDAO().getAvailableSupplies();
    }

    public ArrayList<AvailableSupplies> getAvailableSuppliesByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isPaper, Boolean isPen, Boolean isOrganization, Boolean isComputerAccessory, SortOrder sortOrder) throws SQLException {
        return new AvailableSuppliesDAO().getAvailableSuppliesByAttributes(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory, sortOrder);
    }

    public Patient addPatient(String patientName) throws SQLException {
        return new PatientDAO().addPatient(patientName);
    }

    public void addPatients(List<Patient> patients) throws SQLException {
        new PatientDAO().addPatients(patients);
    }

    public Patient modifyPatient(int patientID, String patientName) throws SQLException, ItemNotFoundException {
        return new PatientDAO().modifyPatient(patientID, patientName);
    }

    public void deletePatient(int patientID) throws SQLException, ItemNotFoundException {
        new PatientDAO().deletePatient(patientID);
    }

    public void deleteAllPatients() throws SQLException {
        new PatientDAO().deleteAllPatients();
    }

    public ArrayList<Patient> getPatients() throws SQLException {
        return new PatientDAO().getPatients();
    }

    public Patient getPatientByID(int patientID) throws SQLException, ItemNotFoundException {
        return new PatientDAO().getPatientByID(patientID);
    }

    public PatientMove addPatientMove(int patientID, Timestamp time, String longName, String staffUsername) throws SQLException {
        return new PatientMoveDAO().addPatientMove(patientID, time, longName, staffUsername);
    }

    public void addPatientMoves(List<PatientMove> patientMoves) throws SQLException {
        new PatientMoveDAO().addPatientMoves(patientMoves);
    }

    //Matches on both patientID and time
    public PatientMove modifyPatientMove(int patientID, Timestamp time, String longName, String staffUsername) throws SQLException, ItemNotFoundException {
        return new PatientMoveDAO().modifyPatientMove(patientID, time, longName, staffUsername);
    }

    //Matches on both patientID and time
    public void deletePatientMove(int patientID, Timestamp time) throws SQLException, ItemNotFoundException {
        new PatientMoveDAO().deletePatientMove(patientID, time);
    }

    public void deleteAllPatientMoves() throws SQLException {
        new PatientMoveDAO().deleteAllPatientMoves();
    }

    public ArrayList<PatientMove> getPatientMoves() throws SQLException {
        return new PatientMoveDAO().getPatientMoves();
    }

    public ArrayList<PatientMove> getPatientMovesByPatient(int patientID) throws SQLException {
        return new PatientMoveDAO().getPatientMovesByPatient(patientID);
    }

    public PatientMove getCurrentPatientMove(int patientID) throws SQLException, ItemNotFoundException {
        return new PatientMoveDAO().getCurrentPatientMove(patientID);
    }
}
