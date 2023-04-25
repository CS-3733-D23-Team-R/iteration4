package edu.wpi.teamR.requestdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableFurniture {
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
}
