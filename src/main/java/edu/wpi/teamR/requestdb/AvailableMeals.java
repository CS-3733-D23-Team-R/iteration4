package edu.wpi.teamR.requestdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AvailableMeals {
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
}
