package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.csv.CSVParameterException;
import edu.wpi.teamR.csv.CSVReader;
import edu.wpi.teamR.csv.CSVWriter;
import edu.wpi.teamR.pathfinding.Line;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import edu.wpi.teamR.mapdb.*;
import net.kurobako.gesturefx.GesturePane;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.geometry.Point2D;
import org.controlsfx.control.PopOver;

public class MapEditorController {
    @FXML
    Button importCSVButton;
    @FXML Button exportCSVButton;
    @FXML
    Button editButton;
    @FXML Button saveButton;

    static File selectedFile;
    static File selectedDirectory;
    FileChooser fileChooser;

    DirectoryChooser directoryChooser;
    @FXML
    ComboBox<String> fileMenu;

    @FXML
    BorderPane borderPane;
    @FXML AnchorPane anchorPane;

    @FXML
    GesturePane gesturePane;

    @FXML
    ComboBox<String> floorComboBox;
    @FXML
    ComboBox<String> tableComboBox;
    ObservableList<String> DAOType =
            FXCollections.observableArrayList("Node", "Edge", "LocationName", "Moves");
    ObservableList<String> floors =
            FXCollections.observableArrayList("Lower Level Two",
                    "Lower Level One",
                    "First Floor",
                    "Second Floor",
                    "Third Floor");

    URL firstFloorLink = Main.class.getResource("images/01_thefirstfloor.png");
    URL secondFloorLink = Main.class.getResource("images/02_thesecondfloor.png");
    URL thirdFloorLink = Main.class.getResource("images/03_thethirdfloor.png");
    URL LLOneLink = Main.class.getResource("images/00_thelowerlevel1.png");
    URL LLTwoLink = Main.class.getResource("images/00_thelowerlevel2.png");

    ImageView imageView;
    int currentFloor = 2;

    URL[] linkArray = {
            LLTwoLink, LLOneLink, firstFloorLink, secondFloorLink, thirdFloorLink,
    };

    String[] floorNames = {
            "Lower Level Two",
            "Lower Level One",
            "First Floor",
            "Second Floor",
            "Third Floor"
    };

    String[] nodeFloorNames = {
            "L2",
            "L1",
            "1",
            "2",
            "3"
    };

    AnchorPane[] nodePanes = new AnchorPane[5];

    private AnchorPane mapPane = new AnchorPane();

    HashMap<String, Integer> floorNamesMap = new HashMap<String, Integer>();

    private MapDatabase mapdb;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        for (int i = 0; i < 5; i++) {
            nodePanes[i] = new AnchorPane();
            floorNamesMap.put(nodeFloorNames[i], i);
            floorNamesMap.put(floorNames[i], i);
        }

