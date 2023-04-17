package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SignageConfigurationController {
    public void start(Stage primaryStage) {
        primaryStage.show();
    }

    @FXML Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE));
    }
}
