package edu.wpi.teamR.controllers;

import edu.wpi.teamR.App;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.datahandling.ShoppingCart;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Size;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class ServiceRequestCartController {

    @FXML
    SearchableComboBox userField;
    @FXML
    SearchableComboBox locationField;
    @FXML
    SearchableComboBox staffField;
    @FXML
    Text paymentField;
    @FXML public VBox cartPane;
    @FXML
    BorderPane cartBar;
    @FXML MFXButton submitButton;
    @FXML
    StackPane cartSlider;
    @FXML
    Text totalPriceLabel;
    @FXML MFXTextField notesField;
    ArrayList<LocationName> locationNames;
    //ArrayList<StaffMembers> staffMembers;

    ShoppingCart cartInstance = ShoppingCart.getInstance();

    DecimalFormat formatPrice = new DecimalFormat("###.00");

    private RequestDatabase reqDatabase = RequestDatabase.getInstance();


    @FXML public void initialize() throws SQLException, ClassNotFoundException, ItemNotFoundException {
        locationField.setValue("Select location");
        staffField.setValue("Select staff");
        submitButton.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        locationNames = App.getMapData().getLocationNames();
//        AuthenticationDAO authDAO = new AuthenticationDAO();
//        staffMembers = authDAO.getUsers();
        totalPriceLabel.setText("$ " + formatPrice.format(ShoppingCart.getInstance().calculateTotal()));

        setLocationChoiceboxes();
        setStaffChoiceBox();

        cartPane.getChildren().clear();

        if(ShoppingCart.getInstance().items.isEmpty()){
            Text emptyLabel = new Text("Empty Cart");
            emptyLabel.setFill(Color.WHITE);
            //emptyLabel.setFont();
            cartPane.getChildren().add(emptyLabel);
        }else {
            ShoppingCart.getInstance().items.forEach(
                    (item, number) -> {
                        System.out.println(item);
                        System.out.println(number);
                        HBox productView = shoppingCartView(item,number);
                        cartPane.getChildren().add(productView);
                    }
            );
        }

    }

    // HBox function, sets up the given item in the shopping cart
    // input : ItemRequest item, Integer number
    // item -> item added to shopping cart
    // number -> number of that item added
    @FXML private HBox shoppingCartView(AvailableItem item, Integer number){
        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER_LEFT);
        //FileInputStream input = new FileInputStream("");
        //Image image = new Image(input);
        //ImageView imageView = new ImageView(image);

        Text productName = new Text(item.getItemName());
        productName.setFill(Color.WHITE);

        Text quantity = new Text(String.valueOf(number));
        quantity.setStyle("-fx-padding:5px;");
        quantity.setFill(Color.WHITE);
        //quantity

        MFXButton plusButton = new MFXButton("+");
        plusButton.setStyle("-fx-padding: 5px;");
        plusButton.setUserData(item.getItemName());
        plusButton.setOnAction(event -> {
            ShoppingCart.getInstance().incrementItem(item);
            quantity.setText(String.valueOf(ShoppingCart.getInstance().items.get(item)));
            this.totalPriceLabel.setText("$ " + formatPrice.format(ShoppingCart.getInstance().calculateTotal()));
        });

        MFXButton minusButton = new MFXButton("-");
        minusButton.setStyle("fx-padding: 5px");
        minusButton.setUserData(item.getItemName());
        minusButton.setOnAction(event -> {
            ShoppingCart.getInstance().decrementItem(item);
            quantity.setText(String.valueOf(String.valueOf(ShoppingCart.getInstance().items.get(item))));
            this.totalPriceLabel.setText("$ " + formatPrice.format(ShoppingCart.getInstance().calculateTotal()));
        });

        //Text price = new Text(String.valueOf("$"+ ShoppingCart.getInstance().items.get(item).

        layout.getChildren().addAll(productName, plusButton, quantity, minusButton);
        return layout;
    }

    /*private HBox totalView(float totalPrice){
        HBox layout = new HBox();
    }*/

    public Timestamp CurrentDateTime(){
        LocalDateTime now = LocalDateTime.now();
        return new Timestamp(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(),now.getMinute(),now.getSecond(),now.getNano());
    }


    @FXML
    public void submit() throws SQLException, ClassNotFoundException {
        String location = locationField.getValue().toString();
        String staff = staffField.getValue().toString();
        String requestor = userField.getValue().toString();
        String additionalNotes = notesField.getText();

        for (AvailableItem itemInHash : cartInstance.items.keySet()){
            for(int i = 0; i<cartInstance.items.get(itemInHash); i++){
                reqDatabase.addItemRequest(itemInHash.getRequestType(), RequestStatus.Unstarted, location, staff, itemInHash.getItemName(), requestor, additionalNotes, CurrentDateTime());
            }
        }

        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
        confirmation.setContentText("Request Order Received!");
        confirmation.show();
    }

    void setLocationChoiceboxes(){
        ArrayList<LocationName> locationNodes = locationNames;
        ArrayList<String> names = new ArrayList<String>();
        for (LocationName l: locationNodes) {
            if(!l.getLongName().contains("Hall")) {
                names.add(l.getLongName());
            }
        }
        ObservableList<String> choices = FXCollections.observableArrayList(names);
        locationField.setItems(choices);
    }

    void setStaffChoiceBox(){
        try {
            ArrayList<User> users = AuthenticationDAO.getInstance().getUsers();
            ArrayList<String> userNames = new ArrayList<>();
            for (User u: users){
                userNames.add(u.getStaffUsername());
            }
            ObservableList<String> staff = FXCollections.observableArrayList(userNames);
            staffField.setItems(staff);
            userField.setItems(staff);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
