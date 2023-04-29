package edu.wpi.teamR.login;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AlertDAOTest {

    private static final AlertDAO alertDAO = new AlertDAO();
    @BeforeAll
    static void startup() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaToTest();
    }
    @AfterAll
    static void end() throws SQLException, ClassNotFoundException {
        alertDAO.deleteAllAlerts();
        Configuration.getConnection().close();
    }
    @BeforeEach
    void reset() throws SQLException, ClassNotFoundException {
        alertDAO.deleteAllAlerts();
    }

    @Test
    void addAlert() throws SQLException {
        ArrayList<Alert> alerts = new ArrayList<>();
        Alert alert1, alert2;
        alerts = alertDAO.getAlerts();
        assertEquals(0,alerts.size());

        alert1 = alertDAO.addAlert("test1", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

        alerts = alertDAO.getAlerts();
        assertEquals(1,alerts.size());
        alert2 = alerts.get(0);
        assertEquals(alert1.getMessage(), alert2.getMessage());
        assertEquals(alert1.getStartDate().toString(), alert2.getStartDate().toString());

        alert1 = alertDAO.addAlert("test2", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        alerts = alertDAO.getAlerts();
        assertEquals(2,alerts.size());
    }

    @Test
    void deleteAlert() throws SQLException, ItemNotFoundException {
        ArrayList<Alert> alerts = new ArrayList<>();
        Alert alert1, alert2, alert3;
        alerts = alertDAO.getAlerts();
        assertEquals(0,alerts.size());

        alert1 = alertDAO.addAlert("test1", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        alert2 = alertDAO.addAlert("test2", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        alerts = alertDAO.getAlerts();
        assertEquals(2,alerts.size());

        alertDAO.deleteAlert(alert2.getMessage(), alert2.getStartDate());

        alerts = alertDAO.getAlerts();
        assertEquals(1,alerts.size());
        alert2 = alerts.get(0);
        assertEquals(alert1.getMessage(), alert2.getMessage());
        assertEquals(alert1.getStartDate().toString(), alert2.getStartDate().toString());

        alertDAO.deleteAlert(alert1.getMessage(), alert1.getStartDate());

        alerts = alertDAO.getAlerts();
        assertEquals(0,alerts.size());
    }

    @Test
    void deleteAllAlerts() throws SQLException {
        ArrayList<Alert> alerts = new ArrayList<>();
        Alert alert1, alert2, alert3;
        alerts = alertDAO.getAlerts();
        assertEquals(0,alerts.size());

        alert1 = alertDAO.addAlert("test1", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        alert2 = alertDAO.addAlert("test2", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        alerts = alertDAO.getAlerts();
        assertEquals(2,alerts.size());

        alertDAO.deleteAllAlerts();

        alerts = alertDAO.getAlerts();
        assertEquals(0,alerts.size());
    }


//    void getAlertsInLastNumDaysDesc() throws SQLException {
//        ArrayList<Alert> alerts = new ArrayList<>();
//        Alert alert1, alert2, alert3;
//        alerts = alertDAO.getAlerts();
//        assertEquals(0,alerts.size());
//
//        alert1 = alertDAO.addAlert("test1", new Date(System.currentTimeMillis() - 86400000)); //86400000 is day in ms
//        alert2 = alertDAO.addAlert("test2", new Date(System.currentTimeMillis() - 86400000*3));
//        alerts = alertDAO.getAlerts();
//        assertEquals(2,alerts.size());
//
//        alerts = alertDAO.getAlertsInLastNumDaysDesc(0);
//        assertEquals(0,alerts.size());
//
//        alerts = alertDAO.getAlertsInLastNumDaysDesc(2);
//        assertEquals(1,alerts.size());
//
//        alerts = alertDAO.getAlertsInLastNumDaysDesc(4);
//        assertEquals(2,alerts.size());
//
//        alerts = alertDAO.getAlertsInLastNumDaysDesc(100);
//        assertEquals(2,alerts.size());
//
//        alerts = alertDAO.getAlertsInLastNumDaysDesc(-5);
//        assertEquals(0,alerts.size());
//    }
}