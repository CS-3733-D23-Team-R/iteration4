package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SignageConfigurationController {
    public void start(Stage primaryStage) {
        primaryStage.show();
    }

    @FXML Button backButton;
    @FXML TableView configurationTable;
    @FXML TableColumn directionColumn;
    @FXML TableColumn locationColumn;
    @FXML TableColumn iconColumn;
    @FXML TableColumn saveColumn;

    @FXML
    public void initialize() {
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE));
    }
}
