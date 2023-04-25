package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.requestdb.AvailableItem;
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

    public void initialize() {
        this.type = type;
        addToCartCircle.setOnMouseEntered(e -> {
            addToCartCircle.setFill(Color.valueOf("F6BD38"));
            addToCartCart.setVisible(false);
        });
        addToCartCircle.setOnMouseExited(e -> {
            addToCartCircle.setFill(Color.valueOf("012D5A"));
            addToCartCart.setVisible(true);
        });
        addToCartCircle.setOnMouseClicked(e -> {
            ShoppingCart cart = ShoppingCart.getInstance();
            RequestDatabase rDat = new RequestDatabase();
            AvailableItem thisItem = null;
            try {
                thisItem = rDat.getAvailableItemByName(cardTitle.getText(), type);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (ItemNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            cart.addItem(thisItem, 1);
        });
    }

    public void setInfo(String title, String description, String price, String imageAddress) {
        cardTitle.setText(title);
        cardDescription.setText(description);
        cardPrice.setText(price);
//        BackgroundImage bImg = new BackgroundImage(new Image(imageAddress), //TODO find images and set them
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.DEFAULT,
//                BackgroundSize.DEFAULT);
//        Background bGround = new Background(bImg);
//        cardImage.setBackground(bGround);
    }
}