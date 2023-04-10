package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LocationNameDAOTest {

    private LocationNameDAO locationNameDAO;
    private Connection connection;
    @BeforeAll
    void starterFunction() throws SQLException, ClassNotFoundException {
        connection = Configuration.getConnection();
        locationNameDAO = new LocationNameDAO(connection);
    }

    @Test
    void getLocations() {

    }

    @Test
    void getLocationByNodeID() {
    }

    @Test
    void getLocationsByNodeType() {
    }

    @Test
    void getLocationByLongName() {
    }

    @Test
    void modifyLocationNameType() {
    }

    @Test
    void modifyLocationNameShortName() {
    }
}