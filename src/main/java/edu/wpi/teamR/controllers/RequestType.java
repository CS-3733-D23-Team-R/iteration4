package edu.wpi.teamR.controllers;

import edu.wpi.teamR.requestdb.RequestStatus;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface RequestType {
    public void addRequest(String requesterName, String location, String staffMember, String additionalNoted, Timestamp requestDate, RequestStatus requestStatus, String type);
    public String getDefaultText();

    public ObservableList<String> getItemList();
}
