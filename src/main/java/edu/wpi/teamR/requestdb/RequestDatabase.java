package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.Connection;
import java.sql.SQLException;
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

    public MealRequest addMealRequest(String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String mealType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        MealRequest output = Dao.addMealRequest(requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType);
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

    public MealRequest getMealRequestByID(int requestID) throws SQLException, ClassNotFoundException {
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
        ArrayList<MealRequest> output = Dao.getMealRequestByStaffMember(staffMember);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsByMealType(String mealType) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestByMealType(mealType);
        connection.close();
        return output;
    }

    public ArrayList<MealRequest> getMealRequestsByRequestStatus(RequestStatus requestStatus) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        MealRequestDAO Dao = new MealRequestDAO(connection);
        ArrayList<MealRequest> output = Dao.getMealRequestByRequestStatus(requestStatus);
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

    public FurnitureRequest getFurnitureRequestByID(int requestID) throws SQLException, ClassNotFoundException {
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

    public ArrayList<ItemRequest> getItemRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO furnitureRequestDAO = new FurnitureRequestDAO(connection);
        MealRequestDAO mealRequestDAO = new MealRequestDAO(connection);
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(furnitureRequestDAO.getFurnitureRequests());
        output.addAll(mealRequestDAO.getMealRequests());
        connection.close();
        return output;
    }

    public ItemRequest getItemRequestByID(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        FurnitureRequestDAO furnitureRequestDAO = new FurnitureRequestDAO(connection);
        MealRequestDAO mealRequestDAO = new MealRequestDAO(connection);
        ItemRequest output;
        try {
            output = furnitureRequestDAO.getFurnitureRequestByID(requestID);
        } catch (Exception e){
            try{
                output = mealRequestDAO.getMealRequestByID(requestID);
            } catch (Exception e2){
                connection.close();
                throw new ItemNotFoundException();
            }
        }
        connection.close();
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByRequesterName(String requesterName) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByRequesterName(requesterName));
        output.addAll(this.getMealRequestsByRequesterName(requesterName));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByLocation(String location) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByLocation(location));
        output.addAll(this.getMealRequestsByLocation(location));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByStaffMember(String staffMember) {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getItemRequestsByStaffMember(staffMember));
        output.addAll(this.getItemRequestsByStaffMember(staffMember));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByItemType(String itemType) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByFurnitureType(itemType));
        output.addAll(this.getMealRequestsByMealType(itemType));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsByRequestStatus(RequestStatus requestStatus) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsByRequestStatus(requestStatus));
        output.addAll(this.getMealRequestsByRequestStatus(requestStatus));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsAfterTime(Timestamp time) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsAfterTime(time));
        output.addAll(this.getMealRequestsAfterTime(time));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsBeforeTime(Timestamp time) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsBeforeTime(time));
        output.addAll(this.getMealRequestsBeforeTime(time));
        return output;
    }

    public ArrayList<ItemRequest> getItemRequestsBetweenTimes(Timestamp firstTime,Timestamp secondTime) throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> output = new ArrayList<>();
        output.addAll(this.getFurnitureRequestsBetweenTimes(firstTime, secondTime));
        output.addAll(this.getMealRequestsBetweenTimes(firstTime, secondTime));
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
