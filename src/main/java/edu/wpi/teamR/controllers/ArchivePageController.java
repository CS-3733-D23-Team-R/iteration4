package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.archive.Archiver;
import edu.wpi.teamR.archive.CSVParameterException;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.RequestDatabase;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class ArchivePageController {

    @FXML
    Button fileButton;
    @FXML Button pathButton;
    @FXML Button submitImport;
    @FXML Button submitExport;
    @FXML Text fileText;
    @FXML Text pathText;

    static File selectedFile;
    static File selectedDirectory;
    FileChooser fileChooser;

    DirectoryChooser directoryChooser;

    MapDatabase mapdb = App.getMapData().getMapdb();

    Archiver archiver = new Archiver(mapdb, new UserDatabase(), new RequestDatabase());

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        fileButton.setOnAction(event -> openFile());
        submitImport.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (SQLException | ClassNotFoundException | FileNotFoundException | CSVParameterException e) {
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
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Zip Files", "*.zip"));
        fileChooser.setTitle("Open File");
        selectedFile = fileChooser.showOpenDialog(fileButton.getScene().getWindow());
        fileText.setText(selectedFile.getName());
    }

    public void submit() throws SQLException, FileNotFoundException, ClassNotFoundException, CSVParameterException {
        archiver.restoreArchive(selectedFile.getAbsolutePath());
        selectedFile = null;
        fileText.setText("");
    }

    public void chooseDirectory() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        selectedDirectory = directoryChooser.showDialog(pathButton.getScene().getWindow());
        pathText.setText(selectedDirectory.getAbsolutePath());
    }

    public void export() throws SQLException, IOException, ClassNotFoundException {
        archiver.createArchive(selectedDirectory.getAbsolutePath() + "/archive.zip");
        selectedDirectory = null;
        pathText.setText("");
    }
}