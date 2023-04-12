package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RequestStatus;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;


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
    @FXML MFXButton backButton;
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


        for (ItemRequest request : RequestDatabase.getInstance().getItemRequests()) {
            requestTable.getItems().add(request);
        }


        staffMemberColumn.setOnEditCommit(event -> {
            ItemRequest request = event.getRowValue();
            request.setStaffMember(event.getNewValue());
            try {
                RequestDatabase.getInstance()(request.getRequestID(), request.getRequesterName(),request.getLocation(),request.getItemType(), event.getNewValue(), request.getAdditionalNotes(), request.getRequestDate(), request.getRequestStatus());
            } catch (SQLException | ClassNotFoundException e) {
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
                        FoodRequestDAO.getInstance().modifyFoodRequestByID(request.getRequestID(), request.getRequesterName(), request.getLocation(), request.getMealType(), request.getStaffMember(), request.getAdditionalNotes(), request.getRequestDate(), status);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    ServiceRequest sr = getTableView().getItems().get(getIndex());
                    changeStatusButton.getSelectionModel().selectItem(sr.getRequestStatus());
                    setGraphic(changeStatusButton);
                }
            }
        });

    }
}