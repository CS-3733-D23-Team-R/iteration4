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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestController {
    public static RequestType requestType = new RequestTypeMeal();
    @FXML Button cancelButton;
    @FXML Button resetButton;
    @FXML Button submitButton;
    @FXML MFXTextField nameField;
    @FXML SearchableComboBox locationBox;
    @FXML MFXTextField staffMemberField;
    @FXML MFXTextField notesField;
    @FXML SearchableComboBox typeBox;
    @FXML Text titleText;
    @FXML Text typeText;
    @FXML AnchorPane topAnchor;
    ObservableList<String> locList;
    ArrayList<String> tempList = new ArrayList<>();

    @FXML
    public void initialize() {
        titleText.setText(requestType.getTitleText());
        typeText.setText(requestType.getTypeText());
        topAnchor.getStyleClass().clear();
        topAnchor.getStyleClass().add(requestType.getStyle());
        try {
            MapDatabase mapDatabase = new MapDatabase();
            for(LocationName locationName: mapDatabase.getLocationNamesByNodeType("DEPT")){
                tempList.add(locationName.getShortName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        locList = FXCollections.observableArrayList(tempList);

        cancelButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
        resetButton.setOnMouseClicked(event -> clear());
        submitButton.setOnMouseClicked(event -> submit());
        typeBox.setValue(requestType.getDefaultText());
        typeBox.setItems(requestType.getItemList());
        locationBox.setValue("Select Location");
        locationBox.setItems(locList);
    }

    public Timestamp CurrentDateTime(){
        LocalDateTime now = LocalDateTime.now();
        return new Timestamp(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),now.getMinute(),now.getSecond(),now.getNano());
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

        try {
            requestType.addRequest(nameField.getText(), location, staffMemberField.getText(), notesField.getText(), CurrentDateTime(), RequestStatus.Unstarted, type);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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