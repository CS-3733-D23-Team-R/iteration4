package edu.wpi.teamR.controllers;

import edu.wpi.teamR.navigation.Navigation;
import edu.wpi.teamR.navigation.Screen;
import edu.wpi.teamR.requestdb.*;
import edu.wpi.teamR.userData.CurrentUser;
import edu.wpi.teamR.userData.UserData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.Button;

public class StaffProfilePageController {
    @FXML Text name, email, occupation, DateOfJoining, phone, time;
    @FXML VBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    @FXML Button viewAllRequests, goToConferenceRooms;

    ImageView CreateNewMessage;

    public void initialize() throws SQLException, ClassNotFoundException, SearchException {
        RequestDatabase requestDatabase = new RequestDatabase();
        UserData thisUserData = UserData.getInstance();
        CurrentUser user = thisUserData.getLoggedIn();
        goToConferenceRooms.setOnMouseClicked(event -> {Navigation.navigate(Screen.ROOM_REQUEST);});
        viewAllRequests.setOnMouseClicked(event -> {Navigation.navigate(Screen.SORT_ORDERS);});
        name.setText(user.getFullName());
        email.setText(user.getEmail());
        occupation.setText(user.getJobTitle());
        DateOfJoining.setText(user.getJoinDate().toString());
        String num = Integer.toString(user.getPhoneNum());
        String formattedPhoneNumber = num.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "($1)-$2-$3");
        phone.setText(formattedPhoneNumber);

        LocalDate date = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formattedDate = date.format(dateTimeFormatter);

        Timeline timeline =
                new Timeline(
                        new KeyFrame(
                                Duration.seconds(0),
                                event -> {
                                    LocalDateTime currentTime = LocalDateTime.now();
                                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm:ss a");
                                    String formattedTime = currentTime.format(timeFormatter);
                                    String formattedDateTime = formattedTime + ", " + formattedDate;
                                    time.setText(formattedDateTime);
                                }),
                        new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        time.setText(formattedDate);

        String thisIsYourStaffUsername = user.getUsername();
        Timestamp dateOfRequest = new Timestamp(1);
        SearchList searchList = new SearchList();
        searchList.addComparison(RequestAttribute.staffUsername, Operation.equalTo, thisIsYourStaffUsername);
        searchList.addComparison(RequestAttribute.requestDate, Operation.greaterThan, dateOfRequest);
        ArrayList<ItemRequest> itemRequests = new RequestDatabase().getItemRequestByAttributes(searchList);

        Calendar cal = Calendar.getInstance();
        for(ItemRequest aRequest : itemRequests){
            Date aDate = aRequest.getRequestDate();
            String name = aRequest.getRequesterName();
            cal.setTime(aDate);
            Text aText = new Text();
            aText.setText(name + "\n" + aRequest.getItemType() + "\n" + aDate.toString());
            aText.setStrokeType(StrokeType.OUTSIDE);
            aText.setStrokeWidth(0.0);
            aText.setId("bodyMedium");
            switch (cal.get(Calendar.DAY_OF_WEEK)){
                case 1:sunday.getChildren().add(aText);
                case 2:monday.getChildren().add(aText);
                case 3:tuesday.getChildren().add(aText);
                case 4:wednesday.getChildren().add(aText);
                case 5:thursday.getChildren().add(aText);
                case 6:friday.getChildren().add(aText);
                case 7:saturday.getChildren().add(aText);
            }
        }
    }
}
