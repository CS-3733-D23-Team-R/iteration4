package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Size;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class ServiceRequestCartController {

    @FXML AnchorPane cartAnchor;
    @FXML
    SearchableComboBox<String> userField;
    @FXML
    SearchableComboBox<String> locationField;
    @FXML
    SearchableComboBox<String> staffField;
    @FXML
    Text paymentField;
    @FXML public VBox cartPane;
    @FXML
    BorderPane cartBar;
    @FXML MFXButton submitButton;
    @FXML
    StackPane cartSlider;
    @FXML Text totalPriceLabel;
    @FXML MFXTextField notesField;
    ArrayList<LocationName> locationNames;
    //ArrayList<StaffMembers> staffMembers;

    ShoppingCart cartInstance = ShoppingCart.getInstance();

    DecimalFormat formatPrice = new DecimalFormat("###.00");

    private RequestDatabase reqDatabase = new RequestDatabase();


    @FXML public void initialize() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        locationField.setValue("Select location");
        staffField.setValue("Select staff");
        submitButton.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        locationNames = App.getMapData().getLocationNames();
//        AuthenticationDAO authDAO = new AuthenticationDAO();
//        staffMembers = authDAO.getUsers();

        setLocationChoiceboxes();
        setStaffChoiceBox();

        cartPane.getChildren().clear();
        refreshCart();

        if(cartInstance.items.isEmpty()){
            Text emptyLabel = new Text("Empty Cart");
            emptyLabel.setFill(Color.BLACK);
            //emptyLabel.setFont();
            cartPane.getChildren().add(emptyLabel);
        }else {
            refreshCart();
            HBox totalPriceLabel = totalView((float) cartInstance.calculateTotal());
            totalPriceLabel.setAlignment(Pos.BOTTOM_RIGHT);
            totalPriceLabel.setStyle("-fx-font-size: 18");
            Separator separator = new Separator();
            separator.setOrientation(Orientation.HORIZONTAL);
            cartPane.getChildren().addAll(separator, totalPriceLabel);
        }

    }

    // HBox function, sets up the given item in the shopping cart
    // input : ItemRequest item, Integer number
    // item -> item added to shopping cart
    // number -> number of that item added
    @FXML private HBox shoppingCartView(IAvailableItem item, Integer number){
        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER_RIGHT);
        //FileInputStream input = new FileInputStream("");
        //Image image = new Image(input);
        //ImageView imageView = new ImageView(image);
//        String imageURL = "images/serviceRequests/availableItems/";
//        switch(item.getRequestType()){
//            case Furniture -> imageURL = imageURL + "furniture/";
//            case Flower -> imageURL = imageURL + "flowers/";
//            case Supplies -> imageURL = imageURL + "supplies/";
//            case Meal -> imageURL = imageURL + "food/";
//        }
//        imageURL = imageURL + item.getImageReference();
//        Image requestImage = new Image(Objects.requireNonNull(Main.class.getResource(imageURL)).toExternalForm());
//        ImageView itemImage = new ImageView(requestImage);
//        double widthMax = 50;
//        double imageWidth = requestImage.getWidth();
//        double imageHeight = requestImage.getHeight();
//        if(imageWidth > widthMax) {imageWidth = widthMax;}
//        if(imageHeight > widthMax) {
//            imageHeight = widthMax;
//            if(imageWidth < widthMax){
//
//            }
//        }
//        Rectangle clipRect = new Rectangle(imageWidth, imageHeight);
//        itemImage.setImage(requestImage);
//        itemImage.setClip(clipRect);

        Text productName = new Text(item.getItemName());
        productName.setFill(Color.BLACK);
        productName.setStyle("-fx-font-size: 18");

        Text quantity = new Text(String.valueOf(number));
        quantity.setStyle("-fx-padding:5px;");
        quantity.setFill(Color.BLACK);
        quantity.setStyle("-fx-font-size: 18");

        MFXButton plusButton = new MFXButton("+");
        plusButton.setUserData(item.getItemName());
        plusButton.setOnAction(event -> {
            cartInstance.incrementItem(item);
            quantity.setText(String.valueOf(cartInstance.items.get(item)));
            this.totalPriceLabel.setText("$" + formatPrice.format(cartInstance.calculateTotal()));
        });

        MFXButton minusButton = new MFXButton("-");
        minusButton.setUserData(item.getItemName());
        minusButton.setOnAction(event -> {
            if(cartInstance.getItemQuantity(item) != 0) {
                cartInstance.decrementItem(item);
                quantity.setText(String.valueOf(String.valueOf(cartInstance.items.get(item))));
                this.totalPriceLabel.setText("$" + formatPrice.format(cartInstance.calculateTotal()));
                if (cartInstance.getItemQuantity(item) == 0) {
                    cartInstance.deleteItem(item);
                    refreshCart();
                }
            }
        });

        Text price = new Text(String.valueOf("$"+ formatPrice.format(item.getItemPrice())));
        price.setStyle("-fx-font-size: 18");

