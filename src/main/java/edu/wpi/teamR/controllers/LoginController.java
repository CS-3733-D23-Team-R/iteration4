package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.userData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import java.sql.SQLException;
import edu.wpi.teamR.userData.userData;
import edu.wpi.teamR.userData.currentUser;

public class LoginController {

    @FXML MFXTextField usernameField;
    @FXML MFXTextField passwordField;
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
        AuthenticationDAO authenticationDAO = AuthenticationDAO.getInstance();
        userData thisUserData = edu.wpi.teamR.userData.userData.getInstance();
        User aUser = authenticationDAO.getUserByUsername(usernameField.getText());
        currentUser User = new currentUser(aUser.getStaffUsername(), aUser.getPassword(), aUser.getAccessLevel()); //TODO: update this with alton's new user class
        thisUserData.setLoggedIn(User);
        System.out.println(usernameField.getText());
        System.out.println(passwordField.getText());
        if(User.comparePass(passwordField.getText())){
            if(User.getAccessLevel() == AccessLevel.Admin){
                Navigation.navigate(Screen.ADMINPROFILEPAGE);
            } else if(User.getAccessLevel() == AccessLevel.Staff){
                Navigation.navigate(Screen.STAFFPROFILEPAGE);
            }
        } else{
            System.out.println("incorrect password");
        }
    }
}

