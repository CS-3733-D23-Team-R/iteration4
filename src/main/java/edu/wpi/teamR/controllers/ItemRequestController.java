package edu.wpi.teamR.controllers;

import edu.wpi.teamR.datahandling.ShoppingCart;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import edu.wpi.teamR.requestdb.*;
import javafx.fxml.FXML;

public class ItemRequestController {

    private ShoppingCart cart;


    @FXML
    MFXComboBox requestTypeBox;

    public ShoppingCart getShoppingCartInstance(){
        return cart;
    }

    @FXML public void initialize(){
        this.cart = cart;
        ObservableList<RequestType> requestTypeList = FXCollections.observableArrayList();
        requestTypeList.add(RequestType.flower);


        requestTypeBox.setItems();
    }
}
