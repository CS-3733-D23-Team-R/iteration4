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

import java.awt.event.KeyEvent;
import java.sql.SQLException;

import static edu.wpi.teamR.navigation.Navigation.navigate;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML
    MFXPasswordField passwordField;
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
    }

    @FXML
    public void checkLogIn() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        UserData thisUserData = UserData.getInstance();
        User aUser = new UserDatabase().getUserByUsername(usernameField.getText());
        CurrentUser User = new CurrentUser(aUser.getStaffUsername(), aUser.getPassword(), aUser.getAccessLevel(), aUser.getName(), aUser.getEmail(), aUser.getDepartment(), aUser.getJoinDate(), Integer.parseInt(aUser.getPhoneNum()), aUser.getJobTitle()); //TODO: update this with alton's new user class
        thisUserData.setLoggedIn(User);
        if(User.comparePass(passwordField.getText())){
            if(thisUserData.getLoggedIn().getAccessLevel() == AccessLevel.Admin){
                RootController.getInstance().setSignagePage();
                navigate(Screen.ADMINPROFILEPAGE);
            } else if(thisUserData.getLoggedIn().getAccessLevel() == AccessLevel.Staff){
                navigate(Screen.STAFFPROFILEPAGE);
            }
        } else{
            System.out.println("incorrect password");
        }
    }

}

