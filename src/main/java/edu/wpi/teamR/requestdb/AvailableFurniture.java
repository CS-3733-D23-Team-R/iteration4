package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableFurniture implements Archivable {
    private String itemName, imageReference, description;
    private boolean isSeating, isTable, isPillow, isStorage;

    public AvailableFurniture(String itemName, String imageReference, String description, boolean isSeating, boolean isTable, boolean isPillow, boolean isStorage) {
        this.itemName = itemName;
        this.imageReference = imageReference;
        this.description = description;
        this.isSeating = isSeating;
        this.isTable = isTable;
        this.isPillow = isPillow;
        this.isStorage = isStorage;
    }

    private AvailableFurniture(String[] args) {
        this(args[0], args[1], args[2], Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]), Boolean.parseBoolean(args[5]), Boolean.parseBoolean(args[6]));
    }

    @Override
    public String toCSVEntry() {
        return itemName+","+imageReference+","+description+","+isSeating+","+isTable+","+isPillow+","+isStorage;
    }

    @Override
    public String getCSVColumns() {
        return "itemName,imageReference,description,isSeating,isTable,isPillow,isStorage";
    }
}
