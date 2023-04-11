package edu.wpi.teamR.controllers;

import edu.wpi.teamR.csv.CSVParameterException;
import edu.wpi.teamR.csv.CSVReader;
import edu.wpi.teamR.csv.CSVWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import edu.wpi.teamR.mapdb.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MapEditorController {
    @FXML
    Button importCSVButton;
    @FXML Button exportCSVButton;

    static File selectedFile;
    static File selectedDirectory;
    FileChooser fileChooser;

    DirectoryChooser directoryChooser;
    @FXML
    ComboBox<String> fileMenu;

    @FXML
    public void initialize() {

    }

    public void importCSV () throws IOException, CSVParameterException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        openFile();
        String choice = fileMenu.getValue();
        switch (choice) {
            case "Node":
                CSVReader<Node> nodeCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), Node.class);
                nodeCSVReader.parseCSV();
                break;
            case "Edge":
                CSVReader<Edge> edgeCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), Edge.class);
                edgeCSVReader.parseCSV();
                break;
            case "LocationName":
                CSVReader<LocationName> locationCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), LocationName.class);
                locationCSVReader.parseCSV();
                break;
            case "Moves":
                CSVReader<Move> moveCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), Move.class);
                moveCSVReader.parseCSV();
                break;
        }
    }

    public void openFile() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Open File");
        selectedFile = fileChooser.showOpenDialog(importCSVButton.getScene().getWindow());
    }

    public void chooseDirectory() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        selectedDirectory = directoryChooser.showDialog(exportCSVButton.getScene().getWindow());
    }

    public void export() throws IOException {
        String choice = fileMenu.getValue();
        chooseDirectory();
        switch (choice) {
            case "Node":
                CSVWriter<Node> nodeCSVWriter = new CSVWriter<>();
                nodeCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/Nodes.csv", null);
                break;
            case "Edge":
                CSVWriter<Edge> edgeCSVWriter = new CSVWriter<>();
                edgeCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/Edges.csv", null);
                break;
            case "LocationName":
                CSVWriter<LocationName> locationCSVWriter = new CSVWriter<>();
                locationCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/LocationNames.csv", null);
                break;
            case "Moves":
                CSVWriter<Move> moveCSVWriter = new CSVWriter<>();
                moveCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/Moves.csv", null);
                break;
        }
        selectedDirectory = null;
    }


}
