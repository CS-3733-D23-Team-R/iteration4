package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.archive.Archiver;
import edu.wpi.teamR.archive.CSVParameterException;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ArchivePageController {

    @FXML Button chooseImportButton;
    @FXML Button submitImport;
    @FXML Button submitExport;
    @FXML Text importText;
    @FXML Text exportText;

    static File selectedFile;
    static File selectedDirectory;
    FileChooser fileChooser;

    MapDatabase mapdb = App.getMapData().getMapdb();

    Archiver archiver = new Archiver(mapdb, new UserDatabase(), new RequestDatabase());

    @FXML
    public void initialize() {
        chooseImportButton.setOnAction(event -> openFile());
        submitImport.setOnMouseClicked(event -> {
            try {
                submit();
                importText.setText("Successfully imported backup.");
            } catch (SQLException | CSVParameterException | IOException e) {
                if (e.getClass() == SQLException.class) {
                    importText.setText("Error uploading to database.");
                } else if (e.getCause() instanceof CSVParameterException) {
                    importText.setText("Error parsing CSV parameters.");
                } else if (e.getCause() instanceof IOException) {
                    importText.setText("Error reading file.");
                }
                e.printStackTrace();
            }
        });
        submitExport.setOnMouseClicked(event -> {
            try {
                export();
                exportText.setText("Successfully exported database.");
            } catch (SQLException | IOException e) {
                if (e.getClass() == SQLException.class) {
                    exportText.setText("Error reading from database.");
                } else if (e.getClass() == IOException.class) {
                    exportText.setText("Error reading file.");
                }
                e.printStackTrace();
            }
        });
    }

    public void openFile() {
        fileChooser = new FileChooser();
        File dir = new File("./backups/");
        dir.mkdir();
        fileChooser.setInitialDirectory(dir);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Zip Files", "*.zip"));
        fileChooser.setTitle("Open File");
        selectedFile = fileChooser.showOpenDialog(chooseImportButton.getScene().getWindow());
        if (selectedFile != null)
            importText.setText(selectedFile.getName());
    }

    public void submit() throws SQLException, IOException, CSVParameterException {
        archiver.restoreArchive(selectedFile.getAbsolutePath());
        selectedFile = null;
        importText.setText("");
    }

    public void export() throws SQLException, IOException {
        String exportFile = archiver.createArchive();
        selectedDirectory = null;
        exportText.setText("Created backup " + exportFile);
    }
}