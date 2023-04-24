package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomRequestDAOTest {

    private static RoomRequestDAO roomRequestDAO;
    private static MapDatabase mapDatabase;
    private static AuthenticationDAO authenticationDAO;


    @BeforeAll
    static void startup() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaToTest();
        roomRequestDAO = new RoomRequestDAO();
        mapDatabase = new MapDatabase();
        authenticationDAO = AuthenticationDAO.getInstance();
    }
    @AfterAll
    static void end() throws SQLException, ClassNotFoundException {
        roomRequestDAO.deleteAllRoomRequests();
        mapDatabase.deleteAllConferenceRooms();
        mapDatabase.deleteAllLocationNames();
        authenticationDAO.deleteAllUsers();
    }
    @BeforeEach
    void reset() throws SQLException, ClassNotFoundException {
        roomRequestDAO.deleteAllRoomRequests();
        mapDatabase.deleteAllConferenceRooms();
        mapDatabase.deleteAllLocationNames();
        authenticationDAO.deleteAllUsers();
    }

    @Test
    void addRoomRequest() throws SQLException, ClassNotFoundException {
        authenticationDAO.addUser("staff1", "", "", "", "", "1234567890", new java.sql.Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff2", "", "", "", "", "1234567890", new java.sql.Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");

        Timestamp timestamp1, timestamp2;
        timestamp1 = new Timestamp(System.currentTimeMillis()-1000);
        timestamp2 = new Timestamp(System.currentTimeMillis()+1000);
        RoomRequest roomRequest1, roomRequest2;
        ArrayList<RoomRequest> roomRequests;

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(0, roomRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        mapDatabase.addConferenceRoom("location1", 5, false, false, false);
        roomRequest1 = roomRequestDAO.addRoomRequest("location1", "staff1", timestamp1, timestamp2);

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(1, roomRequests.size());
        roomRequest2 = roomRequests.get(0);

        assertEquals(roomRequest1.getLongname(), roomRequest2.getLongname());
        assertEquals(roomRequest1.getStaffUsername(), roomRequest2.getStaffUsername());
        assertEquals(roomRequest1.getStartTime(), roomRequest2.getStartTime());
        assertEquals(roomRequest1.getEndTime(), roomRequest2.getEndTime());
    }

    @Test
    void deleteRoomRequest() throws SQLException, ClassNotFoundException {
        authenticationDAO.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");

        Timestamp timestamp1, timestamp2;
        timestamp1 = new Timestamp(System.currentTimeMillis()-1000);
        timestamp2 = new Timestamp(System.currentTimeMillis()+1000);
        RoomRequest roomRequest1, roomRequest2;
        ArrayList<RoomRequest> roomRequests;

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(0, roomRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        mapDatabase.addConferenceRoom("location1", 5, false, false, false);
        roomRequest1 = roomRequestDAO.addRoomRequest("location1", "staff1", timestamp1, timestamp2);

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(1, roomRequests.size());

        roomRequestDAO.deleteRoomRequest(roomRequest1.getRoomRequestID());

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(0, roomRequests.size());
    }
//    Tested with others
//    @Test
//    void getRoomRequests() {
//    }

    @Test
    void getRoomRequestByID() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        authenticationDAO.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");

        Timestamp timestamp1, timestamp2;
        timestamp1 = new Timestamp(System.currentTimeMillis()-1000);
        timestamp2 = new Timestamp(System.currentTimeMillis()+1000);
        RoomRequest roomRequest1, roomRequest2, roomRequest3;
        ArrayList<RoomRequest> roomRequests;

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(0, roomRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        mapDatabase.addConferenceRoom("location1", 5, false, false, false);
        roomRequest1 = roomRequestDAO.addRoomRequest("location1", "staff1", timestamp1, timestamp2);
        roomRequest2 = roomRequestDAO.addRoomRequest("location1", "staff2", timestamp1, timestamp2);

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(2, roomRequests.size());

        roomRequest3 = roomRequestDAO.getRoomRequestByID(roomRequest1.getRoomRequestID());
        assertEquals(roomRequest1.getStaffUsername(), roomRequest3.getStaffUsername());

        roomRequest3 = roomRequestDAO.getRoomRequestByID(roomRequest2.getRoomRequestID());
        assertEquals(roomRequest2.getStaffUsername(), roomRequest3.getStaffUsername());
    }

    @Test
    void getRoomRequestsByStaffUsername() throws SQLException, ClassNotFoundException {
        authenticationDAO.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");

        Timestamp timestamp1, timestamp2;
        timestamp1 = new Timestamp(System.currentTimeMillis()-1000);
        timestamp2 = new Timestamp(System.currentTimeMillis()+1000);
        RoomRequest roomRequest1, roomRequest2, roomRequest3;
        ArrayList<RoomRequest> roomRequests;

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(0, roomRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        mapDatabase.addConferenceRoom("location1", 5, false, false, false);
        roomRequest1 = roomRequestDAO.addRoomRequest("location1", "staff1", new Timestamp(System.currentTimeMillis()-1000000), timestamp2);
        roomRequest2 = roomRequestDAO.addRoomRequest("location1", "staff2", timestamp1, timestamp2);
        roomRequest2 = roomRequestDAO.addRoomRequest("location1", "staff2", timestamp1, timestamp2);

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(3, roomRequests.size());

        roomRequests = roomRequestDAO.getRoomRequestsByStaffUsername("staff1");
        assertEquals(1, roomRequests.size());

        assertEquals(roomRequest1.getStartTime(), roomRequests.get(0).getStartTime());

        roomRequests = roomRequestDAO.getRoomRequestsByStaffUsername("staff2");
        assertEquals(2, roomRequests.size());

        roomRequests = roomRequestDAO.getRoomRequestsByStaffUsername("test");
        assertEquals(0, roomRequests.size());
    }

    @Test
    void getRoomRequestsByLongname() throws SQLException, ClassNotFoundException {
        authenticationDAO.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");
        authenticationDAO.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "");

        Timestamp timestamp1, timestamp2;
        timestamp1 = new Timestamp(System.currentTimeMillis()-1000);
        timestamp2 = new Timestamp(System.currentTimeMillis()+1000);
        RoomRequest roomRequest1, roomRequest2, roomRequest3;
        ArrayList<RoomRequest> roomRequests;

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(0, roomRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        mapDatabase.addConferenceRoom("location1", 5, false, false, false);
        mapDatabase.addLocationName("location2", "", "");
        mapDatabase.addConferenceRoom("location2", 5, false, false, false);
        roomRequest1 = roomRequestDAO.addRoomRequest("location1", "staff1", new Timestamp(System.currentTimeMillis()-1000000), timestamp2);
        roomRequest2 = roomRequestDAO.addRoomRequest("location1", "staff2", timestamp1, timestamp2);
        roomRequest2 = roomRequestDAO.addRoomRequest("location1", "staff2", timestamp1, timestamp2);
        roomRequest1 = roomRequestDAO.addRoomRequest("location2", "staff3", timestamp1, timestamp2);

        roomRequests = roomRequestDAO.getRoomRequests();
        assertEquals(4, roomRequests.size());

        roomRequests = roomRequestDAO.getRoomRequestsByLongname("location1");
        assertEquals(3, roomRequests.size());

        roomRequests = roomRequestDAO.getRoomRequestsByLongname("location2");
        assertEquals(1, roomRequests.size());
        assertEquals("staff3", roomRequests.get(0).getStaffUsername());

    }
}