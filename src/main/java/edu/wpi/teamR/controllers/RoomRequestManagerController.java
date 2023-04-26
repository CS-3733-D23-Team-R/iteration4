package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RequestStatus;
import edu.wpi.teamR.requestdb.RoomRequest;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import io.github.palexdev.materialfx.skins.legacy.MFXLegacyTableViewSkin;

import java.sql.SQLException;
import java.util.Objects;

public class RoomRequestManagerController {

    @FXML TableView<RoomRequest> roomTable;
    @FXML TableColumn<RoomRequest, Integer> idColumn;
    @FXML TableColumn<RoomRequest, String> nameColumn;
    @FXML TableColumn<RoomRequest, String> locationColumn;
    @FXML TableColumn<RoomRequest, String> startCol;
    @FXML TableColumn<RoomRequest, String> endCol;
    @FXML TableColumn<RoomRequest, String> dateCol;
    @FXML MFXTextField searchField;

    @FXML TableColumn<RoomRequest, Void> deleteCol;

    @FXML Button newRequestBtn;
    ObservableList<RequestStatus> statusList = FXCollections.observableArrayList(RequestStatus.values());

    private final ObservableList<RoomRequest> dataList = FXCollections.observableArrayList();
    FilteredList<RoomRequest> filteredData = new FilteredList<>(dataList, b -> true);

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        dataList.addAll(new RequestDatabase().getRoomRequests());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("roomRequestID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("longname"));
        newRequestBtn.getStyleClass().add("Button");
        newRequestBtn.setOnAction(event -> Navigation.navigate(Screen.ROOM_REQUEST));
        addButtonToTable();

        for (RoomRequest request : new RequestDatabase().getRoomRequests()) {
            roomTable.getItems().add(request);
        }
        startCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RoomRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RoomRequest, String> data) {
                RoomRequest roomRequest = data.getValue();
                String[] times = roomRequest.getStartTime().toString().split(" ");
                return new SimpleStringProperty(times[1].substring(0,5));
            }
        });
        endCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RoomRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RoomRequest, String> data) {
                RoomRequest roomRequest = data.getValue();
                String[] times = roomRequest.getEndTime().toString().split(" ");
                return new SimpleStringProperty(times[1].substring(0,5));
            }
        });

        dateCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RoomRequest, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RoomRequest, String> data) {
                RoomRequest roomRequest = data.getValue();
                String[] times = roomRequest.getStartTime().toString().split(" ");
                return new SimpleStringProperty(times[0]);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(roomRequest -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if(roomRequest.getStaffUsername().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (roomRequest.getLongName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if ((roomRequest.getRoomRequestID() + "").indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (roomRequest.getStartTime().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (roomRequest.getEndTime().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else  return false;
            });
            SortedList<RoomRequest> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(roomTable.comparatorProperty());
            roomTable.setItems(sortedData);
            deleteCol.setVisible(false);
            deleteCol.setVisible(true);
        });

    }


    private void addButtonToTable() {
        Callback<TableColumn<RoomRequest, Void>, TableCell<RoomRequest, Void>> cellFactory = new Callback<TableColumn<RoomRequest, Void>, TableCell<RoomRequest, Void>>() {
            @Override
            public TableCell<RoomRequest, Void> call(final TableColumn<RoomRequest, Void> param) {
                return new TableCell<RoomRequest, Void>() {

                    private final Button btn = new Button();
                    {
                        ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/delete.png")).toExternalForm());
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(30);
                        btn.getStyleClass().add("food_furniture-clear-button");
                        btn.setGraphic(imageView);
                        btn.setOnAction((ActionEvent event) -> {
                            RoomRequest data = getTableView().getItems().get(getIndex());
                            filteredData.getSource().remove(data);
                            try {
                                new RequestDatabase().deleteRoomRequest(data.getRoomRequestID());
                            } catch (Exception e) {
                                e.printStackTrace();
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

        deleteCol.setCellFactory(cellFactory);
    }
}