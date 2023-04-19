package edu.wpi.teamR.controllers;

import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Objects;

public class EmployeeManagerController {
    @FXML
    TableView<User> theTable;
    @FXML
    TableColumn<User, String> userNameCol, nameCol, emailCol, accessLevelCol, departmentCol, jobTitleCol, joinDateCol;
    @FXML
    TableColumn<User, Integer> phoneNumCol;
    @FXML
    TableColumn<User, Void> deleteCol;
    @FXML
    Button createUser;
    @FXML
    VBox data, addUser;
    @FXML
    Button submit, clear, back;
    @FXML
    MFXTextField name, email, password, phoneNumber, staffUsername, department, jobTitle;
    @FXML
    MFXComboBox<String> accesslevel;
    @FXML
    Text missingFields;
    ObservableList<String> accessLevels = FXCollections.observableArrayList("Admin", "Staff");
    public void initialize() throws SQLException, ClassNotFoundException {
        missingFields.setVisible(false);
        data.setVisible(true);
        addUser.setVisible(false);
        createUser.setVisible(true);
        createUser.setOnMouseClicked(event -> createUser());
        addButtonToTable();
        clear.setOnMouseClicked(event -> clear());
        submit.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        back.setOnMouseClicked(event -> back());
        accesslevel.setItems(accessLevels);
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        accessLevelCol.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        jobTitleCol.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        joinDateCol.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        for(User aUser : AuthenticationDAO.getInstance().getUsers()){
            theTable.getItems().add(aUser);
        }
    }
    public void createUser(){
        addUser.setVisible(true);
        data.setVisible(false);
        createUser.setVisible(false);
    }
    public void clear(){
        name.clear();
        email.clear();
        password.clear();
        phoneNumber.clear();
        staffUsername.clear();
        department.clear();
        jobTitle.clear();
        accesslevel.clear();
    }
    public void submit() throws SQLException, ClassNotFoundException {
        if(name.getText().isEmpty() ||
                email.getText().isEmpty() ||
                password.getText().isEmpty() ||
                phoneNumber.getText().isEmpty() ||
                staffUsername.getText().isEmpty() ||
                department.getText().isEmpty() ||
                jobTitle.getText().isEmpty() ||
                accesslevel.getText().isEmpty()){
            missingFields.setVisible(true);
        } else{
            missingFields.setVisible(false);
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            User newUser = new User(staffUsername.getText(),
                    password.getText(),
                    name.getText(),
                    email.getText(),
                    jobTitle.getText(),
                    phoneNumber.getText(),
                    date,
                    AccessLevel.valueOf(accesslevel.getText()),
                    department.getText());
            AuthenticationDAO.getInstance().addUser(
                    staffUsername.getText(),
                    password.getText(),
                    name.getText(),
                    email.getText(),
                    jobTitle.getText(),
                    phoneNumber.getText(),
                    date,
                    AccessLevel.valueOf(accesslevel.getText()),
                    department.getText()
                    );

            theTable.getItems().add(newUser);
            addUser.setVisible(false);
            data.setVisible(true);
            createUser.setVisible(true);
        }
    }
    public void back(){
        addUser.setVisible(false);
        data.setVisible(true);
        createUser.setVisible(true);
    }
    private void addButtonToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<User, Void>() {

                    private final Button btn = new Button();
                    {
                        ImageView imageView = new ImageView(Objects.requireNonNull(Main.class.getResource("images/delete.png")).toExternalForm());
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(30);
                        btn.getStyleClass().add("food_furniture-clear-button");
                        btn.setGraphic(imageView);
                        btn.setOnAction((ActionEvent event) -> {
                            User data = getTableView().getItems().get(getIndex());
                            theTable.getItems().remove(data);
                            try {
                                AuthenticationDAO.getInstance().deleteUserByUsername(data.getStaffUsername());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        deleteCol.setCellFactory(cellFactory);
    }
}
