package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ProfilePictureController {
    @FXML
    ImageView oneImage, twoImage, threeImage, fourImage, fiveImage, sixImage, sevenImage, eightImage, nineImage, tenImage;
    int imageID;
    boolean imageSet;
    Image returnImage = null;
    public void initialize() {
        Image image1 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/1.png")).toExternalForm());
        oneImage.setImage(image1);
        Image image2 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/2.png")).toExternalForm());
        twoImage.setImage(image2);
        Image image3 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/3.png")).toExternalForm());
        threeImage.setImage(image3);
        Image image4 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/4.png")).toExternalForm());
        fourImage.setImage(image4);
        Image image5 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/5.png")).toExternalForm());
        fiveImage.setImage(image5);
        Image image6 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/6.png")).toExternalForm());
        sixImage.setImage(image6);
        Image image7 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/7.png")).toExternalForm());
        sevenImage.setImage(image7);
        Image image8 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/8.png")).toExternalForm());
        eightImage.setImage(image8);
        Image image9 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/9.png")).toExternalForm());
        nineImage.setImage(image9);
        Image image10 = new Image(Objects.requireNonNull(Main.class.getResource("images/login/profilepictures/10.png")).toExternalForm());
        tenImage.setImage(image10);

        oneImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 1;
            returnImage = image1;
        });
        twoImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 2;
            returnImage = image2;
        });
        threeImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 3;
            returnImage = image3;
        });
        fourImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 4;
            returnImage = image4;
        });
        fiveImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 5;
            returnImage = image5;
        });
        sixImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 6;
            returnImage = image6;
        });
        sevenImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 7;
            returnImage = image7;
        });
        eightImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 8;
            returnImage = image8;
        });
        nineImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 9;
            returnImage = image9;
        });
        tenImage.setOnMouseClicked(event -> {
            imageSet = true;
            imageID = 10;
            returnImage = image10;
        });
    }

    public boolean isImageSet() {
        return imageSet;
    }

    public Image getReturnImage() {
        return returnImage;
    }

    public int getImageID() {
        return imageID;
    }
}
