package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.requestdb.AvailableItem;
import edu.wpi.teamR.requestdb.IAvailableItem;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RequestType;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class ItemCard {

    @FXML
    private AnchorPane addToCartButton;

    @FXML
    private Text cardDescription;

    @FXML
    private VBox cardImage;

    @FXML
    private Text cardPrice;

    @FXML
    private Text cardTitle;

    private RequestType type;
    @FXML
    private Circle addToCartCircle;

    @FXML
    private ImageView addToCartCart;
//    @FXML
//    private Text addToCartPlus;

    private IAvailableItem item;

    public void initialize() {
        this.type = type;
//        addToCartPlus.setVisible(false);
        addToCartCircle.setOnMouseEntered(e -> {
            addToCartCircle.setFill(Color.valueOf("F6BD38"));
            addToCartCart.setVisible(false);
//            addToCartPlus.setVisible(true);
        });
        addToCartCircle.setOnMouseExited(e -> {
            addToCartCircle.setFill(Color.valueOf("012D5A"));
            addToCartCart.setVisible(true);
//            addToCartPlus.setVisible(false);
        });
        addToCartCircle.setOnMouseClicked(e -> {
            ShoppingCart cart = ShoppingCart.getInstance();
            cart.addItem(this.item, 1);
        });
    }

    public void setInfo(IAvailableItem item) {
        this.item = item;
        cardTitle.setText(item.getItemName());
        cardDescription.setText(item.getDescription());
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Double itemPrice = item.getItemPrice();
        String itemPriceString;
        if(itemPrice == null) itemPriceString = "";
        else itemPriceString = "$" + decimalFormat.format(itemPrice);
        cardPrice.setText(itemPriceString);
//        BackgroundImage bImg = new BackgroundImage(new Image(this.imageAddress), //TODO find images and set them
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.DEFAULT,
//                BackgroundSize.DEFAULT);
//        Background bGround = new Background(bImg);
//        cardImage.setBackground(bGround);
    }
}