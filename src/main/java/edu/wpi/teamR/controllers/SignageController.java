package edu.wpi.teamR.controllers;

import edu.wpi.teamR.mapdb.DirectionArrow;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

import static edu.wpi.teamR.mapdb.Direction.*;

public class SignageController {
  public SignageController() throws SQLException, ClassNotFoundException {
  }

  public void start(Stage primaryStage) {
    primaryStage.show();
  }

  @FXML VBox signageBox;

  MapDatabase aMapDatabase = new MapDatabase();

  @FXML
  public void initialize() throws SQLException, ClassNotFoundException {

    signageBox.setBackground(Background.fill(Color.web("#012D5A")));
    setVisible();
  }

  private void setVisible() throws SQLException, ClassNotFoundException {
    for(DirectionArrow directionArrow : aMapDatabase.getDirectionArrows()){
      HBox hBox = new HBox();
      Text text = new Text();
      ImageView image = new ImageView();

      if(directionArrow.getDirection().equals(Up))
      {
        image.setImage(new Image(getClass().getResource("edu/wpi/teamR/images/upArrowWhite.png").getPath()));
      }
      else if(directionArrow.getDirection().equals(Down))
      {
        image.setImage(new Image(getClass().getResource("edu/wpi/teamR/images/downArrowWhite.png").getPath()));
      }
      else if(directionArrow.getDirection().equals(Left))
      {
        image.setImage(new Image(getClass().getResource("edu/wpi/teamR/images/leftArrowWhite.png").getPath()));
      }
      else if(directionArrow.getDirection().equals(Right))
      {
        image.setImage(new Image(getClass().getResource("edu/wpi/teamR/images/rightArrowWhite.png").getPath()));
      }

      text.setText(directionArrow.getLongname());
      hBox.getChildren().add(image);
      hBox.getChildren().add(text);
      signageBox.getChildren().add(hBox);
    }
  }
}
