package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AvailableFlowersDAOTest {

    private static AvailableFlowersDAO availableFlowersDAO = new AvailableFlowersDAO();

    @BeforeAll
    static void starterFunction() throws SQLException {
        Configuration.changeSchemaToTest();
    }
    @BeforeEach
    void deleteOldData() throws SQLException {
        availableFlowersDAO.deleteAllAvailableFlowers();
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException {
        availableFlowersDAO.deleteAllAvailableFlowers();
        Configuration.getConnection().close();
    }

    @Test
    void addAvailableFlowers() throws SQLException {
        ArrayList<AvailableFlowers> availableFlowers = new ArrayList<>();
        AvailableFlowers availableFlowers1, availableFlowers2;
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(0, availableFlowers.size());

        availableFlowers1 = availableFlowersDAO.addAvailableFlowers("item1", "temp", "desc", 0, false, true);
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(1, availableFlowers.size());
        availableFlowers2 = availableFlowers.get(0);
        assertEquals(availableFlowers1.getItemName(), availableFlowers2.getItemName());
        assertEquals(availableFlowers1.getImageReference(), availableFlowers2.getImageReference());
        assertEquals(availableFlowers1.getDescription(), availableFlowers2.getDescription());
        assertEquals(availableFlowers1.getItemPrice(), availableFlowers2.getItemPrice());
        assertEquals(availableFlowers1.isBouqet(), availableFlowers2.isBouqet());
        assertEquals(availableFlowers1.isHasCard(), availableFlowers2.isHasCard());

        availableFlowers1 = availableFlowersDAO.addAvailableFlowers("item2", "temp", "desc", 0, false, true);
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(2, availableFlowers.size());
    }

    @Test
    void deleteAvailableFlowers() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableFlowers> availableFlowers = new ArrayList<>();
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(0, availableFlowers.size());

        availableFlowersDAO.addAvailableFlowers("item1", "temp", "desc", 0, false, true);
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(1, availableFlowers.size());

        availableFlowersDAO.addAvailableFlowers("item2", "temp", "desc", 0, false, true);
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(2, availableFlowers.size());

        availableFlowersDAO.deleteAvailableFlowers("item1");
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(1, availableFlowers.size());

        availableFlowersDAO.deleteAvailableFlowers("item2");
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(0, availableFlowers.size());
    }

    @Test
    void deleteAllAvailableFlowers() throws SQLException {
        ArrayList<AvailableFlowers> availableFlowers = new ArrayList<>();
        availableFlowersDAO.addAvailableFlowers("item1", "temp", "desc", 0, false, true);
        availableFlowersDAO.addAvailableFlowers("item2", "temp", "desc", 0, false, true);
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(2, availableFlowers.size());

        availableFlowersDAO.deleteAllAvailableFlowers();
        availableFlowers = availableFlowersDAO.getAvailableFlowers();
        assertEquals(0, availableFlowers.size());
    }

    @Test
    void modifyAvailableFlowers() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableFlowers> availableFlowers = new ArrayList<>();
        availableFlowersDAO.addAvailableFlowers("item1", "temp", "desc", 0, false, true);
        availableFlowersDAO.addAvailableFlowers("item2", "temp", "desc", 0, false, true);

        availableFlowersDAO.modifyAvailableFlowers("item1", "temp1", "desc", 0, false, true);

        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes("item1", null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableFlowers.size());
        assertEquals("temp1", availableFlowers.get(0).getImageReference());

        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes("item2", null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableFlowers.size());
        assertEquals("temp", availableFlowers.get(0).getImageReference());
    }

    @Test
    void getAvailableFlowersByAttributes() throws SQLException {
        ArrayList<AvailableFlowers> availableFlowers = new ArrayList<>();
        availableFlowersDAO.addAvailableFlowers("item1", "temp1", "desc1", 0, false, true);
        availableFlowersDAO.addAvailableFlowers("item2", "temp2", "desc2", 0, true, false);
        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes("item1", null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableFlowers.size());

        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes("item3", null, null, null, null, null, SortOrder.lowToHigh);
        assertEquals(0, availableFlowers.size());

        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes(null, null, null, 0.0, null, null, SortOrder.highToLow);
        assertEquals(2, availableFlowers.size());

        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes(null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(2, availableFlowers.size());

        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes(null, null, null, null, null, false, SortOrder.unsorted);
        assertEquals(1, availableFlowers.size());

        availableFlowers = availableFlowersDAO.getAvailableFlowersByAttributes(null, null, null, null, null, true, SortOrder.unsorted);
        assertEquals(1, availableFlowers.size());
    }
}