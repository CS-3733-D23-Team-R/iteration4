package edu.wpi.teamR.controllers;

import edu.wpi.teamR.mapdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MapPopupController {
    @FXML
    TextField nodeField;
    @FXML
    TextField locField;
    @FXML
    TextField moveField;
    @FXML
    public void initialize() {}

    @FXML
    public void close(Stage primaryStage) {
        primaryStage.close();
    }

    @FXML public void start(Stage primaryStage) {
        primaryStage.show();
    }

    public void showNodeInformation(Node n) {
        nodeField.setText(Integer.toString(n.getNodeID()));
    }
}
