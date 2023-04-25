package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AvailableSuppliesDAOTest {

    private static AvailableSuppliesDAO availableSuppliesDAO = new AvailableSuppliesDAO();

    @BeforeAll
    static void starterFunction() {
        Configuration.changeSchemaToTest();
    }
    @BeforeEach
    void deleteOldData() throws SQLException {
        availableSuppliesDAO.deleteAllAvailableSupplies();
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException {
        availableSuppliesDAO.deleteAllAvailableSupplies();
        Configuration.getConnection().close();
    }
    @Test
    void addAvailableSupplies() throws SQLException {
        ArrayList<AvailableSupplies> availableSupplies = new ArrayList<>();
        AvailableSupplies availableSupplies1, availableSupplies2;
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(0, availableSupplies.size());

        availableSupplies1 = availableSuppliesDAO.addAvailableSupplies("item1", "temp", "desc", 0, false, true, false, true);
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(1, availableSupplies.size());
        availableSupplies2 = availableSupplies.get(0);
        assertEquals(availableSupplies1.getItemName(), availableSupplies2.getItemName());
        assertEquals(availableSupplies1.getImageReference(), availableSupplies2.getImageReference());
        assertEquals(availableSupplies1.getDescription(), availableSupplies2.getDescription());
        assertEquals(availableSupplies1.getItemPrice(), availableSupplies2.getItemPrice());
        assertEquals(availableSupplies1.isPaper(), availableSupplies2.isPaper());
        assertEquals(availableSupplies1.isPen(), availableSupplies2.isPen());
        assertEquals(availableSupplies1.isOrganization(), availableSupplies2.isOrganization());
        assertEquals(availableSupplies1.isComputerAccessory(), availableSupplies2.isComputerAccessory());

        availableSupplies1 = availableSuppliesDAO.addAvailableSupplies("item2", "temp", "desc", 0, false, true, false, true);
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(2, availableSupplies.size());
    }

    @Test
    void deleteAvailableSupplies() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableSupplies> availableSupplies = new ArrayList<>();
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(0, availableSupplies.size());

        availableSuppliesDAO.addAvailableSupplies("item1", "temp", "desc", 0, false, true, false, true);
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(1, availableSupplies.size());

        availableSuppliesDAO.addAvailableSupplies("item2", "temp", "desc", 0, false, true, false, true);
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(2, availableSupplies.size());

        availableSuppliesDAO.deleteAvailableSupplies("item1");
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(1, availableSupplies.size());

        availableSuppliesDAO.deleteAvailableSupplies("item2");
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(0, availableSupplies.size());
    }

    @Test
    void deleteAllAvailableSupplies() throws SQLException {
        ArrayList<AvailableSupplies> availableSupplies = new ArrayList<>();
        availableSuppliesDAO.addAvailableSupplies("item1", "temp", "desc", 0, false, true, false, true);
        availableSuppliesDAO.addAvailableSupplies("item2", "temp", "desc", 0, false, true, false, true);
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(2, availableSupplies.size());

        availableSuppliesDAO.deleteAllAvailableSupplies();
        availableSupplies = availableSuppliesDAO.getAvailableSupplies();
        assertEquals(0, availableSupplies.size());
    }

    @Test
    void modifyAvailableSupplies() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableSupplies> availableSupplies = new ArrayList<>();
        availableSuppliesDAO.addAvailableSupplies("item1", "temp", "desc", 0, false, true, false, true);
        availableSuppliesDAO.addAvailableSupplies("item2", "temp", "desc", 0, false, true, false, true);

        availableSuppliesDAO.modifyAvailableSupplies("item1", "temp1", "desc", 0, false, true, false, true);

        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes("item1", null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableSupplies.size());
        assertEquals("temp1", availableSupplies.get(0).getImageReference());

        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes("item2", null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableSupplies.size());
        assertEquals("temp", availableSupplies.get(0).getImageReference());
    }

    @Test
    void getAvailableSuppliesByAttributes() throws SQLException {
        ArrayList<AvailableSupplies> availableSupplies = new ArrayList<>();
        availableSuppliesDAO.addAvailableSupplies("item1", "temp1", "desc1", 0, false, true, false, true);
        availableSuppliesDAO.addAvailableSupplies("item2", "temp2", "desc2", 0, true, false, false, true);
        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes("item1", null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableSupplies.size());

        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes("item3", null, null, null, null, null, null, null, SortOrder.lowToHigh);
        assertEquals(0, availableSupplies.size());

        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes(null, null, null, 0.0, null, null, null, null, SortOrder.highToLow);
        assertEquals(2, availableSupplies.size());

        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes(null, null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(2, availableSupplies.size());

        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes(null, null, null, null, null, false, null, null, SortOrder.unsorted);
        assertEquals(1, availableSupplies.size());

        availableSupplies = availableSuppliesDAO.getAvailableSuppliesByAttributes(null, null, null, null, null, true, null, null, SortOrder.unsorted);
        assertEquals(1, availableSupplies.size());
    }
}