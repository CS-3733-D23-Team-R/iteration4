package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;

public class SplashController {

    @FXML private MFXProgressSpinner progressSpinner;

    public void initialize() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //if you want to call a function here you can
                return null;
            }
        };
        new Thread(task).start();
    }
}
