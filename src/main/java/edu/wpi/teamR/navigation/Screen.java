package edu.wpi.teamR.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),
  SIGNAGE("views/Signage.fxml"),
  MEAL_REQUEST("views/MealDelivery.fxml"),
  FURNITURE_REQUEST("views/FurnitureDelivery.fxml"),
  FLOWER_REQUEST("views/FlowerDelivery.fxml"),
  MAP("views/Map.fxml"),
  LOGIN("view/Login.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
