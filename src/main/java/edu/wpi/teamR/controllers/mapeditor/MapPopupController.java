package edu.wpi.teamR.controllers.mapeditor;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.mapdb.update.MapUpdater;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.application.Platform;
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
    MFXDatePicker moveDatePicker;
    @FXML Text buildingText;
    @FXML Text floorText;

    @FXML Text edgeText;

    @FXML Text longNameText;
    @FXML Text shortNameText;
    @FXML Text nodeTypeText;

    Node node;
    Move move;
    MapUpdater mapUpdater;
    MapDatabase mapdb;
    ArrayList<Node> nodes;
    @FXML
    Button deleteButton;

    Boolean isDeleted = false;
    @FXML
    public void initialize() throws SQLException {
        nodes = App.getMapData().getNodes();
        mapdb = App.getMapData().getMapdb();
        moveDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (move != null) {
                LocalDate localDate = Date.valueOf(move.getMoveDate().toLocalDate()).toLocalDate();
                if (!localDate.equals(newValue)) {
                    try {
                        handleDatePickerChange(newValue);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
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

    public void showNodeInformation(MapUpdater updater, Node n) throws SQLException, ItemNotFoundException {
        node = n;
        ArrayList<Move> movesForNode = mapdb.getMovesByNode(n.getNodeID());
        if (movesForNode.size() > 0) {
            move = movesForNode.get(movesForNode.size() - 1);
            Date moveDate = move.getMoveDate();
            LocalDate date = moveDate.toLocalDate();
            Platform.runLater(() -> moveDatePicker.setValue(date));
            String longName = move.getLongName();
            LocationName locationName = mapdb.getLocationNameByLongName(longName);
            String shortName = locationName.getShortName();
            String nodeType = locationName.getNodeType();

            longNameText.setText(longName);
            shortNameText.setText(shortName);
            nodeTypeText.setText(nodeType);
        }
        else {
            moveDatePicker.setValue(null);
        }
        mapUpdater = updater;
        nodeField.setText(Integer.toString(n.getNodeID()));
        xText.setText(Integer.toString(n.getXCoord()));
        yText.setText(Integer.toString(n.getYCoord()));
        buildingText.setText(n.getBuilding());
        floorText.setText(n.getFloorNum());

        ArrayList<Edge> n_edges = mapdb.getEdgesByNode(node.getNodeID());
        StringBuilder edgeString = new StringBuilder();
        for (Edge e: n_edges) {
            edgeString.append(" ").append(e.getStartNode()).append("-").append(e.getEndNode()).append("\n");
        }
        edgeText.setText(edgeString.toString());
    }

    private void handleDatePickerChange(LocalDate newValue) throws SQLException {
        Date sqlDate = Date.valueOf(newValue);
        int nodeID = move.getNodeID();
        String longName = move.getLongName();
        mapUpdater.addMove(nodeID, longName, sqlDate);
        mapdb.addMove(nodeID, longName, sqlDate);
    }

    private void delete() throws SQLException {
        mapUpdater.deleteNode(node.getNodeID());
        nodes.remove(node);
        mapdb.deleteMovesByNode(node.getNodeID());
        mapdb.deleteEdgesByNode(node.getNodeID());
        mapdb.deleteNode(node.getNodeID());
        isDeleted = true;
    }

    public boolean isDeleteButtonPressed() {
        return isDeleted;
    }
}
