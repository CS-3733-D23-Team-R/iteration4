package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
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
import javafx.scene.control.cell.TextFieldTableCell;
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
        //userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        //nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        //emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        accessLevelCol.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));
        //accessLevelCol.setCellFactory(TextFieldTableCell.forTableColumn());
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        //departmentCol.setCellFactory(TextFieldTableCell.forTableColumn());
        jobTitleCol.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        //jobTitleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        joinDateCol.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        /*
        userNameCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setStaffUsername(event.getNewValue());
            try{AuthenticationDAO.getInstance().modifyUserByUsername(
                        event.getNewValue(),
                        aUser.getPassword(),
                        aUser.getName(),
                        aUser.getEmail(),
                        aUser.getJobTitle(),
                        aUser.getPhoneNum(),
                        aUser.getJoinDate(),
                        aUser.getAccessLevel(),
                        aUser.getDepartment());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        nameCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setName(event.getNewValue());
            try{AuthenticationDAO.getInstance().modifyUserByUsername(
                    aUser.getStaffUsername(),
                    aUser.getPassword(),
                    event.getNewValue(),
                    aUser.getEmail(),
                    aUser.getJobTitle(),
                    aUser.getPhoneNum(),
                    aUser.getJoinDate(),
                    aUser.getAccessLevel(),
                    aUser.getDepartment());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        emailCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setName(event.getNewValue());
            try{AuthenticationDAO.getInstance().modifyUserByUsername(
                    aUser.getStaffUsername(),
                    aUser.getPassword(),
                    aUser.getName(),
                    event.getNewValue(),
                    aUser.getJobTitle(),
                    aUser.getPhoneNum(),
                    aUser.getJoinDate(),
                    aUser.getAccessLevel(),
                    aUser.getDepartment());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        accessLevelCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setName(event.getNewValue());
            try{AuthenticationDAO.getInstance().modifyUserByUsername(
                    aUser.getStaffUsername(),
                    aUser.getPassword(),
                    aUser.getName(),
                    aUser.getEmail(),
                    aUser.getJobTitle(),
                    aUser.getPhoneNum(),
                    aUser.getJoinDate(),
                    AccessLevel.valueOf(event.getNewValue()),
                    aUser.getDepartment());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        departmentCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setName(event.getNewValue());
            try{AuthenticationDAO.getInstance().modifyUserByUsername(
                    aUser.getStaffUsername(),
                    aUser.getPassword(),
                    aUser.getName(),
                    aUser.getEmail(),
                    aUser.getJobTitle(),
                    aUser.getPhoneNum(),
                    aUser.getJoinDate(),
                    aUser.getAccessLevel(),
                    event.getNewValue());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        jobTitleCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setName(event.getNewValue());
            try{AuthenticationDAO.getInstance().modifyUserByUsername(
                    aUser.getStaffUsername(),
                    aUser.getPassword(),
                    aUser.getName(),
                    aUser.getEmail(),
                    event.getNewValue(),
                    aUser.getPhoneNum(),
                    aUser.getJoinDate(),
                    aUser.getAccessLevel(),
                    aUser.getDepartment());
            } catch (SQLException | ClassNotFoundException | ItemNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

         */
        for(User aUser : new UserDatabase().getUsers()){
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
            User newUser = new UserDatabase().addUser(
                    staffUsername.getText(),
                    password.getText(),
                    name.getText(),
                    email.getText(),
                    jobTitle.getText(),
                    phoneNumber.getText(),
                    date,
                    AccessLevel.valueOf(accesslevel.getText()),
                    department.getText(),
                    0
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
                                new UserDatabase().deleteUserByUsername(data.getStaffUsername());
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
