package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableMeals implements Archivable {
    private String itemName, imageReference, description;
    private double itemPrice;
    private boolean isVegan, isVegetarian,isDairyFree, isPeanutFree, isGlutenFree;

    public AvailableMeals(String itemName, String imageReference, String description, double itemPrice, boolean isVegan, boolean isVegetarian, boolean isDairyFree, boolean isPeanutFree, boolean isGlutenFree) {
        this.itemName = itemName;
        this.imageReference = imageReference;
        this.description = description;
        this.itemPrice = itemPrice;
        this.isVegan = isVegan;
        this.isVegetarian = isVegetarian;
        this.isDairyFree = isDairyFree;
        this.isPeanutFree = isPeanutFree;
        this.isGlutenFree = isGlutenFree;
    }

    private AvailableMeals(String[] args) {
        this(args[0], args[1], args[2], Double.parseDouble(args[3]), Boolean.parseBoolean(args[4]), Boolean.parseBoolean(args[5]), Boolean.parseBoolean(args[6]), Boolean.parseBoolean(args[7]), Boolean.parseBoolean(args[8]));
    }

    @Override
    public String toCSVEntry() {
        return itemName+","+imageReference+","+description+","+itemPrice+","+isVegan+","+isVegetarian+","+isDairyFree+","+isPeanutFree+","+isGlutenFree;
    }

    @Override
    public String getCSVColumns() {
        return "itemName,imageReference,description,itemPrice,isVegan,isVegetarian,isDairyFree,isPeanutFree,isGlutenFree";
    }
}
