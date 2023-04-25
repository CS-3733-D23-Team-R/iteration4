package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableFlowers implements Archivable {
    private String itemName, imageReference, description;
    private double itemPrice;
    private boolean isBouqet, hasCard;

    public AvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) {
        this.itemName = itemName;
        this.imageReference = imageReference;
        this.description = description;
        this.itemPrice = itemPrice;
        this.isBouqet = isBouqet;
        this.hasCard = hasCard;
    }

    private AvailableFlowers(String[] args) {
        this(args[0], args[1], args[2], Double.parseDouble(args[3]), Boolean.parseBoolean(args[4]), Boolean.parseBoolean(args[5]));
    }

    @Override
    public String toCSVEntry() {
        return itemName+","+imageReference+","+description+","+itemPrice+","+isBouqet+","+hasCard;
    }

    @Override
    public String getCSVColumns() {
        return "itemName,imageReference,description,itemPrice,isBouqet,hasCard";
    }
}
