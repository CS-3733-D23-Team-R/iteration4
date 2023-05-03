package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private MFXScrollPane cardScrollPane;
    @FXML AnchorPane itemRequestAnchorPane;

    boolean toggle = false;



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
    int cardsAcross = 3;
    double x;


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
        itemFilterByComboBox.setValue("Unsorted");
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
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                App.getRootPane().widthProperty().addListener((observable, oldValue, newValue) -> {
                    if(cardsAcross != 2 && newValue.doubleValue() < 1600) {
                        cardsAcross = 2;
                        regenerateCards();
                    }
                    else if(cardsAcross != 3 && newValue.doubleValue() >= 1600) {
                        cardsAcross = 3;
                        regenerateCards();
                    }
                });
                cardScrollPane.widthProperty().addListener((observable, oldVal, newVal) -> {
                    cardGridPane.setMaxWidth(newVal.doubleValue());
                    cardGridPane.setMinWidth(newVal.doubleValue());
                });

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
        VBox vBox = new VBox();
        cardGridPane.add(vBox, col, row);
        loader.setRoot(vBox);
        loader.load();
        ItemCard contentController = loader.getController();
        contentController.setInfo(item);
        //contentController.setWidthBind(cardGridPane);
        cardScrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double val = newValue.doubleValue()/cardsAcross - 20;
            contentController.getCardBase().setMaxWidth(val);
            contentController.getCardBase().setMinWidth(val);
            for(int i = 0; i < 3000; i++){}
        });
    }
    private void regenerateCards(){
        RequestDatabase requestDatabase = new RequestDatabase();
        cardGridPane.getChildren().clear();
//        cardGridPane.getColumnConstraints().clear();
//        cardGridPane.getRowConstraints().clear();
        RowConstraints row = new RowConstraints(458);
        ColumnConstraints col = new ColumnConstraints();
        if(toggle){
            Double val = cardScrollPane.widthProperty().doubleValue();
            cardGridPane.setMaxWidth(val);
            cardGridPane.setMinWidth(val);
        }
        else
            toggle = true;
        col.setHgrow(Priority.ALWAYS);
        //row.setVgrow(Priority.ALWAYS);
        cardGridPane.getColumnConstraints().setAll(col);
        for (int i = 1; i<cardsAcross; i++){
            cardGridPane.getColumnConstraints().add(col);
        }
        cardGridPane.getRowConstraints().setAll(row);
        ArrayList<IAvailableItem> filteredList = new ArrayList<>();
        cardGridPane.setHgap(20.0);
        cardGridPane.setVgap(20.0);
        try {
            switch(this.type){
                case Flower:
                    Boolean isBouquet = null;
                    if(button1Val != null && button1Val) isBouquet = true;
                    if(button2Val != null && button2Val) isBouquet = false;
                    if(button1Val != null && button1Val && button2Val != null && button2Val) isBouquet = null; //if both buttons are selected, all show up
                    filteredList.addAll(requestDatabase.getAvailableFlowersByAttributes(null, null, null, null, isBouquet, this.button3Val, this.sortOrder));
                    break;
                case Furniture:
                    ArrayList<AvailableFurniture> furnitureFilteredList = requestDatabase.getAvailableFurnitureByAttributes(null, null, null, this.button1Val, this.button2Val, this.button3Val, this.button4Val);
                    if(this.button5Val != null && this.button5Val) {
                        ArrayList<AvailableFurniture> tempList = requestDatabase.getAvailableFurnitureByAttributes(null, null, null, false, false, false, false);
                        for (AvailableFurniture f: tempList) {
                            if (f.isPillow() || f.isTable() || f.isSeating() || f.isStorage()) {
                                tempList.remove(f);
                            }
                        }
                        furnitureFilteredList.clear();
                        furnitureFilteredList.addAll(tempList);
                        FilterButton1.setSelected(false);
                        FilterButton2.setSelected(false);
                        FilterButton3.setSelected(false);
                        FilterButton4.setSelected(false);
                    }
                    filteredList.addAll(furnitureFilteredList);
                    break;
                case Supplies:
                    ArrayList<AvailableSupplies> suppliesFilteredList = requestDatabase.getAvailableSuppliesByAttributes(null, null, null, null, this.button2Val, this.button3Val, this.button4Val, this.button1Val, this.sortOrder);
                    if(this.button5Val != null && this.button5Val) {
                        ArrayList<AvailableSupplies> tempList = requestDatabase.getAvailableSuppliesByAttributes(null, null, null, null, false, false, false, false, this.sortOrder);
                        for (AvailableSupplies s: tempList) {
                            if (s.isPen() || s.isPaper() || s.isOrganization() || s.isComputerAccessory()) {
                                tempList.remove(s);
                            }
                        }
                        suppliesFilteredList.clear();
                        suppliesFilteredList.addAll(tempList);
                        FilterButton1.setSelected(false);
                        FilterButton2.setSelected(false);
                        FilterButton3.setSelected(false);
                        FilterButton4.setSelected(false);
                    }
                    filteredList.addAll(suppliesFilteredList);
                    break;
                default:
                    filteredList.addAll(requestDatabase.getAvailableMealsByAttributes(null, null, null, null, this.button2Val, this.button1Val, this.button5Val, this.button4Val, this.button3Val, this.sortOrder));
            }
            int rowCount = filteredList.size()/cardsAcross + 1;
            for (int r = 0; r < rowCount; r++){
                if(r != 0) {cardGridPane.getRowConstraints().setAll(row);}
                for (int c = 0; c < cardsAcross; c++){
                    if(filteredList.size() < r*cardsAcross + (c + 1)) {continue;} //if list is longer than cells in grid pane, continue until loop exit
                    IAvailableItem item = filteredList.get(r*cardsAcross + c);
                    loadCard(c, r, item);
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