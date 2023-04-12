package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.FurnitureRequest;
import edu.wpi.teamR.requestdb.MealRequest;
import edu.wpi.teamR.requestdb.RequestStatus;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.controlsfx.control.SearchableComboBox;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MealDeliveryController {

    @FXML Button submitButton;
    @FXML Button resetButton;
    @FXML Button cancelButton;
    @FXML MFXTextField nameField;
    @FXML MFXTextField staffMemberField;
    @FXML MFXTextField notesField;
    @FXML SearchableComboBox locationField;
    @FXML SearchableComboBox mealTypeField;

    @FXML public void initialize() {
        cancelButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
        resetButton.setOnMouseClicked(event -> clear());
        submitButton.setOnMouseClicked(event -> submit());

        mealTypeField.setValue("Select Furniture");
        locationField.setValue("Select Your Location");
    }

    public Timestamp CurrentDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        return new Timestamp(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),now.getMinute(),now.getSecond(),now.getNano());
    }

    @FXML
    public void clear() {
        nameField.clear();
        staffMemberField.clear();
        notesField.clear();

        mealTypeField.setValue("Select Meal");
        locationField.setValue("Select Your Location");
    }

    @FXML
    public void submit() {
        String mealType = mealTypeField.getValue().toString();
        String location = locationField.getValue().toString();

        if (mealType == "Select Meal") {
            mealType = "";
        }

        if (location == "Select Your Location") {
            location = "";
        }

        Navigation.navigate(Screen.HOME);
        int id = 0;
        ArrayList<MealRequest> foodList = MealRequestDAO.getInstance().getFoodRequests();
        for(MealRequest foodRequest: foodList){
            if(id < foodRequest.getRequestID()){
                id = foodRequest.getRequestID();
            }
        }
        ArrayList<FurnitureRequest> furnList = FurnitureRequestDAO.getInstance().getFurnitureRequests();
        for(FurnitureRequest furnitureRequest: furnList){
            if(id < furnitureRequest.getRequestID()){
                id = furnitureRequest.getRequestID();
            }
        }
        id++;
        try {
            MealRequestDAO.getInstance().addFoodRequest(id, nameField.getText(), location, mealType, staffMemberField.getText(), notesField.getText(), CurrentDateTime(), RequestStatus.Unstarted);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
