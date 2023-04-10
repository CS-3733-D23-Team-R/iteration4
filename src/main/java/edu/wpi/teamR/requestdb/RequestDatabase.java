package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;

public class RequestDatabase {
    private RequestDatabase instance;

    private RequestDatabase() {}

    public RequestDatabase getInstance() {
        if (instance != null)
            instance = new RequestDatabase();
        return instance;
    }

    public MealRequest addMealRequest(String requesterName, String location, String staffMember, String additionalNoted, Timestamp requestDate, RequestStatus requestStatus, String mealType) {return null;}

    public void deleteMealRequest(int requestID) {}

    public ArrayList<MealRequest> getMealRequests(){return null;}

    public MealRequest getMealRequestByID(int requestID){return null;}

    public ArrayList<MealRequest> getMealRequestsByRequesterName(String requesterName) {return null;}

    public ArrayList<MealRequest> getMealRequestsByLocation(String location) {return null;}

    public ArrayList<MealRequest> getMealRequestsByStaffMember(String staffMember) {return null;}

    public ArrayList<MealRequest> getMealRequestsByMealType(String mealType) {return null;}

    public ArrayList<MealRequest> getMealRequestsByRequestStatus(RequestStatus requestStatus) {return null;}

    public ArrayList<MealRequest> getMealRequestsAfterTime(Timestamp time) {return null;}

    public ArrayList<MealRequest> getMealRequestsBeforeTime(Timestamp time) {return null;}

    public ArrayList<MealRequest> getMealRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime) {return null;}


    public FurnitureRequest addFurnitureRequest(String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String furnitureType) {return null;}

    public void deleteFurnitureRequest(int requestID){}

    public ArrayList<FurnitureRequest> getFurnitureRequests() {return null;}

    public FurnitureRequest getFurnitureRequestByID(int requestID) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsByRequesterName(String requesterName) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsByLocation(String location) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsByStaffMember(String staffMember) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsByFurnitureType(String furnitureType) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsByRequestStatus(RequestStatus requestStatus) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsAfterTime(Timestamp time) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsBeforeTime(Timestamp time) {return null;}

    public ArrayList<FurnitureRequest> getFurnitureRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) {return null;}



    public ArrayList<ItemRequest> getItemRequests() {return null;}

    public ItemRequest getItemRequestByID(int requestID) {return null;}

    public ArrayList<ItemRequest> getItemRequestsByRequesterName(String requesterName) {return null;}

    public ArrayList<ItemRequest> getItemRequestsByLocation(String location) {return null;}

    public ArrayList<ItemRequest> getItemRequestsByStaffMember(String staffMember) {return null;}

    public ArrayList<ItemRequest> getItemRequestsByItemType(String itemType) {return null;}

    public ArrayList<ItemRequest> getItemRequestsByRequestStatus(RequestStatus requestStatus) {return null;}

    public ArrayList<ItemRequest> getItemRequestsAfterTime(Timestamp time) {return null;}

    public ArrayList<ItemRequest> getItemRequestsBeforeTime(Timestamp time) {return null;}

    public ArrayList<ItemRequest> getItemRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime) {return null;}



    public RoomRequest addRoomRequest(String longName, Timestamp startTime, Timestamp endTime,String requesterName, String requestReason) {return null;}

    public void deleteRoomRequest(int requestID){}

    public ArrayList<RoomRequest> getRoomRequests() {return null;}

    public RoomRequest getRoomRequestByID(int requestID) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsByRequesterName(String requesterName) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsByLocation(String location) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsByStartTime(Timestamp startTime) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsByEndTime(Timestamp endTime) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsByRequestReason(String requestReason) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsAfterTime(Timestamp time) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsBeforeTime(Timestamp time) {return null;}

    public ArrayList<RoomRequest> getRoomRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) {return null;}
}
