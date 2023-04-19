package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.requestdb.AvailableItem;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.ItemRequestDAO;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import oracle.ucp.common.FailoverStats;
import org.controlsfx.control.SearchableComboBox;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class ServiceRequestCartController {

    @FXML
    MFXTextField userField;
    @FXML
    SearchableComboBox locationField;
    @FXML
    SearchableComboBox staffField;
    @FXML
    Text paymentField;
    @FXML public VBox cartPane;
    @FXML
    BorderPane cartBar;
    @FXML MFXButton submitButton;
    @FXML
    StackPane cartSlider;
    @FXML
    Text totalPriceLabel;
    ArrayList<LocationName> locationNames;

    DecimalFormat formatPrice = new DecimalFormat("###.##");


    @FXML public void initialize() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        //setChoiceboxes();
        userField.setText("Type Your Name");
        paymentField.setText("Charge to Room");
        locationField.setValue("Select a location");
        staffField.setValue("Select a staff member");
        submitButton.setOnMouseClicked(event -> submit());
        locationNames = App.getMapData().getLocationNames();
        totalPriceLabel.setText("$ " + formatPrice.format(ShoppingCart.getInstance().calculateTotal()));


        cartPane.getChildren().clear();

        if(ShoppingCart.getInstance().items.isEmpty()){
            Text emptyLabel = new Text("Empty Cart");
            emptyLabel.setStyle("fx-font-size: 24pt; fx-text-fill: white");
            cartPane.getChildren().add(emptyLabel);
        }else {
            ShoppingCart.getInstance().items.forEach(
                    (item, number) -> {
                        System.out.println(item);
                        System.out.println(number);
                        HBox productView = shoppingCartView(item,number);
                        cartPane.getChildren().add(productView);
                    }
            );
        }

    }

    // HBox function, sets up the given item in the shopping cart
    // input : ItemRequest item, Integer number
    // item -> item added to shopping cart
    // number -> number of that item added
    @FXML private HBox shoppingCartView(AvailableItem item, Integer number){
        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER_LEFT);
        //FileInputStream input = new FileInputStream("");
        //Image image = new Image(input);
        //ImageView imageView = new ImageView(image);

        Text productName = new Text(item.getItemName());
        productName.setStyle("-fx-text-fill: white;");

        Text quantity = new Text(String.valueOf(number));
        quantity.setStyle("-fx-padding:5px");
        quantity.setStyle("-fx-text-fill: white");

        MFXButton plusButton = new MFXButton("+");
        plusButton.setStyle("fx-padding: 5px");
        plusButton.setUserData(item.getItemName());
        plusButton.setOnAction(event -> {
            ShoppingCart.getInstance().incrementItem(item);
            quantity.setText(String.valueOf(ShoppingCart.getInstance().items.get(item)));
            this.totalPriceLabel.setText("$ " + formatPrice.format(ShoppingCart.getInstance().calculateTotal()));
        });

        MFXButton minusButton = new MFXButton("-");
        minusButton.setStyle("fx-padding: 5px");
        minusButton.setUserData(item.getItemName());
        minusButton.setOnAction(event -> {
            ShoppingCart.getInstance().decrementItem(item);
            quantity.setText(String.valueOf(String.valueOf(ShoppingCart.getInstance().items.get(item))));
            this.totalPriceLabel.setText("$ " + formatPrice.format(ShoppingCart.getInstance().calculateTotal()));
        });

        //Text price = new Text(String.valueOf("$"+ ShoppingCart.getInstance().items.get(item).

        layout.getChildren().addAll(productName, plusButton, quantity, minusButton);
        return layout;
    }

    /*private HBox totalView(float totalPrice){
        HBox layout = new HBox();
    }*/


    @FXML
    public void submit(){
        locationField.getValue().toString();
        staffField.getValue().toString();

        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setContentText("Request Order Received!");
        confirmation.show();
    }

    void setChoiceboxes(){
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

}
