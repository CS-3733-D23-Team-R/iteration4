package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.controlsfx.control.PopOver;

public class HomeController {

  @FXML BorderPane borderPane;
  @FXML
  Button mealButton;

  private static Parent root;

  @FXML
  public void initialize() {
    mealButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP_EDITOR));
  }
}
