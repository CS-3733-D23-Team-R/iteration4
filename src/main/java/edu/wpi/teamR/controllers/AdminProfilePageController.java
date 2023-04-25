package edu.wpi.teamR.controllers;

import edu.wpi.teamR.login.User;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminProfilePageController {
    @FXML
    ImageView ProfilePicture;
    @FXML
    Text time;
    @FXML Button toEmployeeManager, toServiceRequests, toConferenceRooms, toSignageConfiguration, createNewAlert, toEditMap;
    @FXML VBox profileCardContainer;
    @FXML StackPane conferenceRoomImage, signageConfigurationImage, createAlertImage, allServiceRequestsImage, employeeManagementImage;
    @FXML
    public void initialize(){
        UserData thisUserData = UserData.getInstance();
        CurrentUser user = thisUserData.getLoggedIn();

        toConferenceRooms.setVisible(false);
        conferenceRoomImage.setOnMouseEntered(event -> {toConferenceRooms.setVisible(true);});
        conferenceRoomImage.setOnMouseExited(event -> {toConferenceRooms.setVisible(false);});

        toEmployeeManager.setVisible(false);
        employeeManagementImage.setOnMouseEntered(event -> {toEmployeeManager.setVisible(true);});
        employeeManagementImage.setOnMouseExited(event -> {toEmployeeManager.setVisible(false);});

        toServiceRequests.setVisible(false);
        allServiceRequestsImage.setOnMouseEntered(event -> {toServiceRequests.setVisible(true);});
        allServiceRequestsImage.setOnMouseExited(event -> {toServiceRequests.setVisible(false);});

        toSignageConfiguration.setVisible(false);
        signageConfigurationImage.setOnMouseEntered(event -> {toSignageConfiguration.setVisible(true);});
        signageConfigurationImage.setOnMouseExited(event -> {toSignageConfiguration.setVisible(false);});

        createNewAlert.setVisible(false);
        createAlertImage.setOnMouseEntered(event -> {createNewAlert.setVisible(true);});
        createAlertImage.setOnMouseExited(event -> {createNewAlert.setVisible(false);});
        createNewAlert.setOnMouseClicked(event -> {
            try {
                newAlert();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        toServiceRequests.setOnMouseClicked(event -> {Navigation.navigate(Screen.SORT_ORDERS);});
        toConferenceRooms.setOnMouseClicked(event -> {Navigation.navigate(Screen.ROOM_REQUEST);});
        toEditMap.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP_EDITOR));
        toEmployeeManager.setOnMouseClicked(event -> {Navigation.navigate(Screen.EMPLOYEEMANAGER);});
        toSignageConfiguration.setOnMouseClicked(event -> {Navigation.navigate(Screen.SIGNAGECONFIGURATION);});
        LocalDate date = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formattedDate = date.format(dateTimeFormatter);

        Timeline timeline =
                new Timeline(
                        new KeyFrame(
                                Duration.seconds(0),
                                event -> {
                                    LocalDateTime currentTime = LocalDateTime.now();
                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a");
                                    String formattedTime = currentTime.format(timeFormatter);
                                    String formattedDateTime = formattedTime + ", " + formattedDate;
                                    time.setText(formattedDateTime);
                                }),
                        new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        time.setText(formattedDate);
        displayProfile(UserData.getInstance().getLoggedIn());
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

    private void newAlert() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/Alerts.fxml"));
        //final BorderPane alert = loader.load();
        Parent popup;
        popup = loader.load();
        Stage alerts = new Stage();
        alerts.initModality(Modality.APPLICATION_MODAL);
        alerts.setTitle("Alerts");
        alerts.setScene(new Scene(popup, 400, 150));
        alerts.showAndWait();
        System.out.print("opened");
    }
}