//        AnchorPane imageAnchorPane = new AnchorPane();
//        imageAnchorPane.setMaxWidth(widthMax);
//        imageAnchorPane.setMinWidth(widthMax);
//        imageAnchorPane.setMaxHeight(widthMax);
//        imageAnchorPane.setMinHeight(widthMax);
//        imageAnchorPane.getChildren().add(itemImage);

        layout.getChildren().addAll(productName, plusButton, quantity, minusButton, price);
        layout.setSpacing(5);
        return layout;
    }

    private HBox totalView(float totalPrice){
        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        Text totalLabel = new Text("Total: ");
        totalLabel.setFill(Color.BLACK);
        totalLabel.setStyle("-fx-font-size: 18");
        this.totalPriceLabel = new Text("$" + String.valueOf(formatPrice.format(totalPrice)));
        layout.getChildren().addAll(totalLabel, this.totalPriceLabel);
        return layout;
    }

    public void refreshCart(){
        cartPane.getChildren().clear();
        cartInstance.items.forEach(
                (item, number) -> {
                    HBox productView = shoppingCartView(item,number);
                    cartPane.getChildren().add(productView);
                }
        );
    }

    public Timestamp CurrentDateTime(){
        // LocalDateTime now = LocalDateTime.now();
        return new Timestamp(System.currentTimeMillis());
    }


    @FXML
    public void submit() throws SQLException, ClassNotFoundException {
        try {
            String location = locationField.getValue();
            String staff = staffField.getValue();
            String requestor = userField.getValue();
            String additionalNotes = notesField.getText();


        for (IAvailableItem itemInHash : cartInstance.items.keySet()){
            for(int i = 0; i<cartInstance.items.get(itemInHash); i++){
                reqDatabase.addItemRequest(itemInHash.getRequestType(), RequestStatus.Unstarted, location, staff, itemInHash.getItemName(), requestor, additionalNotes, CurrentDateTime());
            }
        }

        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setContentText("Request Order Received!");
        confirmation.show();

        } catch (NullPointerException e) {
            System.out.println("Request Submitted without Full Fields");
        }
    }

    void setLocationChoiceboxes(){
        ArrayList<LocationName> locationNodes = locationNames;
        ArrayList<String> names = new ArrayList<String>();
        for (LocationName l: locationNodes) {
            if(!l.getLongName().contains("Hall")) {
                names.add(l.getLongName());
            }
        }
        ObservableList<String> choices = FXCollections.observableArrayList(names);
        locationField.setItems(choices);
    }

    void setStaffChoiceBox(){
        try {
            ArrayList<User> users = new UserDatabase().getUsers();
            ArrayList<String> userNames = new ArrayList<>();
            for (User u: users){
                userNames.add(u.getStaffUsername());
            }
            ObservableList<String> staff = FXCollections.observableArrayList(userNames);
            staffField.setItems(staff);
            userField.setItems(staff);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
