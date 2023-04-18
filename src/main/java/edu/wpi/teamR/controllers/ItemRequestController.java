package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.datahandling.ShoppingCart;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import edu.wpi.teamR.requestdb.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.Objects;

public class ItemRequestController {

    @FXML
    private ComboBox<String> itemFilterByComboBox;

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
        itemTwentytoThirty.setOnMouseExited(event -> textStopHover(itemTwentytoThirty));

        itemThirtytoForty.setOnMouseEntered(event -> textHover(itemThirtytoForty));
        itemThirtytoForty.setOnMouseExited(event -> textStopHover(itemThirtytoForty));

        itemThirtyPlus.setOnMouseEntered(event -> textHover(itemThirtyPlus));
        itemThirtyPlus.setOnMouseExited(event -> textStopHover(itemThirtyPlus));

        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        itemDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        addButtonToTable();

        try {
            for (AvailableItem item : RequestDatabase.getInstance().getAvailableItemsByTypeWithinRangeSortedByPrice(RequestType.Meal, null, null, SortOrder.unsorted)) {
                itemTable.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void addButtonToTable() {
        Callback<TableColumn<AvailableItem, Void>, TableCell<AvailableItem, Void>> cellFactory = new Callback<TableColumn<AvailableItem, Void>, TableCell<AvailableItem, Void>>() {
            @Override
            public TableCell<AvailableItem, Void> call(final TableColumn<AvailableItem, Void> param) {
                return new TableCell<AvailableItem, Void>() {

                    private final Button btn = new Button();
                    {
                        ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/delete.png")).toExternalForm());
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(30);
                        btn.getStyleClass().add("food_furniture-clear-button");
                        btn.setGraphic(imageView);
                        btn.setOnAction((ActionEvent event) -> {
                            AvailableItem data = getTableView().getItems().get(getIndex());
                            itemTable.getItems().remove(data);
                            try {
                                ShoppingCart.getInstance().addItem(data, 1);
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

        itemAddToCartColumn.setCellFactory(cellFactory);
    }

    private void textHover(Text text){
        text.setFill(Color.WHITE);
    }

    private void textStopHover(Text text){
        text.setFill(Color.valueOf("#BDBDBD"));
    }
}
