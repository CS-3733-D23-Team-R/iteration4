package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;
import oracle.ucp.proxy.annotation.Pre;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDAOTest {

    private static MapDatabase mapDatabase;
    private static ItemRequestDAO itemRequestDAO;

    @BeforeAll
    static void starterFunction() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration2test");
        mapDatabase = new MapDatabase();
        itemRequestDAO = new ItemRequestDAO();
    }
    @BeforeEach
    void deleteOldData() throws SQLException, ClassNotFoundException {
        itemRequestDAO.deleteAllItemRequests();
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getUserTableSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
        preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getLocationNameSchemaNameTableName()+";");
        preparedStatement.executeUpdate();

        Statement statement = Configuration.getConnection().createStatement();
        statement.executeUpdate("insert into "+Configuration.getUserTableSchemaNameTableName()+"(staffUsername,password,name,email,accessLevel,department,joindate,phonenum,jobtitle) values ('staff1', '', '', '','Staff','',CURRENT_DATE,'1234567890','');" +
                "insert into "+Configuration.getUserTableSchemaNameTableName()+"(staffUsername,password,name,email,accessLevel,department,joindate,phonenum,jobtitle) values ('staff2', '', '', '','Staff','',CURRENT_DATE,'1234567890','');" +
                "insert into "+Configuration.getUserTableSchemaNameTableName()+"(staffUsername,password,name,email,accessLevel,department,joindate,phonenum,jobtitle) values ('staff3', '', '', '','Staff','',CURRENT_DATE,'1234567890','');");
        //THIS IS BECAUSE WE HAVEN'T YET INTEGRATED USER TABLE
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException, ClassNotFoundException {
        itemRequestDAO.deleteAllItemRequests();
        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getUserTableSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
        preparedStatement = Configuration.getConnection().prepareStatement("DELETE FROM "+Configuration.getLocationNameSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    @Test
    void addItemRequest() throws SQLException, ClassNotFoundException {

        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest1, itemRequest2;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, new Timestamp(System.currentTimeMillis()));

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(1, itemRequests.size());
        itemRequest2 = itemRequests.get(0);
        assertEquals(itemRequest1.getRequestID(), itemRequest2.getRequestID());
        assertEquals(itemRequest1.getRequestType(), itemRequest2.getRequestType());
        assertEquals(itemRequest1.getRequestStatus(), itemRequest2.getRequestStatus());
        assertEquals(itemRequest1.getLongname(), itemRequest2.getLongname());
        assertEquals(itemRequest1.getStaffUsername(), itemRequest2.getStaffUsername());
        assertEquals(itemRequest1.getItemType(), itemRequest2.getItemType());
        assertEquals(itemRequest1.getRequesterName(), itemRequest2.getRequesterName());
        assertEquals(itemRequest1.getAdditionalNotes(), itemRequest2.getAdditionalNotes());
        assertEquals(itemRequest1.getRequestDate(), itemRequest2.getRequestDate());
    }

    @Test
    void deleteItemRequest() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest1, itemRequest2, itemRequest3;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, new Timestamp(System.currentTimeMillis()));
        itemRequest2 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item2", "Dave", null, new Timestamp(System.currentTimeMillis()));

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
        ArrayList<ItemRequest> itemRequests;
        ItemRequest itemRequest1, itemRequest2, itemRequest3;

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());

        mapDatabase.addLocationName("location1", "", "");
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, new Timestamp(System.currentTimeMillis()));
        itemRequest2 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item2", "Dave", null, new Timestamp(System.currentTimeMillis()));

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(2, itemRequests.size());

        itemRequestDAO.deleteAllItemRequests();

        itemRequests = itemRequestDAO.getItemRequests();
        assertEquals(0, itemRequests.size());
    }

//    Already covered in other tests
//    @Test
//    void getItemRequests() {
//    }

    @Test
    void getItemRequestByAttributes() throws SQLException, ClassNotFoundException, SearchException {
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
        itemRequest1 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location1", null, "item1", "Dave", null, timestamp);
        itemRequest2 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location2", null, "item2", "Dave", null, timestamp);
        itemRequest3 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location3", "staff1", "item3", "Dave", null, timestamp);
        itemRequest4 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location4", "staff2", "item4", "Dave", null, timestamp);
        itemRequest5 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location5", "staff3", "item5", "Dave", null, timestamp);
        itemRequest6 = itemRequestDAO.addItemRequest(RequestType.Meal, RequestStatus.Unstarted, "location5", "staff1", "item6", "Dave", null, timestamp);

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
        searchList.addComparison(RequestAttribute.longname, Operation.equalTo, itemRequest1.getLongname());
        itemRequests = itemRequestDAO.getItemRequestByAttributes(searchList);
        assertEquals(1, itemRequests.size());

        //Query
        searchList = new SearchList();
        searchList.addComparison(RequestAttribute.longname, Operation.equalTo, itemRequest5.getLongname());
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