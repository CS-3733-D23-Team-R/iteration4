package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.mapdb.Direction;
import edu.wpi.teamR.mapdb.DirectionArrow;
import edu.wpi.teamR.mapdb.DirectionArrowDAO;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.RequestDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.Objects;

public class SignageConfigurationController {
    public SignageConfigurationController() throws SQLException, ClassNotFoundException {
    }

    public void start(Stage primaryStage) {
        primaryStage.show();
    }

    @FXML Button backButton;
    @FXML TableView<DirectionArrow> configurationTable;
    @FXML TableColumn<DirectionArrow, Direction> directionColumn;
    @FXML TableColumn<DirectionArrow, String> locationColumn;
    @FXML TableColumn<DirectionArrow, Integer> idColumn;
    @FXML TableColumn<DirectionArrow, Void> deleteColumn;

    MapDatabase aMapDatabase = new MapDatabase();

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {

        locationColumn.setCellValueFactory(new PropertyValueFactory<>("longname"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("kioskID"));
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));

        for(DirectionArrow directionArrow : aMapDatabase.getDirectionArrows()){
            configurationTable.getItems().add(directionArrow);
        }

        deleteButton();

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
                        button.getStyleClass().add("signage-configuration-button");
                        button.setGraphic(imageView);
                        button.setOnAction((ActionEvent event) -> {
                            DirectionArrow data = getTableView().getItems().get(getIndex());
                            configurationTable.getItems().remove(data);
                            try {
                                aMapDatabase.deleteDirectionArrowByLongname(data.getLongname());
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
}
