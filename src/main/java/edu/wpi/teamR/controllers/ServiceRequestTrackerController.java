package edu.wpi.teamR.controllers;

import edu.wpi.teamR.*;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class ServiceRequestTrackerController {
    @FXML TableView requestTable;
    @FXML TableColumn selectAllColumn, idColumn, nameColumn, locationColumn, typeColumn, infoColumn, staffColumn, notesColumn, dateColumn, statusColumn, deleteColumn;

    @FXML
    public void initialize() {

    }
}
