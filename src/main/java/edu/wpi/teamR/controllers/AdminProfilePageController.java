package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AdminProfilePageController {
    @FXML Text name, email, occupation, DateOfJoining, phone, time;
    @FXML VBox thread;
    ImageView ProfilePicture;
    @FXML Button ToMapEditor, ToEmployeeManager;
    @FXML
    public void initialize(){
        ToMapEditor.setOnAction(event -> Navigation.navigate(Screen.MAP_EDITOR));
        
    }
}
