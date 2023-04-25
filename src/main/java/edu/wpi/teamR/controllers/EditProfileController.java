package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class EditProfileController {
    @FXML ImageView currentImage, editImage;
    @FXML MFXTextField nameField, phoneField, emailField;
    @FXML VBox profileCardContainer;
    @FXML Button backButton, applyButton;
    @FXML Text errorText;
    @FXML MFXPasswordField currentPasswordField, newPasswordField, retypePasswordField;
    public void initialize() throws SQLException, ItemNotFoundException {
        errorText.setVisible(false);
        editImage.setVisible(false);
        CurrentUser currentUser = UserData.getInstance().getLoggedIn();
        Image image1 = new Image(UserData.getInstance().getLoggedIn().getProfilePictureLocation());
        User backendUser = new UserDatabase().getUserByUsername(currentUser.getUsername());
        currentImage.setImage(image1);
        displayProfile(backendUser);
        if(currentUser.getAccessLevel().equals(AccessLevel.Admin)){
            backButton.setOnMouseClicked(event -> {Navigation.navigate(Screen.ADMINPROFILEPAGE);});
        } else {
            backButton.setOnMouseClicked(event -> {Navigation.navigate(Screen.STAFFPROFILEPAGE);});
        }
        applyButton.setOnMouseClicked(event -> {
            try {
                checkFields(currentUser, backendUser);
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private Node loadCard(User user) throws IOException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/Profile.fxml"));
        Node node = loader.load();
        ProfileController contentController = loader.getController();
        contentController.setInfo(user);
        return node;
    }

    private void displayProfile(User user){
        profileCardContainer.getChildren().clear();
        try {
            profileCardContainer.getChildren().add(loadCard(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFields(CurrentUser currentUser, User backendUser) throws SQLException, ItemNotFoundException {
        errorText.setVisible(false);
        if(currentPasswordField.getText().isBlank()){
            errorText.setText("NEED TO RETYPE OLD PASSWORD TO CONFIRM! (good try)");
            errorText.setVisible(true);
        } else if (!newPasswordField.getText().equals(retypePasswordField.getText())) {
            errorText.setText("PASSWORDS DO NOT MATCH! (did you even check?)");
            errorText.setVisible(true);
        } else if (nameField.getText().isBlank() && phoneField.getText().isBlank() && emailField.getText().isBlank() && newPasswordField.getText().isBlank()) {
            errorText.setText("YOU DIDN'T CHANGE ANYTHING! (why are you here)");
            errorText.setVisible(true);
        } else if (newPasswordField.getText().length() < 5) {
            errorText.setText("PASSWORD TOO SHORT (too easy to guess)");
            errorText.setVisible(true);
        } else if (phoneField.getText().length() < 10) {
            errorText.setText("PHONE NUMBER TOO SHORT");
            errorText.setVisible(true);
        } else if (phoneField.getText().length() > 10) {
            errorText.setText("PHONE NUMBER TOO SHORT");
            errorText.setVisible(true);
        } else if (!new UserDatabase().verifyUser(currentUser.getUsername(), currentPasswordField.getText())) {
            errorText.setText("OLD PASSWORD WRONG TRY AGAIN!");
            errorText.setVisible(true);
        } else { //TODO: make sure strings are letters and phonenum is a num
            UserDatabase accessToDatabase = new UserDatabase();
            String name;
            String phonenum;
            String email;
            String password;
            if(!nameField.getText().isBlank()){
                name = nameField.getText();
            } else {
                name = backendUser.getName();
            }
            if(!phoneField.getText().isBlank()){
                phonenum = phoneField.getText();
            } else {
                phonenum = backendUser.getPhoneNum();
            }
            if(!emailField.getText().isBlank()){
                email = emailField.getText();
            } else {
                email = backendUser.getEmail();
            }
            if(!newPasswordField.getText().isBlank()){
                password = newPasswordField.getText();
            } else {
                password = currentPasswordField.getText();
            }
            accessToDatabase.modifyUserByUsername(currentUser.getUsername(), password, name, email, currentUser.getJobTitle(), phonenum, currentUser.getJoinDate(), currentUser.getAccessLevel(), currentUser.getDepartment(), backendUser.getImageID());
            currentUser.refreshUser();
            displayProfile(backendUser);
        }
    }

}
