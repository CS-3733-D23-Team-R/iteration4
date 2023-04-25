package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import static edu.wpi.teamR.navigation.Navigation.navigate;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML MFXPasswordField passwordField;
    @FXML MFXButton loginButton;

    @FXML
    public void initialize() {
        loginButton.setOnMouseClicked(event -> {
            try {
                checkLogIn();
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                e.printStackTrace();
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                try{
                    checkLogIn();
                } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    public void checkLogIn() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        UserData thisUserData = UserData.getInstance();
        UserDatabase thisUserDatabase = new UserDatabase();
        User aUser = thisUserDatabase.getUserByUsername(usernameField.getText());
        CurrentUser User = new CurrentUser(
                aUser.getStaffUsername(),
                aUser.getAccessLevel(),
                aUser.getName(),
                aUser.getEmail(),
                aUser.getDepartment(),
                aUser.getJoinDate(),
                Integer.parseInt(aUser.getPhoneNum()),
                aUser.getJobTitle(),
                aUser.getImageID()); //TODO: update this with alton's new user class, password hashed?
        thisUserData.setLoggedIn(User);
        if(thisUserDatabase.verifyUser(usernameField.getText(), passwordField.getText())){
            if(thisUserData.getLoggedIn().getAccessLevel() == AccessLevel.Admin){
                RootController.getInstance().setSignagePage();
                navigate(Screen.ADMINPROFILEPAGE);
            } else if(thisUserData.getLoggedIn().getAccessLevel() == AccessLevel.Staff){
                navigate(Screen.STAFFPROFILEPAGE);
            }
        } else{
            System.out.println("Incorrect Password");
        }
    }

}

