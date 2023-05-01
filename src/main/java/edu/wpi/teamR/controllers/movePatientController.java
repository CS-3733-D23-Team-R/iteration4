package edu.wpi.teamR.controllers;

import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.requestdb.Patient;
import edu.wpi.teamR.requestdb.RequestDatabase;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import net.kurobako.gesturefx.GesturePane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class movePatientController {

    RequestDatabase db;
    @FXML GesturePane miniMap;
    AnchorPane mapPane;
    @FXML MFXComboBox patientName, patientDestination, staffMember;
    @FXML MFXTextField currentLocation;
    @FXML MFXDatePicker moveDate;

    public void initialize() throws SQLException {
        db = new RequestDatabase();
        mapPane.getChildren().add(MapStorage.getFirstFloor());
        miniMap.setContent(mapPane);

        ArrayList<Patient> pList = db.getPatients();
        ArrayList<String> pListDisplay = new ArrayList<>();
        for(Patient p : pList)
            pListDisplay.add(p.getPatientName() + " - ID " + p.getPatientID());
        ObservableList<String> patientList = FXCollections.observableArrayList(pListDisplay);
        patientName.setItems(patientList);

        // For patientDestination, use Map Nodes(?)


        // For staffMember, use list of staff accounts

        Platform.runLater(() -> moveDate.setValue(LocalDate.now()));


    }
}
