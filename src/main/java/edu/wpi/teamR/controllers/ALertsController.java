package edu.wpi.teamR.controllers;

import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.UserDatabase;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Timestamp;

public class ALertsController {

    @FXML
    BorderPane alertPopup;
    @FXML
    MFXTextField messageField;
    @FXML
    MFXDatePicker datePicker;
    @FXML
    MFXButton submitButton;

    UserDatabase alerts = new UserDatabase();

    @FXML
    public void initialize() {
        submitButton.setOnMouseClicked(event -> {
            try {
                submitAlert();
                close((Stage) submitButton.getScene().getWindow());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void submitAlert() throws SQLException {
        alerts.addAlert(messageField.getText(), Timestamp.valueOf(datePicker.getCurrentDate().atStartOfDay()));
    }

    @FXML public void close(Stage primaryStage){
        primaryStage.close();
    }

}
