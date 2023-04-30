package edu.wpi.teamR.controllers;

import edu.wpi.teamR.datahandling.MapStorage;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import net.kurobako.gesturefx.GesturePane;

import javax.swing.text.html.ImageView;

public class movePatientController {
    @FXML GesturePane miniMap;
    AnchorPane mapPane;

    public void initialize() {
        mapPane.getChildren().add(MapStorage.getFirstFloor());
        miniMap.setContent(mapPane);
    }
}
