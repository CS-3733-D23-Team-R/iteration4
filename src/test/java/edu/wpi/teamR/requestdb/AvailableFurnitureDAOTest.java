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

class AvailableFurnitureDAOTest {

    private static AvailableFurnitureDAO availableFurnitureDAO = new AvailableFurnitureDAO();

    @BeforeAll
    static void starterFunction() {
        Configuration.changeSchemaToTest();
    }
    @BeforeEach
    void deleteOldData() throws SQLException {
        availableFurnitureDAO.deleteAllAvailableFurniture();
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException {
        availableFurnitureDAO.deleteAllAvailableFurniture();
        Configuration.getConnection().close();
    }
    @Test
    void addAvailableFurniture() throws SQLException {
        ArrayList<AvailableFurniture> availableFurnitures = new ArrayList<>();
        AvailableFurniture availableFurniture1, availableFurniture2;
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(0, availableFurnitures.size());

        availableFurniture1 = availableFurnitureDAO.addAvailableFurniture("item1", "temp", "desc", false, true, false, true);
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(1, availableFurnitures.size());
        availableFurniture2 = availableFurnitures.get(0);
        assertEquals(availableFurniture1.getItemName(), availableFurniture2.getItemName());
        assertEquals(availableFurniture1.getImageReference(), availableFurniture2.getImageReference());
        assertEquals(availableFurniture1.getDescription(), availableFurniture2.getDescription());
        assertEquals(availableFurniture1.isSeating(), availableFurniture2.isSeating());
        assertEquals(availableFurniture1.isTable(), availableFurniture2.isTable());
        assertEquals(availableFurniture1.isPillow(), availableFurniture2.isPillow());
        assertEquals(availableFurniture1.isStorage(), availableFurniture2.isStorage());

        availableFurniture1 = availableFurnitureDAO.addAvailableFurniture("item2", "temp", "desc", false, true, false, true);
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(2, availableFurnitures.size());
    }

    @Test
    void deleteAvailableFurniture() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableFurniture> availableFurnitures = new ArrayList<>();
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(0, availableFurnitures.size());

        availableFurnitureDAO.addAvailableFurniture("item1", "temp", "desc", false, true, false, true);
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(1, availableFurnitures.size());

        availableFurnitureDAO.addAvailableFurniture("item2", "temp", "desc", false, true, false, true);
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(2, availableFurnitures.size());

        availableFurnitureDAO.deleteAvailableFurniture("item1");
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(1, availableFurnitures.size());

        availableFurnitureDAO.deleteAvailableFurniture("item2");
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(0, availableFurnitures.size());
    }

    @Test
    void deleteAllAvailableFurniture() throws SQLException {
        ArrayList<AvailableFurniture> availableFurnitures = new ArrayList<>();
        availableFurnitureDAO.addAvailableFurniture("item1", "temp", "desc", false, true, false, true);
        availableFurnitureDAO.addAvailableFurniture("item2", "temp", "desc", false, true, false, true);
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(2, availableFurnitures.size());

        availableFurnitureDAO.deleteAllAvailableFurniture();
        availableFurnitures = availableFurnitureDAO.getAvailableFurniture();
        assertEquals(0, availableFurnitures.size());
    }

    @Test
    void modifyAvailableFurniture() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableFurniture> availableFurnitures = new ArrayList<>();
        availableFurnitureDAO.addAvailableFurniture("item1", "temp", "desc", false, true, false, true);
        availableFurnitureDAO.addAvailableFurniture("item2", "temp", "desc", false, true, false, true);

        availableFurnitureDAO.modifyAvailableFurniture("item1", "temp1", "desc", false, true, false, true);

        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes("item1", null, null, null, null, null, null);
        assertEquals(1, availableFurnitures.size());
        assertEquals("temp1", availableFurnitures.get(0).getImageReference());

        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes("item2", null, null, null, null, null, null);
        assertEquals(1, availableFurnitures.size());
        assertEquals("temp", availableFurnitures.get(0).getImageReference());
    }

    @Test
    void getAvailableFurnitureByAttributes() throws SQLException {
        ArrayList<AvailableFurniture> availableFurnitures = new ArrayList<>();
        availableFurnitureDAO.addAvailableFurniture("item1", "temp1", "desc1", true, false, true, false);
        availableFurnitureDAO.addAvailableFurniture("item2", "temp2", "desc2", true, true, false, true);
        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes("item1", null, null, null, null, null, null);
        assertEquals(1, availableFurnitures.size());

        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes("item3", null, null, null, null, null, null);
        assertEquals(0, availableFurnitures.size());

        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes(null, null, null, true, null, null, null);
        assertEquals(2, availableFurnitures.size());

        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes(null, null, null, null, null, null, null);
        assertEquals(2, availableFurnitures.size());

        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes(null, null, null, null, null, false, null);
        assertEquals(1, availableFurnitures.size());

        availableFurnitures = availableFurnitureDAO.getAvailableFurnitureByAttributes(null, null, null, null, null, true, null);
        assertEquals(1, availableFurnitures.size());
    }
}