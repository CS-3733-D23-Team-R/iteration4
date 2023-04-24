package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.datahandling.ShoppingCart;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import edu.wpi.teamR.requestdb.*;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import oracle.ucp.common.FailoverStats;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ItemRequestController {
    @FXML
    private ToggleButton flowersButton;

    @FXML
    private ToggleButton foodButton;

    @FXML
    private ToggleButton furnitureButton;

    @FXML
    private ToggleButton suppliesButton;
    @FXML ImageView cartButton;
    @FXML
    AnchorPane requestPageBackground;



    private RequestType type = RequestType.Meal;
    private Double lowerBound = null;
    private Double upperBound = null;
    private SortOrder sortOrder = SortOrder.unsorted;

    private  ArrayList<AvailableItem> items;

    private ObservableList<AvailableItem> obsItems;



    @FXML public void initialize(){

        ObservableList<RequestType> itemTypeList = FXCollections.observableArrayList();
//        for (RequestType type : RequestType.values()) {itemTypeList.add(type);} //add all request types to combo box
//        itemTypeBox.setItems(itemTypeList);

        changeBackground();

        furnitureButton.setOnAction(event -> {
            this.type = RequestType.Furniture;
            regenerateTable();
            changeBackground();
            suppliesButton.setSelected(false);
            foodButton.setSelected(false);
            flowersButton.setSelected(false);
        });
        foodButton.setOnAction(event -> {
            this.type = RequestType.Meal;
            regenerateTable();
            changeBackground();
            furnitureButton.setSelected(false);
            suppliesButton.setSelected(false);
            flowersButton.setSelected(false);
        });

        foodButton.setSelected(true);

        flowersButton.setOnAction(event -> {
            this.type = RequestType.Flower;
            regenerateTable();
            changeBackground();
            furnitureButton.setSelected(false);
            foodButton.setSelected(false);
            suppliesButton.setSelected(false);
        });
        suppliesButton.setOnAction(event -> {
            this.type = RequestType.Supplies;
            regenerateTable();
            changeBackground();
            furnitureButton.setSelected(false);
            foodButton.setSelected(false);
            flowersButton.setSelected(false);
        });


//        itemFilterByComboBox.setOnAction(event -> {
//            String comboType = itemFilterByComboBox.getSelectionModel().getSelectedItem();
//            switch(comboType){
//                case "Price: Low to High":
//                    this.sortOrder = SortOrder.lowToHigh;
//                    break;
//                case "Price: High to Low":
//                    this.sortOrder = SortOrder.highToLow;
//                    break;
//                default: //unsorted
//                    this.sortOrder = SortOrder.unsorted;
//            }
//            regenerateTable();
//        });


        cartButton.setOnMouseClicked(event -> {
            try {
                openCart();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void openCart() throws IOException {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/ServiceRequestCart.fxml"));
        PopOver popover = new PopOver();
        Parent popup;
        popup = loader.load();
        popover.setContentNode(popup);
        popover.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popover.setAutoHide(true);
        popover.show(cartButton);
        System.out.println("opened");
    }


    private void regenerateTable(){
    }

    private void changeBackground() {
        switch (this.type) {
            case Supplies:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("supplies-title");
                break;
            case Flower:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("flower-title");
                break;
            case Furniture:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("furniture-title");
                break;
            default:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("meal-title");
        }
    }

}
