package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.requestdb.AvailableItem;
import edu.wpi.teamR.requestdb.IAvailableItem;
import edu.wpi.teamR.requestdb.RequestDatabase;
import edu.wpi.teamR.requestdb.RequestType;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Objects;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ItemCard extends VBox{

    @FXML
    private AnchorPane addToCartButton;

    @FXML
    private Text cardDescription;

    @FXML
    private AnchorPane cardImage;

    @FXML
    private Text cardPrice;

    @FXML
    private Text cardTitle;

    @FXML
    private Circle addToCartCircle;

    @FXML
    private ImageView addToCartCart;
    @FXML ImageView plusIcon;
    @FXML
    private ImageView cardImageView;

    private IAvailableItem item;

    @FXML HBox hBox;

    @FXML VBox cardBase;

    public void initialize() {
        plusIcon.setVisible(false);
        addToCartButton.setOnMouseEntered(e -> {
            addToCartCircle.setFill(Color.valueOf("F6BD38"));
            addToCartCart.setVisible(false);
            plusIcon.setVisible(true);
        });
        addToCartButton.setOnMouseExited(e -> {
            addToCartCircle.setFill(Color.valueOf("012D5A"));
            addToCartCart.setVisible(true);
            plusIcon.setVisible(false);
        });
        addToCartButton.setOnMouseClicked(e -> {
            ShoppingCart cart = ShoppingCart.getInstance();
            cart.addItem(this.item, 1);
        });
    }
    Rectangle2D rect;
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
        String imageURL = "images/serviceRequests/availableItems/";
        switch(item.getRequestType()){
            case Furniture -> imageURL = imageURL + "furniture/";
            case Flower -> imageURL = imageURL + "flowers/";
            case Supplies -> imageURL = imageURL + "supplies/";
            case Meal -> imageURL = imageURL + "food/";
        }
        imageURL = imageURL + item.getImageReference();
        Image freshCardImage = new Image(Objects.requireNonNull(Main.class.getResource(imageURL)).toExternalForm());
//        double imageAspectRatio = freshCardImage.getWidth() / freshCardImage.getHeight();
        double widthMax = 250;
//        double imageWidth = freshCardImage.getWidth();
//        double imageHeight = freshCardImage.getHeight();
//        if(imageWidth > widthMax) {imageWidth = widthMax;}
        cardImageView.setImage(freshCardImage);
        double newMeasure = (cardImageView.getImage().getWidth() < cardImageView.getImage().getHeight()) ? cardImageView.getImage().getWidth() : cardImageView.getImage().getHeight();
        double x = (cardImageView.getImage().getWidth() - newMeasure) / 2;
        double y = (cardImageView.getImage().getHeight() - newMeasure) / 2;

        rect = new Rectangle2D(x, y, newMeasure, newMeasure);
        cardImageView.setViewport(rect);
        cardImageView.setFitWidth(250);
        cardImageView.setFitHeight(250);
        cardImageView.setSmooth(true);
        Rectangle clipRect = new Rectangle(250, 250);
        clipRect.setArcWidth(90);
        clipRect.setArcHeight(90);
        cardImageView.setClip(clipRect);
    }

    public VBox getCardBase() {
        return cardBase;
    }

    public ImageView getImageView() {
        return cardImageView;
    }

}