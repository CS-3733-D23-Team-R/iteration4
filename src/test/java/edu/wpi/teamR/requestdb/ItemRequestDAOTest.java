package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.mapdb.LocationNameDAO;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestDAOTest {
    private static MapDatabase testMapDatabase;
    private static ItemRequestDAO itemRequestDAO;
    private static Connection connection;



    private Timestamp testTime1 = new Timestamp(2020, 10, 7, 12, 30, 30, 100000000);
    private Timestamp testTime2 = new Timestamp(2023, 11, 25, 23, 59, 59, 100000000);
    @BeforeAll
    static void starterFunction() throws SQLException, ClassNotFoundException {
        testMapDatabase = new MapDatabase();
        Configuration.changeSchemaName("iteration2test");
        connection = Configuration.getConnection();
        itemRequestDAO = new ItemRequestDAO();
    }
    @BeforeEach
    void deleteOldData() throws SQLException, ClassNotFoundException {
        itemRequestDAO.deleteAllItemRequests();
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException, ClassNotFoundException {
        itemRequestDAO.deleteAllItemRequests();
    }

    void addItemRequest() throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> itemRequests; // create empty ArrayList
        itemRequests = itemRequestDAO.getItemRequests(); // should return list w/ 0 items
        assertEquals(itemRequests.size(),0); // should be true
        itemRequestDAO.addItemRequest(RequestType.Flower, RequestStatus.Processing, "", "test1", "test1", "test1", "test1", testTime1); // creates an itemRequest
        // creates an itemRequest ^
        itemRequests = itemRequestDAO.getItemRequests(); // should return list w/ 1 item
        assertEquals(itemRequests.size(), 1); //should be true
        ItemRequest itemRequest = itemRequests.get(0);
        // Checks that all fields are the same
        assertEquals(itemRequest.getRequestType(), RequestType.Flower);
        assertEquals(itemRequest.getRequestStatus(), RequestStatus.Processing);
        assertEquals(itemRequest.getLongname(), "test1");
        assertEquals(itemRequest.getStaffUsername(), "test1");
        assertEquals(itemRequest.getItemType(), "test1");
        assertEquals(itemRequest.getRequesterName(), "test1");
        assertEquals(itemRequest.getAdditionalNotes(), "test1");
        assertEquals(itemRequest.getRequestDate(), testTime1);
    }

    @Test
    void deleteItemRequest() throws SQLException, ItemNotFoundException, ClassNotFoundException {
        ArrayList<ItemRequest> itemRequests; // create empty ArrayList
        ItemRequest sampleItemRequest = itemRequestDAO.addItemRequest(RequestType.Flower, RequestStatus.Processing, "test1", "test1", "test1", "test1", "test1", testTime1); // creates an itemRequest
        itemRequests = itemRequestDAO.getItemRequests(); // should return list w/ 1 item
        ItemRequest itemRequest = itemRequests.get(0);
        itemRequestDAO.deleteItemRequest(sampleItemRequest.getRequestID()); // executes function
        assertEquals(itemRequests.size(), 0); // should be true i.e. the function deleted the only itemRequest from itemRequests
    }

    @Test
    void deleteAllItemRequests() throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> itemRequests; // create empty ArrayList
        itemRequestDAO.addItemRequest(RequestType.Flower, RequestStatus.Processing, "test1", "test1", "test1", "test1", "test1", testTime1); // creates an itemRequest
        itemRequestDAO.addItemRequest(RequestType.Flower, RequestStatus.Processing, "test1", "test1", "test1", "test1", "test1", testTime2); // creates an itemRequest
        itemRequestDAO.deleteAllItemRequests(); // executes function
        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(itemRequests.size(), 0); // should be true
    }

    @Test
    void getItemRequests() throws SQLException, ClassNotFoundException {
        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(itemRequests.size(), 0);
        itemRequestDAO.addItemRequest(RequestType.Flower, RequestStatus.Processing, "test1", "test1", "test1", "test1", "test1", testTime1); // creates an itemRequest
        itemRequests = itemRequestDAO.getItemRequests(); // executes function
        assertEquals(itemRequests.size(), 1);
        itemRequest = itemRequests.get(0);
        assertEquals(itemRequest.getRequestType(), RequestType.Flower);
        assertEquals(itemRequest.getRequestStatus(), RequestStatus.Processing);
        assertEquals(itemRequest.getLongname(), "test1");
        assertEquals(itemRequest.getStaffUsername(), "test1");
        assertEquals(itemRequest.getItemType(), "test1");
        assertEquals(itemRequest.getRequesterName(), "test1");
        assertEquals(itemRequest.getAdditionalNotes(), "test1");
        assertEquals(itemRequest.getRequestDate(), testTime1);
    }

    @Test
    void getItemRequestByAttributes(SearchList searchList) throws SQLException, ClassNotFoundException {
        // creates an empty list
        ArrayList<ItemRequest> itemRequests;
        itemRequestDAO.addItemRequest(RequestType.Flower, RequestStatus.Processing, "test1", "test1", "test1", "test1", "test1", testTime1); // creates an itemRequest
        itemRequestDAO.addItemRequest(RequestType.Flower, RequestStatus.Processing, "test1", "test1", "test1", "test1", "test1", testTime2); // creates an itemRequest
        // gets directionArrow(s) in directionArrows
        itemRequests = itemRequestDAO.getItemRequests(); // should return list w/ 2 items
        ItemRequest itemRequest = itemRequests.get(0);
        //itemRequestDAO.getItemRequestByAttributes();
        assertEquals(itemRequest.getRequestType(), RequestType.Flower);
        assertEquals(itemRequest.getRequestStatus(), RequestStatus.Processing);
        assertEquals(itemRequest.getLongname(), "test1");
        assertEquals(itemRequest.getStaffUsername(), "test1");
        assertEquals(itemRequest.getItemType(), "test1");
        assertEquals(itemRequest.getRequesterName(), "test1");
        assertEquals(itemRequest.getAdditionalNotes(), "test1");
        assertEquals(itemRequest.getRequestDate(), testTime1);
        //assertEquals(itemRequest.getLongname(), "test1");
        //assertEquals(directionArrow.getDirection(), Direction.Up);
        //itemRequestDAO.getItemRequestByAttributes();
        assertEquals(itemRequest.getRequestType(), RequestType.Flower);
        assertEquals(itemRequest.getRequestStatus(), RequestStatus.Processing);
        assertEquals(itemRequest.getLongname(), "test1");
        assertEquals(itemRequest.getStaffUsername(), "test1");
        assertEquals(itemRequest.getItemType(), "test1");
        assertEquals(itemRequest.getRequesterName(), "test1");
        assertEquals(itemRequest.getAdditionalNotes(), "test1");
        assertEquals(itemRequest.getRequestDate(), testTime2);
        //assertEquals(directionArrow.getLongname(), "test2");
        //assertEquals(directionArrow.getDirection(), Direction.Up);

    }
}
