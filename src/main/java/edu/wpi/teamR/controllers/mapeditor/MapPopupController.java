package edu.wpi.teamR.controllers.mapeditor;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.mapdb.update.MapUpdater;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MapPopupController {
    @FXML
    Text nodeField;
    @FXML
    Text xText;
    @FXML
    Text yText;
    @FXML
    DatePicker moveDatePicker;

    Node node;
    Move move;
    MapUpdater mapUpdater;
    MapDatabase mapdb;
    ArrayList<Node> nodes;
    @FXML
    Button deleteButton;
    @FXML
    public void initialize() {
        nodes = App.getMapData().getNodes();
        mapdb = App.getMapData().getMapdb();
        moveDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                handleDatePickerChange(newValue);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        moveDatePicker.setFocusTraversable(false);

        deleteButton.setOnAction(event -> {
            try {
                delete();
                close((PopOver)deleteButton.getScene().getWindow());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void close(PopOver primaryStage) {
        primaryStage.hide();
    }

    @FXML public void start(Stage primaryStage) {
        primaryStage.show();
    }

    public void showNodeInformation(MapUpdater updater, Node n) throws SQLException {
        node = n;
        if (mapdb.getMovesByNode(n.getNodeID()).size() > 0) {
            move = mapdb.getMovesByNode(n.getNodeID()).get(0);
            Date moveDate = move.getMoveDate();
            LocalDate date = moveDate.toLocalDate();
            moveDatePicker.setValue(date);
        }
        else {
            moveDatePicker.setValue(null);
        }
        mapUpdater = updater;
        //locationName = ;
        nodeField.setText(Integer.toString(n.getNodeID()));
        xText.setText(Integer.toString(n.getXCoord()));
        yText.setText(Integer.toString(n.getYCoord()));
    }

    private void handleDatePickerChange(LocalDate newValue) throws SQLException {
        Date sqlDate = Date.valueOf(newValue);
        int nodeID = move.getNodeID();
        String longName = move.getLongName();
        mapUpdater.addMove(nodeID, longName, sqlDate);
    }

    private void delete() throws SQLException {
        mapUpdater.deleteEdgesByNode(node.getNodeID());
        mapUpdater.deleteMovesByNode(move.getNodeID());
        mapUpdater.deleteNode(node.getNodeID());
        nodes.remove(node);
    }
}
