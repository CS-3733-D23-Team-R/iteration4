package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDAOTest {

    private static MapDatabase mapDatabase;
    private static ItemRequestDAO itemRequestDAO;
    private static UserDatabase userDatabase;

    @BeforeAll
    static void starterFunction() throws SQLException {
        Configuration.changeSchemaToTest();
        mapDatabase = new MapDatabase();
        itemRequestDAO = new ItemRequestDAO();
        userDatabase = new UserDatabase();
    }
    @BeforeEach
    void deleteOldData() throws SQLException {
        itemRequestDAO.deleteAllItemRequests();
        userDatabase.deleteAllUsers();
        mapDatabase.deleteAllLocationNames();
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException {
        itemRequestDAO.deleteAllItemRequests();
        userDatabase.deleteAllUsers();
        mapDatabase.deleteAllLocationNames();
        Configuration.getConnection().close();
    }

    @Test
    void addItemRequest() throws SQLException, ClassNotFoundException {
        userDatabase.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);

        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest1, itemRequest2;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, new Timestamp(System.currentTimeMillis()), 1);

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(1, itemRequests.size());
        itemRequest2 = itemRequests.get(0);
        assertEquals(itemRequest1.getRequestID(), itemRequest2.getRequestID());
        assertEquals(itemRequest1.getRequestType(), itemRequest2.getRequestType());
        assertEquals(itemRequest1.getRequestStatus(), itemRequest2.getRequestStatus());
        assertEquals(itemRequest1.getLongName(), itemRequest2.getLongName());
        assertEquals(itemRequest1.getStaffUsername(), itemRequest2.getStaffUsername());
        assertEquals(itemRequest1.getItemType(), itemRequest2.getItemType());
        assertEquals(itemRequest1.getRequesterName(), itemRequest2.getRequesterName());
        assertEquals(itemRequest1.getAdditionalNotes(), itemRequest2.getAdditionalNotes());
        assertEquals(itemRequest1.getRequestDate(), itemRequest2.getRequestDate());
        assertEquals(itemRequest1.getQuantity(), itemRequest2.getQuantity());
    }

    @Test
    void deleteItemRequest() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        userDatabase.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);

        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest1, itemRequest2, itemRequest3;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, new Timestamp(System.currentTimeMillis()), 5);
        itemRequest2 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item2", "Dave", null, new Timestamp(System.currentTimeMillis()), 6);

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(2, itemRequests.size());

        itemRequestDAO.deleteItemRequest(itemRequest1.getRequestID());//first delete

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(1, itemRequests.size());
        itemRequest3 = itemRequests.get(0);
        assertEquals(itemRequest2.getItemType(), itemRequest3.getItemType());

        itemRequestDAO.deleteItemRequest(itemRequest2.getRequestID());//2nd delete

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());
    }

    @Test
    void deleteAllItemRequests() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        userDatabase.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);

        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest1, itemRequest2, itemRequest3;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, new Timestamp(System.currentTimeMillis()),1);
        itemRequest2 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item2", "Dave", null, new Timestamp(System.currentTimeMillis()), 1);

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(2, itemRequests.size());

        itemRequestDAO.deleteAllItemRequests();

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());
    }

    @Test
    void getItemRequestByAttributes() throws SQLException, ClassNotFoundException, SearchException {
        userDatabase.addUser("staff1", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff2", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);
        userDatabase.addUser("staff3", "", "", "", "", "1234567890", new Date(System.currentTimeMillis()), AccessLevel.Staff, "", 0);

        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest1, itemRequest2, itemRequest3, itemRequest4, itemRequest5, itemRequest6;
        SearchList searchList;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        mapDatabase.addLocationName("location2", "", "");
        mapDatabase.addLocationName("location3", "", "");
        mapDatabase.addLocationName("location4", "", "");
        mapDatabase.addLocationName("location5", "", "");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, timestamp, 1);
        itemRequest2 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location2", null, "item2", "Dave", null, timestamp, 1);
        itemRequest3 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location3", "staff1", "item3", "Dave", null, timestamp, 1);
        itemRequest4 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location4", "staff2", "item4", "Dave", null, timestamp, 1);
        itemRequest5 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location5", "staff3", "item5", "Dave", null, timestamp, 1);
        itemRequest6 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location5", "staff1", "item6", "Dave", null, timestamp, 1);

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(6, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.requestID, Operation.equalTo, itemRequest1.getRequestID());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(1, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.requestID, Operation.lessThan, itemRequest6.getRequestID());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(5, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.longName, Operation.equalTo, itemRequest1.getLongName());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(1, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.longName, Operation.equalTo, itemRequest5.getLongName());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(2, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.requestID, Operation.lessThan, itemRequest5.getRequestID());
        searchList.addComparison(RequestAttribute.requestID, Operation.greaterThan, itemRequest2.getRequestID());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(2, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.staffUsername, Operation.equalTo, itemRequest1.getStaffUsername());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(2, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.staffUsername, Operation.equalTo, itemRequest3.getStaffUsername());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(2, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.staffUsername, Operation.equalTo, itemRequest4.getStaffUsername());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(1, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.staffUsername, Operation.equalTo, itemRequest5.getStaffUsername());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(1, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.requesterName, Operation.equalTo, itemRequest1.getRequesterName());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(6, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.additionalNotes, Operation.equalTo, itemRequest1.getAdditionalNotes());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(6, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.requestDate, Operation.equalTo, itemRequest1.getRequestDate());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(6, itemRequests.size());
    }
}