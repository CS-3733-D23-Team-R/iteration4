package edu.wpi.teamR.requestdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableFlowers implements IAvailableItem{
    private String itemName, imageReference, description;
    private Double itemPrice;
    private boolean isBouqet, hasCard;
    private RequestType requestType = RequestType.Flower;

    public AvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) {
        this.itemName = itemName;
        this.imageReference = imageReference;
        this.description = description;
        this.itemPrice = itemPrice;
        this.isBouqet = isBouqet;
        this.hasCard = hasCard;
    }

}
