package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ConferenceRoomDAOTest {

    private static LocationNameDAO locationNameDAO;
    private static ConferenceRoomDAO conferenceRoomDAO;

    @BeforeAll
    static void startup() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration2test");
        Connection connection = Configuration.getConnection();
        locationNameDAO = new LocationNameDAO(connection);
        conferenceRoomDAO = new ConferenceRoomDAO();
    }
    @AfterAll
    static void end() throws SQLException, ClassNotFoundException {
        conferenceRoomDAO.deleteAllConferenceRooms();
        locationNameDAO.deleteAllLocationNames();
        Configuration.getConnection().close();
    }
    @BeforeEach
    void reset() throws SQLException, ClassNotFoundException {
        conferenceRoomDAO.deleteAllConferenceRooms();
        locationNameDAO.deleteAllLocationNames();
    }

    @Test
    void addConferenceRoom() throws SQLException, ClassNotFoundException {
        ArrayList<ConferenceRoom> conferenceRooms;

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(0, conferenceRooms.size());

        locationNameDAO.addLocationName("long1", "", "");
        conferenceRoomDAO.addConferenceRoom("long1", 5, true, true, false);

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(1, conferenceRooms.size());

        ConferenceRoom conferenceRoom = conferenceRooms.get(0);
        assertEquals("long1", conferenceRoom.getLongname());
        assertEquals(5, conferenceRoom.getCapacity());
        assertEquals(true, conferenceRoom.isAccessible());
        assertEquals(true, conferenceRoom.isHasOutlets());
        assertEquals(false, conferenceRoom.isHasScreen());
    }

    @Test
    void deleteConferenceRoom() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        ArrayList<ConferenceRoom> conferenceRooms;

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(0, conferenceRooms.size());

        locationNameDAO.addLocationName("long1", "", "");
        conferenceRoomDAO.addConferenceRoom("long1", 5, true, true, false);

        locationNameDAO.addLocationName("long2", "", "");
        conferenceRoomDAO.addConferenceRoom("long2", 5, true, true, false);

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(2, conferenceRooms.size());

        conferenceRoomDAO.deleteConferenceRoom("long1");

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(1, conferenceRooms.size());

        ConferenceRoom conferenceRoom = conferenceRooms.get(0);
        assertEquals("long2", conferenceRoom.getLongname());
        assertEquals(5, conferenceRoom.getCapacity());
        assertEquals(true, conferenceRoom.isAccessible());
        assertEquals(true, conferenceRoom.isHasOutlets());
        assertEquals(false, conferenceRoom.isHasScreen());


        conferenceRoomDAO.deleteConferenceRoom("long2");

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(0, conferenceRooms.size());

    }

    @Test
    void deleteAllConferenceRooms() throws SQLException, ClassNotFoundException {

        ArrayList<ConferenceRoom> conferenceRooms;
        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(0, conferenceRooms.size());

        locationNameDAO.addLocationName("long1", "", "");
        conferenceRoomDAO.addConferenceRoom("long1", 5, true, true, false);

        locationNameDAO.addLocationName("long2", "", "");
        conferenceRoomDAO.addConferenceRoom("long2", 5, true, true, false);

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(2, conferenceRooms.size());

        conferenceRoomDAO.deleteAllConferenceRooms();

        conferenceRooms = conferenceRoomDAO.getConferenceRooms();
        assertEquals(0, conferenceRooms.size());

    }


//    @Test HANDLED IN OTHERS
//    void getConferenceRooms() {
//    }
}