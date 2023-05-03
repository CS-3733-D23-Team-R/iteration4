package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.CartObserver;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.requestdb.*;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Math.round;

public class ServiceRequestCartController extends CartObserver {

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

    DecimalFormat formatPrice = new DecimalFormat("###.00");

    private RequestDatabase reqDatabase = new RequestDatabase();

    @FXML public void initialize() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        this.cartInstance.attach(this);
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

        if(this.cartInstance.items.isEmpty()){
            Text emptyLabel = new Text("Empty Cart");
            emptyLabel.setFill(Color.BLACK);
            //emptyLabel.setFont();
            cartPane.getChildren().add(emptyLabel);
        }else {
            refreshCart();
        }
        if(UserData.getInstance().isLoggedIn()){
            userField.setValue(UserData.getInstance().getLoggedIn().getFullName());
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
        quantity.setStyle("fx-padding:5px;");
        quantity.setFill(Color.BLACK);
        quantity.setStyle("-fx-font-size: 18");

        MFXButton plusButton = new MFXButton("+");
        plusButton.setUserData(item.getItemName());
        plusButton.setOnAction(event -> {
            this.cartInstance.incrementItem(item);
            quantity.setText(String.valueOf(this.cartInstance.items.get(cartInstance.itemInList(item))));
            this.totalPriceLabel.setText("$" + formatPrice.format(this.cartInstance.calculateTotal()));
        });

        MFXButton minusButton = new MFXButton("-");
        minusButton.setUserData(item.getItemName());
        minusButton.setOnAction(event -> {
            if(this.cartInstance.getItemQuantity(item) != 0) {
                this.cartInstance.decrementItem(item);
                quantity.setText(String.valueOf(String.valueOf(this.cartInstance.items.get(cartInstance.itemInList(item)))));
                this.totalPriceLabel.setText("$" + formatPrice.format(this.cartInstance.calculateTotal()));
                if (this.cartInstance.getItemQuantity(item) == 0) {
                    this.cartInstance.deleteItem(item);
                    refreshCart();
                }
            }
            if(this.cartInstance.items.isEmpty()) {
                cartPane.getChildren().clear();
                Text emptyLabel = new Text("Empty Cart");
                emptyLabel.setFill(Color.BLACK);
                cartPane.getChildren().add(emptyLabel);
            }
        });

        Text price;
        if (!item.getRequestType().equals(RequestType.Furniture) && item.getItemPrice() != null){
            price = new Text(String.valueOf("$"+ formatPrice.format(item.getItemPrice())));
        }
        else {
            price = new Text("$10.00");
            price.setVisible(false);
        }
        price.setStyle("-fx-font-size: 18");


        HBox nameBox = new HBox();
        nameBox.getChildren().add(productName);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        HBox quantityBox = new HBox();
        quantityBox.getChildren().addAll(plusButton,quantity, minusButton);
        quantityBox.setAlignment(Pos.CENTER_RIGHT);
        HBox priceBox = new HBox();
        priceBox.getChildren().add(price);
        priceBox.setAlignment(Pos.CENTER_RIGHT);

        layout.getChildren().addAll(nameBox, quantityBox, priceBox);
        layout.setSpacing(5);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        //HBox.setHgrow(quantityBox, Priority.ALWAYS);
        //HBox.setHgrow(priceBox, Priority.ALWAYS);

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
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setMaxWidth(280);
        vBox.setMinWidth(280);
        scrollPane.setContent(vBox);
        cartPane.getChildren().add(scrollPane);
        this.cartInstance.items.forEach(
                (item, number) -> {
                    HBox productView = shoppingCartView(cartInstance.itemInList(item),number);
                    vBox.getChildren().add(productView);
                }
        );
        HBox totalPriceLabel = totalView((float) this.cartInstance.calculateTotal());
        totalPriceLabel.setAlignment(Pos.BOTTOM_RIGHT);
        totalPriceLabel.setStyle("-fx-font-size: 18");
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        cartPane.getChildren().addAll(separator, totalPriceLabel);
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


        for (IAvailableItem itemInHash : this.cartInstance.items.keySet()){
            reqDatabase.addItemRequest(itemInHash.getRequestType(), RequestStatus.Unstarted, location, staff, itemInHash.getItemName(), requestor, additionalNotes, CurrentDateTime(), this.cartInstance.items.get(itemInHash));
        }

        cartPane.getChildren().clear();

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
                userNames.add(u.getName());
            }
            ObservableList<String> staff = FXCollections.observableArrayList(userNames);
            staffField.setItems(staff);
            userField.setItems(staff);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        refreshCart();
    }
}
