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

class PatientDAOTest {

    private static PatientDAO patientDAO;
    @BeforeAll
    static void beforeAll() throws SQLException {
        patientDAO = new PatientDAO();
        Configuration.changeSchemaToTest();
        Configuration.deleteEverything();
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        Configuration.deleteEverything();
    }

    @AfterAll
    static void afterAll() throws SQLException {
        Configuration.deleteEverything();
        Configuration.getConnection().close();
    }

    @Test
    void addPatient() throws SQLException {
        ArrayList<Patient> patients;
        Patient patient1, patient2;

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 0);

        patient1 = patientDAO.addPatient("Hi");

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 1);
        patient2 = patients.get(0);
        assertEquals(patient1.getPatientID(), patient2.getPatientID());
        assertEquals(patient1.getPatientName(), patient2.getPatientName());
    }

    @Test
    void modifyPatient() throws SQLException, ItemNotFoundException {
        ArrayList<Patient> patients;
        Patient patient1, patient2;

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 0);

        patient1 = patientDAO.addPatient("Hi");
        patient1 = patientDAO.modifyPatient(patient1.getPatientID(), "Hello");

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 1);
        patient2 = patients.get(0);
        assertEquals(patient1.getPatientID(), patient2.getPatientID());
        assertEquals(patient1.getPatientName(), patient2.getPatientName());
    }

    @Test
    void deletePatient() throws SQLException, ItemNotFoundException {
        ArrayList<Patient> patients;
        Patient patient1, patient2;

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 0);

        patient1 = patientDAO.addPatient("Hi");

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 1);

        patient2 = patientDAO.addPatient("Hello");

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 2);

        patientDAO.deletePatient(patient1.getPatientID());

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 1);

        patientDAO.deletePatient(patient2.getPatientID());

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 0);
    }

    @Test
    void deleteAllPatients() throws SQLException {
        ArrayList<Patient> patients;
        Patient patient1, patient2;

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 0);

        patient1 = patientDAO.addPatient("Hi");
        patient2 = patientDAO.addPatient("Hello");

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 2);

        patientDAO.deleteAllPatients();

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 0);
    }

    @Test
    void getPatientByID() throws SQLException, ItemNotFoundException {
        ArrayList<Patient> patients;
        Patient patient1, patient2, patient3, patient4;

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 0);

        patient1 = patientDAO.addPatient("Hi");
        patient2 = patientDAO.addPatient("Hello");

        patients = patientDAO.getPatients();
        assertEquals(patients.size(), 2);

        patient3 = patientDAO.getPatientByID(patient1.getPatientID());
        patient4 = patientDAO.getPatientByID(patient2.getPatientID());

        assertEquals(patient1.getPatientName(), patient3.getPatientName());
        assertEquals(patient1.getPatientID(), patient3.getPatientID());
        assertEquals(patient2.getPatientName(), patient4.getPatientName());
        assertEquals(patient2.getPatientID(), patient4.getPatientID());
    }
}