        floorComboBox.setItems(floors);
        tableComboBox.setItems(DAOType);
        importCSVButton.setOnAction(event -> {
            try {
                importCSV();
            } catch (IOException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                     InvocationTargetException | CSVParameterException e) {
                throw new RuntimeException(e);
            }
        });
        exportCSVButton.setOnAction(event -> {
            try {
                export();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        editButton.setOnAction(event -> gesturePane.setGestureEnabled(false));
        saveButton.setOnAction(event -> gesturePane.setGestureEnabled(true));

        floorComboBox.setOnAction(event -> {
            try {
                changeFloor(floorNamesMap.get(floorComboBox.getValue()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        imageView = new ImageView(linkArray[currentFloor].toExternalForm());
        gesturePane.setContent(mapPane);
        mapPane.getChildren().add(imageView);
        gesturePane.setMinScale(0.25);
        gesturePane.setMaxScale(2);

        mapdb = new MapDatabase();
        displayNodesByFloor(2);
    }

    public void changeFloor(int floorNum) throws SQLException {
        if (floorNum < 5) {
            currentFloor = floorNum;
            imageView = new ImageView(linkArray[currentFloor].toExternalForm());
            mapPane.getChildren().clear();
            mapPane.getChildren().add(imageView);
            reset();
            displayNodesByFloor(floorNum);
        }
    }

    public void reset() {
        // zoom to 1x
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.centreOn(new Point2D(2500, 1700));
    }


    public void displayNodesByFloor(int floor) throws SQLException {
        String f = nodeFloorNames[floor];
        mapPane.getChildren().add(nodePanes[floor]);
        ArrayList<Node> nodes = mapdb.getNodesByFloor(f);
        System.out.println(nodes.size());
        for (Node n: nodes) {
            Circle c = new Circle(n.getXCoord(), n.getYCoord(), 5, Color.RED);
            nodePanes[floor].getChildren().add(c);

            AtomicBoolean dragging = new AtomicBoolean(false);

            c.setOnMouseClicked(event -> {
                if (!dragging.get()) {
                    // Create and configure the PopOver
                    PopOver popOver = new PopOver();
                    final FXMLLoader loader =
                            new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/MapPopup.fxml"));
                    Parent popup = null;
                    try {
                        popup = loader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    MapPopupController controller = loader.getController();
                    controller.showNodeInformation(n);

                    popOver.setContentNode(popup);
                    popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                    popOver.setAutoHide(true);
                    popOver.show(c);
                }
            });
            c.setOnMouseDragged(dragEvent -> {
                if (!gesturePane.isGestureEnabled()) {
                    dragging.set(true);
                    double deltaX = dragEvent.getX() - c.getCenterX();
                    double deltaY = dragEvent.getY() - c.getCenterY();
                    c.setCenterX(dragEvent.getX());
                    c.setCenterY(dragEvent.getY());
                    //n.setXCoord(n.getXCoord() + deltaX); // Update X coordinate of the Node
                    //n.setYCoord(n.getYCoord() + deltaY); // Update Y coordinate of the Node
                }
            });
        }

        ArrayList<Edge> edges = mapdb.getEdgesByFloor(f);
        for (Edge e: edges) {
            Node n1 = mapdb.getNodeByID(e.getStartNode());
            Node n2 = mapdb.getNodeByID(e.getEndNode());
            Line l1 = new Line(n1.getXCoord(), n1.getYCoord(), n2.getXCoord(), n2.getYCoord());
            nodePanes[floor].getChildren().add(l1);
        }
    }

    public void importCSV () throws IOException, CSVParameterException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        openFile();
        String choice = tableComboBox.getValue();
        switch (choice) {
            case "Node" -> {
                CSVReader<Node> nodeCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), Node.class);
                nodeCSVReader.parseCSV();
            }
            case "Edge" -> {
                CSVReader<Edge> edgeCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), Edge.class);
                edgeCSVReader.parseCSV();
            }
            case "LocationName" -> {
                CSVReader<LocationName> locationCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), LocationName.class);
                locationCSVReader.parseCSV();
            }
            case "Moves" -> {
                CSVReader<Move> moveCSVReader = new CSVReader<>(selectedFile.getAbsolutePath(), Move.class);
                moveCSVReader.parseCSV();
            }
        }
        tableComboBox.setValue(null);
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
        String choice = tableComboBox.getValue();
        chooseDirectory();
        switch (choice) {
            case "Node" -> {
                CSVWriter<Node> nodeCSVWriter = new CSVWriter<>();
                nodeCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/Nodes.csv", null);
            }
            case "Edge" -> {
                CSVWriter<Edge> edgeCSVWriter = new CSVWriter<>();
                edgeCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/Edges.csv", null);
            }
            case "LocationName" -> {
                CSVWriter<LocationName> locationCSVWriter = new CSVWriter<>();
                locationCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/LocationNames.csv", null);
            }
            case "Moves" -> {
                CSVWriter<Move> moveCSVWriter = new CSVWriter<>();
                moveCSVWriter.writeCSV(selectedDirectory.getAbsolutePath() + "/Moves.csv", null);
            }
        }
        selectedDirectory = null;
        tableComboBox.setValue(null);
    }

}
