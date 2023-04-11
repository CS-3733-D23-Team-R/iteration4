package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import java.awt.*;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML MFXTextField passwordField;
    @FXML MFXButton loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnMouseClicked(event -> checkLogIn());
    }

    @FXML
    public void checkLogIn() {
        System.out.println(usernameField.getText());
        System.out.println(passwordField.getText());

        /*if (usernameField.getText().equals("admin") && passwordField.getText().equals("admin")) {
            Navigation.navigate(Screen.);
        }*/
    }

}

