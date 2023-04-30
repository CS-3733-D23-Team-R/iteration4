package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.RequestStatus;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.SearchableComboBox;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class SignageConfigurationController {
    public SignageConfigurationController() throws SQLException, ClassNotFoundException {
    }

    public void start(Stage primaryStage) {
        primaryStage.show();
    }
    @FXML Button backButton;
    @FXML Button submitButton;
    @FXML Button refreshButton;
    @FXML TextField idField;
    @FXML SearchableComboBox<String> locationBox;
    @FXML SearchableComboBox<Direction> arrowBox;
    @FXML MFXDatePicker datePicker;
    @FXML TableView<DirectionArrow> configurationTable;
    @FXML TableColumn<DirectionArrow, Direction> directionColumn;
    @FXML TableColumn<DirectionArrow, String> locationColumn;
    @FXML TableColumn<DirectionArrow, Date> dateColumn;
    @FXML TableColumn<DirectionArrow, Integer> idColumn;
    @FXML TableColumn<DirectionArrow, Void> deleteColumn;

    @FXML MFXComboBox kioskLocation;

    ObservableList<String> locationList;
    ObservableList<Direction> directionList;

    MapDatabase aMapDatabase = new MapDatabase();

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {

        locationColumn.setCellValueFactory(new PropertyValueFactory<>("longName"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("kioskID"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        for(DirectionArrow directionArrow : aMapDatabase.getDirectionArrows()){
            configurationTable.getItems().add(directionArrow);
        }

        ArrayList<String> tempList = new ArrayList<String>();
        try {
            for(LocationName locationName: aMapDatabase.getLocationNamesByNodeType("DEPT")){
                tempList.add(locationName.getLongName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        locationList = FXCollections.observableArrayList(tempList);
        locationBox.setItems(locationList);
        kioskLocation.setItems(locationList);
        kioskLocation.setText(App.getKioskLocationString());
        kioskLocation.setOnAction(event -> {
            App.setKioskLocationString(kioskLocation.getValue().toString());
        });

        ArrayList<Direction> tempList2 = new ArrayList<Direction>();
        try {
            tempList2.add(Direction.Up);
            tempList2.add(Direction.Down);
            tempList2.add(Direction.Left);
            tempList2.add(Direction.Right);
            tempList2.add(Direction.StopHere);
        } catch (Exception e) {
            e.printStackTrace();
        }
        directionList = FXCollections.observableArrayList(tempList2);
        arrowBox.setItems(directionList);

        Platform.runLater(() -> datePicker.setValue(LocalDate.now()));

        deleteButton();
        submitButton.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        refreshButton.setOnMouseClicked((event -> {
            try {
                refresh();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }));
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE));
    }

    private void deleteButton() {
        Callback<TableColumn<DirectionArrow, Void>, TableCell<DirectionArrow, Void>> cellFactory = new Callback<TableColumn<DirectionArrow, Void>, TableCell<DirectionArrow, Void>>() {
            @Override
            public TableCell<DirectionArrow, Void> call(final TableColumn<DirectionArrow, Void> param) {
                return new TableCell<DirectionArrow, Void>() {

                    private final Button button = new Button();
                    {
                        ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/delete.png")).toExternalForm());
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(30);
                        button.getStyleClass().add("signage-configuration-button:pressed");
                        button.setGraphic(imageView);
                        button.setOnAction((ActionEvent event) -> {
                            button.setBackground(Background.fill(Color.GRAY));
                            DirectionArrow data = getTableView().getItems().get(getIndex());
                            configurationTable.getItems().remove(data);
                            try {
                                aMapDatabase.deleteDirectionArrowByLongname(data.getLongName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        };

        deleteColumn.setCellFactory(cellFactory);
    }

    private void submit() throws SQLException, ClassNotFoundException {
        String loc = locationBox.getValue();
        Direction arrow = arrowBox.getValue();
        Date date = Date.valueOf(datePicker.getValue());
        int id = Integer.parseInt(idField.getText());

        try {
            aMapDatabase.addDirectionArrow(loc, id, arrow, date);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        locationBox.setValue(null);
        arrowBox.setValue(null);
        datePicker.setValue(null);
        idField.clear();
        refresh();
    }

    private void refresh() throws SQLException, ClassNotFoundException {
        configurationTable.getItems().clear();
        for(DirectionArrow directionArrow : aMapDatabase.getDirectionArrows()){
            configurationTable.getItems().add(directionArrow);
        }
        configurationTable.refresh();
    }
}
