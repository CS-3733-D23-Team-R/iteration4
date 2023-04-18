package edu.wpi.teamR.requestdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableItem {
    private String itemName;
    private RequestType requestType;
    private double itemPrice;
    private String imageReference;

    public AvailableItem(String itemName, RequestType requestType, double itemPrice, String imageReference){
        this.itemName = itemName;
        this.requestType = requestType;
        this.itemPrice = itemPrice;
        this.imageReference = imageReference;
    }
}
