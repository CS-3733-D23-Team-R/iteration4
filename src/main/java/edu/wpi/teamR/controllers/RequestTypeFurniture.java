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
//public class RequestTypeFurniture implements RequestType{
//
//    @Override
//    public void addRequest(String requesterName, String location, String staffMember, String additionalNoted, Timestamp requestDate, RequestStatus requestStatus, String type) throws SQLException, ClassNotFoundException {
//        RequestDatabase.getInstance().addFurnitureRequest(requesterName, location, staffMember, additionalNoted, requestDate, requestStatus, type);
//    }
//
//    @Override
//    public String getDefaultText() {
//        return "Select Furniture";
//    }
//
//    @Override
//    public ObservableList<String> getItemList() {
//        return FXCollections.observableArrayList("Chair - $10.99", "Stretcher - $20.99", "Wardrobe - $23.99");
//    }
//
//    @Override
//    public String getTitleText() {
//        return "Furniture Delivery Request";
//    }
//
//    @Override
//    public String getTypeText() {
//        return "Furniture Type:";
//    }
//
//    @Override
//    public String getStyle() {
//        return "furniture-title";
//    }
//}
