package edu.wpi.teamR.requestdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableMeals implements IAvailableItem{
    private String itemName, imageReference, description;
    private Double itemPrice;
    private boolean isVegan, isVegetarian,isDairyFree, isPeanutFree, isGlutenFree;
    private RequestType requestType = RequestType.Meal;

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
}
