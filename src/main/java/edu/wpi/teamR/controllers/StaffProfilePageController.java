package edu.wpi.teamR.controllers;

import edu.wpi.teamR.requestdb.RequestDatabase;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StaffProfilePageController {
    @FXML Text name, email, occupation, DateOfJoining, phone, time;
    @FXML VBox monday, tuesday, wednesday, thursday, friday, saturday, sunday, thread;

    ImageView CreateNewMessage;

    public void initialize(){
    }
}
