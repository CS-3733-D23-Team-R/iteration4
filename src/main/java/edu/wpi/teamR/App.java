package edu.wpi.teamR;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import java.io.IOException;
import java.sql.SQLException;

import edu.wpi.teamR.datahandling.MapStorage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Setter @Getter private static Stage primaryStage;
  @Setter @Getter private static BorderPane rootPane;
  @Setter @Getter
  public static MapStorage mapData;

  @Setter @Getter public static String kioskLocationString = "75 Lobby Information Desk";

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {
    /* primaryStage is generally only used if one of your components require the stage to display */
    App.primaryStage = primaryStage;
    primaryStage.setTitle("Team R Hospital Application");
    Image icon = new Image(getClass().getResourceAsStream("images/bwhappicon.png"));
    primaryStage.getIcons().add(icon);

    final FXMLLoader splashLoader = new FXMLLoader(App.class.getResource("views/Splash.fxml"));
    final AnchorPane splashRoot = splashLoader.load();
    final Scene splashScene = new Scene(splashRoot);
    final Stage splashStage = new Stage();
    splashStage.getIcons().add(icon);
    splashStage.setScene(splashScene);
    splashStage.initStyle(StageStyle.UNDECORATED);
    splashStage.show();

    Thread thread = new Thread(() -> {
      try {
        final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/Root.fxml"));
        final BorderPane root = loader.load();

        App.rootPane = root;
        App.mapData = new MapStorage();

        final Scene scene = new Scene(root);
        Platform.runLater(() -> primaryStage.setScene(scene));
        Platform.runLater(() -> primaryStage.getScene().getStylesheets().add("https://fonts.googleapis.com/css2?family=Lato:wght@400;700&family=Nunito+Sans:wght@300;400;700&family=Quicksand&family=Roboto:wght@300;400;700&display=swap"));
        Platform.runLater(splashStage::close);
        Platform.runLater(() -> Navigation.navigate(Screen.HOME));
        Platform.runLater(() -> primaryStage.setMaximized(true));
        Platform.runLater(primaryStage::show);

      } catch (IOException e) {
        e.printStackTrace();
      } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    });
    thread.start();
  }

  @Override
  public void stop() throws SQLException {
    log.info("Shutting Down");
    closeConnection();
  }

  public void closeConnection() throws SQLException {
    Configuration.getConnection().close();
  }
}
