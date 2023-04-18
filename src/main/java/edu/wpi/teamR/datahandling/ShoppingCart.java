package edu.wpi.teamR.datahandling;

import edu.wpi.teamR.requestdb.AvailableItem;
import edu.wpi.teamR.requestdb.ItemRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCart {
    public HashMap<AvailableItem, Integer> items = new HashMap<AvailableItem, Integer>();

    private static ShoppingCart instance;



    public ShoppingCart() {}

    public static ShoppingCart getInstance(){
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void deleteCartInstance(){
        instance = null;
    }

    public void addItem(AvailableItem item, int quantity){
        if(!items.containsKey(item)){
            items.putIfAbsent(item, quantity);
        } else {incrementItem(item);}
    }
    public void incrementItem(AvailableItem item){
        items.replace(item, items.get(item) + 1);
    }
    public void deleteItem(AvailableItem item){
        items.remove(item);
    }
    public void decrementItem(AvailableItem item) {
        items.replace(item, items.get(item) - 1);
    }
    public void clearCart(){ items.clear(); }


    //returns the total price of all items in the items shopping cart
    public double calculateTotal() {return 0;};
}
