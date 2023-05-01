package edu.wpi.teamR.controllers.mapeditor;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import javax.xml.stream.Location;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditLocationPopupController {
    MapUpdater mapUpdater;
    MapDatabase mapdb;

    @FXML TextField shortField;
    @FXML
    SearchableComboBox<String> locationComboBox;
    @FXML
    SearchableComboBox<Integer> nodeComboBox;
    @FXML SearchableComboBox<String> typeComboBox;
    @FXML
    Button submitButton;
    @FXML
    Button deleteButton;

    Node node;
    Move move;
    @FXML
    MFXDatePicker datePicker;

    ObservableList<String> locationTypes =
            FXCollections.observableArrayList("HALL", "LABS", "ELEV", "SERV", "CONF", "STAI", "INFO", "REST", "DEPT", "BATH", "EXIT", "RETL");

    public void initialize() throws SQLException {
        this.mapdb = App.getMapData().getMapdb();

        typeComboBox.setItems(locationTypes);
        typeComboBox.setPromptText("Choose Location Type.");

        ArrayList<LocationName> locationNames =  mapdb.getLocationNames();
        ArrayList<String> longNames = new ArrayList<>();
        for (LocationName ln: locationNames) {
            longNames.add(ln.getLongName());
        }
        ObservableList<String> longName_o = FXCollections.observableArrayList(longNames);
        FXCollections.sort(longName_o);
        locationComboBox.setItems(longName_o);
        locationComboBox.setPromptText("Choose Location.");
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            try {
                LocationName ln = mapdb.getLocationNameByLongName(newValue);
                shortField.setText(ln.getShortName());
                typeComboBox.setValue(ln.getNodeType());
                node = mapdb.getNodeFromLocationName(ln.getLongName());
                nodeComboBox.setValue(node.getNodeID());
                move = mapdb.getMovesByNode(node.getNodeID()).get(0);
                datePicker.setValue(move.getMoveDate().toLocalDate());
            } catch (SQLException | ItemNotFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        shortField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("shortName changed from " + oldValue + " to " + newValue);
        });

        ArrayList<Node> nodes =  mapdb.getNodes();
        ArrayList<Integer> nodeIDs = new ArrayList<>();
        for (Node n: nodes) {
            nodeIDs.add(n.getNodeID());
        }
        nodeComboBox.setItems(FXCollections.observableArrayList(nodeIDs));
        nodeComboBox.setPromptText("Choose Node.");

        submitButton.setOnAction(event -> {
            try {
                submitLocationChange();
                close((Stage)submitButton.getScene().getWindow());
            } catch (SQLException | ItemNotFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        deleteButton.setOnAction(event -> {
            try {
                mapUpdater.deleteMovesByLocationName(locationComboBox.getValue());
                mapdb.deleteMovesByLocationName(locationComboBox.getValue());
                mapUpdater.deleteLocationName(locationComboBox.getValue());
                mapdb.deleteLocationName(locationComboBox.getValue());
                close((Stage)submitButton.getScene().getWindow());
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void close(Stage primaryStage) throws SQLException {
        primaryStage.close();
    }

    public void setUpdater(MapUpdater updater) {
        this.mapUpdater = updater;
    }

    public void submitLocationChange() throws SQLException, ItemNotFoundException, ClassNotFoundException {
        LocationName ln = mapdb.getLocationNameByLongName(locationComboBox.getValue());
        if (!ln.getShortName().equals(shortField.getText())) {
            mapUpdater.modifyLocationNameShortName(ln.getLongName(), shortField.getText());
            mapdb.modifyLocationNameShortName(ln.getLongName(), shortField.getText());
        }
        if (!ln.getNodeType().equals(typeComboBox.getValue())) {
            mapUpdater.modifyLocationNameShortName(ln.getLongName(), typeComboBox.getValue());
            mapdb.modifyLocationNameShortName(ln.getLongName(), typeComboBox.getValue());
        }
        if (mapdb.getNodeFromLocationName(ln.getLongName()).getNodeID() != nodeComboBox.getValue()) {
            mapdb.addMove(nodeComboBox.getValue(), ln.getLongName(), Date.valueOf(datePicker.getValue()));
        }
    }
}
