package edu.wpi.teamR.controllers;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.RequestStatus;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.controlsfx.control.SearchableComboBox;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestController {
    RequestType requestType = new RequestTypeFlower();
    @FXML Button cancelButton;
    @FXML Button resetButton;
    @FXML Button submitButton;
    @FXML MFXTextField nameField;
    @FXML SearchableComboBox locationBox;
    @FXML MFXTextField staffMemberField;
    @FXML MFXTextField notesField;
    @FXML SearchableComboBox typeBox;


    ObservableList<String> locList;

    ArrayList<String> tempList;


    @FXML
    public void initialize() {
        try {
            MapDatabase mapDatabase = new MapDatabase();
            for(LocationName locationName: mapDatabase.getLocationNamesByNodeType("DEPT")){
                tempList.add(locationName.getShortName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cancelButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
        resetButton.setOnMouseClicked(event -> clear());
        submitButton.setOnMouseClicked(event -> submit());
        typeBox.setValue(requestType.getDefaultText());
        typeBox.setItems(requestType.getItemList());
    }

    public Timestamp CurrentDateTime(){
        LocalDateTime now = LocalDateTime.now();
        return new Timestamp(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),now.getMinute(),now.getSecond(),now.getNano());
    }

    public void setRequestType(RequestType requestType){
        this.requestType = requestType;
    }

    @FXML
    public void submit(){
        String type = typeBox.getValue().toString();
        if (type == requestType.getDefaultText()) {
            type = "";
        }
        String location = locationBox.getValue().toString();
        if (location == "Select Location") {
            location = "";
        }
        Navigation.navigate(Screen.HOME);
        requestType.addRequest(nameField.toString(), location, staffMemberField.toString(), notesField.toString(), CurrentDateTime(), RequestStatus.Unstarted, type);
    }

    @FXML
    public void clear() {
        nameField.clear();
        staffMemberField.clear();
        notesField.clear();
        typeBox.setValue(requestType.getDefaultText());
        locationBox.setValue("Select Location");
    }
}