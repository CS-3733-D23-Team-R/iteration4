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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class EditProfileController {
    @FXML ImageView currentImage, editImage, oneImage, twoImage, threeImage, fourImage, fiveImage, sixImage, sevenImage, eightImage, nineImage, tenImage;
    @FXML MFXTextField nameField, phoneField, emailField;
    @FXML VBox profileCardContainer, pictureSelectorVBox, entireAccountInformation;
    @FXML Button backButton, applyButton;
    @FXML Text errorText;
    @FXML MFXPasswordField currentPasswordField, newPasswordField, retypePasswordField;
    int imageID;
    public void initialize() throws SQLException, ItemNotFoundException {
        Image image1 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/1.png")).toExternalForm());
        oneImage.setImage(image1);
        Image image2 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/2.png")).toExternalForm());
        twoImage.setImage(image2);
        Image image3 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/3.png")).toExternalForm());
        threeImage.setImage(image3);
        Image image4 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/4.png")).toExternalForm());
        fourImage.setImage(image4);
        Image image5 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/5.png")).toExternalForm());
        fiveImage.setImage(image5);
        Image image6 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/6.png")).toExternalForm());
        sixImage.setImage(image6);
        Image image7 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/7.png")).toExternalForm());
        sevenImage.setImage(image7);
        Image image8 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/8.png")).toExternalForm());
        eightImage.setImage(image8);
        Image image9 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/9.png")).toExternalForm());
        nineImage.setImage(image9);
        Image image10 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/10.png")).toExternalForm());
        tenImage.setImage(image10);
        pictureSelectorVBox.setVisible(false);
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
            pictureSelectorVBox.setVisible(true);
            editImage.setVisible(false);
        });
        oneImage.setOnMouseClicked(event -> {
            imageID = 1;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image1);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        twoImage.setOnMouseClicked(event -> {
            imageID = 2;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image2);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        threeImage.setOnMouseClicked(event -> {
            imageID = 3;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image3);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        fourImage.setOnMouseClicked(event -> {
            imageID = 4;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image4);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        fiveImage.setOnMouseClicked(event -> {
            imageID = 5;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image5);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        sixImage.setOnMouseClicked(event -> {
            imageID = 6;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image6);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        sevenImage.setOnMouseClicked(event -> {
            imageID = 7;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image7);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        eightImage.setOnMouseClicked(event -> {
            imageID = 8;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image8);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        nineImage.setOnMouseClicked(event -> {
            imageID = 9;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image9);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
        });
        tenImage.setOnMouseClicked(event -> {
            imageID = 10;
            pictureSelectorVBox.setVisible(false);
            currentImage.setImage(image10);
            backendUser.setImageID(imageID);
            displayProfile(backendUser);
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
        pictureSelectorVBox.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                pictureSelectorVBox.setVisible(false);
            }
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
