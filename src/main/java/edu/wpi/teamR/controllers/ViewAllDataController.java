package edu.wpi.teamR.controllers;

import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.requestdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class ViewAllDataController {
    @FXML HBox
            nodeHbox,
            edgeHbox,
            locationNameHbox,
            moveHbox,
            directionArrowHbox,
            conferenceRoomHbox,
            userHbox,
            roomRequestHbox,
            serviceRequestHbox,
            allMealsHbox,
            allFurnitureHbox,
            allFlowersHbox,
            allSuppliesHbox,
            allAlertsHbox,
            patientsHbox,
            allPatientMovesHbox;
    @FXML TableView<Node> nodeTable;
    @FXML TableView<Edge> edgeTable;
    @FXML TableView<LocationName> locationNameTable;
    @FXML TableView<Move> moveTable;
    @FXML TableView<DirectionArrow> directionArrowTable;
    @FXML TableView<ConferenceRoom> conferenceRoomTable;
    @FXML TableView<User> userTable;
    @FXML TableView<RoomRequest> roomRequestTable;
    @FXML TableView<ItemRequest> serviceRequestTable;
    @FXML TableView<AvailableMeals> allMealsTable;
    @FXML TableView<AvailableFurniture> allFurnitureTable;
    @FXML TableView<AvailableFlowers> allFlowerTable;
    @FXML TableView<AvailableSupplies> allSuppliesTable;
    @FXML TableView<Alert> alertsTable;
    @FXML TableView<Patient> patientTable;
    @FXML TableView<PatientMove> patientMoveTable;

    public void initialize(){

    }
}
