package edu.wpi.teamR.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),
  SIGNAGE("views/Signage.fxml"),
  MealDelivery("views/MealDelivery.fxml"),
  MAP("views/Map.fxml"),
  FurnitureDelivery("views/FurnitureDelivery.fxml"),
  LOGIN("views/Login.fxml"),
  ADMINHOME("views/AdminHome.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
