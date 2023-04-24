package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.App;
import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.AlertDAO;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXCheckbox;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.animation.Interpolator;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.*;
import edu.wpi.teamR.pathfinding.*;
import edu.wpi.teamR.mapdb.*;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.SearchableComboBox;

public class MapController {
    @FXML
    Button resetButton;
    @FXML Button searchButton;
    @FXML Button floorUpButton;
    @FXML Button floorDownButton;
    @FXML MFXCheckbox accessibleCheckbox;

    @FXML
    SearchableComboBox<String> startField;
    @FXML
    SearchableComboBox<String> endField;
    @FXML
    SearchableComboBox<String> algorithmChoicebox;

    @FXML BorderPane borderPane;
    @FXML AnchorPane anchorPane;

    @FXML GesturePane gesturePane;

    @FXML Text floorText;
    @FXML Button clearButton;
    @FXML MFXCheckbox textCheckbox;

    @FXML
    MFXDatePicker moveDatePicker;
    AnchorPane[] locationPanes = new AnchorPane[5];

    ImageView imageView;

    private final Map<Integer, ImageView> floorMaps = new HashMap<>();
    int currentFloor = 2;

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

    AnchorPane[] paths = new AnchorPane[5];

    private final AnchorPane mapPane = new AnchorPane();

    HashMap<String, Integer> floorNamesMap = new HashMap<>();

    private MapDatabase mapdb;
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    ArrayList<LocationName> locationNames;
    ArrayList<Move> moves;

    ObservableList<String> algorithms =
            FXCollections.observableArrayList("A-Star", "Breadth-First Search", "Depth-First Search", "Dijkstra");

    Pathfinder pathfinder;

    @FXML
    HBox floor3Button;
    @FXML
    HBox floor2Button;
    @FXML
    HBox floor1Button;
    @FXML
    HBox L1Button;
    @FXML
    HBox L2Button;
    @FXML
    CheckComboBox<String> locationFilters;
    ObservableList<String> locationTypes =
            FXCollections.observableArrayList("Lab", "Elevator", "Services", "Conference Room", "Stairs", "Information", "Restroom", "Department", "Bathroom", "Exit", "Retail");
    HashMap<String, String> locationMap = new HashMap<>();

    UserDatabase userdb = new UserDatabase();
    ArrayList<Alert> alertList;
    @FXML Text alertText;


