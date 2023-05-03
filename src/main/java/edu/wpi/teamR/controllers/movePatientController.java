package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.requestdb.Patient;
import edu.wpi.teamR.requestdb.PatientMove;
import edu.wpi.teamR.requestdb.RequestDatabase;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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
    ArrayList<String> times = new ArrayList<>();
    ObservableList<String> patientList, roomList, staffList, timeList;
    ObservableList<PatientMove> defaultTable;
    @FXML GesturePane miniMap;
    AnchorPane mapPane;
    @FXML MFXComboBox patientName, patientDestination, staffMember, moveTime;
    @FXML MFXTextField currentLocation;
    @FXML MFXDatePicker moveDate;
    @FXML TableView<PatientMove> moveTable;
    @FXML TableColumn<PatientMove, String> locCol, staffCol;
    @FXML TableColumn<PatientMove, Integer> idCol;
    @FXML TableColumn<PatientMove, Timestamp> dateCol;
    @FXML Button clearButton, submitButton;

    public void initialize() throws SQLException, ItemNotFoundException {
        db = new RequestDatabase();
        udb = new UserDatabase();
        mapPane = new AnchorPane();
        mapPane.getChildren().add(MapStorage.getFirstFloor());
        miniMap.setContent(mapPane);
        miniMap.setMinScale(0.2);
        miniMap.zoomTo(0.2, new Point2D(2500, 1700));

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

        String addable = "";
        for(int i = 0; i <= 23; i++)
            for(int j = 0; j < 60; j += 5) {
                addable = "";
                if(i < 10)
                    addable = "0" + i + ":";
                else
                    addable = i + ":";
                if(j < 10)
                    addable = addable + "0" + j;
                else
                    addable = addable + j;
                times.add(addable);
            }
        timeList = FXCollections.observableArrayList(times);
        moveTime.setItems(timeList);

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
        submitButton.setOnMouseClicked(event -> {
            while(patientName.getValue() == null || patientDestination == null || staffMember == null || moveDate == null)
                continue;
            String[] pSelect = patientName.getValue().toString().split(" ");
            int pID = Integer.parseInt(pSelect[pSelect.length - 1]);
            String pDestination = patientDestination.getValue().toString();
            String[] sSelect = staffMember.getValue().toString().split(" ");
            String sUsername = sSelect[sSelect.length - 1];
            LocalDate dSelect = moveDate.getValue();
            String[] time = moveTime.getValue().toString().split(":");
            Timestamp dTime = new Timestamp(dSelect.getYear() - 1900,dSelect.getMonthValue() - 1, dSelect.getDayOfMonth(), Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0, 0);
            try {
                db.addPatientMove(pID, dTime, pDestination, sUsername);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        clearButton.setOnMouseClicked(event -> {
            patientName.clearSelection();
            patientDestination.clearSelection();
            staffMember.clearSelection();
            currentLocation.clear();
            moveDate.clear();
            moveTime.clearSelection();
            moveTable.setItems(defaultTable);
        });
    }
}
