package edu.wpi.teamR.controllers;

import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.AlertDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;

import edu.wpi.teamR.userData.UserData;
import io.github.gleidsonmt.dashboardfx.core.interfaces.Root;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeController {

  @FXML BorderPane borderPane;
  @FXML Button loginButton;
  @FXML
  Text time;
  @FXML VBox alertsVBox;

  private static Parent root;

  UserDatabase userDatabase = new UserDatabase();
  List<Alert> alerts;

  SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

  @FXML Text aboutText;
  @FXML Text creditsText;

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

     alerts = userDatabase.getCurrentAlerts();

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

      creditsText.setOnMouseClicked(event -> {Navigation.navigate(Screen.CREDITS);});
      aboutText.setOnMouseClicked(event -> {Navigation.navigate(Screen.ABOUT);});
      if(UserData.getInstance().isLoggedIn()){
          loginButton.setVisible(false);
      }
      RootController rootPane = RootController.getInstance();
      rootPane.showSidebar();
  }

  private HBox alertView(Alert alert){
      HBox layout = new HBox();
      HBox leftBox = new HBox();
      HBox rightBox = new HBox();
      leftBox.setAlignment(Pos.CENTER_LEFT);
      rightBox.setAlignment(Pos.CENTER_RIGHT);
      HBox.setHgrow(leftBox,Priority.ALWAYS);
      HBox.setHgrow(rightBox, Priority.ALWAYS);
      Text message = new Text(alert.getMessage());
      message.setFill(Color.WHITE);
      message.setStyle("-fx-font-size: 18;");
      Text date = new Text(dateFormat.format(alert.getStartDate()));
      date.setFill(Color.WHITE);
      date.setStyle("-fx-font-size: 20;");
      leftBox.getChildren().add(message);
      rightBox.getChildren().add(date);
      layout.setSpacing(100);
      layout.getChildren().addAll(leftBox, rightBox);
      return layout;
  }

}
