package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FurnitureRequestDAOTest {

    private static FurnitureRequestDAO furnitureRequestDAO;
    private static Connection connection;

    private String testName = "Michael";
    private String testType = "TeamR";
    private String testLocation = "Unity hall";
    private String testStaffMember = "Michael Also";
    private String testNotes = "Please";
    private Timestamp testTimeTwo = new Timestamp(2023, 11, 25, 23, 59, 59, 100000000);

    private String testNameTwo = "Wilson";
    private String testTypeTwo = "Students";
    private String testLocationTwo = "Class";
    private String testStaffMemberTwo = "Wong";
    private String testNotesTwo = "Por Favor";
    private Timestamp testTime = new Timestamp(2020, 10, 7, 12, 30, 30, 100000000);

    //these test times are placed in between testTime and testTime2 for testing purposes
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
        furnitureRequestDAO = new FurnitureRequestDAO(connection);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getFurnitureRequestSchemaNameTableName()+";");
    }

    @Test
    void addFurnitureRequest() throws SQLException {
        furnitureRequestDAO.addFurnitureRequest(
            testName,
            testLocation,
            testStaffMember,
            testNotes,
            testTime,
            RequestStatus.Done,
            testType);

        FurnitureRequest aFurnitureRequest = furnitureRequestDAO.getFurnitureRequests().get(0);

        assertEquals(testName, aFurnitureRequest.getRequesterName());
        assertEquals(testLocation, aFurnitureRequest.getLocation());
        assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
        assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
        assertEquals(testTime, aFurnitureRequest.getRequestDate());
        assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
        assertEquals(testType, aFurnitureRequest.getItemType());
    }

    @Test
    void deleteFurnitureRequest() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            FurnitureRequest aFurnitureRequest = furnitureRequestDAO.getFurnitureRequests().get(0);

            int requestID = aFurnitureRequest.getRequestID();

            furnitureRequestDAO.deleteFurnitureRequest(requestID);

            try{
                furnitureRequestDAO.getFurnitureRequestByID(requestID);

                fail();
            } catch (ItemNotFoundException e){
                //expected
            }

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequests() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequests();
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testNameTwo, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationTwo, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestByID() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            FurnitureRequest testFurnitureRequest = furnitureRequestDAO.getFurnitureRequests().get(0);
            int requestID = testFurnitureRequest.getRequestID();
            FurnitureRequest aFurnitureRequest = furnitureRequestDAO.getFurnitureRequestByID(requestID);
            FurnitureRequest bFurnitureRequest = furnitureRequestDAO.getFurnitureRequestByID(requestID + 1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testNameTwo, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationTwo, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsByRequesterName() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsByRequesterName(testName);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testName, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationTwo, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsByLocation() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocation,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsByLocation(testLocation);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testNameTwo, bFurnitureRequest.getRequesterName());
            assertEquals(testLocation, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsByStaffMember() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMember,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsByStaffMember(testStaffMember);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testNameTwo, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationTwo, bFurnitureRequest.getLocation());
            assertEquals(testStaffMember, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsByFurnitureType() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testType);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsByFurnitureType(testType);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testNameTwo, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationTwo, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bFurnitureRequest.getRequestStatus());
            assertEquals(testType, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsByRequestStatus() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Done,
                    testTypeTwo);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsByRequestStatus(RequestStatus.Done);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testNameTwo, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationTwo, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberTwo, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeTwo, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsAfterTime() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsAfterTime(testTimeOnePointFive);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testNameTwo, aFurnitureRequest.getRequesterName());
            assertEquals(testLocationTwo, aFurnitureRequest.getLocation());
            assertEquals(testStaffMemberTwo, aFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, aFurnitureRequest.getRequestStatus());
            assertEquals(testTypeTwo, aFurnitureRequest.getItemType());

            assertEquals(testName, bFurnitureRequest.getRequesterName());
            assertEquals(testLocation, bFurnitureRequest.getLocation());
            assertEquals(testStaffMember, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointEight, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeThree, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsBeforeTime() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsBeforeTime(testTimeOnePointEight);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotes, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTime, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aFurnitureRequest.getRequestStatus());
            assertEquals(testType, aFurnitureRequest.getItemType());

            assertEquals(testNameThree, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationThree, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberThree, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesThree, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointFive, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeThree, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getFurnitureRequestsBetweenTimes() {
        try {
            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            furnitureRequestDAO.addFurnitureRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            furnitureRequestDAO.addFurnitureRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<FurnitureRequest> someFurnitureRequests = furnitureRequestDAO.getFurnitureRequestsBetweenTimes(testTime, testTimeTwo);
            assertEquals(someFurnitureRequests.size(), 2);
            FurnitureRequest aFurnitureRequest = someFurnitureRequests.get(0);
            FurnitureRequest bFurnitureRequest = someFurnitureRequests.get(1);

            assertEquals(testName, aFurnitureRequest.getRequesterName());
            assertEquals(testLocation, aFurnitureRequest.getLocation());
            assertEquals(testStaffMember, aFurnitureRequest.getStaffMember());
            assertEquals(testNotesTwo, aFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointEight, aFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, aFurnitureRequest.getRequestStatus());
            assertEquals(testTypeThree, aFurnitureRequest.getItemType());

            assertEquals(testNameThree, bFurnitureRequest.getRequesterName());
            assertEquals(testLocationThree, bFurnitureRequest.getLocation());
            assertEquals(testStaffMemberThree, bFurnitureRequest.getStaffMember());
            assertEquals(testNotesThree, bFurnitureRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointFive, bFurnitureRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bFurnitureRequest.getRequestStatus());
            assertEquals(testTypeThree, bFurnitureRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }
}