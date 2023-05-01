package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.controllers.mapeditor.*;
import edu.wpi.teamR.archive.CSVParameterException;
import edu.wpi.teamR.archive.CSVWriter;
import edu.wpi.teamR.datahandling.MapStorage;
import edu.wpi.teamR.mapdb.update.*;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import edu.wpi.teamR.mapdb.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import javafx.geometry.Point2D;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PopOver;

public class MapEditorController {
    @FXML
    Button importCSVButton;
    @FXML Button exportCSVButton;
    @FXML Button saveButton;
    @FXML Button newLocationButton;
    @FXML Button newNodeButton;
    @FXML Button newEdgeButton;
    @FXML Button newMoveButton;
    @FXML Button undoButton;
    @FXML Button editLocationButton;
    @FXML Button cancelEdgeButton;
    @FXML Button futureMoveButton;

    @FXML
    BorderPane borderPane;
    @FXML AnchorPane anchorPane;

    @FXML
    GesturePane gesturePane;

    @FXML Text dialogText;

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

    final private AnchorPane mapPane = new AnchorPane();

    HashMap<String, Integer> floorNamesMap = new HashMap<>();

    private MapDatabase mapdb;
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    ArrayList<LocationName> locationNames;
    ArrayList<Move> moves;
    Node selectedNode;
    Circle selectedCircle;
    boolean drawEdgesMode = false;
    @FXML
    HBox edgeHBox;

    MapUpdater updater;

    Map<Integer, Circle> circlesMap = new HashMap<>();
    Map<Integer, List<Line>> linesMap = new HashMap<>();

    Map<Integer, Node> nodeMap = new HashMap<>();
    Boolean dragged = false;

    @FXML
    CheckComboBox<String> locationFilters;
    ObservableList<String> locationTypes =
            FXCollections.observableArrayList("Select All", "Lab", "Elevator", "Services", "Conference Room", "Stairs", "Information", "Restroom", "Department", "Bathroom", "Exit", "Retail");
    HashMap<String, String> locationMap = new HashMap<>();

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
    HashMap<Circle, Node> alignmentNodesList = new HashMap<>();
    ArrayList<Circle> alignmentCirclesList = new ArrayList<>();
    @FXML ImageView infoIcon;
    @FXML
    VBox dbBackupButton;
    @FXML VBox backupBox;

    Color textColor = Color.BLACK;
    Color pathColor = Color.web("#012D5A");

    boolean userAction = true;
    boolean createNode = false;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        for (int i = 0; i < 5; i++) {
            nodePanes[i] = new AnchorPane();
            floorNamesMap.put(nodeFloorNames[i], i);
            floorNamesMap.put(floorNames[i], i);
        }

