package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.currentUser;
import edu.wpi.teamR.userData.userData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminProfilePageController {
    @FXML Text name, email, occupation, DateOfJoining, phone, time;
    @FXML VBox thread;
    ImageView ProfilePicture;
    @FXML Button ToMapEditor, ToEmployeeManager;
    @FXML
    public void initialize(){
        userData thisUserData = userData.getInstance();
        currentUser user = thisUserData.getLoggedIn();
        name.setText(user.getFullName());
        email.setText(user.getEmail());
        occupation.setText(user.getJobTitle());
        DateOfJoining.setText(user.getJoinDate().toString());
        phone.setText(Integer.toString(user.getPhoneNum()));
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formattedDate = date.format(dateTimeFormatter);

        Timeline timeline =
                new Timeline(
                        new KeyFrame(
                                Duration.seconds(0),
                                event -> {
                                    LocalDateTime currentTime = LocalDateTime.now();
                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a");
                                    String formattedTime = currentTime.format(timeFormatter);
                                    String formattedDateTime = formattedTime + ", " + formattedDate;
                                    time.setText(formattedDateTime);
                                }),
                        new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        time.setText(formattedDate);
        ToMapEditor.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP_EDITOR));
        ToEmployeeManager.setOnMouseClicked(event -> {Navigation.navigate(Screen.EMPLOYEEMANAGER);});
    }
}
