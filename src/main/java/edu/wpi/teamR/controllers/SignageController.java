package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.DirectionArrow;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static edu.wpi.teamR.mapdb.Direction.*;

public class SignageController {
  public SignageController() throws SQLException, ClassNotFoundException {
  }

  public void start(Stage primaryStage) {
    primaryStage.show();
  }

  @FXML VBox signageBox;
  @FXML VBox alertBox;
  MapDatabase aMapDatabase = new MapDatabase();
  SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

  @FXML
  public void initialize() throws SQLException, ClassNotFoundException {
    UserDatabase userDatabase = new UserDatabase();
    List<Alert> alertsList = userDatabase.getCurrentAlerts();
    if(alertsList.isEmpty()){
      Text emptyLabel = new Text("No New Announcements");
      emptyLabel.setFill(Color.WHITE);
      alertBox.getChildren().add(emptyLabel);
    }else {
      alertsList.forEach(
              (alert) -> {
                HBox alerts = alertView(alert);
                alertBox.getChildren().add(alerts);
              }
      );
    }
    signageBox.setBackground(Background.fill(Color.web("#012D5A")));
    setVisible();
  }

  private void setVisible() throws SQLException, ClassNotFoundException {
    for(DirectionArrow directionArrow : aMapDatabase.getDirectionArrows()){
      HBox hBox = new HBox();
      Text text = new Text();
      ImageView imageView = new ImageView();

      Image line = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/signageLine.png")));
      ImageView lineView = new ImageView(line);
      lineView.setPreserveRatio(false);
      lineView.setFitHeight(40);
      lineView.setFitWidth(1);


      if(directionArrow.getDirection().equals(Up))
      {
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/upArrowWhite.png")));
        imageView = new ImageView(image);
      }
      else if(directionArrow.getDirection().equals(Down))
      {
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/downArrowWhite.png")));
        imageView = new ImageView(image);
      }
      else if(directionArrow.getDirection().equals(Left))
      {
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/leftArrowWhite.png")));
        imageView = new ImageView(image);
      }
      else if(directionArrow.getDirection().equals(Right))
      {
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/rightArrowWhite.png")));
        imageView = new ImageView(image);
      }
      else if(directionArrow.getDirection().equals(StopHere))
      {
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/stopHere.png")));
        imageView = new ImageView(image);
      }

      imageView.setPreserveRatio(false);
      imageView.setFitHeight(40);
      imageView.setFitWidth(40);

      text.setText(directionArrow.getLongName());
      text.setFont(Font.font(24));
      text.setFill(Color.WHITE);

      if(directionArrow.getDirection().equals(Right))
      {
        hBox.getChildren().add(text);
        hBox.getChildren().add(lineView);
        hBox.getChildren().add(imageView);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 10, 5, 0));
      }
      else if(directionArrow.getDirection().equals(StopHere))
      {
        hBox.getChildren().add(imageView);
        hBox.getChildren().add(lineView);
        hBox.getChildren().add(text);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5, 0, 5, 0));
      }
      else
      {
        hBox.getChildren().add(imageView);
        hBox.getChildren().add(lineView);
        hBox.getChildren().add(text);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 0, 5, 10));

      }

      hBox.setSpacing(25);
      signageBox.getChildren().add(hBox);
    }
    signageBox.setMaxHeight(489);
  }
  private HBox alertView(Alert alert){
    HBox layout = new HBox();
    HBox leftBox = new HBox();
    HBox rightBox = new HBox();
    leftBox.setAlignment(Pos.CENTER_LEFT);
    rightBox.setAlignment(Pos.CENTER_RIGHT);
    Insets insets = new Insets(20);
    rightBox.setPadding(insets);
    HBox.setHgrow(leftBox, Priority.ALWAYS);
    HBox.setHgrow(rightBox, Priority.ALWAYS);
    Text message = new Text(alert.getMessage());
    message.setStyle("-fx-font-size: 24;");
    Text date = new Text(dateFormat.format(alert.getStartDate()));
    date.setStyle("-fx-font-size: 24;");
    leftBox.getChildren().add(message);
    rightBox.getChildren().add(date);
    layout.getChildren().addAll(leftBox, rightBox);
    return layout;
  }
}