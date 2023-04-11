package edu.wpi.teamR.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import org.controlsfx.control.SearchableComboBox;

public class FurnitureDeliveryController {

    @FXML MFXTextField nameField;
    @FXML MFXTextField staffMemberField;
    @FXML MFXTextField notesField;
    @FXML SearchableComboBox locationField;
    @FXML SearchableComboBox furnitureTypeField;

    @FXML public void initialize() {

    }


}
