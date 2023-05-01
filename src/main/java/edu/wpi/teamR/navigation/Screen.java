package edu.wpi.teamR.navigation;

import edu.wpi.teamR.controllers.EditProfileController;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),
  SIGNAGE("views/Signage.fxml"),
  MEAL_REQUEST("views/DeliveryRequest.fxml"),
  FURNITURE_REQUEST("views/DeliveryRequest.fxml"),
  FLOWER_REQUEST("views/DeliveryRequest.fxml"),
  ROOM_REQUEST("views/ConfrenceRoom.fxml"),
  MAP("views/Map.fxml"),
  LOGIN("views/Login.fxml"),
  MAP_EDITOR("views/MapEditor.fxml"),
  SORT_ORDERS("views/SortOrders.fxml"),
  SCREENSAVER("views/Screensaver.fxml"),
  ADMINPROFILEPAGE("views/AdminProfilePage.fxml"),
  STAFFPROFILEPAGE("views/StaffProfilePage.fxml"),
  EMPLOYEEMANAGER("views/EmployeeManager.fxml"),
  ITEMREQUEST("views/ItemRequest.fxml"),
  ROOM_REQUEST_MANAGER("views/RoomRequestManager.fxml"),
  SIGNAGECONFIGURATION("views/SignageConfiguration.fxml"),
  ADDEMPLOYEE("views/AddEmployee.fxml"),
  EDITPROFILE("views/EditProfile.fxml"),
  CREDITS("views/CreditsPage.fxml"),
  MY_ROOM_RESERVATIONS("views/MyRoomReservations.fxml"),
  ABOUT("views/About.fxml"),
  ALERTS("views/Alerts.fxml"),
  MOVEPATIENT("views/movePatient.fxml"),
  VIEWDATA("views/ViewAllData.fxml"),
  DASHBOARD("views/Dashboard.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
