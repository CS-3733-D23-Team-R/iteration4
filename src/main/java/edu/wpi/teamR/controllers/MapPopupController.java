package edu.wpi.teamR.controllers;

import edu.wpi.teamR.mapdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class MapPopupController {
    @FXML
    Text nodeField;
    @FXML
    TextField locField;
    @FXML
    DatePicker moveDatePicker;

    Node node;
    Move move;
    MapDatabase mdb;
    LocationName locationName;
    @FXML
    Button deleteButton;
    @FXML
    public void initialize() {
        moveDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                handleDatePickerChange(newValue);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        locField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                updateLocationName();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        moveDatePicker.setFocusTraversable(false);
        locField.setFocusTraversable(false);

        deleteButton.setOnAction(event -> {
            try {
                delete();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void close(Stage primaryStage) {
        primaryStage.close();
    }

    @FXML public void start(Stage primaryStage) {
        primaryStage.show();
    }

    public void showNodeInformation(MapDatabase mapdb, Node n, LocationName l, Move m) {
        node = n;
        move = m;
        mdb = mapdb;
        locationName = l;
        nodeField.setText(Integer.toString(n.getNodeID()));
        locField.setText(l.getShortName());

        Date moveDate = m.getMoveDate();
        LocalDate date = moveDate.toLocalDate();
        moveDatePicker.setValue(date);
    }

    private void handleDatePickerChange(LocalDate newValue) throws SQLException {
        Date sqlDate = Date.valueOf(newValue);
        int nodeID = move.getNodeID();
        String longName = move.getLongName();
        mdb.deleteMove(move.getNodeID(), move.getLongName(), move.getMoveDate());
        mdb.addMove(nodeID, longName, sqlDate);
    }

    private void updateLocationName() throws SQLException {
        mdb.modifyLocationNameShortName(locationName.getLongName(), locField.getText());
    }

    private void delete() throws SQLException {
        mdb.deleteEdgesByNode(node.getNodeID());
        mdb.deleteMovesByNode(move.getNodeID());
        mdb.deleteNode(node.getNodeID());
    }
}
