package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RequestStatus;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import io.github.palexdev.materialfx.skins.legacy.MFXLegacyTableViewSkin;

import java.sql.SQLException;
import java.util.Objects;

public class SortOrdersController {

    @FXML TableView<ItemRequest> requestTable;
    @FXML TableColumn<ItemRequest, Integer> idColumn;
    @FXML TableColumn<ItemRequest, String> nameColumn;
    @FXML TableColumn<ItemRequest, String> locationColumn;
    @FXML TableColumn<ItemRequest, String> requestTypeColumn;
    @FXML TableColumn<ItemRequest, String> notesColumn;
    @FXML TableColumn<ItemRequest, String> staffMemberColumn;
    @FXML TableColumn<ItemRequest, String> timeColumn;
    @FXML TableColumn<ItemRequest, String> statusColumn;
    @FXML MFXTextField searchField;

    @FXML TableColumn<ItemRequest, Void> deleteCol;
    ObservableList<RequestStatus> statusList = FXCollections.observableArrayList(RequestStatus.values());
    private final ObservableList<ItemRequest> dataList = FXCollections.observableArrayList();
    FilteredList<ItemRequest> filteredData = new FilteredList<>(dataList, b -> true);

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        dataList.addAll(new RequestDatabase().getItemRequests());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("longname"));
        staffMemberColumn.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        staffMemberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        requestTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addButtonToTable();

        for (ItemRequest request : new RequestDatabase().getItemRequests()) {
            requestTable.getItems().add(request);
        }

        staffMemberColumn.setOnEditCommit(event -> {
            ItemRequest request = event.getRowValue();
            request.setStaffMember(event.getNewValue());
            try {
                //RequestDatabase.getInstance().modifyItemRequestByID(request.getRequestID(), request.getRequesterName(),request.getLongname(), event.getNewValue(), request.getAdditionalNotes(), request.getRequestDate(), request.getRequestStatus(),request.getItemType());
                new RequestDatabase().modifyItemRequestByID(
                        request.getRequestID(),
                        request.getRequestType(),
                        request.getRequestStatus(),
                        request.getLongName(),
                        event.getNewValue(),
                        request.getItemType(),
                        request.getRequesterName(),
                        request.getAdditionalNotes(),
                        request.getRequestDate());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<RequestStatus> changeStatusButton = new ComboBox<RequestStatus>(statusList);
            {
                changeStatusButton.setOnAction(event -> {
                    ItemRequest request = (ItemRequest) getTableView().getItems().get(getIndex());
                    try {
                        RequestStatus status = changeStatusButton.getSelectionModel().getSelectedItem();
                        request.setRequestStatus(status);
                        new RequestDatabase().modifyItemRequestByID(
                                request.getRequestID(),
                                request.getRequestType(),
                                status,
                                request.getLongName(),
                                request.getStaffUsername(),
                                request.getItemType(),
                                request.getRequesterName(),
                                request.getAdditionalNotes(),
                                request.getRequestDate());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    ItemRequest sr = getTableView().getItems().get(getIndex());
                    changeStatusButton.setValue(sr.getRequestStatus());

                    Circle circle = new Circle(8);
                    RequestStatus status = sr.getRequestStatus();
                    switch (status) {
                        case Unstarted:
                            circle.setFill(Color.RED);
                            break;
                        case Processing:
                            circle.setFill(Color.YELLOW);
                            break;
                        case Done:
                            circle.setFill(Color.LIMEGREEN);
                            break;
                        default:
                            circle.setFill(Color.TRANSPARENT);
                            break;
                    }

                    changeStatusButton.valueProperty().addListener((observable, oldValue, newValue) -> {
                        switch (newValue) {
                            case Unstarted:
                                circle.setFill(Color.RED);
                                break;
                            case Processing:
                                circle.setFill(Color.YELLOW);
                                break;
                            case Done:
                                circle.setFill(Color.GREEN);
                                break;
                            default:
                                circle.setFill(Color.TRANSPARENT);
                                break;
                        }
                    });

                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.getChildren().addAll(changeStatusButton, circle);

                    setGraphic(hbox);
                }
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData = new FilteredList<>(dataList, b -> true);
            filteredData.setPredicate(itemRequest -> {
                if(newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if(itemRequest.getRequesterName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (itemRequest.getLongName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (itemRequest.getRequestType().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (itemRequest.getAdditionalNotes().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (itemRequest.getRequestDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else
                    return itemRequest.getRequestStatus().toString().toLowerCase().contains(lowerCaseFilter);
            });
            SortedList<ItemRequest> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(requestTable.comparatorProperty());
            requestTable.setItems(sortedData);
            statusColumn.setVisible(false);
            statusColumn.setVisible(true);
        });
    }

    private void addButtonToTable() {
        Callback<TableColumn<ItemRequest, Void>, TableCell<ItemRequest, Void>> cellFactory = new Callback<TableColumn<ItemRequest, Void>, TableCell<ItemRequest, Void>>() {
            @Override
            public TableCell<ItemRequest, Void> call(final TableColumn<ItemRequest, Void> param) {
                return new TableCell<ItemRequest, Void>() {
                    private final Button btn = new Button();
                    {
                        ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/delete.png")).toExternalForm());
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(30);
                        btn.getStyleClass().add("food_furniture-clear-button");
                        btn.setGraphic(imageView);
                        btn.setOnAction((ActionEvent event) -> {
                            ItemRequest data = getTableView().getItems().get(getIndex());
                            filteredData.getSource().remove(data);
                            try {
                                new RequestDatabase().deleteItemRequest(data.getRequestID());
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
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        deleteCol.setCellFactory(cellFactory);
    }
}