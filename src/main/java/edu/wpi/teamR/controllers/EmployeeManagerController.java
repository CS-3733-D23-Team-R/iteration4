package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
/* TODO:
    Make it so you can edit phone numbers

 */

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
    @FXML VBox profileCardContainer;
    ObservableList<AccessLevel> accessLevels = FXCollections.observableArrayList(AccessLevel.Admin, AccessLevel.Staff);
    public void initialize() throws SQLException, ClassNotFoundException {
        createUser.setVisible(true);
        createUser.setOnMouseClicked(event -> Navigation.navigate(Screen.ADDEMPLOYEE));
        addButtonToTable();
        User user = new UserDatabase().getUsers().get(0);
        displayProfile(user);
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("staffUsername"));
        userNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        departmentCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        departmentCol.setCellFactory(TextFieldTableCell.forTableColumn());
        jobTitleCol.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        jobTitleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        joinDateCol.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        theTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Get the selected Person object
                User selectedUser = theTable.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    // Do something with the selected Person object
                    displayProfile(selectedUser);
                }
            }
        });
        userNameCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setStaffUsername(event.getNewValue());
            updateUser(aUser);
        });
        nameCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setName(event.getNewValue());
            updateUser(aUser);
        });
        emailCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setEmail(event.getNewValue());
            updateUser(aUser);
        });
        accessLevelCol.setCellFactory(column -> new TableCell<>() {
            private final MFXComboBox<AccessLevel> accessLevelMFXComboBox = new MFXComboBox<AccessLevel>(accessLevels);
            {
                accessLevelMFXComboBox.setMaxWidth(70);
                accessLevelMFXComboBox.setOnAction(event -> {
                    User user1 = getTableView().getItems().get(getIndex());
                    try {
                        AccessLevel accessLevel = accessLevelMFXComboBox.getSelectionModel().getSelectedItem();
                        user1.setAccessLevel(accessLevel);
                        updateUser(user1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user1 = getTableView().getItems().get(getIndex());
                    accessLevelMFXComboBox.getSelectionModel().selectItem(user1.getAccessLevel());
                    setGraphic(accessLevelMFXComboBox);
                }
            }
        });

        departmentCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setDepartment(event.getNewValue());
            updateUser(aUser);
        });
        jobTitleCol.setOnEditCommit(event -> {
            User aUser = event.getRowValue();
            aUser.setJobTitle(event.getNewValue());
            updateUser(aUser);
        });

        ArrayList<User> list = new UserDatabase().getUsers();
        for(User aUser : list){
            theTable.getItems().add(aUser);
        }

        theTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void updateUser(User aUser){
        try{new UserDatabase().modifyUserByUsername(
                aUser.getStaffUsername(),
                aUser.getName(),
                aUser.getEmail(),
                aUser.getJobTitle(),
                aUser.getPhoneNum(),
                aUser.getJoinDate(),
                aUser.getAccessLevel(),
                aUser.getDepartment(),
                aUser.getImageID());
        } catch (SQLException | ItemNotFoundException e) {
            throw new RuntimeException(e);
        }
        displayProfile(aUser);
    }

    private Node loadCard(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamR/views/Profile.fxml"));
        Node node = loader.load();
        ProfileController contentController = loader.getController();
        contentController.setInfo(user);
        return node;
    }

    private void displayProfile(User user){
        profileCardContainer.getChildren().clear();
        try {
            profileCardContainer.getChildren().add(loadCard(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                                new RequestDatabase().deleteItemRequestsByUser(data.getStaffUsername());
                                new RequestDatabase().deleteRoomRequestByUser(data.getStaffUsername());
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
