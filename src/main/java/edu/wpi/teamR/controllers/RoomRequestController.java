package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.mapdb.ConferenceRoom;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RoomRequest;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
//    SELECT * FROM iteration2.conferenceroom;
public class RoomRequestController {
    @FXML MFXButton submitButton;
    @FXML MFXButton resetButton;
    @FXML DatePicker datePicker;
    @FXML MFXScrollPane scrollPane;
    @FXML VBox cardsContainer;
    @FXML MFXComboBox locationBox;
    @FXML MFXComboBox startTimeBox;
    @FXML MFXComboBox endTimeBox;
    @FXML MFXToggleButton plugToggle;
    @FXML MFXToggleButton screenToggle;
    @FXML MFXToggleButton wheelchairToggle;

    ArrayList<String> timeArray = new ArrayList<>();
    MapDatabase mapDatabase;
    String start;
    String end;
    LocalDate date;
    String location;
    Timestamp startTime;
    Timestamp endTime;


    {
        try {
            mapDatabase = new MapDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException, ItemNotFoundException, IOException {
        fillArray();
        ArrayList<String> locNames = new ArrayList<>();
        locNames.add("Select Location- Optional");
        for(ConferenceRoom conferenceRoom: mapDatabase.getConferenceRooms()){
            locNames.add(conferenceRoom.getLongname());
        }
        scrollPane.setFitToWidth(true);
        Text text = new Text("Select Desired Room Specifications");
        text.getStyleClass().add("title");
        cardsContainer.getChildren().add(text);
        ObservableList<String> startTimeList = FXCollections.observableArrayList(timeArray);
        ObservableList<String> locList = FXCollections.observableArrayList(locNames);
        startTimeBox.setValue("Start Time");
        endTimeBox.setValue("End Time");
        startTimeBox.setItems(startTimeList);
        startTimeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<String> endTimeList = new ArrayList<>(timeArray.subList(timeArray.indexOf(startTimeBox.getValue()) + 1, timeArray.size() -1));
            endTimeBox.setItems(FXCollections.observableArrayList(endTimeList));
        });
        locationBox.setValue("Select Location- Optional");
        locationBox.setItems(locList);
        submitButton.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        resetButton.setOnMouseClicked(event -> reset());
    }

    private Node loadCard(ConferenceRoom item) throws IOException, SQLException, ClassNotFoundException, ItemNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/RoomCard.fxml"));
        Node node = loader.load();
        RoomCard contentController = loader.getController();
        contentController.setInfo(item.getLongname(), "Capacity: "+ item.getCapacity(), "Floor: " + mapDatabase.getNodeFromLocationName(item.getLongname()).getFloorNum());
        addDisplayFeatures(item, contentController.getFeatureBox());
        Button btn = contentController.getButton();
        btn.setOnAction(event -> {
            btn.getStyleClass().add("reserveButtonConfirm");
            btn.setText(null);
            ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/checked.png")).toExternalForm());
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(28);
            btn.setGraphic(imageView);
            btn.setOnAction(null);
            try {
                new RequestDatabase().addRoomRequest(item.getLongname(), UserData.getInstance().getLoggedIn().getUsername(), startTime, endTime);
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        return node;
    }

    void submit() throws SQLException, ClassNotFoundException, ItemNotFoundException, IOException {
        start = startTimeBox.getValue().toString();
        end = endTimeBox.getValue().toString();
        date =  datePicker.getValue();
        startTime = stringToTimestamp(start);
        endTime = stringToTimestamp(end);
        location = locationBox.getValue().toString();
        cardsContainer.getChildren().clear();
        ArrayList<ConferenceRoom> roomList = mapDatabase.getConferenceRooms();
        if(location == "Select Location- Optional") {
            for (ConferenceRoom conferenceRoom : roomList) {
                if (isAvailable(conferenceRoom))
                {
                    cardsContainer.getChildren().add(loadCard(conferenceRoom));
                }
            }
        }
        else{
            if (isAvailable(mapDatabase.getConferenceRoomByLongname(location))){
                cardsContainer.getChildren().add(loadCard(mapDatabase.getConferenceRoomByLongname(location)));
            }
        }
        if(cardsContainer.getChildren().isEmpty()){
            Text text = new Text("No Rooms Available");
            text.getStyleClass().add("title");
            cardsContainer.getChildren().add(text);
        }
    }

    Timestamp stringToTimestamp(String time){
        String[] timeList = time.split(":");
        int hour = Integer.parseInt(timeList[0]);
        int min = Integer.parseInt(timeList[1]);
        return new Timestamp(date.getYear(),date.getMonthValue(), date.getDayOfMonth(), hour, min, 0, 0);
    }

    void reset(){
        startTimeBox.setValue("Start Time");
        endTimeBox.setValue("Start Time");
        locationBox.setValue("Select Location- Optional");
    }

    void fillArray(){
        for(int i = 0; i <= 23; i++){
            timeArray.add(i + ":00");
            timeArray.add(i + ":15");
            timeArray.add(i + ":30");
            timeArray.add(i + ":45");
        }
    }

    private void addDisplayFeatures(ConferenceRoom data, HBox hbox) {
        ImageView plug = new ImageView(Objects.requireNonNull(Main.class.getResource("images/plug.png")).toExternalForm());
        ImageView tv = new ImageView(Objects.requireNonNull(Main.class.getResource("images/television.png")).toExternalForm());
        ImageView chair = new ImageView(Objects.requireNonNull(Main.class.getResource("images/disabled.png")).toExternalForm());
        plug.setPreserveRatio(true);
        plug.setFitHeight(50);
        tv.setPreserveRatio(true);
        tv.setFitHeight(50);
        chair.setPreserveRatio(true);
        chair.setFitHeight(50);
        hbox.getChildren().clear();
        if (data.isHasOutlets()) {
            hbox.getChildren().add(plug);
        }
        if (data.isHasScreen()) {
            hbox.getChildren().add(tv);
        }
        if (data.isAccessible()) {
            hbox.getChildren().add(chair);
        }

    }

    public boolean isAvailable(ConferenceRoom conferenceRoom) throws SQLException, ClassNotFoundException {
        if(wheelchairToggle.isSelected() && !conferenceRoom.isAccessible()){
            return false;
        }
        if(screenToggle.isSelected() && !conferenceRoom.isHasScreen()){
            return false;
        }
        if(plugToggle.isSelected() && !conferenceRoom.isHasOutlets()){
            return false;
        }
        for(RoomRequest roomRequest: new RequestDatabase().getRoomRequestsByLongname(conferenceRoom.getLongname())){
            Timestamp requestStart = roomRequest.getStartTime();
            Timestamp requestEnd = roomRequest.getEndTime();
            if((requestStart.after(startTime) && requestStart.before(endTime)) ||
                    (requestEnd.after(startTime) && requestEnd.before(endTime)) ||
                    (requestEnd.equals(endTime) || requestStart.equals(startTime)) ||
                    (startTime.after(requestStart) && startTime.before(requestEnd))){
                return false;
            }
        }
        return true;
    }
}