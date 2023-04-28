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
import java.util.Iterator;
import java.util.Map;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
    StackPane floor3Button;
    @FXML
    StackPane floor2Button;
    @FXML
    StackPane floor1Button;
    @FXML
    StackPane L1Button;
    @FXML
    StackPane L2Button;
    HashMap<Integer, StackPane> floorButtonMap = new HashMap<>();
    @FXML
    CheckComboBox<String> locationFilters;
    ObservableList<String> locationTypes =
            FXCollections.observableArrayList("Select All", "Lab", "Elevator", "Services", "Conference Room", "Stairs", "Information", "Restroom", "Department", "Bathroom", "Exit", "Retail");
    HashMap<String, String> locationMap = new HashMap<>();

    UserDatabase userdb = new UserDatabase();
    ArrayList<Alert> alertList;
    @FXML Text alertText;
    @FXML StackPane alertPane;
    @FXML VBox textVBox;
    @FXML
    MFXScrollPane textScrollPane;
    @FXML VBox directionsVBox;
    @FXML ImageView closeAlert;

    Color textColor = Color.BLACK;
    Color pathColor = Color.web("#012D5A");

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
            floorNamesMap.put(nodeFloorNames[i], i);
        }

        gesturePane.setContent(mapPane);
        mapPane.getChildren().add(floorMaps.get(currentFloor));
        mapPane.getChildren().add(paths[currentFloor]);
        gesturePane.setMinScale(0.25);
        gesturePane.setMaxScale(2);
        resetButton.setOnMouseClicked(event -> reset());
        clearButton.setOnMouseClicked(event -> {
            try {
                clearPath();
                startField.setValue("Select Start");
                endField.setValue("Select End");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
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
                    for (String s: change.getAddedSubList()) {
                        try {
                            displayLocationNamesByType(currentFloor, s);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                else if (change.wasRemoved()) {
                    ObservableList<javafx.scene.Node> children = paths[currentFloor].getChildren();
                    for (javafx.scene.Node child : children) {
                        if (child instanceof Text) {
                            anchorPane.getChildren().remove(child);
                        }
                    }
                    try {
                        displayLocationNames(currentFloor);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Platform.runLater(() -> moveDatePicker.setValue(LocalDate.now()));

        alertList = userdb.getAlertsInLastNumDaysDesc(3);
        if (alertList.size() > 0) {
            alertText.setText(alertList.get(0).getMessage());
        }
        else {
            alertPane.setVisible(false);
            alertPane.setManaged(false);
        }

        setFloorButtonMap();
        textDirections(false);
        textCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            textDirections(newVal);
        });

        closeAlert.setOnMouseClicked(event -> {
            alertPane.setVisible(false);
            alertPane.setManaged(false);
        });
    }

    // Reset to original zoom
    public void reset() {
        // zoom to 1x
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.centreOn(new Point2D(2500, 1700));
    }

    public void clearPath() throws SQLException {
        paths[currentFloor].getChildren().clear();
        mapPane.getChildren().remove(paths[currentFloor]);
        for (int i = 0; i < 5; i++) {
            paths[i] = new AnchorPane();
        }
        displayLocationNames(currentFloor);
        directionsVBox.getChildren().clear();
        removeIndicators();
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
            StackPane currentFloorVbox = floorButtonMap.get(currentFloor);
            currentFloorVbox.getStyleClass().remove("floor-box-focused");
            currentFloorVbox.getStyleClass().add("floor-box");
            StackPane newFloorVbox = floorButtonMap.get(floorNum);
            newFloorVbox.getStyleClass().remove("floor-box");
            newFloorVbox.getStyleClass().add("floor-box-focused");

            currentFloor = floorNum;
            imageView = floorMaps.get(currentFloor);
            mapPane.getChildren().clear();
            mapPane.getChildren().add(imageView);
            mapPane.getChildren().add(paths[currentFloor]);
            floorText.setText(floorNames[currentFloor]);
            
            if (locationFilters.getCheckModel().getCheckedItems().size() > 0) {
                displayLocationNames(currentFloor);
            }

            reset();
        }
    }

    public void displayPath(String startLocation, String endLocation, Boolean accessible) throws Exception {
        int currentStage = 1;

        clearPath();
        mapPane.getChildren().add(paths[currentFloor]);
        updatePathfindingAlgorithm(algorithmChoicebox.getValue());

        Move startNodeMove = mapdb.getLatestMoveForLocationByDate(startLocation, java.sql.Date.valueOf(moveDatePicker.getValue()));
        Move endNodeMove = mapdb.getLatestMoveForLocationByDate(endLocation, java.sql.Date.valueOf(moveDatePicker.getValue()));

        int startID = startNodeMove.getNodeID();
        int endID = endNodeMove.getNodeID();

        Path mapPath = pathfinder.findPath(startID, endID, accessible);
        ArrayList<Integer> currentPath = mapPath.getPath();

        Node startNode = mapdb.getNodeByID(startID);
        Node endNode = mapdb.getNodeByID(endID);

        if (!startNode.getFloorNum().equals(nodeFloorNames[currentFloor])){
            displayFloorNum(floorNamesMap.get(startNode.getFloorNum()));
        }

        createCircle(startNode, currentFloor, startLocation);

        int drawFloor = currentFloor;
        for (int i = 0; i < currentPath.size() - 1; i++) {
            Node n1 = mapdb.getNodeByID(currentPath.get(i));
            Node n2 = mapdb.getNodeByID(currentPath.get(i + 1));
            if (n1.getFloorNum().equals(nodeFloorNames[drawFloor]) && n2.getFloorNum().equals(nodeFloorNames[drawFloor])) {
                Line l1 = new Line(n1.getXCoord(), n1.getYCoord(), n2.getXCoord(), n2.getYCoord());
                l1.setStroke(pathColor);
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
                        animateZoomTo(n2.getXCoord(), n2.getYCoord(), 5);
                        //gesturePane.zoomTo(1, 1, new Point2D(n2.getXCoord(), n2.getYCoord()));
                        //gesturePane.centreOn(new Point2D(n2.getXCoord(), n2.getYCoord()));
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
                nextFloorRect.toFront();

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

                Label indicator = new Label(Integer.toString(currentStage++));
                indicator.setTextFill(Color.RED);
                StackPane indicate_button = floorButtonMap.get(drawFloor);
                indicate_button.getChildren().add(indicator);
                int labelCount = 0;
                for (javafx.scene.Node currentItem: indicate_button.getChildren()) {
                    if (currentItem instanceof Label) {
                        labelCount++;
                    }
                }
                if (labelCount == 1){
                    indicator.setTranslateX(-25);
                }
                else {
                    indicator.setTranslateX(25);
                }

                drawFloor = newFloor;
            }
        }
        createCircle(endNode, drawFloor, endLocation);

        PathToText ptt = new PathToText(mapPath, java.sql.Date.valueOf(moveDatePicker.getValue()));
        ArrayList<String> textualDirections = ptt.getTextualPath();
        for (String dir: textualDirections) {
            Text curr = new Text(dir);
            curr.getStyleClass().add("body");
            directionsVBox.getChildren().add(curr);
        }

        Label indicator = new Label(Integer.toString(currentStage++));
        indicator.setTextFill(Color.RED);
        StackPane indicate_button = floorButtonMap.get(drawFloor);
        indicate_button.getChildren().add(indicator);
        int labelCount = 0;
        for (javafx.scene.Node currentItem: indicate_button.getChildren()) {
            if (currentItem instanceof Label) {
                labelCount++;
            }
        }
        if (labelCount == 1){
            indicator.setTranslateX(-25);
        }
        else {
            indicator.setTranslateX(25);
        }

        gesturePane.zoomTo(1, 1, new Point2D(startNode.getXCoord(), startNode.getYCoord()));
        gesturePane.centreOn(new Point2D(startNode.getXCoord(), startNode.getYCoord()));
    }

    private void createCircle(Node endNode, int drawFloor, String locationName) {
        Circle end = new Circle(endNode.getXCoord(), endNode.getYCoord(), 5, pathColor);
        Text endText = new Text(locationName);
        if (locationFilters.getCheckModel().getCheckedItems().size() > 0) {
            endText.setText("");
        }
        endText.setX(endNode.getXCoord() + 10);
        endText.setY(endNode.getYCoord());
        endText.setFill(textColor);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.WHITE);
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);

        endText.setEffect(dropShadow);
        endText.setFont(Font.font(18));
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
                        t.setFill(textColor);
                        DropShadow dropShadow = new DropShadow();
                        dropShadow.setColor(Color.WHITE);
                        dropShadow.setRadius(5);
                        dropShadow.setOffsetX(3);
                        dropShadow.setOffsetY(3);

                        t.setEffect(dropShadow);
                        t.setFont(Font.font(18));
                        t.setX(n.getXCoord() + 10);
                        t.setY(n.getYCoord());

                        t.setOnMouseClicked(event -> selectLocation(m.getLocationNames().get(0).getLongName()));

                        paths[floor].getChildren().add(t);
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
            case "Dijkstra" -> pathfinder.setAlgorithm(Algorithm.Dijkstra);
            default -> System.out.println("Error - invalid pathfinding algorithm.");
        }
    }

    public void setFloorButtonMap(){
        floorButtonMap.put(0, L2Button);
        floorButtonMap.put(1, L1Button);
        floorButtonMap.put(2, floor1Button);
        floorButtonMap.put(3, floor2Button);
        floorButtonMap.put(4, floor3Button);
    }

    public void selectLocation(String locationName){
        System.out.println(startField.getValue());
        if (startField.getValue().equals("Select Start") || startField.getValue() == null) {
            startField.setValue(locationName);
        }
        else if (endField.getValue().equals("Select End") || endField.getValue() == null) {
            endField.setValue(locationName);
        }
    }

    public void textDirections(boolean setting){
        textVBox.setVisible(setting);
        textVBox.setManaged(setting);
    }

    public void removeIndicators() {
        for (Map.Entry<Integer, StackPane> entry : floorButtonMap.entrySet()) {
            StackPane stackPane = entry.getValue();
            ObservableList<javafx.scene.Node> children = stackPane.getChildren();
            children.removeIf(child -> child instanceof Label);
        }
    }

}