package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import java.awt.*;
import java.sql.SQLException;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML MFXTextField passwordField;
    @FXML MFXButton loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnMouseClicked(event -> {
            try {
                checkLogIn();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void checkLogIn() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        System.out.println(usernameField.getText());
        System.out.println(passwordField.getText());
        User user = AuthenticationDAO.getInstance().getUser(usernameField.getText());

        if (passwordField.getText().equals(user.getPassword())) {
            Navigation.navigate(Screen.MAP_EDITOR);
        }
    }

}

