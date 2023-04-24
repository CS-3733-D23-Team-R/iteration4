package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class RoomCard extends VBox {
    @FXML Label nameLabel;
    @FXML Label floorLabel;
    @FXML Label capacityLabel;
    @FXML HBox featureBox;
    @FXML Button reserveButton;

    @FXML
    public void initialize() {}
    public void setInfo(String name, String capacity, String floor) {
        nameLabel.setText(name);
        capacityLabel.setText(capacity);
        floorLabel.setText(floor);
    }

    public HBox getFeatureBox() {
        return featureBox;
    }

    public Button getButton() {
        return reserveButton;
    }
}