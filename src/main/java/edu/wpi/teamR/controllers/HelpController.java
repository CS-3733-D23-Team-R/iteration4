package edu.wpi.teamR.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HelpController {

  @FXML Button userManualButton;

  @FXML
  public void initialize() {
    userManualButton.setOnMouseClicked(event -> viewUserManual());
  }

  @FXML
  public void close(Stage primaryStage) {
    primaryStage.close();
  }

  @FXML
  public void start(Stage primaryStage) {
    primaryStage.show();
  }

  private void viewUserManual() {
    String url = "https://docs.google.com/document/d/1v4bLPNDgx1ff8fVih4D98vuN3wGwO7FgsX2yB5DXQvU/edit?usp=sharing";

    try {
      Desktop.getDesktop().browse(new URI(url));
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }
}