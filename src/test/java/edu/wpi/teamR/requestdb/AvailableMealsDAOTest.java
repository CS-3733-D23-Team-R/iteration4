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

class AvailableMealsDAOTest {

    private static AvailableMealsDAO availableMealsDAO = new AvailableMealsDAO();

    @BeforeAll
    static void starterFunction() throws SQLException {
        Configuration.changeSchemaToTest();
    }
    @BeforeEach
    void deleteOldData() throws SQLException {
        availableMealsDAO.deleteAllAvailableMeals();
    }

    @AfterAll
    static void clearDataDeleteConnection() throws SQLException {
        availableMealsDAO.deleteAllAvailableMeals();
        Configuration.getConnection().close();
    }

    @Test
    void addAvailableMeals() throws SQLException {
        ArrayList<AvailableMeals> availableMeals = new ArrayList<>();
        AvailableMeals availableMeals1, availableMeals2;
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(0, availableMeals.size());

        availableMeals1 = availableMealsDAO.addAvailableMeals("item1", "temp", "desc", 0, false, true, false, true, false);
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(1, availableMeals.size());
        availableMeals2 = availableMeals.get(0);
        assertEquals(availableMeals1.getItemName(), availableMeals2.getItemName());
        assertEquals(availableMeals1.getImageReference(), availableMeals2.getImageReference());
        assertEquals(availableMeals1.getDescription(), availableMeals2.getDescription());
        assertEquals(availableMeals1.getItemPrice(), availableMeals2.getItemPrice());
        assertEquals(availableMeals1.isVegan(), availableMeals2.isVegan());
        assertEquals(availableMeals1.isVegetarian(), availableMeals2.isVegetarian());
        assertEquals(availableMeals1.isDairyFree(), availableMeals2.isDairyFree());
        assertEquals(availableMeals1.isPeanutFree(), availableMeals2.isPeanutFree());
        assertEquals(availableMeals1.isGlutenFree(), availableMeals2.isGlutenFree());

        availableMeals1 = availableMealsDAO.addAvailableMeals("item2", "temp", "desc", 0, false, true, false, true, false);
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(2, availableMeals.size());
    }

    @Test
    void deleteAvailableMeals() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableMeals> availableMeals = new ArrayList<>();
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(0, availableMeals.size());

        availableMealsDAO.addAvailableMeals("item1", "temp", "desc", 0, false, true, false, true, false);
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(1, availableMeals.size());

        availableMealsDAO.addAvailableMeals("item2", "temp", "desc", 0, false, true, false, true, false);
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(2, availableMeals.size());

        availableMealsDAO.deleteAvailableMeals("item1");
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(1, availableMeals.size());

        availableMealsDAO.deleteAvailableMeals("item2");
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(0, availableMeals.size());
    }

    @Test
    void deleteAllAvailableMeals() throws SQLException {
        ArrayList<AvailableMeals> availableMeals = new ArrayList<>();
        availableMealsDAO.addAvailableMeals("item1", "temp", "desc", 0, false, true, false, true, false);
        availableMealsDAO.addAvailableMeals("item2", "temp", "desc", 0, false, true, false, true, false);
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(2, availableMeals.size());

        availableMealsDAO.deleteAllAvailableMeals();
        availableMeals = availableMealsDAO.getAvailableMeals();
        assertEquals(0, availableMeals.size());
    }

    @Test
    void modifyAvailableMeals() throws SQLException, ItemNotFoundException {
        ArrayList<AvailableMeals> availableMeals = new ArrayList<>();
        availableMealsDAO.addAvailableMeals("item1", "temp", "desc", 0, false, true, false, true, false);
        availableMealsDAO.addAvailableMeals("item2", "temp", "desc", 0, false, true, false, true, false);

        availableMealsDAO.modifyAvailableMeals("item1", "temp1", "desc", 0, false, true, false, true, false);

        availableMeals = availableMealsDAO.getAvailableMealsByAttributes("item1", null, null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableMeals.size());
        assertEquals("temp1", availableMeals.get(0).getImageReference());

        availableMeals = availableMealsDAO.getAvailableMealsByAttributes("item2", null, null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableMeals.size());
        assertEquals("temp", availableMeals.get(0).getImageReference());
    }

    @Test
    void getAvailableMealsByAttributes() throws SQLException {
        ArrayList<AvailableMeals> availableMeals = new ArrayList<>();
        availableMealsDAO.addAvailableMeals("item1", "temp1", "desc1", 0, false, true, false, true, false);
        availableMealsDAO.addAvailableMeals("item2", "temp2", "desc2", 0, true, false, false, true, false);
        availableMeals = availableMealsDAO.getAvailableMealsByAttributes("item1", null, null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableMeals.size());

        availableMeals = availableMealsDAO.getAvailableMealsByAttributes("item3", null, null, null, null, null, null, null, null, SortOrder.lowToHigh);
        assertEquals(0, availableMeals.size());

        availableMeals = availableMealsDAO.getAvailableMealsByAttributes(null, null, null, 0.0, null, null, null, null, null, SortOrder.highToLow);
        assertEquals(2, availableMeals.size());

        availableMeals = availableMealsDAO.getAvailableMealsByAttributes(null, null, null, null, null, null, null, null, null, SortOrder.unsorted);
        assertEquals(2, availableMeals.size());

        availableMeals = availableMealsDAO.getAvailableMealsByAttributes(null, null, null, null, null, false, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableMeals.size());

        availableMeals = availableMealsDAO.getAvailableMealsByAttributes(null, null, null, null, null, true, null, null, null, SortOrder.unsorted);
        assertEquals(1, availableMeals.size());
    }
}