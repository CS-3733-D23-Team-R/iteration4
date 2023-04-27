package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class EditProfileController {
    @FXML ImageView currentImage, editImage;
    @FXML MFXTextField nameField, phoneField, emailField;
    @FXML VBox profileCardContainer, entireAccountInformation;
    @FXML Button backButton, applyButton;
    @FXML Text errorText;
    @FXML MFXPasswordField currentPasswordField, newPasswordField, retypePasswordField;
    int imageID;
    public void initialize() throws SQLException, ItemNotFoundException {
        errorText.setVisible(false);
        editImage.setVisible(false);
        phoneField.setTextLimit(10);
        CurrentUser currentUser = UserData.getInstance().getLoggedIn();
        Image myImage = new Image(UserData.getInstance().getLoggedIn().getProfilePictureLocation());
        User backendUser = new UserDatabase().getUserByUsername(currentUser.getUsername());
        nameField.setText(currentUser.getFullName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(String.valueOf(currentUser.getPhoneNum()));
        currentImage.setImage(myImage);
        imageID = backendUser.getImageID();
        displayProfile(backendUser);
        if(currentUser.getAccessLevel().equals(AccessLevel.Admin)){
            backButton.setOnMouseClicked(event -> {Navigation.navigate(Screen.ADMINPROFILEPAGE);});
        } else {
            backButton.setOnMouseClicked(event -> {Navigation.navigate(Screen.STAFFPROFILEPAGE);});
        }
        applyButton.setOnMouseClicked(event -> {
            try {
                checkFields(currentUser, backendUser);
                RootController root = RootController.getInstance();
                root.setProfileIcon(UserDatabase.getProfilePictureFromID(imageID));
                if(currentUser.getAccessLevel().equals(AccessLevel.Admin)){
                    Navigation.navigate(Screen.ADMINPROFILEPAGE);
                } else {
                    Navigation.navigate(Screen.STAFFPROFILEPAGE);
                }
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        currentImage.setOnMouseEntered(event -> {
            editImage.setVisible(true);
        });

        editImage.setOnMouseEntered(event -> {
            Image invertedEditButton = new Image(Objects.requireNonNull(Main.class.getResource("images/login/invertededitbutton.png")).toExternalForm());
            editImage.setImage(invertedEditButton);
            editImage.setVisible(true);
        });
        currentImage.setOnMouseExited(event -> {
            editImage.setVisible(false);
        });
        editImage.setOnMouseExited(event -> {
            editImage.setVisible(false);
        });
        editImage.setOnMouseClicked(event -> {
            PopOver popOver = new PopOver();
            AnchorPane content;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/ProfilePicture.fxml"));
            try {
                content = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ProfilePictureController controller = loader.getController();

            popOver.setContentNode(content);

            content.setOnMouseClicked(evt -> {
                if (evt.getTarget() instanceof ImageView) {
                    if (controller.isImageSet()) {
                        imageID = controller.getImageID();
                        currentImage.setImage(controller.getReturnImage());
                        System.out.println(controller.getImageID());
                        backendUser.setImageID(controller.getImageID());
                        displayProfile(backendUser);
                        popOver.hide();
                    }
                }
            });

            popOver.setAutoHide(true);

            popOver.show(currentImage);
        });

        nameField.textProperty().addListener((observable,oldValue,newValue) -> {
            backendUser.setName(newValue);
            displayProfile(backendUser);
        });
        phoneField.textProperty().addListener((observable,oldValue,newValue) -> {
            backendUser.setPhoneNum(newValue);
            displayProfile(backendUser);
        });
        emailField.textProperty().addListener((observable,oldValue,newValue) -> {
            backendUser.setEmail(newValue);
            displayProfile(backendUser);
        });

    }

    private Node loadCard(User user) throws IOException {
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
        errorText.setFill(Color.RED);
        if(currentPasswordField.getText().isBlank()) {
            errorText.setText("NEED TO RETYPE OLD PASSWORD TO CONFIRM! (good try)");
            errorText.setVisible(true);
        } else if (!newPasswordField.getText().equals(retypePasswordField.getText())) {
            errorText.setText("NEW PASSWORDS DO NOT MATCH! (did you even check?)");
            errorText.setVisible(true);
        } else if (nameField.getText().isBlank() && phoneField.getText().isBlank() && emailField.getText().isBlank() && newPasswordField.getText().isBlank()) {
            errorText.setText("YOU DIDN'T CHANGE ANYTHING! (why are you here)");
            errorText.setVisible(true);
        } else if (newPasswordField.getText().length() < 5 && !newPasswordField.getText().isBlank()) {
            errorText.setText("PASSWORD TOO SHORT (too easy to guess)");
            errorText.setVisible(true);
        } else if (phoneField.getText().length() > 10) {
            errorText.setText("PHONE NUMBER TOO LONG");
            errorText.setVisible(true);
        } else if (!new UserDatabase().verifyUser(currentUser.getUsername(), currentPasswordField.getText())) {
            errorText.setText("OLD PASSWORD WRONG TRY AGAIN!");
            errorText.setVisible(true);
        } else {
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
            accessToDatabase.modifyUserByUsername(currentUser.getUsername(), name, email, currentUser.getJobTitle(), phonenum, currentUser.getJoinDate(), currentUser.getAccessLevel(), currentUser.getDepartment(), imageID);
            accessToDatabase.modifyUserPasswordByUsername(currentUser.getUsername(), password);
            currentUser.refreshUser();
            displayProfile(backendUser);
            errorText.setFill(Color.GREEN);
            errorText.setText("SUCCESS!");
            errorText.setVisible(true);
            FadeTransition ft1 = new FadeTransition(Duration.seconds(1), errorText);
            ft1.setFromValue(0);
            ft1.setToValue(1);
            FadeTransition ft2 = new FadeTransition(Duration.seconds(1), errorText);
            ft2.setFromValue(1.0);
            ft2.setToValue(0);
            ft1.setOnFinished(event -> {ft2.play();});
            ft1.play();
            currentPasswordField.setText("");
            newPasswordField.setText("");
            retypePasswordField.setText("");
            nameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
            phoneField.setText(String.valueOf(currentUser.getPhoneNum()));
        }
    }
}
