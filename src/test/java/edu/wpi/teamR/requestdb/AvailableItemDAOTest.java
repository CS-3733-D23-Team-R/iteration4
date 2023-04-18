package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class AvailableItemDAOTest {

    private static AvailableItemDAO availableItemDAO;
    @Test
    void getAvailableItemsByTypeWithinRangeSortedByPrice() throws SQLException, ClassNotFoundException {
        // create two AvailableItem(s) with appropriate differing data-types
        availableItemDAO.getAvailableItemsByTypeWithinRangeSortedByPrice(RequestType.Flower, 0.1,0.1, SortOrder.unsorted); // execute said function with appropriate parameter
        // check that the function retrieved the correct AvailableItem through multiple assertEquals
    }

    @Test
    void getAvailableItemByName() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        // create two AvailableItem(s) with different names
        availableItemDAO.getAvailableItemByName("",RequestType.Flower); // execute said function with appropriate parameter
        // check that the function retrieved the correct AvailableItem through multiple assertEquals
    }

}