        saveButton.setOnAction(event -> {
            try {
                saveChanges();
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        newLocationButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/NewLocationPopup.fxml"));
            try {
                Parent popupRoot = loader.load();
                NewLocationPopupController popupController = loader.getController();
                popupController.setMapUpdater(updater);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Create New Location");
                popupStage.setScene(new Scene(popupRoot, 400, 200));
                RootController root = RootController.getInstance();
                root.setPopupState(true);
                popupStage.showAndWait();
                root.setPopupState(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        newNodeButton.setOnAction(event -> {
            edgeDialog(true);
            dialogText.setText("Click to add node to point");
            createNode = true;
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setNewNodeEvent();
            }
        });


        edgeDialog(false);
        newEdgeButton.setOnAction(event -> {
            drawEdgesMode=true;
            edgeDialog(true);
        });

        undoButton.setOnAction(event -> {
            try {
                undoAction();
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


        imageView = new ImageView(linkArray[currentFloor].toExternalForm());
        gesturePane.setContent(mapPane);
        mapPane.getChildren().add(imageView);
        gesturePane.setMinScale(0.25);
        gesturePane.setMaxScale(2);

        try {
            mapdb = App.getMapData().getMapdb();
            updater = new MapUpdater(mapdb);
            nodes = App.getMapData().getNodes();
            edges = App.getMapData().getEdges();
            locationNames = App.getMapData().getLocationNames();
            moves = App.getMapData().getMoves();
            ArrayList<MapLocation> locations = mapdb.getMapLocationsByFloor(nodeFloorNames[currentFloor]);
            if (locations.size() > 0) {
                for (MapLocation location : locations) {
                    nodeMap.put(location.getNode().getNodeID(), location.getNode());
                }
                displayEdgesByFloor(currentFloor);
                displayNodesByFloor(currentFloor);
            }
            else {
                System.out.println("tables are empty");
            }
        }
        catch (ItemNotFoundException e) {
            e.printStackTrace();
        }

        editLocationButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/EditLocationPopup.fxml"));
            try {
                Parent popupRoot = loader.load();
                EditLocationPopupController popupController = loader.getController();
                popupController.setUpdater(updater);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Edit Location");
                popupStage.setScene(new Scene(popupRoot, 300, 400));
                RootController root = RootController.getInstance();
                root.setPopupState(true);
                popupStage.showAndWait();
                root.setPopupState(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        futureMoveButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/FutureMovesTable.fxml"));
            try {
                Parent popupRoot = loader.load();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Future Moves");
                popupStage.setScene(new Scene(popupRoot, 400, 500));
                RootController root = RootController.getInstance();
                root.setPopupState(true);
                popupStage.showAndWait();
                root.setPopupState(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        cancelEdgeButton.setOnAction(event -> {
            if (drawEdgesMode) {
                drawEdgesMode = false;
            }
            else if (createNode) {
                createNode = false;
            }
            edgeDialog(false);
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
                            userAction = true;
                        }
                        ObservableList<javafx.scene.Node> children = nodePanes[currentFloor].getChildren();
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

        setFloorButtonMap();

        floor3Button.setOnMouseClicked(event -> {
            try {
                changeFloor(4);
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        floor2Button.setOnMouseClicked(event -> {
            try {
                changeFloor(3);
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        floor1Button.setOnMouseClicked(event -> {
            try {
                changeFloor(2);
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        L1Button.setOnMouseClicked(event -> {
            try {
                changeFloor(1);
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        L2Button.setOnMouseClicked(event -> {
            try {
                changeFloor(0);
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        newMoveButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/NewMovePopup.fxml"));
            try {
                Parent popupRoot = loader.load();
                NewMovePopupController popupController = loader.getController();
                popupController.setUpdater(updater);

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Create New Move");
                popupStage.setScene(new Scene(popupRoot, 400, 200));
                RootController root = RootController.getInstance();
                root.setPopupState(true);
                popupStage.showAndWait();
                root.setPopupState(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        infoIcon.setOnMouseClicked(event -> {
            PopOver popOver = new PopOver();
            final FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/EditorInfoPopup.fxml"));
            Parent popup;
            try {
                popup = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            popOver.setContentNode(popup);
            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
            popOver.setAutoHide(true);

            popOver.show(infoIcon);
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                App.getPrimaryStage().getScene().setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.Z && event.isControlDown()) {
                        try {
                            undoAction();
                        } catch (SQLException | ItemNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });

        reset();
    }

    private void edgeDialog(boolean setting) {
        edgeHBox.setVisible(setting);
        edgeHBox.setManaged(setting);
        if (selectedCircle != null) {
            selectedNode = null;
            selectedCircle.setFill(pathColor);
            selectedCircle = null;
            drawEdgesMode = false;
        }
        if (drawEdgesMode) {
            dialogText.setText("Click Another Node to Draw Edge");
        }
    }

    public void changeFloor(int floorNum) throws SQLException, ItemNotFoundException {
        if (floorNum <= 4) {
            HBox currentFloorVbox = floorButtonMap.get(currentFloor);
            currentFloorVbox.getStyleClass().remove("floor-box-focused");
            currentFloorVbox.getStyleClass().add("floor-box");
            HBox newFloorVbox = floorButtonMap.get(floorNum);
            newFloorVbox.getStyleClass().remove("floor-box");
            newFloorVbox.getStyleClass().add("floor-box-focused");

            currentFloor = floorNum;
            imageView = new ImageView(linkArray[currentFloor].toExternalForm());
            mapPane.getChildren().clear();
            mapPane.getChildren().add(imageView);
            if (selectedNode == null) {
                reset();
            }
            setNewNodeEvent();
            redraw();
        }
    }

    public void reset() {
        // zoom to 1x
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.zoomTo(0.25, 0.25, new Point2D(2500, 1700));
        gesturePane.centreOn(new Point2D(2500, 1700));
    }


    public void displayNodesByFloor(int floor) throws SQLException, ItemNotFoundException {
        String f = nodeFloorNames[floor];
        mapPane.getChildren().add(nodePanes[floor]);

        ArrayList<MapLocation> mapLocations = mapdb.getMapLocationsByFloor(f);
        for (MapLocation l: mapLocations) {
            drawNode(l, floor);
        }
    }

    public void displayEdgesByFloor(int floor) throws SQLException, ItemNotFoundException {
        if (floor <= 4) {
            String f = nodeFloorNames[floor];
            ArrayList<Edge> edges = mapdb.getEdgesByFloor(f);
            for (Edge e: edges) {
                Node n1 = mapdb.getNodeByID(e.getStartNode());
                Node n2 = mapdb.getNodeByID(e.getEndNode());

                if (n1.getFloorNum().equals(n2.getFloorNum())) {
                    final Line l1 = new Line(n1.getXCoord(), n1.getYCoord(), n2.getXCoord(), n2.getYCoord());
                    l1.setStroke(pathColor);
                    l1.setStrokeWidth(4);
                    addLine(e.getStartNode(), l1);
                    addLine(e.getEndNode(), l1);
                    nodePanes[floor].getChildren().add(l1);
                    l1.toBack();

                    l1.setOnMouseClicked(event -> {
                        if (event.getButton().equals(MouseButton.SECONDARY)) {
                            updater.deleteEdge(n1.getNodeID(), n2.getNodeID());
                            try {
                                mapdb.deleteEdge(n1.getNodeID(), n2.getNodeID());
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            updater.endAction();
                            linesMap.remove(e.getStartNode());
                            linesMap.remove(e.getEndNode());
                            nodePanes[floor].getChildren().remove(l1);
                            nodePanes[floor].requestLayout();
                        }
                    });
                }
            }
        }
    }

    public void drawNode(MapLocation l, int floor) {
        if (floor <= 4) {
            Node n = l.getNode();

            Circle c = new Circle(n.getXCoord(), n.getYCoord(), 5, pathColor);
            nodePanes[floor].getChildren().add(c);
            circlesMap.put(n.getNodeID(), c);

            setupMapNode(floor, n, c);
        }
    }

    private void setupMapNode(int floor, Node n, Circle c) {
        c.setOnMouseClicked(event -> {
            if (event.isShiftDown()) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (alignmentCirclesList.contains(c)) {
                        alignmentNodesList.remove(c);
                        alignmentCirclesList.remove(c);
                        c.setStroke(Color.TRANSPARENT);
                        c.setStrokeWidth(0);
                    }
                    else {
                        alignmentNodesList.put(c, n);
                        alignmentCirclesList.add(c);
                        c.setStroke(Color.web("#F6BD38"));
                        c.setStrokeWidth(2);
                    }
                }
            }
            else if (event.getButton() == MouseButton.PRIMARY && !dragged && drawEdgesMode) {
                if (selectedNode == null) {
                    selectedNode = n;
                    selectedCircle = c;
                    selectedCircle.setFill(Color.YELLOW);
                } else {
                    if (selectedNode.equals(n)) {
                        selectedCircle.setFill(pathColor);
                        selectedNode = null;
                        drawEdgesMode = false;
                        edgeDialog(false);
                        return;
                    }
                    if (selectedNode.getFloorNum().equals(n.getFloorNum())) {
                        Line l1 = new Line(selectedNode.getXCoord(), selectedNode.getYCoord(), n.getXCoord(), n.getYCoord());
                        l1.setStrokeWidth(4);
                        l1.setStroke(pathColor);
                        nodePanes[floor].getChildren().add(l1);
                        addLine(n.getNodeID(), l1);
                        addLine(selectedNode.getNodeID(), l1);
                        l1.toBack();
                        selectedCircle.setFill(pathColor);
                        l1.setOnMouseClicked(evt -> {
                            if (evt.getButton() == MouseButton.SECONDARY) {
                                linesMap.remove(n.getNodeID());
                                nodePanes[floor].getChildren().remove(l1);
                                System.out.println("Edge removed");
                            }
                        });
                    }
                    else {
                        if (Math.abs(selectedNode.getXCoord() - n.getXCoord()) > 20 || Math.abs(selectedNode.getYCoord() - n.getYCoord()) > 20) {
                            dialogText.setText("Cannot create edge!");
                            selectedNode = null;
                            return;
                        }
                        String startNodeType;
                        String endNodeType;
                        try {
                            startNodeType = mapdb.getNodeTypeByNodeID(selectedNode.getNodeID());
                            endNodeType = mapdb.getNodeTypeByNodeID(n.getNodeID());
                        } catch (SQLException | ItemNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        if (!(startNodeType.equals(endNodeType) && (startNodeType.equals("STAI") || startNodeType.equals("ELEV")))) {
                            dialogText.setText("Cannot create edge!");
                            selectedNode = null;
                            return;
                        }
                    }
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/EdgeCreatedPopup.fxml"));
                    try {
                        Parent popupRoot = loader.load();
                        EdgeCreatedPopupController popupController = loader.getController();
                        popupController.setText(selectedNode.getNodeID(), n.getNodeID());

                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setTitle("Edge Created");
                        popupStage.setScene(new Scene(popupRoot, 400, 200));
                        RootController root = RootController.getInstance();
                        root.setPopupState(true);
                        popupStage.showAndWait();
                        root.setPopupState(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updater.addEdge(selectedNode.getNodeID(), n.getNodeID());
                    try {
                        mapdb.addEdge(selectedNode.getNodeID(), n.getNodeID());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    updater.endAction();
                    selectedNode = null;
                    drawEdgesMode = false;
                    edgeDialog(false);
                }
            }
            else if (dragged) {
                dragged = false;
            }
            else {
                if (event.getButton() == MouseButton.SECONDARY) {
                    if (alignmentNodesList.size() > 0) {
                        ContextMenu contextMenu = new ContextMenu();

                        MenuItem alignVertically = new MenuItem("Align Vertically");
                        MenuItem alignHorizontally = new MenuItem("Align Horizontally");

                        alignVertically.setOnAction(e -> {
                            if (alignmentCirclesList.size() > 0) {
                                for (Circle current: alignmentCirclesList) {
                                    current.setCenterX(alignmentCirclesList.get(0).getCenterX());
                                    current.setStroke(Color.TRANSPARENT);
                                    current.setStrokeWidth(0);

                                    Node associated = alignmentNodesList.get(current);
                                    try {
                                        updater.modifyCoords(associated.getNodeID(), (int)current.getCenterX(), (int)current.getCenterY());
                                        mapdb.modifyCoords(associated.getNodeID(), (int)current.getCenterX(), (int)current.getCenterY());
                                    } catch (SQLException | ItemNotFoundException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                                updater.endAction();
                            }
                            for (Circle current: alignmentCirclesList) {
                                Node associated = alignmentNodesList.get(current);
                                redrawEdges(associated);
                            }
                            alignmentNodesList.clear();
                            alignmentCirclesList.clear();
                        });
                        alignHorizontally.setOnAction(e -> {
                            if (alignmentNodesList.size() > 0) {
                                for (Circle current: alignmentCirclesList) {
                                    current.setCenterY(alignmentCirclesList.get(0).getCenterY());
                                    current.setStroke(Color.TRANSPARENT);
                                    current.setStrokeWidth(0);

                                    Node associated = alignmentNodesList.get(current);
                                    try {
                                        updater.modifyCoords(associated.getNodeID(), (int)current.getCenterX(), (int)current.getCenterY());
                                        mapdb.modifyCoords(associated.getNodeID(), (int)current.getCenterX(), (int)current.getCenterY());
                                    } catch (SQLException | ItemNotFoundException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                                updater.endAction();
                            }
                            for (Circle current: alignmentCirclesList) {
                                Node associated = alignmentNodesList.get(current);
                                redrawEdges(associated);
                            }
                            alignmentNodesList.clear();
                            alignmentCirclesList.clear();
                        });

                        contextMenu.getItems().addAll(alignVertically, alignHorizontally);

                        contextMenu.show(c, event.getScreenX(), event.getScreenY());
                    }
                    else if(gesturePane.isGestureEnabled() && alignmentNodesList.size() == 0) {
                        PopOver popOver = new PopOver();
                        final FXMLLoader loader =
                                new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/MapPopup.fxml"));
                        Parent popup;
                        try {
                            popup = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                        MapPopupController controller = loader.getController();
                        try {
                            controller.showNodeInformation(updater, n);
                        } catch (SQLException | ItemNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        popOver.setContentNode(popup);
                        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                        popOver.setAutoHide(true);

                        popOver.showingProperty().addListener((observable, oldValue, newValue) -> {
                            if (controller.isDeleteButtonPressed()) {
                                nodePanes[floor].getChildren().remove(c);
                                if(linesMap.containsKey(n.getNodeID())) {
                                    nodePanes[floor].getChildren().removeAll(linesMap.get(n.getNodeID()));
                                }
                            }
                        });
                        popOver.show(c);
                    }
                }
            }
        });

        c.setOnMouseDragged(dragEvent -> {
            if (dragEvent.getButton().equals(MouseButton.SECONDARY)|| drawEdgesMode || dragEvent.isShiftDown()) return;
            gesturePane.setGestureEnabled(false);
            c.setCenterX(dragEvent.getX());
            c.setCenterY(dragEvent.getY());
            c.setFill(Color.LIMEGREEN);
            dragged = true;
        });
        c.setOnMouseReleased(dragEvent -> {
            if (dragEvent.getButton().equals(MouseButton.SECONDARY) || drawEdgesMode || dragEvent.isShiftDown()) return;
            gesturePane.setGestureEnabled(true);
            c.setFill(pathColor);
            try {
                if(linesMap.containsKey(n.getNodeID())) {
                    nodePanes[floor].getChildren().removeAll(linesMap.get(n.getNodeID()));
                }
                List<Edge> n_edges;
                n_edges = mapdb.getEdgesByNode(n.getNodeID());
                Line l1 = null;
                for(Edge e: n_edges) {
                    Node startNode = mapdb.getNodeByID(e.getStartNode());
                    Node endNode = mapdb.getNodeByID(e.getEndNode());
                    if (n.getNodeID() == e.getStartNode() && endNode.getFloorNum().equals(nodeFloorNames[floor])) {
                        l1 = new Line(dragEvent.getX(), dragEvent.getY(), endNode.getXCoord(), endNode.getYCoord());
                    }
                    else if (n.getNodeID() == e.getEndNode() && startNode.getFloorNum().equals(nodeFloorNames[floor])){
                        l1 = new Line(startNode.getXCoord(), startNode.getYCoord(), dragEvent.getX(), dragEvent.getY());
                    }
                    if (l1 != null && !nodePanes[floor].getChildren().contains(l1)) {
                        l1.setStroke(pathColor);
                        l1.setStrokeWidth(4);
                        nodePanes[floor].getChildren().add(l1);
                        l1.toBack();
                        addLine(startNode.getNodeID(), l1);
                        addLine(endNode.getNodeID(), l1);
                    }
                }
                updater.modifyCoords(n.getNodeID(), (int) dragEvent.getX(), (int) dragEvent.getY());
                mapdb.modifyCoords(n.getNodeID(), (int) dragEvent.getX(), (int) dragEvent.getY());
                updater.endAction();
            } catch (SQLException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void redraw() throws SQLException, ItemNotFoundException {
        nodePanes[currentFloor].getChildren().clear();
        mapPane.getChildren().remove(nodePanes[currentFloor]);
        displayEdgesByFloor(currentFloor);
        displayNodesByFloor(currentFloor);
        displayLocationNames(currentFloor);
    }

    public void undoAction() throws SQLException, ItemNotFoundException {
        List<UndoData> data = updater.undo();
        System.out.println(data);
        for (UndoData undo: data) {
            MapDataType type = undo.data().getDataType();
            switch (type) {
                case NODE -> {
                    EditType editType = undo.editType();
                    Node node = (Node) (undo.data());
                    nodes.add(node);
                    switch (editType) {
                        case ADDITION -> {
                            Circle add = circlesMap.get(node.getNodeID());
                            nodePanes[currentFloor].getChildren().remove(add);
                            nodes.remove(node);
                            mapdb.deleteNode(node.getNodeID());
                        }
                        case MODIFICATION -> {
                            Circle mod = circlesMap.get(node.getNodeID());
                            mod.setCenterX(node.getXCoord());
                            mod.setCenterY(node.getYCoord());
                            mapdb.modifyCoords(node.getNodeID(), node.getXCoord(), node.getYCoord());
                            redrawEdges(node);
                        }
                        case DELETION -> {
                            Circle deleted = new Circle(node.getXCoord(), node.getYCoord(), 4, pathColor);

                            setupMapNode(currentFloor, node, deleted);

                            nodePanes[currentFloor].getChildren().add(deleted);
                            nodes.add(node);
                            mapdb.addNode(node);
                        }
                    }
                }
                case EDGE -> {
                    EditType editType = undo.editType();
                    Edge edge = (Edge) (undo.data());
                    edges.add(edge);
                    switch(editType) {
                        case ADDITION -> {
                            List<Line> line_list = linesMap.get(edge.getStartNode());
                            nodePanes[currentFloor].getChildren().remove(line_list.get(line_list.size()-1));
                            edges.remove(edge);
                            mapdb.deleteEdge(edge.getStartNode(), edge.getEndNode());
                        }
                        case MODIFICATION -> {
                            Node startNode = mapdb.getNodeByID(edge.getStartNode());
                            Node endNode = mapdb.getNodeByID(edge.getEndNode());
                            if (linesMap.containsKey(edge.getStartNode())) {
                                nodePanes[currentFloor].getChildren().removeAll(linesMap.get(edge.getStartNode()));
                            }
                            if (linesMap.containsKey(edge.getEndNode())) {
                                nodePanes[currentFloor].getChildren().removeAll(linesMap.get(edge.getEndNode()));
                            }
                            Line line = new Line(startNode.getXCoord(), startNode.getYCoord(), endNode.getXCoord(), endNode.getYCoord());
                            line.setStrokeWidth(4);
                            line.setStroke(pathColor);

                            line.setOnMouseClicked(event -> {
                                if (event.getButton().equals(MouseButton.SECONDARY)) {
                                    updater.deleteEdge(startNode.getNodeID(), endNode.getNodeID());
                                    try {
                                        mapdb.deleteEdge(startNode.getNodeID(), endNode.getNodeID());
                                    } catch (SQLException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    updater.endAction();
                                    linesMap.remove(edge.getStartNode());
                                    linesMap.remove(edge.getEndNode());
                                    nodePanes[currentFloor].getChildren().remove(line);
                                    nodePanes[currentFloor].requestLayout();
                                }
                            });

                            nodePanes[currentFloor].getChildren().add(line);
                            edges.add(edge);
                            mapdb.addEdge(edge.getStartNode(), edge.getEndNode());
                        }
                        case DELETION -> {
                            Node startNode = mapdb.getNodeByID(edge.getStartNode());
                            Node endNode = mapdb.getNodeByID(edge.getEndNode());
                            Line line = new Line(startNode.getXCoord(), startNode.getYCoord(), endNode.getXCoord(), endNode.getYCoord());
                            line.setStrokeWidth(4);
                            line.setStroke(pathColor);
                            nodePanes[currentFloor].getChildren().add(line);
                            edges.add(edge);
                            mapdb.addEdge(edge.getStartNode(), edge.getEndNode());
                        }
                    }
                }
                case MOVE -> {
                    Move move = (Move) (undo.data());
                    EditType editType = undo.editType();
                    moves.add(move);
                    switch(editType) {
                        case ADDITION -> {
                            moves.remove(move);
                            mapdb.deleteMove(move.getNodeID(), move.getLongName(), move.getMoveDate());
                        }
                        case MODIFICATION -> {
                        }
                        case DELETION -> {
                            moves.add(move);
                            mapdb.addMove(move.getNodeID(), move.getLongName(), move.getMoveDate());
                        }
                    }
                }
                case LOCATION_NAME -> {
                    LocationName locationName = (LocationName) (undo.data());
                    locationNames.add(locationName);
                    mapdb.addLocationName(locationName.getLongName(), locationName.getShortName(), locationName.getNodeType());
                }
            }
        }
    }

    private void saveChanges() throws SQLException, ItemNotFoundException {
        updater.endAction();
        updater.clearUpdates();
        redraw();
    }

    private void addLine(int nodeID, Line line) {
        if (linesMap.containsKey(nodeID)) {
            linesMap.get(nodeID).add(line);
        } else {
            ArrayList<Line> lines = new ArrayList<>();
            lines.add(line);
            linesMap.put(nodeID, lines);
        }
    }

    private void redrawEdges(Node n) {
        try {
            if(linesMap.containsKey(n.getNodeID())) {
                nodePanes[currentFloor].getChildren().removeAll(linesMap.get(n.getNodeID()));
            }
            List<Edge> n_edges;
            n_edges = mapdb.getEdgesByNode(n.getNodeID());
            for(Edge e: n_edges) {
                Node startNode = mapdb.getNodeByID(e.getStartNode());
                Node endNode = mapdb.getNodeByID(e.getEndNode());
                if (startNode.getFloorNum().equals(endNode.getFloorNum())) {
                    final Line l1 = new Line(startNode.getXCoord(), startNode.getYCoord(), endNode.getXCoord(), endNode.getYCoord());
                    l1.setStroke(pathColor);
                    l1.setStrokeWidth(4);
                    addLine(e.getStartNode(), l1);
                    addLine(e.getEndNode(), l1);
                    nodePanes[currentFloor].getChildren().add(l1);
                    l1.toBack();

                    l1.setOnMouseClicked(event -> {
                        if (event.getButton().equals(MouseButton.SECONDARY)) {
                            updater.deleteEdge(startNode.getNodeID(), endNode.getNodeID());
                            try {
                                mapdb.deleteEdge(startNode.getNodeID(), endNode.getNodeID());
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            updater.endAction();
                            linesMap.remove(e.getStartNode());
                            linesMap.remove(e.getEndNode());
                            nodePanes[currentFloor].getChildren().remove(l1);
                            nodePanes[currentFloor].requestLayout();
                        }
                    });
                }
            }
        } catch (SQLException | ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
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

                        nodePanes[floor].getChildren().add(t);
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

    public void setFloorButtonMap(){
        floorButtonMap.put(0, L2Button);
        floorButtonMap.put(1, L1Button);
        floorButtonMap.put(2, floor1Button);
        floorButtonMap.put(3, floor2Button);
        floorButtonMap.put(4, floor3Button);
    }

    public void createNode(int x, int y) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/mapeditor/NewNodePopup.fxml"));
        try {
            Parent popupRoot = loader.load();
            NewNodePopupController popupController = loader.getController();
            popupController.setUpdater(updater);
            popupController.open(x,y, nodeFloorNames[currentFloor]);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Create New Node");
            popupStage.setScene(new Scene(popupRoot, 400, 200));
            RootController root = RootController.getInstance();
            root.setPopupState(true);
            popupStage.showAndWait();
            root.setPopupState(false);

            Node newNode = nodes.get(nodes.size()-1);
            Circle newCircle = new Circle(newNode.getXCoord(), newNode.getYCoord(), 4, pathColor);
            nodePanes[currentFloor].getChildren().add(newCircle);
            circlesMap.put(newNode.getNodeID(), newCircle);

            setupMapNode(currentFloor, newNode, newCircle);

            edgeDialog(false);
            createNode = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNewNodeEvent() {
        nodePanes[currentFloor].setOnMouseClicked(event -> {
            if (createNode) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                createNode(x,y);
            }
        });
    }
}
