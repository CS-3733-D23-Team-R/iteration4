package edu.wpi.teamR.controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

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
        VBox[] VboxFront = new VBox[]{benFront, altonFront, nathanielFront, bryceFront, johnFront, gwenFront, zeshFront, simonFront, ashFront, jpFront};
        VBox[] VboxBack = new VBox[]{benBack, altonBack, nathanielBack, bryceBack, johnBack, gwenBack, zeshBack, simonBack, ashBack, jpBack};
        for(int i = 0; i< VboxFront.length; i++){
            int finalI = i;
            VboxFront[i].setOnMouseClicked(event -> {
                RotateTransition rotateTransition = createRotation(0,90,VboxFront[finalI]);
                RotateTransition rotateTransition1 = createRotation(90, 0, VboxBack[finalI]);
                rotateTransition.setOnFinished(event1 -> {
                    VboxFront[finalI].setVisible(false);
                    VboxBack[finalI].setVisible(true);
                    rotateTransition1.play();
                });
                rotateTransition.play();
            });
            VboxFront[i].setOnMouseDragged(event -> {
                RotateTransition rotateTransition = createSpinningRotation(0, 360, VboxFront[finalI]);
                rotateTransition.play();
            });
        }
        for(int i = 0; i< VboxBack.length; i++){
            VboxBack[i].setVisible(false);
            int finalI = i;
            VboxBack[i].setOnMouseClicked(event -> {
                RotateTransition rotateTransition = createRotation(0,90,VboxBack[finalI]);
                RotateTransition rotateTransition1 = createRotation(90, 0, VboxFront[finalI]);
                rotateTransition.setOnFinished(event1 -> {
                    VboxBack[finalI].setVisible(false);
                    VboxFront[finalI].setVisible(true);
                    rotateTransition1.play();
                });
                rotateTransition.play();
            });
            VboxBack[i].setOnMouseDragged(event -> {
                RotateTransition rotateTransition = createSpinningRotation(0, 360, VboxBack[finalI]);
                rotateTransition.play();
            });
        }

        for(int i = 0; i< VboxFront.length; i++){
            int finalI = i;
            RotateTransition rotateTransition = openingFlip(0,90,VboxFront[finalI]);
            RotateTransition rotateTransition1 = openingFlip(90, 0, VboxBack[finalI]);
            rotateTransition.setOnFinished(event1 -> {
                VboxFront[finalI].setVisible(false);
                VboxBack[finalI].setVisible(true);
                rotateTransition1.play();
                rotateTransition.play();
            });
        }
        for(int i = 0; i< VboxBack.length; i++){
            VboxBack[i].setVisible(false);
            int finalI = i;
            RotateTransition rotateTransition = openingFlip(0,90,VboxBack[finalI]);
            RotateTransition rotateTransition1 = openingFlip(90, 0, VboxFront[finalI]);
            rotateTransition.setOnFinished(event1 -> {
                VboxBack[finalI].setVisible(false);
                VboxFront[finalI].setVisible(true);
                rotateTransition1.play();
            });
            rotateTransition.play();
        }
    }
    private RotateTransition createRotation(double fromAngle, double toAngle, Node node) {
        RotateTransition rotation = new RotateTransition(Duration.millis(250), node);
        rotation.setAxis(Rotate.Y_AXIS);
        rotation.setFromAngle(fromAngle);
        rotation.setToAngle(toAngle);
        rotation.setInterpolator(Interpolator.LINEAR);
        return rotation;
    }

    private RotateTransition createSpinningRotation(double fromAngle, double toAngle, Node node) {
        RotateTransition rotation = new RotateTransition(Duration.millis(250), node);
        rotation.setAxis(Rotate.Y_AXIS);
        rotation.setFromAngle(fromAngle);
        rotation.setToAngle(toAngle);
        rotation.setInterpolator(Interpolator.LINEAR);
        rotation.setCycleCount(5);
        return rotation;
    }

    private RotateTransition openingFlip(double fromAngle, double toAngle, Node node) {
        RotateTransition rotation = new RotateTransition(Duration.millis(0.1), node);
        rotation.setAxis(Rotate.Y_AXIS);
        rotation.setFromAngle(fromAngle);
        rotation.setToAngle(toAngle);
        rotation.setInterpolator(Interpolator.LINEAR);
        return rotation;
    }
}
