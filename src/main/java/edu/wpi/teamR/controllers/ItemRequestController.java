package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.login.User;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import edu.wpi.teamR.requestdb.*;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import oracle.ucp.common.FailoverStats;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
    @FXML
    private ComboBox<String> itemFilterByComboBox;
    @FXML
    private Text otherFilterTitle;

    @FXML
    private ToggleButton FilterButton1;

    @FXML
    private ToggleButton FilterButton2;

    @FXML
    private ToggleButton FilterButton3;

    @FXML
    private ToggleButton FilterButton4;

    @FXML
    private ToggleButton FilterButton5;
    @FXML Button clearFiltersButton;
    @FXML BorderPane itemRequestPane;

    @FXML
    private GridPane cardGridPane;


    private RequestType type = RequestType.Meal;
    private Double lowerBound = null;
    private Double upperBound = null;
    private SortOrder sortOrder = SortOrder.unsorted;

    private  ArrayList<AvailableItem> items;

    private ObservableList<AvailableItem> obsItems;

    private ObservableList<String> priceList = FXCollections.observableArrayList("Unsorted", "Price: High to Low", "Price: Low to High");

    boolean cartOpen = false;


    @FXML public void initialize() throws IOException {

        ObservableList<RequestType> itemTypeList = FXCollections.observableArrayList();
//        for (RequestType type : RequestType.values()) {itemTypeList.add(type);} //add all request types to combo box
//        itemTypeBox.setItems(itemTypeList);

        changeTypeState(RequestType.Meal);
        foodButton.setSelected(true);


        furnitureButton.setOnAction(event -> {changeTypeState(RequestType.Furniture);});
        foodButton.setOnAction(event -> {changeTypeState(RequestType.Meal);});
        flowersButton.setOnAction(event -> {changeTypeState(RequestType.Flower);});
        suppliesButton.setOnAction(event -> {changeTypeState(RequestType.Supplies);});

        itemFilterByComboBox.setItems(priceList);
        itemFilterByComboBox.setOnAction(event -> {
            String comboType = itemFilterByComboBox.getSelectionModel().getSelectedItem();
            switch(comboType){
                case "Price: Low to High":
                    this.sortOrder = SortOrder.lowToHigh;
                    break;
                case "Price: High to Low":
                    this.sortOrder = SortOrder.highToLow;
                    break;
                default: //unsorted
                    this.sortOrder = SortOrder.unsorted;
            }
        });

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

    private void changeTypeState(RequestType type) {
        this.type = type;
        FilterButton1.setSelected(false);
        FilterButton2.setSelected(false);
        FilterButton3.setSelected(false);
        FilterButton4.setSelected(false);
        FilterButton5.setSelected(false);
        switch (this.type) {
            case Supplies:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("supplies-title");
                furnitureButton.setSelected(false);
                foodButton.setSelected(false);
                flowersButton.setSelected(false);
                otherFilterTitle.setText("Filter By");
                setButtonFourandFive(true);
                FilterButton1.setText("Computer");
                FilterButton2.setText("Paper");
                FilterButton3.setText("Pens");
                FilterButton4.setText("Organizational");
                FilterButton5.setText("Miscellaneous");
                break;
            case Flower:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("flower-title");
                furnitureButton.setSelected(false);
                foodButton.setSelected(false);
                suppliesButton.setSelected(false);
                otherFilterTitle.setText("Filter By");
                setButtonFourandFive(false);
                FilterButton1.setText("Bouquet");
                FilterButton2.setText("Individual Flower");
                FilterButton3.setText("Has Card");
                break;
            case Furniture:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("furniture-title");
                suppliesButton.setSelected(false);
                foodButton.setSelected(false);
                flowersButton.setSelected(false);
                otherFilterTitle.setText("Filter By");
                setButtonFourandFive(true);
                FilterButton1.setText("Seating");
                FilterButton2.setText("Tables");
                FilterButton3.setText("Pillows");
                FilterButton4.setText("Storage");
                FilterButton5.setText("Miscellaneous");
                break;
            default:
                requestPageBackground.getStyleClass().clear();
                requestPageBackground.getStyleClass().add("meal-title");
                furnitureButton.setSelected(false);
                suppliesButton.setSelected(false);
                flowersButton.setSelected(false);
                otherFilterTitle.setText("Dietary Restrictions");
                setButtonFourandFive(true);
                FilterButton1.setText("Vegetarian");
                FilterButton2.setText("Vegan");
                FilterButton3.setText("Gluten Free");
                FilterButton4.setText("Peanut Free");
                FilterButton5.setText("Dairy Free");
        }
    }

    private void setButtonFourandFive(Boolean state){
        FilterButton4.setVisible(state);
        FilterButton4.setManaged(state);
        FilterButton5.setVisible(state);
        FilterButton5.setManaged(state);
    }

    private Node loadCard(AvailableItem item) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/ItemCard.fxml"));
        Node node = loader.load();
        ItemCard contentController = loader.getController();
        contentController.setInfo(item);
        return node;
    }

    private void regenerateCards(){
        RequestDatabase requestDatabase = new RequestDatabase();
        try {
            ArrayList<AvailableItem> filteredList = new ArrayList<>();
            switch(this.type){
                default:
                    filteredList = requestDatabase.getAvailableItemsByTypeWithinRangeSortedByPrice(this.type, this.upperBound, this.lowerBound, this.sortOrder);
            }

            for (AvailableItem item : filteredList){
                cardGridPane.getChildren().add(loadCard(item));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
