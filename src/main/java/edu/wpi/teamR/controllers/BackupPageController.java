package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.base.MFXCombo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class BackupPageController {
    @FXML MFXButton backButton;
    @FXML MFXButton fileButton;
    @FXML MFXButton pathButton;
    @FXML MFXButton submitImport;
    @FXML MFXButton submitExport;
    @FXML Text fileText;
    @FXML Text pathText;

    static File selectedFile;
    static File selectedDirectory;
    FileChooser fileChooser;

    DirectoryChooser directoryChooser;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ADMINHOME));
        fileButton.setOnAction(event -> openFile());
        submitImport.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (SQLException | ClassNotFoundException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        pathButton.setOnMouseClicked(event -> chooseDirectory());
        submitExport.setOnMouseClicked(event -> {
            try {
                export();
            } catch (ClassNotFoundException | SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void openFile() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Open File");
        selectedFile = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
        fileText.setText(selectedFile.getName());
    }

    public void submit() throws SQLException, FileNotFoundException, ClassNotFoundException {
        selectedFile = null;
        fileText.setText("...");
    }

    public void chooseDirectory() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        selectedDirectory = directoryChooser.showDialog(pathButton.getScene().getWindow());
        pathText.setText(selectedDirectory.getAbsolutePath());
    }

    public void export() throws SQLException, IOException, ClassNotFoundException {
        selectedDirectory = null;
        pathText.setText("...");
    }
}