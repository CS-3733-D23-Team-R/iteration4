package edu.wpi.teamR.controllers;

import edu.wpi.teamR.mapdb.MapDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class NewLocationPopupController {
    MapDatabase mapDB;

    @FXML
    TextField longField;
    @FXML TextField shortField;
    @FXML
    ComboBox<String> typeBox;
    @FXML
    Button addButton;

    ObservableList<String> locationTypes =
            FXCollections.observableArrayList("HALL", "LABS", "ELEV", "SERV", "CONF", "STAI", "INFO", "REST", "DEPT", "BATH", "EXIT", "RETL");

    public void initialize() {
        typeBox.setItems(locationTypes);
        addButton.setOnAction(event -> {
            try {
                createNewLocation();
                close((Stage)addButton.getScene().getWindow());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void close(Stage primaryStage) throws SQLException {
        primaryStage.close();
    }

    public void createNewLocation() throws SQLException {
        mapDB.addLocationName(longField.getText(), shortField.getText(), typeBox.getValue());
    }

    public void setMapDB(MapDatabase mdb) {
        mapDB = mdb;
    }
}
