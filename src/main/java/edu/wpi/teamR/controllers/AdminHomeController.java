package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AdminHomeController {

    @FXML
    Text signagePage;


    @FXML
    public void initialize() {
        signagePage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE));
    }

}
