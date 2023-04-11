package edu.wpi.teamR.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),
  SIGNAGE("views/Signage.fxml"),
  MealDelivery("views/MealDelivery.fxml"),
  MAP("views/Map.fxml"),
  LOGIN("view/Login.fxml"),
  FurnitureDelivery("views/FurnitureDelivery.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
