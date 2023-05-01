package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.controllers.mapeditor.NewLocationPopupController;
import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
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
    @FXML Button toEmployeeManager, toServiceRequests, toConferenceRooms, toSignageConfiguration, toAlerts, toEditMap, toMovePatients;
    @FXML VBox profileCardContainer;
    @FXML StackPane conferenceRoomImage, signageConfigurationImage, createAlertImage, allServiceRequestsImage, employeeManagementImage;

    private final AnchorPane mapPane = new AnchorPane();

    @FXML
    GesturePane gesturePane;
    @FXML Button backupButton;

    @FXML
    public void initialize(){
        UserData thisUserData = UserData.getInstance();
        CurrentUser user = thisUserData.getLoggedIn();

        toAlerts.setOnMouseClicked(event -> { Navigation.navigate(Screen.ALERTS);});
        toServiceRequests.setOnMouseClicked(event -> {Navigation.navigate(Screen.SORT_ORDERS);});
        toConferenceRooms.setOnMouseClicked(event -> {Navigation.navigate(Screen.ROOM_REQUEST_MANAGER);});
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

        gesturePane.setContent(mapPane);
        mapPane.getChildren().add(MapStorage.getFirstFloor());
        gesturePane.setMinScale(0.25);
        gesturePane.setMaxScale(2);
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));

        backupButton.setOnMouseClicked(event -> {Navigation.navigate(Screen.VIEWDATA);});
        toMovePatients.setOnMouseClicked(event -> {Navigation.navigate(Screen.MOVEPATIENT);});
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

}
