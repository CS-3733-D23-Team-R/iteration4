package edu.wpi.teamR.controllers;

import edu.wpi.teamR.datahandling.ShoppingCart;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import edu.wpi.teamR.requestdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ItemRequestController {

    @FXML
    private ComboBox<String> itemFilterByComboBox;

    @FXML
    private TextField itemMaxField;

    @FXML
    private TextField itemMinField;

    @FXML
    private TableView<?> itemTable;

    @FXML
    private Text itemTentoTwenty;

    @FXML
    private Text itemThirtytoForty;

    @FXML
    private Text itemTwentytoThirty;

    @FXML
    private MFXComboBox<RequestType> itemTypeBox;

    @FXML
    private Text itemZerotoTen;

    @FXML
    private Text itemThirtyPlus;


    @FXML public void initialize(){
        ObservableList<RequestType> itemTypeList = FXCollections.observableArrayList();
        for (RequestType type : RequestType.values()) {itemTypeList.add(type);} //add all request types to combo box
        itemTypeBox.setItems(itemTypeList);

        itemFilterByComboBox.setItems(FXCollections.observableArrayList("Unsorted", "Price: Low to High", "Price: High to Low"));

        itemZerotoTen.setOnMouseEntered(event -> textHover(itemZerotoTen));
        itemZerotoTen.setOnMouseExited(event -> textStopHover(itemZerotoTen));

        itemTentoTwenty.setOnMouseEntered(event -> textHover(itemTentoTwenty));
        itemTentoTwenty.setOnMouseExited(event -> textStopHover(itemTentoTwenty));

        itemTwentytoThirty.setOnMouseEntered(event -> textHover(itemTwentytoThirty));
        itemTwentytoThirty.setOnMouseExited(event -> textStopHover(itemTentoTwenty));

        itemThirtytoForty.setOnMouseEntered(event -> textHover(itemThirtytoForty));
        itemThirtytoForty.setOnMouseExited(event -> textStopHover(itemThirtytoForty));

        itemThirtyPlus.setOnMouseEntered(event -> textHover(itemThirtyPlus));
        itemThirtyPlus.setOnMouseExited(event -> textStopHover(itemThirtyPlus));
    }

    private void textHover(Text text){
        text.setFill(Color.WHITE);
    }

    private void textStopHover(Text text){
        text.setFill(Color.RED);
    }
}
