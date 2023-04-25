package edu.wpi.teamR.controllers;

import edu.wpi.teamR.login.AccessLevel;
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

public class EditProfileController {
    @FXML ImageView currentImage, editImage;
    @FXML MFXTextField nameField, phoneField, emailField;
    @FXML VBox profileCardContainer;
    @FXML Button backButton, applyButton;
    @FXML Text errorText;
    @FXML MFXPasswordField currentPasswordField, newPasswordField, retypePasswordField;
    public void initialize(){
        editImage.setVisible(false);
        CurrentUser aUser = UserData.getInstance().getLoggedIn();
        Image image1 = new Image(UserData.getInstance().getLoggedIn().getProfilePictureLocation());
        currentImage.setImage(image1);
        displayProfile(aUser);
        if(aUser.getAccessLevel().equals(AccessLevel.Admin)){
            backButton.setOnMouseClicked(event -> {Navigation.navigate(Screen.ADMINPROFILEPAGE);});
        } else {
            backButton.setOnMouseClicked(event -> {Navigation.navigate(Screen.STAFFPROFILEPAGE);});
        }
        applyButton.setOnMouseClicked(event -> {checkFields();});

    }

    private Node loadCard(CurrentUser user) throws IOException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/Profile.fxml"));
        Node node = loader.load();
        ProfileController contentController = loader.getController();
        contentController.setInfo(user);
        return node;
    }

    private void displayProfile(CurrentUser user){
        profileCardContainer.getChildren().clear();
        try {
            profileCardContainer.getChildren().add(loadCard(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFields(){

    }

}
