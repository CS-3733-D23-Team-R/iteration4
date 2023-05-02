package edu.wpi.teamR.controllers;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Objects;
public class AddEmployeeController {
    @FXML
    Button submit, clear, back;
    @FXML
    MFXTextField name, email, password, phoneNumber, staffUsername, department, jobTitle;
    @FXML
    MFXComboBox<AccessLevel> accesslevel;
    @FXML
    Text missingFields;

    ObservableList<AccessLevel> accessLevels = FXCollections.observableArrayList(AccessLevel.Admin, AccessLevel.Staff);
    public void initialize() throws SQLException, ClassNotFoundException {
        missingFields.setVisible(false);
        clear.setOnMouseClicked(event -> clear());
        submit.setOnMouseClicked(event -> {
            try {
                submit();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        back.setOnMouseClicked(event -> back());
        accesslevel.setText("Access Level");
        accesslevel.setItems(accessLevels);
        phoneNumber.setTextLimit(10);
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
                accesslevel.getText().isEmpty() || accesslevel.getText() == "Access Level"){
            missingFields.setVisible(true);
        } else{
            missingFields.setVisible(false);
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            new UserDatabase().addUser(
                    staffUsername.getText(),
                    password.getText(),
                    name.getText(),
                    email.getText(),
                    jobTitle.getText(),
                    phoneNumber.getText(),
                    date,
                    accesslevel.getSelectedItem(),
                    department.getText(),
                    0
                    );
            back();
        }
    }
    public void back(){
        Navigation.navigate(Screen.EMPLOYEEMANAGER);
    }

}
