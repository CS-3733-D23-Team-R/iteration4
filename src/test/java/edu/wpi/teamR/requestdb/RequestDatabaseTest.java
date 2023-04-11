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
    private static RequestDatabase requestDatabase;
    private static FurnitureRequestDAO furnitureRequestDAO;
    private static MealRequestDAO mealRequestDAO;
    private static FlowerRequestDAO flowerRequestDAO;
    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {
        Configuration.changeSchemaName("iteration1test");
        requestDatabase = RequestDatabase.getInstance();
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
        furnitureRequestDAO.addFurnitureRequest("John", "WPI", "Wong", "Need to get the thing", new Timestamp(2022, 12, 12, 12, 12, 12, 12), RequestStatus.Processing, "Arm");
        mealRequestDAO.addMealRequest("John", "WPI", "Wong", "Need to get the thing", new Timestamp(2022, 12, 12, 12, 12, 12, 12), RequestStatus.Done, "Finger");
        flowerRequestDAO.addFlowerRequest("John", "WPI", "Wong", "Need to get the thing", new Timestamp(2022, 12, 12, 12, 12, 12, 12), RequestStatus.Processing, "corn-cockle");
        ArrayList<ItemRequest> actual = requestDatabase.getItemRequests();
        assertEquals(3, actual.size());
    }
    @Test
    void getItemRequestByID() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        FurnitureRequest aFurniture = furnitureRequestDAO.addFurnitureRequest("John", "WPI", "Wong", "Need to get the thing", new Timestamp(2022, 12, 12, 12, 12, 12, 12), RequestStatus.Processing, "Arm");
        MealRequest aMeal = mealRequestDAO.addMealRequest("John", "WPI", "Wong", "Need to get the thing", new Timestamp(2022, 12, 12, 12, 12, 12, 12), RequestStatus.Done, "Finger");
        FlowerRequest aFlower = flowerRequestDAO.addFlowerRequest("John", "WPI", "Wong", "Need to get the thing", new Timestamp(2022, 12, 12, 12, 12, 12, 12), RequestStatus.Processing, "corn-cockle");
        assertEquals(aFurniture.getRequestID(), requestDatabase.getFurnitureRequestByID(aFurniture.getRequestID()).getRequestID());
        assertEquals(aFurniture.getRequesterName(), requestDatabase.getFurnitureRequestByID(aFurniture.getRequestID()).getRequesterName());
        assertEquals(aFurniture.getRequestDate(), requestDatabase.getFurnitureRequestByID(aFurniture.getRequestID()).getRequestDate());
        assertEquals(aFurniture.getRequestStatus(), requestDatabase.getFurnitureRequestByID(aFurniture.getRequestID()).getRequestStatus());
        assertEquals(aFurniture.getItemType(), requestDatabase.getFurnitureRequestByID(aFurniture.getRequestID()).getItemType());
    }
}
