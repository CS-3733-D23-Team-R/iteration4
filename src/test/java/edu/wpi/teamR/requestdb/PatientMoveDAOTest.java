package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class PatientMoveDAOTest {

    private static PatientDAO patientDAO;
    private static PatientMoveDAO patientMoveDAO;
    private static MapDatabase mapDatabase;
    private static UserDatabase userDatabase;
    private static Patient patient1, patient2;
    private static LocationName locationName1, locationName2;
    private static User user1, user2;


    @BeforeAll
    static void beforeAll() throws SQLException {
        patientDAO = new PatientDAO();
        patientMoveDAO = new PatientMoveDAO();
        mapDatabase = new MapDatabase();
        userDatabase = new UserDatabase();
        Configuration.changeSchemaToTest();
        Configuration.deleteEverything();
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        Configuration.deleteEverything();
        patient1 = patientDAO.addPatient("Name1");
        patient2 = patientDAO.addPatient("Name2");
        locationName1 = mapDatabase.addLocationName("lName1", "", "");
        locationName2 = mapDatabase.addLocationName("lName2", "", "");
        user1 = userDatabase.addUser("user1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()),AccessLevel.Admin, "", 1);
        user2 = userDatabase.addUser("user2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()),AccessLevel.Admin, "", 1);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        Configuration.deleteEverything();
        Configuration.getConnection().close();
    }

    @Test
    void addPatientMove() throws SQLException {
        ArrayList<PatientMove> patientMoves;
        PatientMove patientMove1, patientMove2;

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(0, patientMoves.size());

        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(1, patientMoves.size());
        patientMove2 = patientMoves.get(0);
        assertEquals(patientMove1.getPatientID(), patientMove2.getPatientID());
        assertEquals(patientMove1.getTime(), patientMove2.getTime());
        assertEquals(patientMove1.getLongName(), patientMove2.getLongName());
        assertEquals(patientMove1.getStaffUsername(), patientMove2.getStaffUsername());

        patientMove1 = patientMoveDAO.addPatientMove(patient2.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName2", "user2");

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(2, patientMoves.size());
    }

    @Test
    void modifyPatientMove() throws SQLException, ItemNotFoundException {
        ArrayList<PatientMove> patientMoves;
        PatientMove patientMove1, patientMove2;

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(0, patientMoves.size());

        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);

        patientMove1 = patientMoveDAO.modifyPatientMove(patientMove1.getPatientID(), patientMove1.getTime(), "lName2", "user1");

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(1, patientMoves.size());
        patientMove2 = patientMoves.get(0);
        assertEquals(patientMove1.getPatientID(), patientMove2.getPatientID());
        assertEquals(patientMove1.getTime(), patientMove2.getTime());
        assertEquals(patientMove1.getLongName(), patientMove2.getLongName());
        assertEquals(patientMove1.getStaffUsername(), patientMove2.getStaffUsername());
    }

    @Test
    void deletePatientMove() throws SQLException, ItemNotFoundException {
        ArrayList<PatientMove> patientMoves;
        PatientMove patientMove1, patientMove2;

        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);
        patientMove2 = patientMoveDAO.addPatientMove(patient2.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName2", "user2");

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(2, patientMoves.size());

        patientMoveDAO.deletePatientMove(patientMove1.getPatientID(), patientMove1.getTime());

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(1, patientMoves.size());

        patientMoveDAO.deletePatientMove(patientMove2.getPatientID(), patientMove2.getTime());

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(0, patientMoves.size());
    }

    @Test
    void deleteAllPatientMoves() throws SQLException {
        ArrayList<PatientMove> patientMoves;
        PatientMove patientMove1, patientMove2;

        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);
        patientMove2 = patientMoveDAO.addPatientMove(patient2.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName2", "user2");

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(2, patientMoves.size());

        patientMoveDAO.deleteAllPatientMoves();

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(0, patientMoves.size());
    }

    @Test
    void getPatientMovesByPatient() throws SQLException {
        ArrayList<PatientMove> patientMoves;
        PatientMove patientMove1, patientMove2;

        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);
        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);
        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);
        patientMove2 = patientMoveDAO.addPatientMove(patient2.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName2", "user2");
        patientMove2 = patientMoveDAO.addPatientMove(patient2.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName2", "user2");

        patientMoves = patientMoveDAO.getPatientMoves();
        assertEquals(5, patientMoves.size());

        patientMoves = patientMoveDAO.getPatientMovesByPatient(patientMove1.getPatientID());
        assertEquals(3, patientMoves.size());

        patientMoves = patientMoveDAO.getPatientMovesByPatient(patientMove2.getPatientID());
        assertEquals(2, patientMoves.size());
    }

    @Test
    void getCurrentPatientMove() throws SQLException, ItemNotFoundException {
        ArrayList<PatientMove> patientMoves;
        PatientMove patientMove1, patientMove2, patientMove3, patientMove4, patientMove5, patientMove6;

        patientMove1 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);
        patientMove2 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName1", null);
        patientMove3 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()), "lName2", "user1");
        patientMove4 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()+100000), "lName1", null);
        patientMove5 = patientMoveDAO.addPatientMove(patient1.getPatientID(), new Timestamp(System.currentTimeMillis()+1000000), "lName1", null);

        patientMove6 = patientMoveDAO.getCurrentPatientMove(patient1.getPatientID());
        assertEquals(patientMove3.getStaffUsername(), patientMove6.getStaffUsername());
        assertEquals(patientMove3.getTime(), patientMove6.getTime());
        assertEquals(patientMove3.getPatientID(), patientMove6.getPatientID());
        assertEquals(patientMove3.getLongName(), patientMove6.getLongName());
    }
}