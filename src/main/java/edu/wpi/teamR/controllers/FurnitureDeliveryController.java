package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.controlsfx.control.SearchableComboBox;

public class FurnitureDeliveryController {

    @FXML Button submitButton;
    @FXML Button resetButton;
    @FXML Button cancelButton;
    @FXML MFXTextField nameField;
    @FXML MFXTextField staffMemberField;
    @FXML MFXTextField notesField;
    @FXML SearchableComboBox locationField;
    @FXML SearchableComboBox furnitureTypeField;

    @FXML public void initialize() {
        cancelButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    }


}
