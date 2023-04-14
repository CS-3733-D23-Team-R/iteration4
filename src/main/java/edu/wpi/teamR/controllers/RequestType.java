package edu.wpi.teamR.controllers;

import edu.wpi.teamR.requestdb.RequestStatus;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.sql.Timestamp;

public interface RequestType {
     void addRequest(String requesterName, String location, String staffMember, String additionalNoted, Timestamp requestDate, RequestStatus requestStatus, String type) throws SQLException, ClassNotFoundException;
     String getDefaultText();

   ObservableList<String> getItemList();

   String getTitleText();

    String getTypeText();

    String getStyle();
}
