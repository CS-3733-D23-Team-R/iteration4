package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FlowerRequestDAOTest {

    private static FlowerRequestDAO flowerRequestDAO;
    private static Connection connection;

    private String testName = "Michael";
    private String testType = "Raijuus";
    private String testLocation = "Unity hall";
    private String testStaffMember = "Michael Also";
    private String testNotes = "Please";
    private Timestamp testTime = new Timestamp(2020, 10, 7, 12, 30, 30, 100000000);
    private Timestamp testTimeTwo = new Timestamp(2023, 11, 25, 23, 59, 59, 100000000);
    private String testNameTwo = "Wilson";
    private String testTypeTwo = "Students";
    private String testLocationTwo = "Class";
    private String testStaffMemberTwo = "Wong";
    private String testNotesTwo = "Por Favor";
    private Timestamp testTimeOnePointTwo = new Timestamp(2021, 10, 7, 12, 30, 30, 100000000);
    private Timestamp testTimeOnePointFive = new Timestamp(2022, 10, 7, 12, 30, 30, 100000000);
    private Timestamp testTimeOnePointEight = new Timestamp(2023, 10, 7, 12, 30, 30, 100000000);
    private String testNameThree = "Alton";
    private String testTypeThree = "Test Cases";
    private String testLocationThree = "Pushed";
    private String testStaffMemberThree = "Nathaniel";
    private String testNotesThree = "ASAP";
    @BeforeAll
    static void setUpConnection() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration1test");
    }
    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        connection = Configuration.getConnection();
        flowerRequestDAO = new FlowerRequestDAO(connection);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getFlowerRequestSchemaNameTableName()+";");
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void addFlowerRequest() throws SQLException {
        flowerRequestDAO.addFlowerRequest(
                testName,
                testLocation,
                testStaffMember,
                testNotes,
                testTime,
                RequestStatus.Done,
                testType);

        FlowerRequest aFlowerRequest = flowerRequestDAO.getFlowerRequests().get(0);

        assertEquals(testName, aFlowerRequest.getRequesterName());
        assertEquals(testLocation, aFlowerRequest.getLocation());
        assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
        assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
        assertEquals(testTime, aFlowerRequest.getRequestDate());
        assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
        assertEquals(testType, aFlowerRequest.getItemType());
    }

    @Test
    void deleteFlowerRequest() throws SQLException {
        flowerRequestDAO.addFlowerRequest(
                testName,
                testLocation,
                testStaffMember,
                testNotes,
                testTime,
                RequestStatus.Done,
                testType);

        FlowerRequest aFlowerRequest = flowerRequestDAO.getFlowerRequests().get(0);

        int requestID = aFlowerRequest.getRequestID();

        flowerRequestDAO.deleteFlowerRequest(requestID);

        try{
            flowerRequestDAO.getFlowerRequestByID(requestID);

            fail();
        } catch (ItemNotFoundException e){
            //expected
        }
    }

    @Test
    void getFlowerRequests() throws SQLException {
        flowerRequestDAO.addFlowerRequest(
                testName,
                testLocation,
                testStaffMember,
                testNotes,
                testTime,
                RequestStatus.Done,
                testType);

        flowerRequestDAO.addFlowerRequest(
                testNameTwo,
                testLocationTwo,
                testStaffMemberTwo,
                testNotesTwo,
                testTimeTwo,
                RequestStatus.Unstarted,
                testTypeTwo);

        ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequests();
        assertEquals(someFlowerRequests.size(), 2);
        FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
        FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

        assertEquals(testName, aFlowerRequest.getRequesterName());
        assertEquals(testLocation, aFlowerRequest.getLocation());
        assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
        assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
        assertEquals(testTime, aFlowerRequest.getRequestDate());
        assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
        assertEquals(testType, aFlowerRequest.getItemType());

        assertEquals(testNameTwo, bFlowerRequest.getRequesterName());
        assertEquals(testLocationTwo, bFlowerRequest.getLocation());
        assertEquals(testStaffMemberTwo, bFlowerRequest.getStaffMember());
        assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
        assertEquals(testTimeTwo, bFlowerRequest.getRequestDate());
        assertEquals(RequestStatus.Unstarted, bFlowerRequest.getRequestStatus());
        assertEquals(testTypeTwo, bFlowerRequest.getItemType());
    }

    @Test
    void getFlowerRequestByID() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            FlowerRequest testFlowerRequest = flowerRequestDAO.getFlowerRequests().get(0);
            int requestID = testFlowerRequest.getRequestID();
            FlowerRequest aFlowerRequest = flowerRequestDAO.getFlowerRequestByID(requestID);
            FlowerRequest bFlowerRequest = flowerRequestDAO.getFlowerRequestByID(requestID + 1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTime, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
            assertEquals(testType, aFlowerRequest.getItemType());

            assertEquals(testNameTwo, bFlowerRequest.getRequesterName());
            assertEquals(testLocationTwo, bFlowerRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsByRequesterName() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsByRequesterName(testName);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTime, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
            assertEquals(testType, aFlowerRequest.getItemType());

            assertEquals(testName, bFlowerRequest.getRequesterName());
            assertEquals(testLocationTwo, bFlowerRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsByLocation() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocation,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsByLocation(testLocation);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTime, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
            assertEquals(testType, aFlowerRequest.getItemType());

            assertEquals(testNameTwo, bFlowerRequest.getRequesterName());
            assertEquals(testLocation, bFlowerRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsByStaffMember() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMember,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsByStaffMember(testStaffMember);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTime, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
            assertEquals(testType, aFlowerRequest.getItemType());

            assertEquals(testNameTwo, bFlowerRequest.getRequesterName());
            assertEquals(testLocationTwo, bFlowerRequest.getLocation());
            assertEquals(testStaffMember, bFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsByFlowerType() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testType);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsByFlowerType(testType);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTime, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
            assertEquals(testType, aFlowerRequest.getItemType());

            assertEquals(testNameTwo, bFlowerRequest.getRequesterName());
            assertEquals(testLocationTwo, bFlowerRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFlowerRequest.getRequestStatus());
            assertEquals(testType, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsByRequestStatus() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Done,
                    testTypeTwo);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsByRequestStatus(RequestStatus.Done);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTime, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
            assertEquals(testType, aFlowerRequest.getItemType());

            assertEquals(testNameTwo, bFlowerRequest.getRequesterName());
            assertEquals(testLocationTwo, bFlowerRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsAfterTime() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            flowerRequestDAO.addFlowerRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsAfterTime(testTimeOnePointFive);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testNameTwo, aFlowerRequest.getRequesterName());
            assertEquals(testLocationTwo, aFlowerRequest.getLocation());
            assertEquals(testStaffMemberTwo, aFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, aFlowerRequest.getRequestStatus());
            assertEquals(testTypeTwo, aFlowerRequest.getItemType());

            assertEquals(testName, bFlowerRequest.getRequesterName());
            assertEquals(testLocation, bFlowerRequest.getLocation());
            assertEquals(testStaffMember, bFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointEight, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeThree, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsBeforeTime() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            flowerRequestDAO.addFlowerRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsBeforeTime(testTimeOnePointEight);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotes, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTime, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFlowerRequest.getRequestStatus());
            assertEquals(testType, aFlowerRequest.getItemType());

            assertEquals(testNameThree, bFlowerRequest.getRequesterName());
            assertEquals(testLocationThree, bFlowerRequest.getLocation());
            assertEquals(testStaffMemberThree, bFlowerRequest.getStaffMember());
            assertEquals(testNotesThree, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointFive, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeThree, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFlowerRequestsBetweenTimes() {
        try {
            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            flowerRequestDAO.addFlowerRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            flowerRequestDAO.addFlowerRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            flowerRequestDAO.addFlowerRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<FlowerRequest> someFlowerRequests = flowerRequestDAO.getFlowerRequestsBetweenTimes(testTime, testTimeTwo);
            assertEquals(someFlowerRequests.size(), 2);
            FlowerRequest aFlowerRequest = someFlowerRequests.get(0);
            FlowerRequest bFlowerRequest = someFlowerRequests.get(1);

            assertEquals(testName, aFlowerRequest.getRequesterName());
            assertEquals(testLocation, aFlowerRequest.getLocation());
            assertEquals(testStaffMember, aFlowerRequest.getStaffMember());
            assertEquals(testNotesTwo, aFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointEight, aFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, aFlowerRequest.getRequestStatus());
            assertEquals(testTypeThree, aFlowerRequest.getItemType());

            assertEquals(testNameThree, bFlowerRequest.getRequesterName());
            assertEquals(testLocationThree, bFlowerRequest.getLocation());
            assertEquals(testStaffMemberThree, bFlowerRequest.getStaffMember());
            assertEquals(testNotesThree, bFlowerRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointFive, bFlowerRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bFlowerRequest.getRequestStatus());
            assertEquals(testTypeThree, bFlowerRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }
}