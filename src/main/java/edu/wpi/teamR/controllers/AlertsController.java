package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RoomRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Objects;

public class AlertsController {

    @FXML
    BorderPane alertPopup;
    @FXML
    TextArea messageField;
    @FXML
    MFXDatePicker datePicker, setStart, setEnd;
    @FXML
    Button submitButton, submitSet;
    @FXML
    TableView alertTable;
    @FXML
    TableColumn<Alert, String> messageColumn;
    @FXML TableColumn<Alert, String> startDate, endDate;
    @FXML TableColumn<Alert, Void> deleteCol;

    UserDatabase alerts = new UserDatabase();

    private final ObservableList<Alert> dataList = FXCollections.observableArrayList();
    FilteredList<Alert> filteredData = new FilteredList<>(dataList, b -> true);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @FXML
    public void initialize() throws SQLException {
        submitButton.setOnMouseClicked(event -> {
            try {
                submitAlert();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        messageField.setWrapText(true);
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        startDate.setCellValueFactory(new PropertyValueFactory<>("time"));
        //startDate.setCellFactory(TextFieldTableCell.forTableColumn());
//        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
//        endDate.setCellFactory(TextFieldTableCell.forTableColumn());
        addButtonToTable();

        for (Alert alert : new UserDatabase().getAlerts()) {
            alertTable.getItems().add(alert);
        }

//        messageColumn.setOnEditCommit(event -> {
//            Alert aAlert= event.getRowValue();
//            aAlert.setMessage(event.getNewValue());
//
//        });
//        startDate.setOnEditCommit(event -> {
//            Alert aAlert = event.getRowValue();
//            aAlert.setTime(Timestamp.valueOf(event.getNewValue()));
//
//        });

    }

    private void submitAlert() throws SQLException {
        alerts.addAlert(messageField.getText(), Timestamp.valueOf(datePicker.getValue().atTime(LocalTime.now())));
    }


    private void addButtonToTable() {
        Callback<TableColumn<Alert, Void>, TableCell<Alert, Void>> cellFactory = new Callback<TableColumn<Alert, Void>, TableCell<Alert, Void>>() {
            @Override
            public TableCell<Alert, Void> call(final TableColumn<Alert, Void> param) {
                return new TableCell<Alert, Void>() {

                    private final Button btn = new Button();
                    {
                        ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/delete.png")).toExternalForm());
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(30);
                        btn.getStyleClass().add("food_furniture-clear-button");
                        btn.setGraphic(imageView);
                        btn.setOnAction((ActionEvent event) -> {
                            Alert data = getTableView().getItems().get(getIndex());
                            filteredData.getSource().remove(data);
                            try {
                                new UserDatabase().deleteAlert(data.getMessage(), data.getTime());
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
