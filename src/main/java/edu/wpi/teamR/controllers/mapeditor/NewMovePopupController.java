package edu.wpi.teamR.controllers.mapeditor;

import edu.wpi.teamR.App;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import edu.wpi.teamR.mapdb.Node;
import edu.wpi.teamR.mapdb.update.MapUpdater;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class NewMovePopupController {

    MapUpdater mapUpdater;
    MapDatabase mapdb;

    @FXML
    SearchableComboBox<String> locationBox;
    @FXML
    SearchableComboBox<Integer> nodeBox;
    @FXML
    MFXDatePicker moveDatePicker;

    @FXML
    Button addButton;

    ObservableList<Integer> nodeIDs;
    ObservableList<String> locationNames;
    public void initialize() throws SQLException {
        mapdb = App.getMapData().getMapdb();
        ArrayList<Node> nodes = App.getMapData().getNodes();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Node n: nodes) {
            ids.add(n.getNodeID());
        }
        nodeIDs = FXCollections.observableArrayList(ids);

        ArrayList<LocationName> ln_objects = App.getMapData().getLocationNames();
        ArrayList<String> names = new ArrayList<>();
        for (LocationName ln: ln_objects) {
            if (!ln.getLongName().contains("Hall")) {
                names.add(ln.getLongName());
            }
        }
        locationNames = FXCollections.observableArrayList(names);

        nodeBox.setItems(nodeIDs);
        locationBox.setItems(locationNames);

        addButton.setOnAction(event -> {
            try {
                createNewMove();
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

    public void createNewMove() throws SQLException {
        mapUpdater.addMove(nodeBox.getValue(), locationBox.getValue(), Date.valueOf(moveDatePicker.getValue()));
        Move m = mapdb.addMove(nodeBox.getValue(), locationBox.getValue(), Date.valueOf(moveDatePicker.getValue()));
    }

    public void setUpdater(MapUpdater updater) {
        mapUpdater = updater;
    }
}
