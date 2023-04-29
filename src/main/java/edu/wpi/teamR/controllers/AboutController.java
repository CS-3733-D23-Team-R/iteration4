package edu.wpi.teamR.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class AboutController {
    @FXML VBox
            benFront, benBack,
            altonFront, altonBack,
            nathanielFront, nathanielBack,
            bryceFront, bryceBack,
            johnFront, johnBack,
            gwenFront, gwenBack,
            zeshFront, zeshBack,
            simonFront, simonBack,
            ashFront, ashBack,
            jpFront, jpBack;
    @FXML
    public void initialize(){
        benBack.setVisible(false);
        altonBack.setVisible(false);
        nathanielBack.setVisible(false);
        bryceBack.setVisible(false);
        johnBack.setVisible(false);
        gwenBack.setVisible(false);
        zeshBack.setVisible(false);
        simonBack.setVisible(false);
        ashBack.setVisible(false);
        ashBack.setVisible(false);
        johnBack.setVisible(false);
    }
}
