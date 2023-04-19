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
    }

    @Test
    void getAvailableItemByName() throws SQLException, ClassNotFoundException, ItemNotFoundException {
    }

}
