package edu.wpi.teamR.controllers;

import edu.wpi.teamR.requestdb.RequestDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceRequestTrackerController {
    @FXML TableView requestTable;
    @FXML TableColumn selectAllColumn, idColumn, nameColumn, locationColumn, typeColumn, infoColumn, staffColumn, notesColumn, dateColumn, statusColumn, deleteColumn;
    RequestDatabase db;

    @FXML
    public void initialize() {
        db = RequestDatabase.getInstance();
    }
}
