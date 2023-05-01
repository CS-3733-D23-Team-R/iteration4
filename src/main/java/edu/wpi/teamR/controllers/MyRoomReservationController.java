package edu.wpi.teamR.controllers;

import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.WeekView;
import com.calendarfx.view.page.WeekPage;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.requestdb.*;
import edu.wpi.teamR.userData.CurrentUser;
import javafx.fxml.FXML;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.Main;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.userData.UserData;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
import io.github.palexdev.materialfx.skins.legacy.MFXLegacyTableViewSkin;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.*;

public class MyRoomReservationController {
    @FXML
    Button newRequestBtn;

    @FXML
    StackPane stackPane;

    @FXML Button deleteBtn;

    @FXML
    public void initialize() throws SQLException {
        newRequestBtn.setOnAction(event -> Navigation.navigate(Screen.ROOM_REQUEST));
        Calendar roomCal = new Calendar("Room Reservations");
        RequestDatabase requestDatabase = new RequestDatabase();
        UserData thisUserData = UserData.getInstance();
        CurrentUser user = thisUserData.getLoggedIn();
        ArrayList<Entry> entries = new ArrayList<>();
        for(RoomRequest request: requestDatabase.getRoomRequestsByStaffUsername(user.getUsername())){
            Interval interval = new Interval(request.getStartTime().toLocalDateTime(), request.getEndTime().toLocalDateTime());
            Entry entry = new Entry<>(request.getLongName(), interval);
            entry.setId(request.getRoomRequestID()+"");
            entries.add(entry);
        }
        roomCal.addEntries(entries);
        roomCal.setReadOnly(true);
        CalendarSource calendarSource = new CalendarSource("My Calender");
        calendarSource.getCalendars().add(roomCal);
        CalendarView calendarView = new CalendarView();
        calendarView.getCalendarSources().add(calendarSource);
        try {
            calendarView.setCreateEntryClickCount(1000);
        }
        catch (IllegalArgumentException e){
            System.out.println("Too many clicks");
        }
        WeekPage weekPage = calendarView.getWeekPage();
        stackPane.getChildren().add(weekPage);
        Text text = new Text("My Room Reservations");
        text.getStyleClass().add("title");
        stackPane.getChildren().add(text);
        deleteBtn.setOnAction(event -> {
            if(weekPage.getSelections() != null){
                for(Entry entry: weekPage.getSelections()) {
                    roomCal.removeEntry(entry);
                    try {
                        requestDatabase.deleteRoomRequest(Integer.parseInt(entry.getId()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
