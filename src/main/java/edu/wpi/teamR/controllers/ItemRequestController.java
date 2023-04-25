package edu.wpi.teamR.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import edu.wpi.teamR.requestdb.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
    @FXML
    private ScrollPane cardScrollPane;



    private RequestType type = RequestType.Meal;
//    private Double lowerBound = null;
//    private Double upperBound = null;
    private SortOrder sortOrder = SortOrder.unsorted;

    private Boolean button1Val = null;
    private Boolean button2Val = null;
    private Boolean button3Val = null;
    private Boolean button4Val = null;
    private Boolean button5Val = null;

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

        FilterButton1.setOnAction(event -> {if(FilterButton1.isSelected()){button1Val = true;} else {button1Val = null;} regenerateCards();});
        FilterButton2.setOnAction(event -> {if(FilterButton2.isSelected()){button2Val = true;} else {button2Val = null;} regenerateCards();});
        FilterButton3.setOnAction(event -> {if(FilterButton3.isSelected()){button3Val = true;} else {button3Val = null;} regenerateCards();});
        FilterButton4.setOnAction(event -> {if(FilterButton4.isSelected()){button4Val = true;} else {button4Val = null;} regenerateCards();});
        FilterButton5.setOnAction(event -> {if(FilterButton5.isSelected()){button5Val = true;} else {button5Val = null;} regenerateCards();});

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
            regenerateCards();
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
        button1Val = null;
        button2Val = null;
        button3Val = null;
        button4Val = null;
        button5Val = null;
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
        regenerateCards();
    }

    private void setButtonFourandFive(Boolean state){
        FilterButton4.setVisible(state);
        FilterButton4.setManaged(state);
        FilterButton5.setVisible(state);
        FilterButton5.setManaged(state);
    }

    private void loadCard(int col, int row, IAvailableItem item) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/ItemCard.fxml"));
        AnchorPane anchorPane = new AnchorPane();
        cardGridPane.add(anchorPane, col, row);
        loader.setRoot(anchorPane);
        loader.load();
        ItemCard contentController = loader.getController();
        contentController.setInfo(item);
    }

    private void regenerateCards(){
        RequestDatabase requestDatabase = new RequestDatabase();
        cardGridPane.getChildren().clear();
        cardGridPane.getColumnConstraints().clear();
        cardGridPane.getRowConstraints().clear();
        RowConstraints row = new RowConstraints(458);
        ColumnConstraints col = new ColumnConstraints(458);
        cardGridPane.getRowConstraints().setAll(row, row);
        cardGridPane.getColumnConstraints().setAll(col);
        ArrayList<? extends IAvailableItem> filteredList = new ArrayList<>();
        cardGridPane.setHgap(10.0);
        cardGridPane.setVgap(10.0);
        try {
            switch(this.type){
                case Flower:
                    Boolean isBouquet = null;
                    if(button1Val != null && button1Val) isBouquet = true;
                    if(button2Val != null && button2Val) isBouquet = false;
                    if(button1Val != null && button1Val && button2Val != null && button2Val) isBouquet = null; //if both buttons are selected, all show up
                    filteredList = requestDatabase.getAvailableFlowersByAttributes(null, null, null, null, isBouquet, this.button3Val, this.sortOrder);
                    break;
                case Furniture:
                    filteredList = requestDatabase.getAvailableFurnitureByAttributes(null, null, null, this.button1Val, this.button2Val, this.button3Val, this.button4Val);
//                    if(this.button5Val) { TODO fix misc button
//                        ArrayList<? extends IAvailableItem> miscList = requestDatabase.getAvailableFurnitureByAttributes(null, null, null, false, false, false, false);
//                        filteredList.addAll(miscList);
//                    }
                    break;
                case Supplies:
                    filteredList = requestDatabase.getAvailableSuppliesByAttributes(null, null, null, null, this.button2Val, this.button3Val, this.button4Val, this.button1Val, this.sortOrder);
                    //TODO add misc button
                    break;
                default:
                    filteredList = requestDatabase.getAvailableMealsByAttributes(null, null, null, null, this.button2Val, this.button1Val, this.button5Val, this.button4Val, this.button3Val, this.sortOrder);
            }
            int colCount = filteredList.size()/2 + 1;
            for (int c = 0; c < colCount; c++){
                if(c != 0){cardGridPane.getColumnConstraints().add(col);}
                for (int r = 0; r < 2; r++){
                    if(filteredList.size() < c*2 + r + 1) {continue;}
                    IAvailableItem item = filteredList.get(c*2 + r);
                    loadCard(r, c, item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        cardScrollPane.setPannable(false);
    }
}
