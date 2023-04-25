package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
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
import javafx.scene.layout.BorderPane;
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
    @FXML
    private TextField itemMaxField;
    @FXML
    private TextField itemMinField;
    @FXML
    private TableView<AvailableItem> itemTable;
    @FXML
    private TableColumn<AvailableItem, String> itemNameColumn;
    @FXML
    private TableColumn<AvailableItem, String> itemDetailsColumn;
    @FXML
    private TableColumn<AvailableItem, Double> itemPriceColumn;
    @FXML
    private TableColumn<AvailableItem, Void> itemQuantityColumn;
    @FXML
    private TableColumn<AvailableItem, Void> itemAddToCartColumn;
    @FXML
    private Text itemTentoTwenty;
    @FXML
    private Text itemThirtytoForty;
    @FXML
    private Text itemTwentytoThirty;
//    @FXML
//    private MFXComboBox<RequestType> itemTypeBox;
    @FXML
    private Text itemZerotoTen;
    @FXML
    private Text itemThirtyPlus;
    @FXML ImageView cartButton;
    @FXML
    AnchorPane requestPageBackground;
    @FXML Button clearFiltersButton;
    @FXML BorderPane itemRequestPane;



    private RequestType type = RequestType.Meal;
    private Double lowerBound = null;
    private Double upperBound = null;
    private SortOrder sortOrder = SortOrder.unsorted;
    @FXML
    private Button minMaxGoButton;

    private  ArrayList<AvailableItem> items;

    private ObservableList<AvailableItem> obsItems;

    boolean cartOpen = false;


    @FXML public void initialize() throws IOException {

        ObservableList<RequestType> itemTypeList = FXCollections.observableArrayList();
//        for (RequestType type : RequestType.values()) {itemTypeList.add(type);} //add all request types to combo box
//        itemTypeBox.setItems(itemTypeList);

        clearFiltersButton.setOnAction(event -> clearFilters());

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
        if(!cartOpen) {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/ServiceRequestCart.fxml"));
            final BorderPane root = loader.load();
            itemRequestPane.setRight(root);
            cartOpen = true;
        } else {
            itemRequestPane.setRight(null);
            cartOpen = false;
        }


//        PopOver popover = new PopOver();
//        Parent popup;
//        popup = loader.load();
//        popover.setContentNode(popup);
//        popover.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
//        popover.setAutoHide(true);
//        popover.show(cartButton);
        System.out.println("opened");
    }

    private void addButtonToTable() {
        Callback<TableColumn<AvailableItem, Void>, TableCell<AvailableItem, Void>> cellFactory = new Callback<TableColumn<AvailableItem, Void>, TableCell<AvailableItem, Void>>() {
            @Override
            public TableCell<AvailableItem, Void> call(final TableColumn<AvailableItem, Void> param) {
                return new TableCell<AvailableItem, Void>() {

                    private final Button btn = new Button();
                    {
                        btn.getStyleClass().add("food_furniture-clear-button");
                        btn.setText("Add to Cart");
                        btn.setOnAction((ActionEvent event) -> {
                            AvailableItem data = getTableView().getItems().get(getIndex());

                            try {
                                ShoppingCart cartInstance = ShoppingCart.getInstance();
                                cartInstance.addItem(data, 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
//                        btn.setOnMouseClicked(event -> {
//                            btn.set("primaryLightGrey");
//                        });
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

        itemAddToCartColumn.setCellFactory(cellFactory);
    }

    private void regenerateTable(){
        try {
            items = new RequestDatabase().getAvailableItemsByTypeWithinRangeSortedByPrice(this.type, this.upperBound, this.lowerBound, this.sortOrder);
            obsItems = FXCollections.observableArrayList(items);
            itemTable.setItems(obsItems);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    private void clearFilters(){
        this.upperBound = null;
        this.lowerBound = null;
        itemMaxField.clear();
        itemMinField.clear();
        regenerateTable();
    }

}
