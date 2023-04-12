package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class RequestDatabaseTest {
    private Timestamp testTime = new Timestamp(2023, 12, 12, 12, 12, 12, 0);
    private static RequestDatabase requestDatabase;
    private static FurnitureRequestDAO furnitureRequestDAO;
    private static MealRequestDAO mealRequestDAO;
    private static FlowerRequestDAO flowerRequestDAO;
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration1test");
        connection = Configuration.getConnection();
        requestDatabase = RequestDatabase.getInstance();
        furnitureRequestDAO = new FurnitureRequestDAO(connection);
        mealRequestDAO = new MealRequestDAO(connection);
        flowerRequestDAO = new FlowerRequestDAO(connection);
    }
    @BeforeEach
    void reset() throws SQLException, ClassNotFoundException {
        connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getFlowerRequestSchemaNameTableName()+";");
        statement.executeUpdate("DELETE FROM "+Configuration.getMealRequestSchemaNameTableName()+";");
        statement.executeUpdate("DELETE FROM "+Configuration.getFurnitureRequestSchemaNameTableName()+";");
    }
    @AfterEach
    void endInstance() throws SQLException {
        connection.close();
    }
    @Test
    void getItemRequestTest() throws SQLException, ClassNotFoundException {
        furnitureRequestDAO.addFurnitureRequest("John", "WPI", "Wong", "Need to get the thing", testTime, RequestStatus.Processing, "Arm");
        mealRequestDAO.addMealRequest("John", "WPI", "Wong", "Need to get the thing", testTime, RequestStatus.Done, "Finger");
        flowerRequestDAO.addFlowerRequest("John", "WPI", "Wong", "Need to get the thing", testTime, RequestStatus.Processing, "corn-cockle");
        ArrayList<ItemRequest> actual = requestDatabase.getItemRequests();
        assertEquals(3, actual.size());
        actual = requestDatabase.getItemRequestsByRequesterName("John");
        assertEquals(3, actual.size());
        actual = requestDatabase.getItemRequestsByRequestStatus(RequestStatus.Done);
        assertEquals(1, actual.size());
        actual = requestDatabase.getItemRequestsByLocation("WPI");
        assertEquals(3, actual.size());
        actual = requestDatabase.getItemRequestsByStaffMember("Wong");
        assertEquals(3, actual.size());
        actual = requestDatabase.getItemRequestsByItemType("Finger");
        assertEquals(1, actual.size());
    }
    @Test
    void getItemRequestByIDTest() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        FurnitureRequest aFurniture = furnitureRequestDAO.addFurnitureRequest("John", "WPI", "Wong", "Need to get the thing", testTime, RequestStatus.Processing, "Arm");
        MealRequest aMeal = mealRequestDAO.addMealRequest("John", "WPI", "Wong", "Need to get the thing", testTime, RequestStatus.Done, "Finger");
        FlowerRequest aFlower = flowerRequestDAO.addFlowerRequest("John", "WPI", "Wong", "Need to get the thing", testTime, RequestStatus.Processing, "corn-cockle");

        assertEquals(aFurniture.getRequestID(), requestDatabase.getItemRequestByID(aFurniture.getRequestID()).getRequestID());
        assertEquals(aFurniture.getRequesterName(), requestDatabase.getItemRequestByID(aFurniture.getRequestID()).getRequesterName());
        assertEquals(aFurniture.getRequestDate(), requestDatabase.getItemRequestByID(aFurniture.getRequestID()).getRequestDate());
        assertEquals(aFurniture.getRequestStatus(), requestDatabase.getItemRequestByID(aFurniture.getRequestID()).getRequestStatus());
        assertEquals(aFurniture.getItemType(), requestDatabase.getItemRequestByID(aFurniture.getRequestID()).getItemType());
        assertEquals(aFurniture.getLocation(), requestDatabase.getItemRequestByID(aFurniture.getRequestID()).getLocation());
        assertEquals(aFurniture.getAdditionalNotes(), requestDatabase.getItemRequestByID(aFurniture.getRequestID()).getAdditionalNotes());
        System.out.println(aMeal.getRequestID());
        System.out.println(requestDatabase.getItemRequestByID(aMeal.getRequestID()).getRequestID());
        assertEquals(aMeal.getRequestID(), requestDatabase.getItemRequestByID(aMeal.getRequestID()).getRequestID());
        assertEquals(aMeal.getRequesterName(), requestDatabase.getItemRequestByID(aMeal.getRequestID()).getRequesterName());
        assertEquals(aMeal.getRequestDate(), requestDatabase.getItemRequestByID(aMeal.getRequestID()).getRequestDate());
        assertEquals(aMeal.getRequestStatus(), requestDatabase.getItemRequestByID(aMeal.getRequestID()).getRequestStatus());
        assertEquals(aMeal.getItemType(), requestDatabase.getItemRequestByID(aMeal.getRequestID()).getItemType());
        assertEquals(aMeal.getLocation(), requestDatabase.getItemRequestByID(aMeal.getRequestID()).getLocation());
        assertEquals(aMeal.getAdditionalNotes(), requestDatabase.getItemRequestByID(aMeal.getRequestID()).getAdditionalNotes());

        assertEquals(aFlower.getRequestID(), requestDatabase.getItemRequestByID(aFlower.getRequestID()).getRequestID());
        assertEquals(aFlower.getRequesterName(), requestDatabase.getItemRequestByID(aFlower.getRequestID()).getRequesterName());
        assertEquals(aFlower.getRequestDate(), requestDatabase.getItemRequestByID(aFlower.getRequestID()).getRequestDate());
        assertEquals(aFlower.getRequestStatus(), requestDatabase.getItemRequestByID(aFlower.getRequestID()).getRequestStatus());
        assertEquals(aFlower.getItemType(), requestDatabase.getItemRequestByID(aFlower.getRequestID()).getItemType());
        assertEquals(aFlower.getLocation(), requestDatabase.getItemRequestByID(aFlower.getRequestID()).getLocation());
        assertEquals(aFlower.getAdditionalNotes(), requestDatabase.getItemRequestByID(aFlower.getRequestID()).getAdditionalNotes());
    }
}
