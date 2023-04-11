package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FlowerRequestDAOTest {

    private static FlowerRequestDAO flowerRequestDAO;
    private static Connection connection;
    @BeforeAll
    static void setUpConnection() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration1test");
    }
    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        connection = Configuration.getConnection();
        flowerRequestDAO = new FlowerRequestDAO(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void addFlowerRequest() {
    }

    @Test
    void deleteFlowerRequest() {
    }

    @Test
    void getFlowerRequests() {
    }

    @Test
    void getFlowerRequestByID() {
    }

    @Test
    void getFlowerRequestsByRequesterName() {
    }

    @Test
    void getFlowerRequestsByLocation() {
    }

    @Test
    void getFlowerRequestsByStaffMember() {
    }

    @Test
    void getFlowerRequestsByFlowerType() {
    }

    @Test
    void getFlowerRequestsByRequestStatus() {
    }

    @Test
    void getFlowerRequestsAfterTime() {
    }

    @Test
    void getFlowerRequestsBeforeTime() {
    }

    @Test
    void getFlowerRequestsBetweenTimes() {
    }
}