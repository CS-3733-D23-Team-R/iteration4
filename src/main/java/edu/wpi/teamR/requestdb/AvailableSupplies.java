package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class AvailableSupplies implements Archivable {
    private String itemName, imageReference, description;
    private double itemPrice;
    private boolean isPaper, isPen, isOrganization, isComputerAccessory;

    public AvailableSupplies(String itemName, String imageReference, String description, double itemPrice, boolean isPaper, boolean isPen, boolean isOrganization, boolean isComputerAccessory) {
        this.itemName = itemName;
        this.imageReference = imageReference;
        this.description = description;
        this.itemPrice = itemPrice;
        this.isPaper = isPaper;
        this.isPen = isPen;
        this.isOrganization = isOrganization;
        this.isComputerAccessory = isComputerAccessory;
    }

    private AvailableSupplies(String[] args) {
        this(args[0], args[1], args[2], Double.parseDouble(args[3]), Boolean.parseBoolean(args[4]), Boolean.parseBoolean(args[5]), Boolean.parseBoolean(args[6]), Boolean.parseBoolean(args[7]));
    }

    @Override
    public String toCSVEntry() {
        return itemName+","+imageReference+","+description+","+itemPrice+","+isPaper+","+isPen+","+isOrganization+","+isComputerAccessory;
    }

    @Override
    public String getCSVColumns() {
        return "itemName,imageReference,description,itemPrice,isPaper,isPen,isOrganization,isComputerAccessory";
    }
}
