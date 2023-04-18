package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.ConferenceRoomDAO;
import edu.wpi.teamR.mapdb.LocationNameDAO;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RoomRequestDAOTest {

    private static RoomRequestDAO roomRequestDAO;
    private static MapDatabase mapDatabase;

    @BeforeAll
    static void startup() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration2test");
        roomRequestDAO = new RoomRequestDAO();
        mapDatabase = new MapDatabase();
    }
    @AfterAll
    static void end() throws SQLException, ClassNotFoundException {
        roomRequestDAO.deleteAllRoomRequests();
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getConferenceRoomSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
        Configuration.getConnection().close();
    }
    @BeforeEach
    void reset() throws SQLException, ClassNotFoundException {
        roomRequestDAO.deleteAllRoomRequests();
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getConferenceRoomSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    @Test
    void addRoomRequest() throws SQLException, ClassNotFoundException {
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
//    leaving for rn because massive pain
//    @Test
//    void getRoomRequestsByDate() {
//
//        Calendar calendar = java.util.Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_YEAR, 100);
//        java.util.Date date = calendar.getTime();
//
//    }
}