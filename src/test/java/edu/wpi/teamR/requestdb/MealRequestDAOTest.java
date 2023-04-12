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

class MealRequestDAOTest {

    private static MealRequestDAO mealRequestDAO;
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
        mealRequestDAO = new MealRequestDAO(connection);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getMealRequestSchemaNameTableName()+";");
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void addMealRequest() throws SQLException {
        mealRequestDAO.addMealRequest(
                testName,
                testLocation,
                testStaffMember,
                testNotes,
                testTime,
                RequestStatus.Done,
                testType);

        MealRequest aMealRequest = mealRequestDAO.getMealRequests().get(0);

        assertEquals(testName, aMealRequest.getRequesterName());
        assertEquals(testLocation, aMealRequest.getLocation());
        assertEquals(testStaffMember, aMealRequest.getStaffMember());
        assertEquals(testNotes, aMealRequest.getAdditionalNotes());
        assertEquals(testTime, aMealRequest.getRequestDate());
        assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
        assertEquals(testType, aMealRequest.getItemType());
    }

    @Test
    void deleteMealRequest() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            MealRequest aMealRequest = mealRequestDAO.getMealRequests().get(0);

            int requestID = aMealRequest.getRequestID();

            mealRequestDAO.deleteMealRequest(requestID);

            try{
                mealRequestDAO.getMealRequestByID(requestID);

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
    void getMealRequests() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequests();
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testNameTwo, bMealRequest.getRequesterName());
            assertEquals(testLocationTwo, bMealRequest.getLocation());
            assertEquals(testStaffMemberTwo, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bMealRequest.getRequestStatus());
            assertEquals(testTypeTwo, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestByID() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            MealRequest testMealRequest = mealRequestDAO.getMealRequests().get(0);
            int requestID = testMealRequest.getRequestID();
            MealRequest aMealRequest = mealRequestDAO.getMealRequestByID(requestID);
            MealRequest bMealRequest = mealRequestDAO.getMealRequestByID(requestID + 1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testNameTwo, bMealRequest.getRequesterName());
            assertEquals(testLocationTwo, bMealRequest.getLocation());
            assertEquals(testStaffMemberTwo, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bMealRequest.getRequestStatus());
            assertEquals(testTypeTwo, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsByRequesterName() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            mealRequestDAO.addMealRequest(
                    testName,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsByRequesterName(testName);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testName, bMealRequest.getRequesterName());
            assertEquals(testLocationTwo, bMealRequest.getLocation());
            assertEquals(testStaffMemberTwo, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bMealRequest.getRequestStatus());
            assertEquals(testTypeTwo, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsByLocation() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocation,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsByLocation(testLocation);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testNameTwo, bMealRequest.getRequesterName());
            assertEquals(testLocation, bMealRequest.getLocation());
            assertEquals(testStaffMemberTwo, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bMealRequest.getRequestStatus());
            assertEquals(testTypeTwo, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsByStaffMember() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMember,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsByStaffMember(testStaffMember);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testNameTwo, bMealRequest.getRequesterName());
            assertEquals(testLocationTwo, bMealRequest.getLocation());
            assertEquals(testStaffMember, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bMealRequest.getRequestStatus());
            assertEquals(testTypeTwo, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsByMealType() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testType);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsByMealType(testType);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testNameTwo, bMealRequest.getRequesterName());
            assertEquals(testLocationTwo, bMealRequest.getLocation());
            assertEquals(testStaffMemberTwo, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, bMealRequest.getRequestStatus());
            assertEquals(testType, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsByRequestStatus() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Done,
                    testTypeTwo);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsByRequestStatus(RequestStatus.Done);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testNameTwo, bMealRequest.getRequesterName());
            assertEquals(testLocationTwo, bMealRequest.getLocation());
            assertEquals(testStaffMemberTwo, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, bMealRequest.getRequestStatus());
            assertEquals(testTypeTwo, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsAfterTime() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            mealRequestDAO.addMealRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsAfterTime(testTimeOnePointFive);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testNameTwo, aMealRequest.getRequesterName());
            assertEquals(testLocationTwo, aMealRequest.getLocation());
            assertEquals(testStaffMemberTwo, aMealRequest.getStaffMember());
            assertEquals(testNotesTwo, aMealRequest.getAdditionalNotes());
            assertEquals(testTimeTwo, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Unstarted, aMealRequest.getRequestStatus());
            assertEquals(testTypeTwo, aMealRequest.getItemType());

            assertEquals(testName, bMealRequest.getRequesterName());
            assertEquals(testLocation, bMealRequest.getLocation());
            assertEquals(testStaffMember, bMealRequest.getStaffMember());
            assertEquals(testNotesTwo, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointEight, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bMealRequest.getRequestStatus());
            assertEquals(testTypeThree, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsBeforeTime() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            mealRequestDAO.addMealRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsBeforeTime(testTimeOnePointEight);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotes, aMealRequest.getAdditionalNotes());
            assertEquals(testTime, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Done, aMealRequest.getRequestStatus());
            assertEquals(testType, aMealRequest.getItemType());

            assertEquals(testNameThree, bMealRequest.getRequesterName());
            assertEquals(testLocationThree, bMealRequest.getLocation());
            assertEquals(testStaffMemberThree, bMealRequest.getStaffMember());
            assertEquals(testNotesThree, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointFive, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bMealRequest.getRequestStatus());
            assertEquals(testTypeThree, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void getMealRequestsBetweenTimes() {
        try {
            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotes,
                    testTime,
                    RequestStatus.Done,
                    testType);

            mealRequestDAO.addMealRequest(
                    testNameTwo,
                    testLocationTwo,
                    testStaffMemberTwo,
                    testNotesTwo,
                    testTimeTwo,
                    RequestStatus.Unstarted,
                    testTypeTwo);


            mealRequestDAO.addMealRequest(
                    testName,
                    testLocation,
                    testStaffMember,
                    testNotesTwo,
                    testTimeOnePointEight,
                    RequestStatus.Processing,
                    testTypeThree);

            mealRequestDAO.addMealRequest(
                    testNameThree,
                    testLocationThree,
                    testStaffMemberThree,
                    testNotesThree,
                    testTimeOnePointFive,
                    RequestStatus.Processing,
                    testTypeThree);

            ArrayList<MealRequest> someMealRequests = mealRequestDAO.getMealRequestsBetweenTimes(testTime, testTimeTwo);
            assertEquals(someMealRequests.size(), 2);
            MealRequest aMealRequest = someMealRequests.get(0);
            MealRequest bMealRequest = someMealRequests.get(1);

            assertEquals(testName, aMealRequest.getRequesterName());
            assertEquals(testLocation, aMealRequest.getLocation());
            assertEquals(testStaffMember, aMealRequest.getStaffMember());
            assertEquals(testNotesTwo, aMealRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointEight, aMealRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, aMealRequest.getRequestStatus());
            assertEquals(testTypeThree, aMealRequest.getItemType());

            assertEquals(testNameThree, bMealRequest.getRequesterName());
            assertEquals(testLocationThree, bMealRequest.getLocation());
            assertEquals(testStaffMemberThree, bMealRequest.getStaffMember());
            assertEquals(testNotesThree, bMealRequest.getAdditionalNotes());
            assertEquals(testTimeOnePointFive, bMealRequest.getRequestDate());
            assertEquals(RequestStatus.Processing, bMealRequest.getRequestStatus());
            assertEquals(testTypeThree, bMealRequest.getItemType());

        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }
    }
}