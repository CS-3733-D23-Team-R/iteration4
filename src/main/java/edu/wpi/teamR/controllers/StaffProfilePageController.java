package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.*;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StaffProfilePageController {
    @FXML Text time;
    @FXML Button viewAllRequests, toConferenceRooms, toMovePatients, statsButton;
    @FXML VBox profileCardContainer;
    @FXML StackPane conferenceRoomImage;
    @FXML TableView<ItemRequest> table;
    @FXML TableColumn<ItemRequest, Integer> idCol, quantityCol;
    @FXML TableColumn<ItemRequest, String> requestTypeCol, nameCol, locationCol, notesCol, dateCol, statusCol, itemCol;
    private final ObservableList<ItemRequest> dataList = FXCollections.observableArrayList();
    ObservableList<RequestStatus> statusList = FXCollections.observableArrayList(RequestStatus.values());
    public void initialize() throws SQLException, ClassNotFoundException, SearchException {
        CurrentUser user = UserData.getInstance().getLoggedIn();
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
        toConferenceRooms.setOnAction(event -> Navigation.navigate(Screen.MY_ROOM_RESERVATIONS));
        statsButton.setOnAction(event -> {Navigation.navigate(Screen.DASHBOARD);});
        SearchList aSearchList = new SearchList();
        aSearchList.addComparison(RequestAttribute.staffUsername, Operation.equalTo, user.getUsername());
        dataList.addAll(new RequestDatabase().getItemRequestByAttributes(aSearchList));
        idCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        itemCol.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        statusCol.setCellFactory(column -> new TableCell<>(){
            private final MFXComboBox<RequestStatus> changeStatusButton = new MFXComboBox<>(statusList);

            {
                changeStatusButton.setMaxWidth(70);
                changeStatusButton.setOnAction(event -> {
                    ItemRequest request = getTableView().getItems().get(getIndex());
                    try {
                        RequestStatus status = changeStatusButton.getSelectionModel().getSelectedItem();
                        request.setRequestStatus(status);
                        new RequestDatabase().modifyItemRequestByID(
                                request.getRequestID(),
                                request.getRequestType(),
                                status,
                                request.getLongName(),
                                request.getStaffUsername(),
                                request.getItemType(),
                                request.getRequesterName(),
                                request.getAdditionalNotes(),
                                request.getRequestDate(),
                                request.getQuantity());
                    } catch (SQLException | ItemNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                } else{
                    ItemRequest request = getTableView().getItems().get(getIndex());
                    changeStatusButton.getSelectionModel().selectItem(request.getRequestStatus());
                    setGraphic(changeStatusButton);
                }
            }
        });
        for(ItemRequest request: new RequestDatabase().getItemRequests()){
            table.getItems().add(request);
        }
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
