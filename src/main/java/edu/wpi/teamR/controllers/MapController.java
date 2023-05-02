package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.MapDatabase;
import io.github.palexdev.materialfx.controls.MFXCheckbox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
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
    HBox floor3Button;
    @FXML
    HBox floor2Button;
    @FXML
    HBox floor1Button;
    @FXML
    HBox L1Button;
    @FXML
    HBox L2Button;
    HashMap<Integer, HBox> floorButtonMap = new HashMap<>();
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

    boolean userAction = true;
    @FXML StackPane compassPane;
    @FXML HBox floorOrderHBox;

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
        startField.setValue(App.getKioskLocationString());
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
            if (userAction) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        if (change.getAddedSubList().contains("Select All")) {
                            userAction = false;
                            locationFilters.getCheckModel().checkAll();
                            locationFilters.setTitle("All");
                            try {
                                displayLocationNames(currentFloor);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            userAction = true;
                        }
                        else {
                            for (String s : change.getAddedSubList()) {
                                try {
                                    displayLocationNamesByType(currentFloor, s);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    else if (change.wasRemoved()) {
                        if (change.getRemoved().contains("Select All")) {
                            userAction = false;
                            locationFilters.getCheckModel().clearChecks();
                            locationFilters.setTitle("");
                            userAction = true;
                        }
                        ObservableList<javafx.scene.Node> children = paths[currentFloor].getChildren();
                        children.removeIf(child -> child instanceof Text);
                        try {
                            displayLocationNames(currentFloor);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });


        Platform.runLater(() -> moveDatePicker.setValue(LocalDate.now()));

        alertList = (ArrayList<Alert>) userdb.getCurrentAlerts();
        if (alertList.size() > 0) {
            alertText.setText(alertList.get(0).getMessage());
        }
        else {
            alertPane.setVisible(false);
            alertPane.setManaged(false);
        }

        setFloorButtonMap();
        textDirections(false);
        textCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> textDirections(newVal));

        closeAlert.setOnMouseClicked(event -> {
            alertPane.setVisible(false);
            alertPane.setManaged(false);
        });

        // compass code
        Circle base = new Circle(37, Color.LIGHTGRAY);
        base.setStroke(Color.SILVER);
        base.setStrokeWidth(2);

        Line needle = new Line(0, 0, 30, 0);
        needle.setStroke(Color.RED);
        needle.setStrokeWidth(3);
        needle.setRotate(90);

        compassPane.getChildren().addAll(base, needle);

        Label north = new Label("N");
        north.setTranslateX(-21);
        north.setTranslateY(-21);
        north.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        compassPane.getChildren().add(north);

        Label south = new Label("S");
        south.setTranslateX(21);
        south.setTranslateY(21);
        south.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        compassPane.getChildren().add(south);

        Label east = new Label("E");
        east.setTranslateX(21);
        east.setTranslateY(-21);
        east.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        compassPane.getChildren().add(east);

        Label west = new Label("W");
        west.setTranslateX(-21);
        west.setTranslateY(21);
        west.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        compassPane.getChildren().add(west);

        Label northeast = new Label("NE");
        northeast.setTranslateY(-30);
        northeast.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        northeast.setTextFill(Color.RED);
        compassPane.getChildren().add(northeast);

        Label southeast = new Label("SE");
        southeast.setTranslateX(30);
        southeast.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        compassPane.getChildren().add(southeast);

        Label southwest = new Label("SW");
        southwest.setTranslateY(30);
        southwest.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        compassPane.getChildren().add(southwest);

        Label northwest = new Label("NW");
        northwest.setTranslateX(-30);

        northwest.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        compassPane.getChildren().add(northwest);

        RotateTransition rotate = new RotateTransition(Duration.seconds(2), needle);
        rotate.setByAngle(10);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setAutoReverse(true);
        rotate.play();
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
                .centreOn(new Point2D(x, y));
    }

    public void search() throws Exception {
        String start = startField.getValue();
        String end = endField.getValue();
        Boolean isAccessible = accessibleCheckbox.isSelected();
        displayPath(start, end, isAccessible);
    }

    public void displayFloorNum(int floorNum) throws SQLException {
        if (floorNum <= 4) {
            HBox currentFloorVbox = floorButtonMap.get(currentFloor);
            currentFloorVbox.getStyleClass().remove("floor-box-focused");
            currentFloorVbox.getStyleClass().add("floor-box");
            HBox newFloorVbox = floorButtonMap.get(floorNum);
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

                drawFloor = newFloor;
            }
        }
        createCircle(endNode, drawFloor, endLocation);

        PathToText ptt = new PathToText(mapPath, java.sql.Date.valueOf(moveDatePicker.getValue()));
        ArrayList<TextByFloor> textualDirections = ptt.getTextualPathByFloor();
        for (int i = 0; i < textualDirections.size(); i++) {
            TextByFloor dir = textualDirections.get(i);
            Text nextFloorText = new Text("Floor: " + dir.getFloorNum());
            HBox newFloorSet = new HBox();
            newFloorSet.setAlignment(Pos.CENTER_LEFT);
            nextFloorText.getStyleClass().add("bodyMediumBold");
            newFloorSet.getChildren().addAll(nextFloorText);
            directionsVBox.getChildren().add(newFloorSet);
            if (i == textualDirections.size() -1) {
                Text traverseText = new Text(dir.getFloorNum());
                traverseText.getStyleClass().add("body");
                floorOrderHBox.getChildren().add(traverseText);
            }
            else {
                Text traverseText = new Text("Floor " + dir.getFloorNum() + " ");
                traverseText.getStyleClass().add("body");
                ImageView triangle = new ImageView();
                triangle.setFitWidth(20);
                triangle.setFitHeight(20);
                triangle.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/map/pathTriangle.png"))));
                floorOrderHBox.getChildren().add(traverseText);
                floorOrderHBox.getChildren().add(triangle);
            }
            ArrayList<String> floorText = dir.getFloorText();
            for (int j = 0; j < floorText.size(); j++) {
                String textDir = floorText.get(j);
                Text curr = new Text(textDir);
                HBox directionSet = new HBox();
                ImageView arrow = new ImageView();
                directionSet.setAlignment(Pos.CENTER_LEFT);
                arrow.setFitWidth(20);
                arrow.setFitHeight(20);
                if (textDir.contains("left")|| textDir.contains("Left")) {
                    arrow.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/leftArrowBlack.png"))));
                }
                else if (textDir.contains("right") || textDir.contains("Right")) {
                    arrow.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/rightArrowBlack.png"))));
                }
                else if (j == floorText.size() - 1 && i != textualDirections.size() - 1) {
                    int currFloor = floorNamesMap.get(dir.getFloorNum());
                    int nextFloor = floorNamesMap.get(textualDirections.get(i+1).getFloorNum());
                    if (currFloor < nextFloor) {
                        arrow.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/upArrowBlack.png"))));
                    }
                    else if (currFloor > nextFloor) {
                        arrow.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("images/downArrowBlack.png"))));
                    }
                }
                curr.getStyleClass().add("body");
                curr.setWrappingWidth(275);
                directionSet.getChildren().add(arrow);
                directionSet.getChildren().add(curr);
                directionSet.setSpacing(5);
                directionsVBox.getChildren().add(directionSet);
            }
            directionsVBox.getChildren().add(new Text(""));
        }

        floorOrderHBox.setVisible(true);
        floorOrderHBox.setManaged(true);
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
                if (!s.equals("Select All")) {
                    displayLocationNamesByType(floor, s);
                }
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
        floorOrderHBox.getChildren().clear();
        floorOrderHBox.setVisible(false);
        floorOrderHBox.setManaged(false);
    }
}