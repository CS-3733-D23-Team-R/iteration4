package edu.wpi.teamR.controllers;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.AuthenticationDAO;
import edu.wpi.teamR.login.User;
import edu.wpi.teamR.userData.CurrentUser;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Objects;
import java.awt.*;

public class ProfileController {
    @FXML Text name, email, dept, hireDate, phoneNumber, jobTitle;
    @FXML ImageView profileImage;
    @FXML Button editProfileBtn;

    public void initialize() {}

    public void setInfo(User user){
        name.setText(user.getName());
        email.setText(user.getEmail());
        dept.setText(user.getDepartment());
        hireDate.setText(user.getJoinDate().toString());
        phoneNumber.setText(user.getPhoneNum());
        jobTitle.setText(user.getJobTitle());
        //profileImage.setImage(AuthenticationDAO.getInstance().getUserByUsername(user.getName()).getProfileImageID());
//        editProfileBtn.setVisible(false);
//        editProfileBtn.setDisable(true);
    }

    public void setInfo(CurrentUser user){
        name.setText(user.getFullName());
        email.setText(user.getEmail());
        dept.setText(user.getDepartment());
        hireDate.setText(user.getJoinDate().toString());
        phoneNumber.setText(user.getPhoneNum() + "");
        jobTitle.setText(user.getJobTitle());
        //profileImage.setImage(AuthenticationDAO.getInstance().getUserByUsername(user.getName()).getProfileImageID());
//        editProfileBtn.setVisible(true);
    }
    public Button getBtn() {
        return editProfileBtn;
    }
}
