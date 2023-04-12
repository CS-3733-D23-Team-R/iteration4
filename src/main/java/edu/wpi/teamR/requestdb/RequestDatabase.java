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

    public MealRequest addMealRequest(String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String mealType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        MealRequest output = Dao.addMealRequest(requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType);
        connection.close();
        return output;
    }

    MealRequest modifyMealRequest(int requestID, String newRequesterName, String newLocation, String newStaffMember, String newAdditionalNotes, Timestamp newRequestDate, RequestStatus newRequestStatus, String newMealType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO mealRequestDAO = new MealRequestDAO(connection);
        MealRequest output = mealRequestDAO.modifyMealRequest(requestID, newRequesterName, newLocation, newStaffMember, newAdditionalNotes, newRequestDate, newRequestStatus, newMealType);
        connection.close();
        return output;
    }

    public void deleteMealRequest(int requestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        Dao.deleteMealRequest(requestID);
        connection.close();
    }

    public ArrayList<MealRequest> getMealRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequests();
        connection.close();
        return output;
    }

    public MealRequest getMealRequestByID(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        MealRequest output = Dao.getMealRequestByID(requestID);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsByRequesterName(String requesterName) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsByRequesterName(requesterName);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsByLocation(String location) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsByLocation(location);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsByStaffMember(String staffMember) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsByStaffMember(staffMember);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsByMealType(String mealType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsByMealType(mealType);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsByRequestStatus(RequestStatus requestStatus) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsByRequestStatus(requestStatus);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsAfterTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsAfterTime(time);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsBeforeTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsBeforeTime(time);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestsBetweenTimes(firstTime, secondTime);
        connection.close();
        return output;
    }


    public FurnitureRequest addFurnitureRequest(String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String furnitureType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        FurnitureRequest output = Dao.addFurnitureRequest(requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, furnitureType);
        connection.close();
        return output;
    }

    FurnitureRequest modifyFurnitureRequest(int requestID, String newRequesterName, String newLocation, String newStaffMember, String newAdditionalNotes, Timestamp newRequestDate, RequestStatus newRequestStatus, String newFurnitureType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO furnitureRequestDAO = new FurnitureRequestDAO(connection);
        FurnitureRequest output = furnitureRequestDAO.modifyFurnitureRequest(requestID, newRequesterName, newLocation, newStaffMember, newAdditionalNotes, newRequestDate, newRequestStatus, newFurnitureType);
        connection.close();
        return output;
    }

    public void deleteFurnitureRequest(int requestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        Dao.deleteFurnitureRequest(requestID);
        connection.close();
    }

    public ArrayList<FurnitureRequest> getFurnitureRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequests();
        connection.close();
        return output;
    }

    public FurnitureRequest getFurnitureRequestByID(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        FurnitureRequest output = Dao.getFurnitureRequestByID(requestID);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsByRequesterName(String requesterName) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsByRequesterName(requesterName);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsByLocation(String location) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsByLocation(location);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsByStaffMember(String staffMember) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsByStaffMember(staffMember);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsByFurnitureType(String furnitureType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsByFurnitureType(furnitureType);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsByRequestStatus(RequestStatus requestStatus) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsByRequestStatus(requestStatus);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsAfterTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsAfterTime(time);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsBeforeTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsBeforeTime(time);
        connection.close();
        return output;
    }

    public ArrayList<FurnitureRequest> getFurnitureRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO Dao = new FurnitureRequestDAO(connection);
        ArrayList<FurnitureRequest> output = Dao.getFurnitureRequestsBetweenTimes(firstTime, secondTime);
        connection.close();
        return output;
    }

    public FlowerRequest addFlowerRequest(String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String flowerType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        FlowerRequest output = Dao.addFlowerRequest(requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType);
        connection.close();
        return output;
    }

    FlowerRequest modifyFlowerRequest(int requestID, String newRequesterName, String newLocation, String newStaffMember, String newAdditionalNotes, Timestamp newRequestDate, RequestStatus newRequestStatus, String newFlowerType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO flowerRequestDAO = new FlowerRequestDAO(connection);
        FlowerRequest output = flowerRequestDAO.modifyFlowerRequest(requestID, newRequesterName, newLocation, newStaffMember, newAdditionalNotes, newRequestDate, newRequestStatus, newFlowerType);
        connection.close();
        return output;
    }

    public void deleteFlowerRequest(int requestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        Dao.deleteFlowerRequest(requestID);
        connection.close();
    }

    public ArrayList<FlowerRequest> getFlowerRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequests();
        connection.close();
        return output;
    }

    public FlowerRequest getFlowerRequestByID(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        FlowerRequest output = Dao.getFlowerRequestByID(requestID);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsByRequesterName(String requesterName) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsByRequesterName(requesterName);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsByLocation(String location) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsByLocation(location);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsByStaffMember(String staffMember) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsByStaffMember(staffMember);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsByFlowerType(String flowerType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsByFlowerType(flowerType);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsByRequestStatus(RequestStatus requestStatus) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsByRequestStatus(requestStatus);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsAfterTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsAfterTime(time);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsBeforeTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsBeforeTime(time);
        connection.close();
        return output;
    }

    public ArrayList<FlowerRequest> getFlowerRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FlowerRequestDAO Dao = new FlowerRequestDAO(connection);
        ArrayList<FlowerRequest> output = Dao.getFlowerRequestsBetweenTimes(firstTime, secondTime);
        connection.close();
        return output;
    }

    void deleteItemRequest(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        ItemRequest itemRequest = this.getItemRequestByID(requestID);
        if (itemRequest instanceof MealRequest){
            this.deleteMealRequest(requestID);
        } else if (itemRequest instanceof FurnitureRequest){
            this.deleteFurnitureRequest(requestID);
        } else if (itemRequest instanceof FlowerRequest){
            this.deleteFlowerRequest(requestID);
        } else {
            throw new ItemNotFoundException();
        }
        connection.close();
    }

    ItemRequest modifyItemRequest(int requestID, String newRequesterName, String newLocation, String newStaffMember, String newAdditionalNotes, Timestamp newRequestDate, RequestStatus newRequestStatus, String newItemType) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        ItemRequest itemRequest = this.getItemRequestByID(requestID);
        ItemRequest output;
        if (itemRequest instanceof MealRequest){
            output = this.modifyMealRequest(requestID, newRequesterName, newLocation, newStaffMember, newAdditionalNotes, newRequestDate, newRequestStatus, newItemType);
        } else if (itemRequest instanceof FurnitureRequest){
            output = this.modifyFurnitureRequest(requestID, newRequesterName, newLocation, newStaffMember, newAdditionalNotes, newRequestDate, newRequestStatus, newItemType);
        } else if (itemRequest instanceof FlowerRequest){
            output = this.modifyFlowerRequest(requestID, newRequesterName, newLocation, newStaffMember, newAdditionalNotes, newRequestDate, newRequestStatus, newItemType);
        } else {
            throw new ItemNotFoundException();
        }
        connection.close();
        return output;
    }

    public ArrayList<ItemRequest> getItemRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO furnitureRequestDAO = new FurnitureRequestDAO(connection);
        MealRequestDAO mealRequestDAO = new MealRequestDAO(connection);
        FlowerRequestDAO flowerRequestDAO = new FlowerRequestDAO(connection);
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(furnitureRequestDAO.getFurnitureRequests());
        output.addAll(mealRequestDAO.getMealRequests());
        output.addAll(flowerRequestDAO.getFlowerRequests());
        connection.close();
        return output;
    }

    public ItemRequest getItemRequestByID(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO furnitureRequestDAO = new FurnitureRequestDAO(connection);
        MealRequestDAO mealRequestDAO = new MealRequestDAO(connection);
        FlowerRequestDAO flowerRequestDAO = new FlowerRequestDAO(connection);
        ItemRequest output;
        try {
            output = furnitureRequestDAO.getFurnitureRequestByID(requestID);
        } catch (ItemNotFoundException e) {
            try {
                output = mealRequestDAO.getMealRequestByID(requestID);
            } catch (ItemNotFoundException e2) {
                try {
                    output = flowerRequestDAO.getFlowerRequestByID(requestID);
                } catch (SQLException ex) {
                    connection.close();
                    throw new ItemNotFoundException();
                }
            }
        }
        connection.close();
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByRequesterName(String requesterName) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByRequesterName(requesterName));
        output.addAll(this.getMealRequestsByRequesterName(requesterName));
        output.addAll(this.getFlowerRequestsByRequesterName(requesterName));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByLocation(String location) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByLocation(location));
        output.addAll(this.getMealRequestsByLocation(location));
        output.addAll(this.getFlowerRequestsByLocation(location));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByStaffMember(String staffMember) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByStaffMember(staffMember));
        output.addAll(this.getMealRequestsByStaffMember(staffMember));
        output.addAll(this.getFlowerRequestsByStaffMember(staffMember));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByItemType(String itemType) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByFurnitureType(itemType));
        output.addAll(this.getMealRequestsByMealType(itemType));
        output.addAll(this.getFlowerRequestsByFlowerType(itemType));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByRequestStatus(RequestStatus requestStatus) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByRequestStatus(requestStatus));
        output.addAll(this.getMealRequestsByRequestStatus(requestStatus));
        output.addAll(this.getFlowerRequestsByRequestStatus(requestStatus));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsAfterTime(Timestamp time) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsAfterTime(time));
        output.addAll(this.getMealRequestsAfterTime(time));
        output.addAll(this.getFlowerRequestsAfterTime(time));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsBeforeTime(Timestamp time) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsBeforeTime(time));
        output.addAll(this.getMealRequestsBeforeTime(time));
        output.addAll(this.getFlowerRequestsBeforeTime(time));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsBetweenTimes(firstTime, secondTime));
        output.addAll(this.getMealRequestsBetweenTimes(firstTime, secondTime));
        output.addAll(this.getFlowerRequestsBetweenTimes(firstTime, secondTime));
        return output;
    }

    public RoomRequest addRoomRequest(String longName, Timestamp startTime, Timestamp endTime,String requesterName, String requestReason) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        RoomRequest output = Dao.addRoomRequest(longName, startTime, endTime, requesterName, requestReason);
        connection.close();
        return output;
    }

    public void deleteRoomRequest(int requestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        Dao.deleteRoomRequest(requestID);
        connection.close();
    }

    public ArrayList<RoomRequest> getRoomRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequests();
        connection.close();
        return output;
    }

    public RoomRequest getRoomRequestByID(int requestID) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        RoomRequest output = Dao.getRoomRequestByID(requestID);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByRequesterName(String requesterName) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByRequesterName(requesterName);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByLocation(String location) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByLocation(location);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByStartTime(Timestamp startTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByStartTime(startTime);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByEndTime(Timestamp endTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByEndTime(endTime);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsByRequestReason(String requestReason) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsByRequestReason(requestReason);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsAfterTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsAfterTime(time);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsBeforeTime(Timestamp time) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsBeforeTime(time);
        connection.close();
        return output;
    }

    public ArrayList<RoomRequest> getRoomRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        RoomRequestDAO Dao = new RoomRequestDAO(connection);
        ArrayList<RoomRequest> output = Dao.getRoomRequestsBetweenTimes(firstTime, secondTime);
        connection.close();
        return output;
    }
}
