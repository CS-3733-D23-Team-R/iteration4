//package edu.wpi.teamR.controllers;
//
//import edu.wpi.teamR.requestdb.RequestDatabase;
//import edu.wpi.teamR.requestdb.RequestStatus;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//
//public class RequestTypeFlower implements RequestType{
//    @Override
//    public void addRequest(String requesterName, String location, String staffMember, String additionalNoted, Timestamp requestDate, RequestStatus requestStatus, String type) throws SQLException, ClassNotFoundException {
//        RequestDatabase.getInstance().addFlowerRequest(requesterName, location, staffMember, additionalNoted, requestDate, requestStatus, type);
//    }
//
//    @Override
//    public String getDefaultText() {
//        return "Select Flower";
//    }
//
//    @Override
//    public ObservableList<String> getItemList() {
//        return FXCollections.observableArrayList("Petunias - $11.99", "Roses - $12.99", "Chrysanthemums - $15.99");
//    }
//
//    @Override
//    public String getTitleText() {
//        return "Flower Delivery Request";
//    }
//
//    @Override
//    public String getTypeText() {
//        return "Flower Type:";
//    }
//
//    @Override
//    public String getStyle() {
//        return "flower-title";
//    }
//}
