package edu.wpi.teamR.controllers;

import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.requestdb.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    @FXML TableColumn<Node, Integer> node_nodeID, node_xCoord, node_yCoord;
    @FXML TableColumn<Node, String> node_building, node_floor;

    @FXML TableView<Edge> edgeTable;
    @FXML TableColumn<Edge, Integer> edge_startNode, edge_endNode;

    @FXML TableView<LocationName> locationNameTable;
    @FXML TableColumn<LocationName, String> locationName_longName, locationName_shortName, locationName_nodeType;

    @FXML TableView<Move> moveTable;
    @FXML TableColumn<Move, Integer> move_nodeID;
    @FXML TableColumn<Move, String> move_Date, move_longName;

    @FXML TableView<DirectionArrow> directionArrowTable;
    @FXML TableColumn<DirectionArrow, String> directionArrow_longName, directionArrow_direction, directionArrow_date;
    @FXML TableColumn<DirectionArrow, Integer> directionArrow_kioskID;

    @FXML TableView<ConferenceRoom> conferenceRoomTable;
    @FXML TableColumn<ConferenceRoom, String> conferenceRoom_longName, conferenceRoom_isAccessible, conferenceRoom_hasOutlets, conferenceRoom_hasScreen;
    @FXML TableColumn<ConferenceRoom, Integer> conferenceRoom_capacity;

    @FXML TableView<User> userTable;
    @FXML TableColumn<User, String>
            user_staffUsername,
            user_password,
            user_name,
            user_email,
            user_jobTitle,
            user_department,
            user_phoneNumber,
            user_salt,
            user_joinDate,
            user_accessLevel;
    @FXML TableColumn<User, Integer> user_imageID;

    @FXML TableView<RoomRequest> roomRequestTable;
    @FXML TableColumn<RoomRequest, Integer> roomRequest_roomRequestID;
    @FXML TableColumn<RoomRequest, String> roomRequest_longName, roomRequest_staffUsername, roomRequest_startTime, roomRequest_endTime;

    @FXML TableView<ItemRequest> serviceRequestTable;
    @FXML TableColumn<ItemRequest, String>
            serviceRequest_longName,
            serviceRequest_staffUsername,
            serviceRequest_requestType,
            serviceRequest_requestStatus,
            serviceRequest_requesterName,
            serviceRequest_additionalNotes,
            serviceRequest_requestDate,
            serviceRequest_itemType;
    @FXML TableColumn<ItemRequest, Integer> serviceRequest_requestID;

    @FXML TableView<AvailableMeals> allMealsTable;
    @FXML TableColumn<AvailableMeals, String>
            allMeals_itemName,
            allMeals_imageReference,
            allMeals_description,
            allMeals_isVegan,
            allMeals_isVegetarian,
            allMeals_isDairyFree,
            allMeals_isPeanutFree,
            allMeals_isGlutenFree;
    @FXML TableColumn<AvailableMeals, Integer> allMeals_itemPrice;

    @FXML TableView<AvailableFurniture> allFurnitureTable;
    @FXML TableColumn<AvailableFurniture, String>
            allFurniture_itemName,
            allFurniture_imageReference,
            allFurniture_description,
            allFurniture_isSeating,
            allFurniture_isTable,
            allFurniture_isPillow,
            allFurniture_isStorage;

    @FXML TableView<AvailableFlowers> allFlowerTable;
    @FXML TableColumn<AvailableFlowers, String>
            allFlowers_itemName,
            allFlowers_imageReference,
            allFlowers_description,
            allFlowers_isBoquet,
            allFlowers_hasCard;
    @FXML TableColumn<AvailableFlowers, Integer> allFlowers_price;

    @FXML TableView<AvailableSupplies> allSuppliesTable;
    @FXML TableColumn<AvailableSupplies, String>
            allSupplies_itemName,
            allSupplies_imageReference,
            allSupplies_description,
            allSupplies_isPaper,
            allSupplies_isPen,
            allSupplies_isOrganization,
            allSupplies_isComputerAccessory;
    @FXML TableColumn<AvailableSupplies, Integer> allSupplies_price;

    @FXML TableView<Alert> alertsTable;
    @FXML TableColumn<Alert, String> alert_message, alert_startDate, alert_endDate;

    @FXML TableView<Patient> patientTable;
    @FXML TableColumn<Patient, String> patient_patientName;
    @FXML TableColumn<Patient, Integer> patient_patientID;

    @FXML TableView<PatientMove> patientMoveTable;
    @FXML TableColumn<PatientMove, Integer> patientMove_patientID;
    @FXML TableColumn<PatientMove, String> patientMove_longName, patientMove_staffUsername, patientMove_time;

    public void initialize(){
        alertsTable.setVisible(false);
        alert_message.setCellValueFactory(new PropertyValueFactory<>("message"));
        alert_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        alert_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        allFlowerTable.setVisible(false);
        allFlowers_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allFlowers_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allFlowers_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allFlowers_hasCard.setCellValueFactory(new PropertyValueFactory<>("hasCard"));
        allFlowers_isBoquet.setCellValueFactory(new PropertyValueFactory<>("isBouqet"));
        allFlowers_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        allFurnitureTable.setVisible(false);
        allFurniture_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allFurniture_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allFurniture_isPillow.setCellValueFactory(new PropertyValueFactory<>("isPillow"));
        allFurniture_isSeating.setCellValueFactory(new PropertyValueFactory<>("isSeating"));
        allFurniture_isStorage.setCellValueFactory(new PropertyValueFactory<>("isStorage"));
        allFurniture_isTable.setCellValueFactory(new PropertyValueFactory<>("isTable"));
        allFurniture_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        allMealsTable.setVisible(false);
        allMeals_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allMeals_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allMeals_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allMeals_itemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        allMeals_isVegan.setCellValueFactory(new PropertyValueFactory<>("isVegan"));
        allMeals_isDairyFree.setCellValueFactory(new PropertyValueFactory<>("isDairyFree"));
        allMeals_isGlutenFree.setCellValueFactory(new PropertyValueFactory<>("isGlutenFree"));
        allMeals_isPeanutFree.setCellValueFactory(new PropertyValueFactory<>("isPeanutFree"));
        allMeals_isVegetarian.setCellValueFactory(new PropertyValueFactory<>("isVegetarian"));

        allSuppliesTable.setVisible(false);
        allSupplies_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allSupplies_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allSupplies_price.setCellValueFactory(new PropertyValueFactory<>("itemprice"));
        allSupplies_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allSupplies_isComputerAccessory.setCellValueFactory(new PropertyValueFactory<>("iscomputeraccessory"));
        allSupplies_isOrganization.setCellValueFactory(new PropertyValueFactory<>("isorganization"));
        allSupplies_isPaper.setCellValueFactory(new PropertyValueFactory<>("ispaper"));
        allSupplies_isPen.setCellValueFactory(new PropertyValueFactory<>("ispen"));

        conferenceRoomTable.setVisible(false);
        conferenceRoom_longName.setCellValueFactory(new PropertyValueFactory<>("longname"));
        conferenceRoom_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        conferenceRoom_hasOutlets.setCellValueFactory(new PropertyValueFactory<>("hasoutlets"));
        conferenceRoom_hasScreen.setCellValueFactory(new PropertyValueFactory<>("hasscreen"));
        conferenceRoom_isAccessible.setCellValueFactory(new PropertyValueFactory<>("isaccessible"));

        directionArrowTable.setVisible(false);
        directionArrow_longName.setCellValueFactory(new PropertyValueFactory<>("longname"));
        directionArrow_kioskID.setCellValueFactory(new PropertyValueFactory<>("kioskID"));
        directionArrow_direction.setCellValueFactory(new PropertyValueFactory<>("direction"));
        directionArrow_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        edgeTable.setVisible(false);
        edge_endNode.setCellValueFactory(new PropertyValueFactory<>("endNode"));
        edge_startNode.setCellValueFactory(new PropertyValueFactory<>("startnode"));

        serviceRequestTable.setVisible(false);
        serviceRequest_additionalNotes.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        serviceRequest_itemType.setCellValueFactory(new PropertyValueFactory<>("itemtype"));
        serviceRequest_longName.setCellValueFactory(new PropertyValueFactory<>("longname"));
        serviceRequest_requestDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        serviceRequest_requestID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        serviceRequest_requesterName.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
        serviceRequest_requestStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        serviceRequest_requestType.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        serviceRequest_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));

        locationNameTable.setVisible(false);
        locationName_longName.setCellValueFactory(new PropertyValueFactory<>("longname"));
        locationName_shortName.setCellValueFactory(new PropertyValueFactory<>("shortname"));
        locationName_nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));

        moveTable.setVisible(false);
        move_Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        move_longName.setCellValueFactory(new PropertyValueFactory<>("longname"));
        move_nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));

        nodeTable.setVisible(false);
        node_building.setCellValueFactory(new PropertyValueFactory<>("building"));
        node_xCoord.setCellValueFactory(new PropertyValueFactory<>("xcoord"));
        node_yCoord.setCellValueFactory(new PropertyValueFactory<>("ycoord"));
        node_nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
        node_floor.setCellValueFactory(new PropertyValueFactory<>("floor"));

        patientTable.setVisible(false);


        patientMoveTable.setVisible(false);
        roomRequestTable.setVisible(false);
        userTable.setVisible(false);


    }
}
