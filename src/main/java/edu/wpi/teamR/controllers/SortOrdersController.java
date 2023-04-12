package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RequestStatus;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.sql.SQLException;

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

    @FXML TableColumn<ItemRequest, Void> deleteCol;
    ObservableList<RequestStatus> statusList = FXCollections.observableArrayList(RequestStatus.values());


    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        staffMemberColumn.setCellValueFactory(new PropertyValueFactory<>("staffMember"));
        staffMemberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        //statusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        requestTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addButtonToTable();

        for (ItemRequest request : RequestDatabase.getInstance().getItemRequests()) {
            requestTable.getItems().add(request);
        }


        staffMemberColumn.setOnEditCommit(event -> {
            ItemRequest request = event.getRowValue();
            request.setStaffMember(event.getNewValue());
            try {
                RequestDatabase.getInstance().modifyItemRequestByID(request.getRequestID(), request.getRequesterName(),request.getLocation(), event.getNewValue(), request.getAdditionalNotes(), request.getRequestDate(), request.getRequestStatus(),request.getItemType());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        //dao = FoodRequestDAO.createInstance(""

        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final MFXComboBox<RequestStatus> changeStatusButton = new MFXComboBox<RequestStatus>(statusList);
            {
                //changeStatusButton.setValue();
                changeStatusButton.setOnAction(event -> {
                    ItemRequest request = (ItemRequest) getTableView().getItems().get(getIndex());
                    try {
                        RequestStatus status = changeStatusButton.getSelectionModel().getSelectedItem();
                        request.setRequestStatus(status);
                        RequestDatabase.getInstance().modifyItemRequestByID(request.getRequestID(), request.getRequesterName(), request.getLocation(), request.getStaffMember(), request.getAdditionalNotes(), request.getRequestDate(), status, request.getItemType());
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
                    changeStatusButton.getSelectionModel().selectItem(sr.getRequestStatus());
                    setGraphic(changeStatusButton);
                }
            }
        });

    }

    private void addButtonToTable() {
        Callback<TableColumn<ItemRequest, Void>, TableCell<ItemRequest, Void>> cellFactory = new Callback<TableColumn<ItemRequest, Void>, TableCell<ItemRequest, Void>>() {
            @Override
            public TableCell<ItemRequest, Void> call(final TableColumn<ItemRequest, Void> param) {
                final TableCell<ItemRequest, Void> cell = new TableCell<ItemRequest, Void>() {

                    private final Button btn = new Button();
                    {
                        ImageView imageView = new ImageView(Main.class.getResource("images/delete.png").toExternalForm());
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(30);
                        btn.getStyleClass().add("food_furniture-clear-button");
                        btn.setGraphic(imageView);
                        btn.setOnAction((ActionEvent event) -> {
                            ItemRequest data = getTableView().getItems().get(getIndex());
                            requestTable.getItems().remove(data);
                            try {
                                RequestDatabase.getInstance().deleteItemRequestByID(data.getRequestID());
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
                return cell;
            }
        };

        deleteCol.setCellFactory(cellFactory);
    }
}