package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.mapdb.ConferenceRoom;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RoomRequest;
import edu.wpi.teamR.userData.userData;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/*TODO: RoomRequestController-master
    make table display all textual info
        room floor
    clean up FXML page
    ISSUES:
        No actual way to get here yet
        kinda looks bland


    SELECT * FROM iteration2.conferenceroom;
 */
public class RoomRequestController {
    @FXML MFXButton submitButton;
    @FXML MFXButton resetButton;
    @FXML DatePicker datePicker;
    @FXML TableView<ConferenceRoom> conferenceTable;
    @FXML TableColumn<ConferenceRoom, String> roomCol;
    @FXML TableColumn<ConferenceRoom, String> floorCol;
    @FXML TableColumn<ConferenceRoom, String> capacityCol;
    @FXML TableColumn<ConferenceRoom, String> featuresCol;
    @FXML TableColumn<ConferenceRoom, Void> requestCol;
    @FXML MFXComboBox locationBox;
    @FXML MFXComboBox startTimeBox;
    @FXML MFXComboBox endTimeBox;

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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        fillArray();
        ArrayList<String> locNames = new ArrayList<>();
        for(ConferenceRoom conferenceRoom: mapDatabase.getConferenceRooms()){
            locNames.add(conferenceRoom.getLongname());
        }
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
        roomCol.setCellValueFactory(new PropertyValueFactory<>("longname"));
        floorCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConferenceRoom, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ConferenceRoom, String> data) {
                ConferenceRoom conferenceRoom = data.getValue();
                try {
                    return new SimpleStringProperty(mapDatabase.getNodeFromLocationName(conferenceRoom.getLongname()).getFloorNum());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (ItemNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        addButtonToTable();
        addDisplayFeatures();
        submitButton.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        resetButton.setOnMouseClicked(event -> reset());
    }

    void submit() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        conferenceTable.getItems().clear();
        start = startTimeBox.getValue().toString();
        end = endTimeBox.getValue().toString();
        date =  datePicker.getValue();
        startTime = stringToTimestamp(start);
        endTime = stringToTimestamp(end);
        location = locationBox.getValue().toString();
        ArrayList<ConferenceRoom> roomList = mapDatabase.getConferenceRooms();
        if(location == "Select Location- Optional") {
            for (ConferenceRoom conferenceRoom : roomList) {
                if (isAvailable(conferenceRoom))
                    conferenceTable.getItems().add(conferenceRoom);
            }
        }
        else{
            if (isAvailable(mapDatabase.getConferenceRoomByLongname(location)))
                conferenceTable.getItems().add(mapDatabase.getConferenceRoomByLongname(location));
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
        conferenceTable.getItems().clear();
    }

    void fillArray(){
        for(int i = 0; i <= 23; i++){
            timeArray.add(i + ":00");
            timeArray.add(i + ":15");
            timeArray.add(i + ":30");
            timeArray.add(i + ":45");
        }
    }

    private void addDisplayFeatures() {
        featuresCol.setCellFactory(column -> {
            return new TableCell<ConferenceRoom, String>() {
                private HBox hbox = new HBox();
                private ImageView image1 = new ImageView(Objects.requireNonNull(Main.class.getResource("images/plug.png")).toExternalForm());
                private ImageView image2 = new ImageView(Objects.requireNonNull(Main.class.getResource("images/television.png")).toExternalForm());
                private ImageView image3 = new ImageView(Objects.requireNonNull(Main.class.getResource("images/disabled.png")).toExternalForm());

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    image1.setPreserveRatio(true);
                    image1.setFitHeight(30);
                    image2.setPreserveRatio(true);
                    image2.setFitHeight(30);
                    image3.setPreserveRatio(true);
                    image3.setFitHeight(30);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        ConferenceRoom data = getTableRow().getItem();

                        // Get the boolean values from your data object
                        boolean hasImage1 = data.isHasOutlets();
                        boolean hasImage2 = data.isHasScreen();
                        boolean hasImage3 = data.isAccessible();

                        // Clear any previous images from the HBox
                        hbox.getChildren().clear();

                        // Add up to 3 images to the HBox based on the boolean values
                        if (hasImage1) {
                            hbox.getChildren().add(image1);
                        }
                        if (hasImage2) {
                            hbox.getChildren().add(image2);
                        }
                        if (hasImage3) {
                            hbox.getChildren().add(image3);
                        }

                        setGraphic(hbox);
                    }
                }
            };
        });
        }

    private void addButtonToTable() {
        Callback<TableColumn<ConferenceRoom, Void>, TableCell<ConferenceRoom, Void>> cellFactory = new Callback<TableColumn<ConferenceRoom, Void>, TableCell<ConferenceRoom, Void>>() {
            @Override
            public TableCell<ConferenceRoom, Void> call(final TableColumn<ConferenceRoom, Void> param) {
                return new TableCell<ConferenceRoom, Void>() {

                    private final Button btn = new Button();
                    {
                        btn.getStyleClass().add("reserveButton");
                        btn.setText("Reserve");
                        btn.setOnAction((ActionEvent event) -> {
                            btn.getStyleClass().add("reserveButtonConfirm");
                            btn.setText(null);
                            ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/checked.png")).toExternalForm());
                            imageView.setPreserveRatio(true);
                            imageView.setFitHeight(28);
                            btn.setGraphic(imageView);
                            btn.setOnAction(null);
                            try {
                                ConferenceRoom room = getTableView().getItems().get(getIndex());
                                RequestDatabase.getInstance().addRoomRequest(room.getLongname(), userData.getInstance().getLoggedIn().getUsername(), startTime, endTime);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        requestCol.setCellFactory(cellFactory);
    }

    public boolean isAvailable(ConferenceRoom conferenceRoom) throws SQLException, ClassNotFoundException {
        for(RoomRequest roomRequest: RequestDatabase.getInstance().getRoomRequestsByLongname(conferenceRoom.getLongname())){
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
