package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.Alert;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.*;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.*;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ViewAllDataController {
    @FXML Text title;
    @FXML Button backupButton, backButton;
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
    @FXML TableColumn<DirectionArrow, String> directionArrow_longName, directionArrow_date;
    @FXML TableColumn<DirectionArrow, Direction> directionArrow_direction;
    @FXML TableColumn<DirectionArrow, Integer> directionArrow_kioskID;

    @FXML TableView<ConferenceRoom> conferenceRoomTable;
    @FXML TableColumn<ConferenceRoom, String> conferenceRoom_longName, conferenceRoom_hasOutlets, conferenceRoom_hasScreen, conferenceRoom_isAccessible;
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
    @FXML TableColumn<ItemRequest, Integer> serviceRequest_requestID, serviceRequest_quantity;

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
    Boolean isNodeHbox,
            isEdgeHbox,
            isLocationNameHbox,
            isMoveHbox,
            isDirectionArrowHbox,
            isConferenceRoomHbox,
            isUserHbox,
            isRoomRequestHbox,
            isServiceRequestHbox,
            isAllMealsHbox,
            isAllFurnitureHbox,
            isAllFlowersHbox,
            isAllSuppliesHbox,
            isAllAlertsHbox,
            isPatientsHbox,
            isAllPatientMovesHbox;

    HBox[] buttons = new HBox[]{
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
            allPatientMovesHbox};


    public void initialize() throws SQLException {
        alertsTable.setVisible(false);
        alertsTable.getItems().addAll(new UserDatabase().getAlerts());
        alert_message.setCellValueFactory(new PropertyValueFactory<>("message"));
        alert_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        alert_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        allFlowerTable.setVisible(false);
        allFlowerTable.getItems().addAll(new RequestDatabase().getAvailableFlowers());
        allFlowers_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allFlowers_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allFlowers_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allFlowers_hasCard.setCellValueFactory(new PropertyValueFactory<>("hasCard"));
        allFlowers_isBoquet.setCellValueFactory(new PropertyValueFactory<>("bouqet"));
        allFlowers_price.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        allFurnitureTable.setVisible(false);
        allFurnitureTable.getItems().addAll(new RequestDatabase().getAvailableFurniture());
        allFurniture_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allFurniture_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allFurniture_isPillow.setCellValueFactory(new PropertyValueFactory<>("pillow"));
        allFurniture_isSeating.setCellValueFactory(new PropertyValueFactory<>("seating"));
        allFurniture_isStorage.setCellValueFactory(new PropertyValueFactory<>("storage"));
        allFurniture_isTable.setCellValueFactory(new PropertyValueFactory<>("table"));
        allFurniture_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        allMealsTable.setVisible(false);

        allMealsTable.getItems().addAll(new RequestDatabase().getAvailableMeals());
        allMeals_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allMeals_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allMeals_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allMeals_itemPrice.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        allMeals_isVegan.setCellValueFactory(new PropertyValueFactory<>("vegan"));
        allMeals_isDairyFree.setCellValueFactory(new PropertyValueFactory<>("dairyFree"));
        allMeals_isGlutenFree.setCellValueFactory(new PropertyValueFactory<>("glutenFree"));
        allMeals_isPeanutFree.setCellValueFactory(new PropertyValueFactory<>("peanutFree"));
        allMeals_isVegetarian.setCellValueFactory(new PropertyValueFactory<>("vegetarian"));

        allSuppliesTable.setVisible(false);
        allSuppliesTable.getItems().addAll(new RequestDatabase().getAvailableSupplies());
        allSupplies_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allSupplies_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allSupplies_price.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        allSupplies_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allSupplies_isComputerAccessory.setCellValueFactory(new PropertyValueFactory<>("computerAccessory"));
        allSupplies_isOrganization.setCellValueFactory(new PropertyValueFactory<>("organization"));
        allSupplies_isPaper.setCellValueFactory(new PropertyValueFactory<>("paper"));
        allSupplies_isPen.setCellValueFactory(new PropertyValueFactory<>("pen"));

        conferenceRoomTable.setVisible(false);
        conferenceRoomTable.getItems().addAll(new MapDatabase().getConferenceRooms());
        conferenceRoom_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        conferenceRoom_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        conferenceRoom_hasOutlets.setCellValueFactory(new PropertyValueFactory<>("hasOutlets"));
        conferenceRoom_hasScreen.setCellValueFactory(new PropertyValueFactory<>("hasScreen"));
        conferenceRoom_isAccessible.setCellValueFactory(new PropertyValueFactory<>("accessible"));

        directionArrowTable.setVisible(false);
        directionArrowTable.getItems().addAll(new MapDatabase().getDirectionArrows());
        directionArrow_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        directionArrow_kioskID.setCellValueFactory(new PropertyValueFactory<>("kioskID"));
        directionArrow_direction.setCellValueFactory(new PropertyValueFactory<>("direction"));
        directionArrow_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        edgeTable.setVisible(false);
        edgeTable.getItems().addAll(new MapDatabase().getEdges());
        edge_endNode.setCellValueFactory(new PropertyValueFactory<>("endNode"));
        edge_startNode.setCellValueFactory(new PropertyValueFactory<>("startNode"));

        serviceRequestTable.setVisible(false);
        serviceRequestTable.getItems().addAll(new RequestDatabase().getItemRequests());
        serviceRequest_additionalNotes.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        serviceRequest_itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        serviceRequest_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        serviceRequest_requestDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        serviceRequest_requestID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        serviceRequest_requesterName.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
        serviceRequest_requestStatus.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        serviceRequest_requestType.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        serviceRequest_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        serviceRequest_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));

        locationNameTable.setVisible(false);
        locationNameTable.getItems().addAll(new MapDatabase().getLocationNames());
        locationName_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        locationName_shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        locationName_nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));

        moveTable.setVisible(false);
        moveTable.getItems().addAll(new MapDatabase().getMoves());
        move_Date.setCellValueFactory(new PropertyValueFactory<>("moveDate"));
        move_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        move_nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));

        nodeTable.setVisible(false);
        nodeTable.getItems().addAll(new MapDatabase().getNodes());
        node_building.setCellValueFactory(new PropertyValueFactory<>("building"));
        node_xCoord.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
        node_yCoord.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
        node_nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
        node_floor.setCellValueFactory(new PropertyValueFactory<>("floorNum"));

        patientTable.setVisible(false);
        patientTable.getItems().addAll(new RequestDatabase().getPatients());
        patient_patientID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        patient_patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        patientMoveTable.setVisible(false);
        patientMoveTable.getItems().addAll(new RequestDatabase().getPatientMoves());
        patientMove_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        patientMove_patientID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        patientMove_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        patientMove_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        roomRequestTable.setVisible(false);
        roomRequestTable.getItems().addAll(new RequestDatabase().getRoomRequests());
        roomRequest_endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        roomRequest_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        roomRequest_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        roomRequest_startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        roomRequest_roomRequestID.setCellValueFactory(new PropertyValueFactory<>("roomRequestID"));

        userTable.setVisible(false);
        userTable.getItems().addAll(new UserDatabase().getUsers());
        user_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        user_accessLevel.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_imageID.setCellValueFactory(new PropertyValueFactory<>("imageID"));
        user_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        user_jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        user_joinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        user_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        user_salt.setCellValueFactory(new PropertyValueFactory<>("salt"));
        user_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));

        nodeHbox.setOnMouseClicked(event -> {
            hideAllTables();
            nodeTable.setVisible(true);
            setAllButtonsGrey();
            nodeHbox.setStyle("-fx-background-color: #0067B1");

        });
        edgeHbox.setOnMouseClicked(event -> {
            hideAllTables();
            edgeTable.setVisible(true);
            setAllButtonsGrey();
            edgeHbox.setStyle("-fx-background-color: #0067B1");
        });
        locationNameHbox.setOnMouseClicked(event -> {
            hideAllTables();
            locationNameTable.setVisible(true);
            setAllButtonsGrey();
            locationNameHbox.setStyle("-fx-background-color: #0067B1");
        });
        moveHbox.setOnMouseClicked(event -> {
            hideAllTables();
            moveTable.setVisible(true);
            setAllButtonsGrey();
            moveHbox.setStyle("-fx-background-color: #0067B1");
        });
        directionArrowHbox.setOnMouseClicked(event -> {
            hideAllTables();
            directionArrowTable.setVisible(true);
            setAllButtonsGrey();
            directionArrowHbox.setStyle("-fx-background-color: #0067B1");
        });
        conferenceRoomHbox.setOnMouseClicked(event -> {
            hideAllTables();
            conferenceRoomTable.setVisible(true);
            setAllButtonsGrey();
            conferenceRoomHbox.setStyle("-fx-background-color: #0067B1");
        });
        userHbox.setOnMouseClicked(event -> {
            hideAllTables();
            userTable.setVisible(true);
            setAllButtonsGrey();
            userHbox.setStyle("-fx-background-color: #0067B1");
        });
        roomRequestHbox.setOnMouseClicked(event -> {
            hideAllTables();
            roomRequestTable.setVisible(true);
            setAllButtonsGrey();
            roomRequestHbox.setStyle("-fx-background-color: #0067B1");
        });
        serviceRequestHbox.setOnMouseClicked(event -> {
            hideAllTables();
            serviceRequestTable.setVisible(true);
            setAllButtonsGrey();
            serviceRequestHbox.setStyle("-fx-background-color: #0067B1");
        });
        allMealsHbox.setOnMouseClicked(event -> {
            hideAllTables();
            allMealsTable.setVisible(true);
            setAllButtonsGrey();
            allMealsHbox.setStyle("-fx-background-color: #0067B1");
        });
        allFurnitureHbox.setOnMouseClicked(event -> {
            hideAllTables();
            allFurnitureTable.setVisible(true);
            setAllButtonsGrey();
            allFurnitureHbox.setStyle("-fx-background-color: #0067B1");
        });
        allFlowersHbox.setOnMouseClicked(event -> {
            hideAllTables();
            allFlowerTable.setVisible(true);
            setAllButtonsGrey();
            allFlowersHbox.setStyle("-fx-background-color: #0067B1");
        });
        allSuppliesHbox.setOnMouseClicked(event -> {
            hideAllTables();
            allSuppliesTable.setVisible(true);
            setAllButtonsGrey();
            allSuppliesHbox.setStyle("-fx-background-color: #0067B1");
        });
        allAlertsHbox.setOnMouseClicked(event -> {
            hideAllTables();
            alertsTable.setVisible(true);
            setAllButtonsGrey();
            allAlertsHbox.setStyle("-fx-background-color: #0067B1");
        });
        patientsHbox.setOnMouseClicked(event -> {
            hideAllTables();
            patientTable.setVisible(true);
            setAllButtonsGrey();
            patientsHbox.setStyle("-fx-background-color: #0067B1");
        });
        allPatientMovesHbox.setOnMouseClicked(event -> {
            hideAllTables();
            patientMoveTable.setVisible(true);
            setAllButtonsGrey();
            allPatientMovesHbox.setStyle("-fx-background-color: #0067B1");
        });
        backupButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/ArchivePage.fxml"));
            try {
                Parent popupRoot = loader.load();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setTitle("Archive Manager");
                popupStage.setScene(new Scene(popupRoot, 550, 300));
                RootController root = RootController.getInstance();
                root.setPopupState(true);
                popupStage.showAndWait();
                root.setPopupState(false);
                refreshData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        hboxConfigure(allAlertsHbox);
        hboxConfigure(allFlowersHbox);
        hboxConfigure(allFurnitureHbox);
        hboxConfigure(allMealsHbox);
        hboxConfigure(allSuppliesHbox);
        hboxConfigure(nodeHbox);
        hboxConfigure(edgeHbox);
        hboxConfigure(locationNameHbox);
        hboxConfigure(moveHbox);
        hboxConfigure(directionArrowHbox);
        hboxConfigure(conferenceRoomHbox);
        hboxConfigure(userHbox);
        hboxConfigure(roomRequestHbox);
        hboxConfigure(serviceRequestHbox);
        hboxConfigure(allPatientMovesHbox);
        hboxConfigure(patientsHbox);
        backButton.setOnAction(event -> {
            Navigation.navigate(Screen.ADMINPROFILEPAGE);
        });
    }
    public void hideAllTables(){
        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        locationNameTable.setVisible(false);
        moveTable.setVisible(false);
        directionArrowTable.setVisible(false);
        conferenceRoomTable.setVisible(false);
        userTable.setVisible(false);
        roomRequestTable.setVisible(false);
        serviceRequestTable.setVisible(false);
        allMealsTable.setVisible(false);
        allFurnitureTable.setVisible(false);
        allFlowerTable.setVisible(false);
        allSuppliesTable.setVisible(false);
        alertsTable.setVisible(false);
        patientTable.setVisible(false);
        patientMoveTable.setVisible(false);
    }

    public void setAllButtonsGrey(){
        nodeHbox.setStyle("-fx-background-color: #D9D9D9");
        isNodeHbox = false;
        allFlowersHbox.setStyle("-fx-background-color: #D9D9D9");
        isAllFlowersHbox = false;
        allSuppliesHbox.setStyle("-fx-background-color: #D9D9D9");
        isAllSuppliesHbox = false;
        patientsHbox.setStyle("-fx-background-color: #D9D9D9");
        isPatientsHbox = false;
        allAlertsHbox.setStyle("-fx-background-color: #D9D9D9");
        isAllAlertsHbox = false;
        allPatientMovesHbox.setStyle("-fx-background-color: #D9D9D9");
        isAllPatientMovesHbox = false;
        roomRequestHbox.setStyle("-fx-background-color: #D9D9D9");
        isRoomRequestHbox = false;
        userHbox.setStyle("-fx-background-color: #D9D9D9");
        isUserHbox = false;
        locationNameHbox.setStyle("-fx-background-color: #D9D9D9");
        isLocationNameHbox = false;
        edgeHbox.setStyle("-fx-background-color: #D9D9D9");
        isEdgeHbox = false;
        allFurnitureHbox.setStyle("-fx-background-color: #D9D9D9");
        isAllFurnitureHbox = false;
        serviceRequestHbox.setStyle("-fx-background-color: #D9D9D9");
        isServiceRequestHbox = false;
        conferenceRoomHbox.setStyle("-fx-background-color: #D9D9D9");
        isConferenceRoomHbox = false;
        directionArrowHbox.setStyle("-fx-background-color: #D9D9D9");
        isDirectionArrowHbox = false;
        moveHbox.setStyle("-fx-background-color: #D9D9D9");
        isMoveHbox = false;
        allMealsHbox.setStyle("-fx-background-color: #D9D9D9");
        isAllMealsHbox = false;
    }

    public void hboxConfigure(HBox hbox){

        hbox.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!hbox.getStyle().equals("-fx-background-color: #0067B1")){
                    hbox.setStyle("-fx-background-color: #FFFFFF");
                }
            }
        });
        hbox.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!hbox.getStyle().equals("-fx-background-color: #0067B1")){
                    hbox.setStyle("-fx-background-color: #D9D9D9");
                }
            }
        });

    }

    public void refreshData() throws SQLException {
        setAllButtonsGrey();
        alertsTable.getItems().clear();
        alertsTable.setVisible(false);
        alertsTable.getItems().addAll(new UserDatabase().getAlerts());
        alert_message.setCellValueFactory(new PropertyValueFactory<>("message"));
        alert_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        alert_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        allFlowerTable.getItems().clear();
        allFlowerTable.setVisible(false);
        allFlowerTable.getItems().addAll(new RequestDatabase().getAvailableFlowers());
        allFlowers_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allFlowers_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allFlowers_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allFlowers_hasCard.setCellValueFactory(new PropertyValueFactory<>("hasCard"));
        allFlowers_isBoquet.setCellValueFactory(new PropertyValueFactory<>("bouqet"));
        allFlowers_price.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        allFurnitureTable.getItems().clear();
        allFurnitureTable.setVisible(false);
        allFurnitureTable.getItems().addAll(new RequestDatabase().getAvailableFurniture());
        allFurniture_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allFurniture_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allFurniture_isPillow.setCellValueFactory(new PropertyValueFactory<>("pillow"));
        allFurniture_isSeating.setCellValueFactory(new PropertyValueFactory<>("seating"));
        allFurniture_isStorage.setCellValueFactory(new PropertyValueFactory<>("storage"));
        allFurniture_isTable.setCellValueFactory(new PropertyValueFactory<>("table"));
        allFurniture_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        allMealsTable.setVisible(false);
        allMealsTable.getItems().clear();
        allMealsTable.getItems().addAll(new RequestDatabase().getAvailableMeals());
        allMeals_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allMeals_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allMeals_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allMeals_itemPrice.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        allMeals_isVegan.setCellValueFactory(new PropertyValueFactory<>("vegan"));
        allMeals_isDairyFree.setCellValueFactory(new PropertyValueFactory<>("dairyFree"));
        allMeals_isGlutenFree.setCellValueFactory(new PropertyValueFactory<>("glutenFree"));
        allMeals_isPeanutFree.setCellValueFactory(new PropertyValueFactory<>("peanutFree"));
        allMeals_isVegetarian.setCellValueFactory(new PropertyValueFactory<>("vegetarian"));

        allSuppliesTable.setVisible(false);
        allSuppliesTable.getItems().clear();
        allSuppliesTable.getItems().addAll(new RequestDatabase().getAvailableSupplies());
        allSupplies_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        allSupplies_imageReference.setCellValueFactory(new PropertyValueFactory<>("imageReference"));
        allSupplies_price.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        allSupplies_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        allSupplies_isComputerAccessory.setCellValueFactory(new PropertyValueFactory<>("computerAccessory"));
        allSupplies_isOrganization.setCellValueFactory(new PropertyValueFactory<>("organization"));
        allSupplies_isPaper.setCellValueFactory(new PropertyValueFactory<>("paper"));
        allSupplies_isPen.setCellValueFactory(new PropertyValueFactory<>("pen"));

        conferenceRoomTable.setVisible(false);
        conferenceRoomTable.getItems().clear();
        conferenceRoomTable.getItems().addAll(new MapDatabase().getConferenceRooms());
        conferenceRoom_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        conferenceRoom_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        conferenceRoom_hasOutlets.setCellValueFactory(new PropertyValueFactory<>("hasOutlets"));
        conferenceRoom_hasScreen.setCellValueFactory(new PropertyValueFactory<>("hasScreen"));
        conferenceRoom_isAccessible.setCellValueFactory(new PropertyValueFactory<>("accessible"));

        directionArrowTable.setVisible(false);
        directionArrowTable.getItems().clear();
        directionArrowTable.getItems().addAll(new MapDatabase().getDirectionArrows());
        directionArrow_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        directionArrow_kioskID.setCellValueFactory(new PropertyValueFactory<>("kioskID"));
        directionArrow_direction.setCellValueFactory(new PropertyValueFactory<>("direction"));
        directionArrow_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        edgeTable.setVisible(false);
        edgeTable.getItems().clear();
        edgeTable.getItems().addAll(new MapDatabase().getEdges());
        edge_endNode.setCellValueFactory(new PropertyValueFactory<>("endNode"));
        edge_startNode.setCellValueFactory(new PropertyValueFactory<>("startNode"));

        serviceRequestTable.setVisible(false);
        serviceRequestTable.getItems().clear();
        serviceRequestTable.getItems().addAll(new RequestDatabase().getItemRequests());
        serviceRequest_additionalNotes.setCellValueFactory(new PropertyValueFactory<>("additionalNotes"));
        serviceRequest_itemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        serviceRequest_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        serviceRequest_requestDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        serviceRequest_requestID.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        serviceRequest_requesterName.setCellValueFactory(new PropertyValueFactory<>("requesterName"));
        serviceRequest_requestStatus.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        serviceRequest_requestType.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        serviceRequest_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        serviceRequest_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));

        locationNameTable.setVisible(false);
        locationNameTable.getItems().clear();
        locationNameTable.getItems().addAll(new MapDatabase().getLocationNames());
        locationName_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        locationName_shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        locationName_nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));

        moveTable.setVisible(false);
        moveTable.getItems().clear();
        moveTable.getItems().addAll(new MapDatabase().getMoves());
        move_Date.setCellValueFactory(new PropertyValueFactory<>("moveDate"));
        move_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        move_nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));

        nodeTable.setVisible(false);
        nodeTable.getItems().clear();
        nodeTable.getItems().addAll(new MapDatabase().getNodes());
        node_building.setCellValueFactory(new PropertyValueFactory<>("building"));
        node_xCoord.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
        node_yCoord.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
        node_nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
        node_floor.setCellValueFactory(new PropertyValueFactory<>("floorNum"));

        patientTable.setVisible(false);
        patientTable.getItems().clear();
        patientTable.getItems().addAll(new RequestDatabase().getPatients());
        patient_patientID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        patient_patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        patientMoveTable.setVisible(false);
        patientMoveTable.getItems().clear();
        patientMoveTable.getItems().addAll(new RequestDatabase().getPatientMoves());
        patientMove_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        patientMove_patientID.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        patientMove_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        patientMove_time.setCellValueFactory(new PropertyValueFactory<>("time"));

        roomRequestTable.setVisible(false);
        roomRequestTable.getItems().clear();
        roomRequestTable.getItems().addAll(new RequestDatabase().getRoomRequests());
        roomRequest_endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        roomRequest_longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
        roomRequest_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        roomRequest_startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        roomRequest_roomRequestID.setCellValueFactory(new PropertyValueFactory<>("roomRequestID"));

        userTable.setVisible(false);
        userTable.getItems().clear();
        userTable.getItems().addAll(new UserDatabase().getUsers());
        user_department.setCellValueFactory(new PropertyValueFactory<>("department"));
        user_accessLevel.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));
        user_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        user_imageID.setCellValueFactory(new PropertyValueFactory<>("imageID"));
        user_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        user_jobTitle.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        user_joinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        user_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        user_phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        user_salt.setCellValueFactory(new PropertyValueFactory<>("salt"));
        user_staffUsername.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
    }

}
