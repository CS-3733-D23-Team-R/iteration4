package edu.wpi.teamR.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import edu.wpi.teamR.requestdb.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.util.ArrayList;

public class ItemRequestController {

//    @FXML
//    private ComboBox<String> itemFilterByComboBox;
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



    private RequestType type = RequestType.Meal;
    private Double lowerBound = null;
    private Double upperBound = null;
    private SortOrder sortOrder = SortOrder.unsorted;
    @FXML
    private Button minMaxGoButton;

    private  ArrayList<AvailableItem> items;

    private ObservableList<AvailableItem> obsItems;

    @FXML AnchorPane itemPane;



    @FXML public void initialize(){

        ObservableList<RequestType> itemTypeList = FXCollections.observableArrayList();
//        for (RequestType type : RequestType.values()) {itemTypeList.add(type);} //add all request types to combo box
//        itemTypeBox.setItems(itemTypeList);

        clearFiltersButton.setOnAction(event -> clearFilters());

        changeBackground();

        furnitureButton.setOnAction(event -> {
            this.type = RequestType.Furniture;
            changeBackground();
            suppliesButton.setSelected(false);
            foodButton.setSelected(false);
            flowersButton.setSelected(false);
        });
        foodButton.setOnAction(event -> {
            this.type = RequestType.Meal;
            changeBackground();
            furnitureButton.setSelected(false);
            suppliesButton.setSelected(false);
            flowersButton.setSelected(false);
        });

        foodButton.setSelected(true);

        flowersButton.setOnAction(event -> {
            this.type = RequestType.Flower;
            changeBackground();
            furnitureButton.setSelected(false);
            foodButton.setSelected(false);
            suppliesButton.setSelected(false);
        });
        suppliesButton.setOnAction(event -> {
            this.type = RequestType.Supplies;
            changeBackground();
            furnitureButton.setSelected(false);
            foodButton.setSelected(false);
            flowersButton.setSelected(false);
        });

//        itemFilterByComboBox.setItems(FXCollections.observableArrayList("Unsorted", "Price: Low to High", "Price: High to Low"));

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

        minMaxGoButton.setOnAction(event -> {
            this.lowerBound = Double.valueOf(itemMinField.getText());
            this.upperBound = Double.valueOf(itemMaxField.getText());
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
    private void textHover(Text text){
        text.setFill(Color.valueOf("F6BD38"));
    }

    private void textStopHover(Text text){
        text.setFill(Color.WHITE);
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
    }

}
