package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.requestdb.RoomRequest;
import edu.wpi.teamR.requestdb.RoomRequestDAO;
import org.junit.jupiter.api.Test;
import edu.wpi.teamR.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomRequestDAOTest {
    private static RoomRequestDAO roomRequestDAO;
    private static Connection connection;
    @BeforeAll
    static void starterFunction() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration1test");
        connection = Configuration.getConnection();
        roomRequestDAO = new RoomRequestDAO(connection);
    }
    @BeforeEach
    void deleteOldData() throws SQLException {
        roomRequestDAO.deleteAllRoomRequests();
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException {
        roomRequestDAO.deleteAllRoomRequests();
        connection.close();
    }
    @Test
    void addRoomRequest() {
    }

    @Test
    void deleteRoomRequest() {
    }

    @Test
    void getRoomRequests() throws SQLException {
        ArrayList<RoomRequest> roomRequests;
        RoomRequest roomRequest;

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(roomRequests.size(), 0);

        RoomRequestDAO.addRoomRequest("RoomRequest1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name1", "Reason1");

        roomRequests = RoomRequestDAO.getRoomRequests();
        assertEquals(roomRequests.size(), 1);
        roomRequest = roomRequests.get(0);
        assertEquals(roomRequest.getLocation(), "RoomRequest1");
        assertEquals(roomRequest.getStartTime(), new Timestamp(System.currentTimeMillis()));
        assertEquals(roomRequest.getEndTime(), new Timestamp(System.currentTimeMillis()));
        assertEquals(roomRequest.getRequesterName(), "Name1");
        assertEquals(roomRequest.getRequestReason(), "Reason1");

        roomRequestDAO.addRoomRequest("RoomRequest2", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name2", "Reason2");

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(roomRequests.size(), 2);

        roomRequestDAO.addRoomRequest("RoomRequest3", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name3", "Reason3");

        roomRequests = RoomRequestDAO.getRoomRequests();
        assertEquals(roomRequests.size(), 3);
    }

    @Test
    void getRoomRequestByID() throws SQLException {
        ArrayList<RoomRequest> roomRequests;
        RoomRequest roomRequest;

        RoomRequestDAO.addRoomRequest("RoomRequest1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name1", "Reason1");
        RoomRequestDAO.addRoomRequest("RoomRequest2", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name2", "Reason2");
        RoomRequestDAO.addRoomRequest("RoomRequest3", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name3", "Reason3");

        roomRequest = roomRequestDAO.getRoomRequestByID(0);
        assertEquals(roomRequest.getRequestID(), 0);

        roomRequest = roomRequestDAO.getRoomRequestByID(2);
        assertEquals(roomRequest.getRequestID(), 2);

        roomRequest = roomRequestDAO.getRoomRequestByID(1);
        assertEquals(roomRequest.getRequestID(), 1);

        roomRequest = roomRequestDAO.getRoomRequestByID(0);
        assertEquals(roomRequest.getRequestID(), 0);
    }

    @Test
    void getRoomRequestsByRequesterName() throws SQLException {
        ArrayList<RoomRequest> roomRequests;
        RoomRequest roomRequest;

        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("test"); //TODO: ERROR CASE FOR MISSED SELECT

        RoomRequestDAO.addRoomRequest("RoomRequest1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name1", "Reason1");
        RoomRequestDAO.addRoomRequest("RoomRequest2", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name2", "Reason2");
        RoomRequestDAO.addRoomRequest("RoomRequest3", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name3", "Reason3");

        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("Name1");
        roomRequest = roomRequests.get(0);
        assertEquals(roomRequest.getRequesterName(), "Name1");


        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("Name2");
        assertEquals(roomRequest.getRequesterName(), "Name2");


        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("Name3");
        assertEquals(roomRequest.getRequesterName(), "Name3");
    }

    @Test
    void getRoomRequestsByLocation() throws SQLException {
        ArrayList<RoomRequest> roomRequests;
        RoomRequest roomRequest;

        roomRequests = roomRequestDAO.getRoomRequestsByLocation("test"); //TODO: ERROR CASE FOR MISSED SELECT

        RoomRequestDAO.addRoomRequest("RoomRequest1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name1", "Reason1");
        RoomRequestDAO.addRoomRequest("RoomRequest2", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name2", "Reason2");
        RoomRequestDAO.addRoomRequest("RoomRequest3", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name3", "Reason3");

        roomRequests = roomRequestDAO.getRoomRequestsByLocation("Location1");
        roomRequest = roomRequests.get(0);
        assertEquals(roomRequest.getLocation(), "Location1");


        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("Location2");
        assertEquals(roomRequest.getRequesterName(), "Location2");


        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("Location3");
        assertEquals(roomRequest.getRequesterName(), "Location3");
    }

    @Test
    void getRoomRequestsByStartTime() throws SQLException {
        ArrayList<RoomRequest> roomRequests;
        RoomRequest roomRequest;

        RoomRequestDAO.addRoomRequest("RoomRequest1", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name1", "Reason1");
        RoomRequestDAO.addRoomRequest("RoomRequest2", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name2", "Reason2");
        RoomRequestDAO.addRoomRequest("RoomRequest3", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Name3", "Reason3");

        roomRequests = roomRequestDAO.getRoomRequestsByStartTime(currentTimeMillis());
        roomRequest = roomRequests.get(0);
        assertEquals(roomRequest.getLocation(), "Location1");


        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("Location2");
        assertEquals(roomRequest.getRequesterName(), "Location2");


        roomRequests = roomRequestDAO.getRoomRequestsByRequesterName("Location3");
        assertEquals(roomRequest.getRequesterName(), "Location3");
    }

    @Test
    void getRoomRequestsByEndTime() {
    }

    @Test
    void getRoomRequestsByRequestReason() {
    }

    @Test
    void getRoomRequestsAfterTime() {
    }

    @Test
    void getRoomRequestsBeforeTime() {
    }

    @Test
    void getRoomRequestsBetweenTimes() {
    }
}