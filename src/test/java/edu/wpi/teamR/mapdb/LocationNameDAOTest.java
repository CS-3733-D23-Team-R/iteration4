package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LocationNameDAOTest {

    private LocationNameDAO locationNameDAO;
    private Connection connection;
    @BeforeAll
    void starterFunction() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("prototype1test");
        connection = Configuration.getConnection();
        locationNameDAO = new LocationNameDAO(connection);
    }

    @BeforeEach
    void deleteOldData() throws SQLException {
        locationNameDAO.deleteAllLocationNames();
    }

    @AfterAll
    void clearDataDeleteConnection() throws SQLException {
        locationNameDAO.deleteAllLocationNames();
        connection.close();
    }

    @Test
    void getLocations() throws SQLException {
        ArrayList<LocationName> locationNames;
        LocationName locationName;

        locationNames = locationNameDAO.getLocations();
        assertEquals(locationNames.size(), 0);

        locationNameDAO.addLocationName("Location1", "l1", "node");

        locationNames = locationNameDAO.getLocations();
        assertEquals(locationNames.size(), 1);
        locationName = locationNames.get(0);
        assertEquals(locationName.getLongName(), "Location1");
        assertEquals(locationName.getShortName(), "l1");
        assertEquals(locationName.getNodeType(), "node");

        locationNameDAO.addLocationName("Location2", "l2", "node");

        locationNames = locationNameDAO.getLocations();
        assertEquals(locationNames.size(), 2);

        locationNameDAO.addLocationName("Location3", "l2", "node");

        locationNames = locationNameDAO.getLocations();
        assertEquals(locationNames.size(), 3);
    }

    @Test
    void getLocationsByNodeType() throws SQLException {
        ArrayList<LocationName> locationNames;
        LocationName locationName;

        locationNames = locationNameDAO.getLocationsByNodeType("test");
        assertEquals(locationNames.size(), 0);

        locationNameDAO.addLocationName("Location1", "l1", "node");
        locationNameDAO.addLocationName("Location2", "l2", "node");
        locationNameDAO.addLocationName("Location3", "l3", "diff");

        locationNames = locationNameDAO.getLocationsByNodeType("node");
        assertEquals(locationNames.size(), 2);

        locationNames = locationNameDAO.getLocationsByNodeType("diff");
        assertEquals(locationNames.size(), 1);

        locationNames = locationNameDAO.getLocationsByNodeType("test");
        assertEquals(locationNames.size(), 0);
    }

    @Test
    void getLocationByLongName() throws SQLException {
        LocationName locationName;

        locationName = locationNameDAO.getLocationByLongName("test"); //TODO: ERROR CASE FOR MISSED SELECT

        locationNameDAO.addLocationName("Location1", "l1", "node");
        locationNameDAO.addLocationName("Location2", "l2", "node");
        locationNameDAO.addLocationName("Location3", "l3", "diff");

        locationName = locationNameDAO.getLocationByLongName("Location1");
        assertEquals(locationName.getLongName(), "Location1");
        assertEquals(locationName.getLongName(), "l1");
        assertEquals(locationName.getLongName(), "node");

        locationName = locationNameDAO.getLocationByLongName("Location2");
        assertEquals(locationName.getLongName(), "Location2");
        assertEquals(locationName.getLongName(), "l2");
        assertEquals(locationName.getLongName(), "node");

        locationName = locationNameDAO.getLocationByLongName("Location3");
        assertEquals(locationName.getLongName(), "Location3");
        assertEquals(locationName.getLongName(), "l3");
        assertEquals(locationName.getLongName(), "diff");

        locationName = locationNameDAO.getLocationByLongName("test");
    }

    @Test
    void modifyLocationNameType() throws SQLException {
        LocationName locationName;

        locationNameDAO.addLocationName("Location1", "l1", "node");
        locationNameDAO.addLocationName("Location2", "l2", "node");
        locationNameDAO.addLocationName("Location3", "l3", "diff");

        locationName = locationNameDAO.getLocationByLongName("Location1");
        assertEquals(locationName.getLongName(), "Location1");
        assertEquals(locationName.getLongName(), "l1");
        assertEquals(locationName.getLongName(), "node");

        locationNameDAO.modifyLocationNameType("Location1", "test");

        locationName = locationNameDAO.getLocationByLongName("Location1");
        assertEquals(locationName.getLongName(), "Location1");
        assertEquals(locationName.getLongName(), "l1");
        assertEquals(locationName.getLongName(), "test");

        //Ensure everything else is unchanged
        assertEquals(locationName.getLongName(), "Location2");
        assertEquals(locationName.getLongName(), "l2");
        assertEquals(locationName.getLongName(), "node");
        assertEquals(locationName.getLongName(), "Location3");
        assertEquals(locationName.getLongName(), "l3");
        assertEquals(locationName.getLongName(), "diff");
    }

    @Test
    void modifyLocationNameShortName() throws SQLException {
        LocationName locationName;

        locationNameDAO.addLocationName("Location1", "l1", "node");
        locationNameDAO.addLocationName("Location2", "l2", "node");
        locationNameDAO.addLocationName("Location3", "l3", "diff");

        locationName = locationNameDAO.getLocationByLongName("Location1");
        assertEquals(locationName.getLongName(), "Location1");
        assertEquals(locationName.getLongName(), "l1");
        assertEquals(locationName.getLongName(), "node");

        locationNameDAO.modifyLocationNameShortName("Location1", "testShortName");

        locationName = locationNameDAO.getLocationByLongName("Location1");
        assertEquals(locationName.getLongName(), "Location1");
        assertEquals(locationName.getLongName(), "l1");
        assertEquals(locationName.getLongName(), "testShortName");

        //Ensure everything else is unchanged
        assertEquals(locationName.getLongName(), "Location2");
        assertEquals(locationName.getLongName(), "l2");
        assertEquals(locationName.getLongName(), "node");
        assertEquals(locationName.getLongName(), "Location3");
        assertEquals(locationName.getLongName(), "l3");
        assertEquals(locationName.getLongName(), "diff");
    }
}