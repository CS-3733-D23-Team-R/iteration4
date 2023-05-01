package edu.wpi.teamR.controllers.mapeditor;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Node;
import edu.wpi.teamR.mapdb.update.MapUpdater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class NewNodePopupController {
    MapUpdater mapUpdater;
    ArrayList<Node> nodes;
    MapDatabase mapdb;

    @FXML
    TextField xField;
    @FXML TextField yField;
    @FXML
    ComboBox<String> floorCB;
    @FXML
    ComboBox<String> buildingCB;
    @FXML
    Button addButton;

    ObservableList<String> floors =
            FXCollections.observableArrayList("L2",
                    "L1",
                    "1",
                    "2",
                    "3");
    ObservableList<String> buildingNames =
            FXCollections.observableArrayList("15 Francis", "45 Francis", "BTM", "Shapiro", "Tower");

    public void initialize() {
        nodes = App.getMapData().getNodes();
        mapdb = App.getMapData().getMapdb();
        floorCB.setItems(floors);
        buildingCB.setItems(buildingNames);
        addButton.setOnAction(event -> {
            try {
                createNewNode();
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

    public void createNewNode() throws SQLException {
        Node n = mapdb.addNode(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()), floorCB.getValue(), buildingCB.getValue());
        nodes.add(n);
        App.getMapData().setNodes(nodes);
        mapUpdater.addNode(n);
    }

    public void setUpdater(MapUpdater updater) {
        mapUpdater = updater;
    }

    public void open(int x, int y, String floor) {
        xField.setText(Integer.toString(x));
        yField.setText(Integer.toString(y));
        floorCB.setValue(floor);
    }
}
