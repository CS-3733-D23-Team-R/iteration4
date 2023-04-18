package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.SQLException;

public class AvailableItemDAOTest {
    private static AvailableItemDAO availableItemDAO;
    private static Connection connection;

    @BeforeAll
    static void starterFunction() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("iteration2test");
        connection = Configuration.getConnection();
        availableItemDAO = new AvailableItemDAO();
    }
    @BeforeEach
    void deleteOldData() throws SQLException {
       // availableItemDAO.deleteAllAvailableItemDAO(); // ?
    }
}
