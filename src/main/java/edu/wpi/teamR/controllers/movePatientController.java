package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.requestdb.Patient;
import edu.wpi.teamR.requestdb.PatientMove;
import edu.wpi.teamR.requestdb.RequestDatabase;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import net.kurobako.gesturefx.GesturePane;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

public class movePatientController {

    RequestDatabase db;
    UserDatabase udb;
    ArrayList<String> rooms = new ArrayList<>();
    ObservableList<String> patientList, roomList, staffList;
    @FXML GesturePane miniMap;
    AnchorPane mapPane;
    @FXML MFXComboBox patientName, patientDestination, staffMember;
    @FXML MFXTextField currentLocation;
    @FXML MFXDatePicker moveDate;
    @FXML TableView<PatientMove> moveTable;
    @FXML TableColumn<PatientMove, String> locCol, staffCol;
    @FXML TableColumn<PatientMove, Integer> idCol;
    @FXML TableColumn<PatientMove, Timestamp> dateCol;

    public void initialize() throws SQLException, ItemNotFoundException {
        db = new RequestDatabase();
        udb = new UserDatabase();
        mapPane.getChildren().add(MapStorage.getFirstFloor());
        miniMap.setContent(mapPane);

        ArrayList<Patient> pList = db.getPatients();
        ArrayList<String> pList0 = new ArrayList<>();
        for(Patient p : pList)
            pList0.add(p.getPatientName() + " - ID: " + p.getPatientID());
        patientList = FXCollections.observableArrayList(pList0);
        patientName.setItems(patientList);

        for(int i = 0; i < 25; i++)
            rooms.add("Room " + (i + 1));
        roomList = FXCollections.observableArrayList(rooms);
        patientDestination.setItems(roomList);

        ArrayList<User> uList = udb.getUsers();
        ArrayList<String> userList0 = new ArrayList<>();
        for(User u : uList)
            userList0.add(u.getName() + " - Username: " + u.getStaffUsername());
        staffList = FXCollections.observableArrayList(userList0);
        staffMember.setItems(staffList);

        Platform.runLater(() -> moveDate.setValue(LocalDate.now()));

        idCol.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
        staffCol.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));

        patientName.setOnAction(event -> {
            try {
                String[] patientSelection = patientName.getValue().toString().split(" ");
                String patientID = patientSelection[patientSelection.length - 1];
                ArrayList<PatientMove> selection = db.getPatientMovesByPatient(Integer.parseInt(patientID));
                moveTable.setItems(FXCollections.observableArrayList(selection));
                PatientMove currentMove = db.getCurrentPatientMove(Integer.parseInt(patientID));
                currentLocation.setText(currentMove.getLongName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
