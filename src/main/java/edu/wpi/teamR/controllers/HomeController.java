package edu.wpi.teamR.controllers;

import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.AlertDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;

import edu.wpi.teamR.userData.UserData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class HomeController {

  @FXML BorderPane borderPane;
  @FXML Button loginButton;
  @FXML
  Text time;
  @FXML VBox alertsVBox;

  private static Parent root;

  UserDatabase userDatabase = new UserDatabase();
  ArrayList<Alert> alerts;

  @FXML
  public void initialize() throws SQLException {
    loginButton.setOnAction(event -> {
        if (!UserData.getInstance().isLoggedIn())
            Navigation.navigate(Screen.LOGIN);
    });

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

     alerts = userDatabase.getAlerts();

      if(alerts.isEmpty()){
          Text emptyLabel = new Text("No New Announcements");
          emptyLabel.setFill(Color.WHITE);
          alertsVBox.getChildren().add(emptyLabel);
      }else {
          alerts.forEach(
                  (alert) -> {
                      HBox alerts = alertView(alert);
                      alertsVBox.getChildren().add(alerts);
                  }
          );
      }

  }

  private HBox alertView(Alert alert){
      HBox layout = new HBox();
      layout.setAlignment(Pos.CENTER_LEFT);
      Text message = new Text(alert.getMessage());
      message.setFill(Color.WHITE);
      message.setStyle("-fx-font-size: 18;");
      //message.setStyle("-fx-margin-left: 30px;");
      message.setStyle("fx-border-insets: 30px");
      Text date = new Text(alert.getTime().toString());
      date.setFill(Color.WHITE);
      date.setStyle("-fx-font-size: 20;");
      //date.setStyle("-fx-margin-left: 30px;");
      layout.getChildren().addAll(message, date);
      return layout;
  }

}
