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
import java.util.Objects;

import static edu.wpi.teamR.navigation.Navigation.navigate;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML MFXPasswordField passwordField;
    @FXML MFXButton loginButton;
    @FXML Text errorText;

    @FXML
    public void initialize() {
        errorText.setVisible(false);
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
        UserDatabase thisUserDatabase = new UserDatabase();
        if(thisUserDatabase.verifyUser(usernameField.getText(), passwordField.getText())){
            UserData thisUserData = UserData.getInstance();
            User aUser = thisUserDatabase.getUserByUsername(usernameField.getText());
            CurrentUser User = new CurrentUser(
                    aUser.getStaffUsername(),
                    aUser.getAccessLevel(),
                    aUser.getName(),
                    aUser.getEmail(),
                    aUser.getDepartment(),
                    aUser.getJoinDate(),
                    Long.parseLong(aUser.getPhoneNum()),
                    aUser.getJobTitle(),
                    aUser.getImageID());
            thisUserData.setLoggedIn(User);

            RootController root = RootController.getInstance();
            root.setLogoutButton(true);
            root.setProfileIcon(User.getProfilePictureLocation());

            if(User.getAccessLevel().toString().equals(AccessLevel.Admin.toString())){
                RootController.getInstance().setSignagePage();
                navigate(Screen.ADMINPROFILEPAGE);
            } else if(User.getAccessLevel().toString().equals(AccessLevel.Staff.toString())){
                navigate(Screen.STAFFPROFILEPAGE);
            }
        } else{
            System.out.println("Incorrect Password");
            errorText.setText("INCORRECT PASSWORD");
            errorText.setVisible(true);
        }
    }

}

