package edu.wpi.teamR.controllers;

import edu.wpi.teamR.mapdb.Direction;
import edu.wpi.teamR.mapdb.DirectionArrow;
import edu.wpi.teamR.mapdb.DirectionArrowDAO;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;

public class SignageConfigurationController {
    public void start(Stage primaryStage) {
        primaryStage.show();
    }

    @FXML Button backButton;
    @FXML TableView<DirectionArrow> configurationTable;
    @FXML TableColumn<DirectionArrow, Direction> directionColumn;
    @FXML TableColumn<DirectionArrow, String> locationColumn;
    @FXML TableColumn<DirectionArrow, Integer> iconColumn;
    @FXML TableColumn<DirectionArrow, Void> saveColumn;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        MapDatabase aMapDatabase = new MapDatabase();

        iconColumn.setCellValueFactory(new PropertyValueFactory<>("kioskID"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("longname"));

        for(DirectionArrow directionArrow : aMapDatabase.getDirectionArrows()){
            configurationTable.getItems().add(directionArrow);
        }

        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE));
    }
}
