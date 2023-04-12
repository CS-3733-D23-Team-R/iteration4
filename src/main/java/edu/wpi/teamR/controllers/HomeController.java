package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;

import java.awt.*;
import java.io.IOException;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import org.controlsfx.control.PopOver;
import javafx.scene.control.Button;

public class HomeController {

  @FXML BorderPane borderPane;
  @FXML Button loginButton;

  private static Parent root;

  @FXML
  public void initialize() {
    loginButton.setOnAction(event -> Navigation.navigate(Screen.LOGIN));

  }
}
