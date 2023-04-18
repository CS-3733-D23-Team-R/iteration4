//package edu.wpi.teamR.requestdb;
//
//import edu.wpi.teamR.ItemNotFoundException;
//import org.junit.jupiter.api.Test;
//import edu.wpi.teamR.Configuration;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Time;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RoomRequestDAOTest {
//    private static RoomRequestDAO roomRequestDAO;
//    private static Connection connection;
//    /*
//    RoomRequest
//    String location/longName
//    Timestamp startTime
//    Timestamp endTime
//    String requesterName
//    String requestReason
//     */
//    private String testLocation = "Unity Hall";
//    private Timestamp testStartTime = new Timestamp(2020, 10, 7, 12, 30, 30, 100000000);
//    private Timestamp testEndTime = new Timestamp(2021, 11, 8, 13, 31, 31, 100000000);
//    private String testRequesterName = "Joe Biden";
//    private String testRequestReason = "No particular reason";
//
//    private String testLocationTwo = "Morgan Hall";
//    private Timestamp testStartTimeTwo = new Timestamp(2023, 11, 25, 23, 59, 59, 100000000);
//    private Timestamp testEndTimeTwo = new Timestamp(2024, 12, 26, 1, 1, 1, 100000000);
//    private String testRequesterNameTwo = "Donald Trump";
//    private String testRequestReasonTwo = "All the particular reasons";
//
//    private String testLocationThree = "South Village";
//    private Timestamp testStartTimeThree = new Timestamp(2027, 10, 23, 23, 59, 59, 100000000);;
//    private Timestamp testEndTimeThree = new Timestamp(2028, 11, 1, 1, 1, 1, 100000000);
//    private String testRequesterNameThree = "Thanos";
//    private String testRequestReasonThree = "Trying to hit the gym";
//
//    //these test times are placed in between testTime and testTime2 for testing purposes
//    private Timestamp testTimeOnePointTwo = new Timestamp(2021, 10, 7, 12, 30, 30, 100000000);
//    private Timestamp testTimeOnePointFive = new Timestamp(2022, 10, 7, 12, 30, 30, 100000000);
//    private Timestamp testTimeOnePointEight = new Timestamp(2023, 10, 7, 12, 30, 30, 100000000);
//
//    @BeforeAll
//    static void starterFunction() throws SQLException, ClassNotFoundException {
//        Configuration.changeSchemaName("iteration1test");
//        roomRequestDAO = new RoomRequestDAO();
//    }
//    @BeforeEach
//    void deleteOldData() throws SQLException, ClassNotFoundException {
//        roomRequestDAO.deleteAllRoomRequests();
//    }
//
//    @Test
//    void addRoomRequest() throws SQLException, ClassNotFoundException {
//        roomRequestDAO.addRoomRequest(
//                testLocation,
//                testStartTime,
//                testEndTime,
//                testRequesterName,
//                testRequestReason);
//
//        RoomRequest aRoomRequest = roomRequestDAO.getRoomRequests().get(0);
//
//        assertEquals(testLocation, aRoomRequest.getLocation());
//        assertEquals(testStartTime, aRoomRequest.getStartTime());
//        assertEquals(testEndTime, aRoomRequest.getEndTime());
//        assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//        assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//    }
//
//    @Test
//    void deleteRoomRequest() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            RoomRequest aRoomRequest = roomRequestDAO.getRoomRequests().get(0);
//
//            int requestID = aRoomRequest.getRequestID();
//
//            roomRequestDAO.deleteRoomRequest(requestID);
//
//            roomRequestDAO.getRoomRequestByID(requestID);
//
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequests() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequests();
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationTwo, bRoomRequest.getLocation());
//            assertEquals(testStartTimeTwo, bRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, bRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, bRoomRequest.getRequestReason());
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestByID() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//            RoomRequest testRoomRequest = roomRequestDAO.getRoomRequests().get(0);
//            int requestID = testRoomRequest.getRequestID();
//            RoomRequest aRoomRequest = roomRequestDAO.getRoomRequestByID(requestID);
//            RoomRequest bRoomRequest = roomRequestDAO.getRoomRequestByID(requestID + 1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationTwo, bRoomRequest.getLocation());
//            assertEquals(testStartTimeTwo, bRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, bRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsByRequesterName() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterName,
//                    testRequestReasonTwo);
//
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsByRequesterName(testRequesterName);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationTwo, bRoomRequest.getLocation());
//            assertEquals(testStartTimeTwo, bRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, bRoomRequest.getEndTime());
//            assertEquals(testRequesterName, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsByLocation() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsByLocation(testLocation);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocation, bRoomRequest.getLocation());
//            assertEquals(testStartTimeTwo, bRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, bRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsByStartTime() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTime,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsByStartTime(testStartTime);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationTwo, bRoomRequest.getLocation());
//            assertEquals(testStartTime, bRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, bRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsByEndTime() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTime,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsByEndTime(testEndTime);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationTwo, bRoomRequest.getLocation());
//            assertEquals(testStartTimeTwo, bRoomRequest.getStartTime());
//            assertEquals(testEndTime, bRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsByRequestReason() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReason);
//
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsByRequestReason(testRequestReason);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationTwo, bRoomRequest.getLocation());
//            assertEquals(testStartTimeTwo, bRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, bRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsAfterTime() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason
//                    );
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testTimeOnePointEight,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationThree,
//                    testTimeOnePointFive,
//                    testEndTimeThree,
//                    testRequesterNameThree,
//                    testRequestReasonThree);
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsAfterTime(testTimeOnePointFive);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocationTwo, aRoomRequest.getLocation());
//            assertEquals(testStartTimeTwo, aRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, aRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocation, bRoomRequest.getLocation());
//            assertEquals(testTimeOnePointEight, bRoomRequest.getStartTime());
//            assertEquals(testEndTime, bRoomRequest.getEndTime());
//            assertEquals(testRequesterName, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsBeforeTime() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testTimeOnePointEight,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationThree,
//                    testTimeOnePointFive,
//                    testEndTimeThree,
//                    testRequesterNameThree,
//                    testRequestReasonThree);
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsBeforeTime(testTimeOnePointEight);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocation, aRoomRequest.getLocation());
//            assertEquals(testStartTime, aRoomRequest.getStartTime());
//            assertEquals(testEndTime, aRoomRequest.getEndTime());
//            assertEquals(testRequesterName, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReason, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationThree, bRoomRequest.getLocation());
//            assertEquals(testTimeOnePointFive, bRoomRequest.getStartTime());
//            assertEquals(testRequesterNameThree, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonThree, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    void getRoomRequestsBetweenTimes() throws ClassNotFoundException {
//        try {
//            roomRequestDAO.addRoomRequest(
//                    testLocation,
//                    testStartTime,
//                    testEndTime,
//                    testRequesterName,
//                    testRequestReason);
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testStartTimeTwo,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo);
//
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationTwo,
//                    testTimeOnePointEight,
//                    testEndTimeTwo,
//                    testRequesterNameTwo,
//                    testRequestReasonTwo
//                    );
//
//            roomRequestDAO.addRoomRequest(
//                    testLocationThree,
//                    testTimeOnePointFive,
//                    testEndTimeThree,
//                    testRequesterNameThree,
//                    testRequestReasonThree
//                    );
//
//            ArrayList<RoomRequest> someRoomRequests = roomRequestDAO.getRoomRequestsBetweenTimes(testStartTime, testStartTimeTwo);
//            assertEquals(someRoomRequests.size(), 2);
//            RoomRequest aRoomRequest = someRoomRequests.get(0);
//            RoomRequest bRoomRequest = someRoomRequests.get(1);
//
//            assertEquals(testLocationTwo, aRoomRequest.getLocation());
//            assertEquals(testTimeOnePointEight, aRoomRequest.getStartTime());
//            assertEquals(testEndTimeTwo, aRoomRequest.getEndTime());
//            assertEquals(testRequesterNameTwo, aRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonTwo, aRoomRequest.getRequestReason());
//
//            assertEquals(testLocationThree, bRoomRequest.getLocation());
//            assertEquals(testTimeOnePointFive, bRoomRequest.getStartTime());
//            assertEquals(testEndTimeThree, bRoomRequest.getEndTime());
//            assertEquals(testRequesterNameThree, bRoomRequest.getRequesterName());
//            assertEquals(testRequestReasonThree, bRoomRequest.getRequestReason());
//
//        } catch (SQLException e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//}