    @FXML
    public void initialize() throws Exception {
        mapdb = App.getMapData().getMapdb();
        nodes = App.getMapData().getNodes();
        edges = App.getMapData().getEdges();
        locationNames = App.getMapData().getLocationNames();
        moves = App.getMapData().getMoves();

        floorMaps.put(0, MapStorage.getLowerLevel2());
        floorMaps.put(1, MapStorage.getLowerLevel1());
        floorMaps.put(2, MapStorage.getFirstFloor());
        floorMaps.put(3, MapStorage.getSecondFloor());
        floorMaps.put(4, MapStorage.getThirdFloor());

        for (int i = 0; i < 5; i++) {
            paths[i] = new AnchorPane();
            locationPanes[i] = new AnchorPane();
            floorNamesMap.put(nodeFloorNames[i], i);
        }

        gesturePane.setContent(mapPane);
        mapPane.getChildren().add(floorMaps.get(currentFloor));
        gesturePane.setMinScale(0.25);
        gesturePane.setMaxScale(2);
        resetButton.setOnMouseClicked(event -> reset());
        clearButton.setOnMouseClicked(event -> clearPath());
        searchButton.setOnMouseClicked(event -> {
            try {
                search();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        floorText.setText(floorNames[currentFloor]);
        startField.setValue("Select Start");
        endField.setValue("Select End");

        reset();

        setChoiceboxes();

        pathfinder = new Pathfinder(mapdb);
        algorithmChoicebox.setItems(algorithms);
        algorithmChoicebox.setValue("A-Star");

        floor3Button.setOnMouseClicked(event -> {
            try {
                displayFloorNum(4);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        floor2Button.setOnMouseClicked(event -> {
            try {
                displayFloorNum(3);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        floor1Button.setOnMouseClicked(event -> {
            try {
                displayFloorNum(2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        L1Button.setOnMouseClicked(event -> {
            try {
                displayFloorNum(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        L2Button.setOnMouseClicked(event -> {
            try {
                displayFloorNum(0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        locationFilters.getItems().addAll(locationTypes);
        initializeLocationMap();
        locationFilters.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    if (locationFilters.getCheckModel().getCheckedItems().size() == 1) {
                        mapPane.getChildren().add(locationPanes[currentFloor]);
                    }
                    for (String s: change.getAddedSubList()) {
                        try {
                            displayLocationNamesByType(currentFloor, s);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                else if (change.wasRemoved()) {
                    locationPanes[currentFloor].getChildren().clear();
                    if (locationFilters.getCheckModel().getCheckedItems().size() == 0) {
                        mapPane.getChildren().remove(locationPanes[currentFloor]);
                    }
                    try {
                        displayLocationNames(currentFloor);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        moveDatePicker.setValue(LocalDate.now());

        alertList = userdb.getAlerts();
        if (alertList.size() > 0) {
            alertText.setText(alertList.get(alertList.size() - 1).getMessage());
        }
    }

    // Reset to original zoom
    public void reset() {
        // zoom to 1x
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.centreOn(new Point2D(2500, 1700));
    }

    public void clearPath() {
        paths[currentFloor].getChildren().clear();
        mapPane.getChildren().remove(paths[currentFloor]);
        for (int i = 0; i < 5; i++) {
            paths[i] = new AnchorPane();
        }
    }

    // zoom into a desired location
    // TODO fix zoomTo error
    public void animateZoomTo(int x, int y, int time) {
        // gesturePane.animate(Duration.millis(time)).zoomTo(1);
        // animate with some options
        gesturePane
                .animate(Duration.millis(time))
                .interpolateWith(Interpolator.EASE_BOTH)
                .afterFinished(() -> System.out.println("Done!"))
                .centreOn(new Point2D(x, y));
    }

    public void search() throws Exception {
    /*TODO
    take info from fields
    calculate route
    find spread of nodes on current floor
    animateZoom to show all nodes on this floor
    create path between nodes on ALL floors
    create/display textual path? (would have to add spot to display)
     */
        String start = startField.getValue();
        String end = endField.getValue();
        Boolean isAccessible = accessibleCheckbox.isSelected();
        displayPath(start, end, isAccessible);
    }

    public void displayFloorNum(int floorNum) throws SQLException {
        if (floorNum <= 4) {
            locationPanes[currentFloor].getChildren().clear();
            currentFloor = floorNum;
            imageView = floorMaps.get(currentFloor);
            mapPane.getChildren().clear();
            mapPane.getChildren().add(imageView);
            mapPane.getChildren().add(paths[currentFloor]);
            floorText.setText(floorNames[currentFloor]);
            
            if (locationFilters.getCheckModel().getCheckedItems().size() > 0) {
                displayLocationNames(currentFloor);
                mapPane.getChildren().add(locationPanes[currentFloor]);
            }

            reset();
        }
    }

    public void displayPath(String startLocation, String endLocation, Boolean accessible) throws Exception {
        clearPath();
        updatePathfindingAlgorithm(algorithmChoicebox.getValue());
        mapPane.getChildren().add(paths[currentFloor]);

        Move startNodeMove = mapdb.getLatestMoveForLocationByDate(startLocation, java.sql.Date.valueOf(moveDatePicker.getValue()));
        Move endNodeMove = mapdb.getLatestMoveForLocationByDate(endLocation, java.sql.Date.valueOf(moveDatePicker.getValue()));

        int startID = startNodeMove.getNodeID();
        int endID = endNodeMove.getNodeID();

        System.out.println("Start ID: " + startID + " End ID: " + endID);

        Path mapPath = pathfinder.findPath(startID, endID, accessible);
        ArrayList<Integer> currentPath = mapPath.getPath();

        Node startNode = mapdb.getNodeByID(startID);
        Node endNode = mapdb.getNodeByID(endID);

        if (!startNode.getFloorNum().equals(nodeFloorNames[currentFloor])){
            displayFloorNum(floorNamesMap.get(startNode.getFloorNum()));
        }

        createCircle(startNode, currentFloor, startField);

        int drawFloor = currentFloor;
        for (int i = 0; i < currentPath.size() - 1; i++) {
            Node n1 = mapdb.getNodeByID(currentPath.get(i));
            Node n2 = mapdb.getNodeByID(currentPath.get(i + 1));
            if (n1.getFloorNum().equals(nodeFloorNames[drawFloor]) && n2.getFloorNum().equals(nodeFloorNames[drawFloor])) {
                Line l1 = new Line(n1.getXCoord(), n1.getYCoord(), n2.getXCoord(), n2.getYCoord());
                l1.setStroke(Color.RED);
                l1.setStrokeWidth(4);
                paths[drawFloor].getChildren().add(l1);
            }
            else {
                Rectangle square = new Rectangle(n1.getXCoord(), n1.getYCoord(), 20, 20);
                square.setFill(Color.LIMEGREEN);
                int newFloor = floorNamesMap.get(n2.getFloorNum());
                square.setOnMouseClicked(event -> {
                    try {
                        displayFloorNum(newFloor);
                        gesturePane.zoomTo(1, 1, new Point2D(n2.getXCoord(), n2.getYCoord()));
                        gesturePane.centreOn(new Point2D(n2.getXCoord(), n2.getYCoord()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                paths[drawFloor].getChildren().add(square);
                Rectangle nextFloorRect = new Rectangle(n2.getXCoord(), n2.getYCoord(), 20, 20);
                nextFloorRect.setFill(Color.LIMEGREEN);
                paths[floorNamesMap.get(n2.getFloorNum())].getChildren().add(nextFloorRect);
                int finalDrawFloor = drawFloor;
                nextFloorRect.setOnMouseClicked(event -> {
                    try {
                        displayFloorNum(finalDrawFloor);
                        gesturePane.zoomTo(1, 1, new Point2D(n2.getXCoord(), n2.getYCoord()));
                        gesturePane.centreOn(new Point2D(n2.getXCoord(), n2.getYCoord()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                square.toFront();

                Text t = new Text("Click to go to next floor");
                t.setFill(Color.LIMEGREEN);
                t.setX(n1.getXCoord());
                t.setY(n1.getYCoord() + 30);
                paths[drawFloor].getChildren().add(t);
                t.toBack();

                t.setOnMouseClicked(event -> {
                    try {
                        displayFloorNum(newFloor);
                        gesturePane.zoomTo(1, 1, new Point2D(n2.getXCoord(), n2.getYCoord()));
                        gesturePane.centreOn(new Point2D(n2.getXCoord(), n2.getYCoord()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                drawFloor = newFloor;
            }
        }
        createCircle(endNode, drawFloor, endField);

        gesturePane.zoomTo(1, 1, new Point2D(startNode.getXCoord(), startNode.getYCoord()));
        gesturePane.centreOn(new Point2D(startNode.getXCoord(), startNode.getYCoord()));
    }

    private void createCircle(Node endNode, int drawFloor, SearchableComboBox<String> endField) {
        Circle end = new Circle(endNode.getXCoord(), endNode.getYCoord(), 5, Color.RED);
        Text endText = new Text(endField.getValue());
        if (locationFilters.getCheckModel().getCheckedItems().size() > 0) {
            endText.setText("");
        }
        endText.setX(endNode.getXCoord() + 10);
        endText.setY(endNode.getYCoord());
        endText.setFill(Color.RED);
        paths[drawFloor].getChildren().add(end);
        paths[drawFloor].getChildren().add(endText);
    }

    void setChoiceboxes(){
        ArrayList<LocationName> locationNodes = locationNames;
        ArrayList<String> names = new ArrayList<>();
        for (LocationName l: locationNodes) {
            if(!l.getLongName().contains("Hall")) {
                names.add(l.getLongName());
            }
        }
        ObservableList<String> choices = FXCollections.observableArrayList(names);
        FXCollections.sort(choices);
        startField.setItems(choices);
        endField.setItems(choices);
    }

    public void displayLocationNames(int floor) throws SQLException {
        if (floor <= 4) {
            ObservableList<String> checkedItems = locationFilters.getCheckModel().getCheckedItems();
            for (String s: checkedItems) {
                displayLocationNamesByType(floor, s);
            }
        }
    }

    public void displayLocationNamesByType(int floor, String type) throws SQLException {
        String f = nodeFloorNames[floor];
        ArrayList<MapLocation> locs = mapdb.getMapLocationsByFloor(f);
        if (locs.size() > 0) {
            for (MapLocation m: locs) {
                if (m.getLocationNames().size() > 0) {
                    String shortName = m.getLocationNames().get(0).getShortName();
                    String checkedType = locationMap.get(type);
                    if (checkedType.equals(m.getLocationNames().get(0).getNodeType())) {
                        Text t = new Text();
                        Node n = m.getNode();
                        t.setText(shortName);
                        t.setFill(Color.RED);
                        t.setX(n.getXCoord() + 10);
                        t.setY(n.getYCoord());

                        locationPanes[floor].getChildren().add(t);
                    }
                }
            }
        }
    }

    public void initializeLocationMap() {
        //"Lab", "Elevator", "Services", "Conference Room", "Stairs", "Information", "Restroom", "Department", "Bathroom", "Exit", "Retail"
        locationMap.put("Lab", "LAB");
        locationMap.put("Elevator", "ELEV");
        locationMap.put("Services", "SERV");
        locationMap.put("Conference Room", "CONF");
        locationMap.put("Stairs", "STAI");
        locationMap.put("Information", "INFO");
        locationMap.put("Restroom", "REST");
        locationMap.put("Department", "DEPT");
        locationMap.put("Bathroom", "BATH");
        locationMap.put("Exit", "EXIT");
        locationMap.put("Retail", "RETL");
    }

    private void updatePathfindingAlgorithm(String algo) {
        switch (algo) {
            case "A-Star" -> pathfinder.setAlgorithm(Algorithm.Astar);
            case "Breadth-First Search" -> pathfinder.setAlgorithm(Algorithm.BFS);
            case "Depth-First Search" -> pathfinder.setAlgorithm(Algorithm.DFS);
            default -> System.out.println("Error - invalid pathfinding algorithm.");
        }
    }
}