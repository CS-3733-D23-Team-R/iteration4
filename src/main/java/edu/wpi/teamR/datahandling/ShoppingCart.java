package edu.wpi.teamR.datahandling;

import edu.wpi.teamR.controllers.ServiceRequestCartController;
import edu.wpi.teamR.requestdb.AvailableItem;
import edu.wpi.teamR.requestdb.IAvailableItem;
import edu.wpi.teamR.requestdb.ItemRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ShoppingCart {
    public HashMap<IAvailableItem, Integer> items = new HashMap<IAvailableItem, Integer>();

    private static ShoppingCart instance;

    //ServiceRequestCartController test = new ServiceRequestCartController();



    private ShoppingCart() {}

    public static ShoppingCart getInstance(){
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void deleteCartInstance(){
        instance = null;
    }

    public void addItem(IAvailableItem item, int quantity){
        if(!items.containsKey(item)){
            items.putIfAbsent(item, quantity);
           // test.refreshCart();
        } else {incrementItem(item);}
    }
    public void incrementItem(IAvailableItem item){
        items.replace(item, items.get(item) + 1);
    }
    public void deleteItem(IAvailableItem item){
        items.remove(item);
    }
    public void decrementItem(IAvailableItem item) {
        items.replace(item, items.get(item) - 1);
    }
    public void clearCart(){ items.clear(); }

    public int getItemQuantity(IAvailableItem item){
        return items.get(item);
    }


    //returns the total price of all items in the items shopping cart
    public double calculateTotal() {
        Set<IAvailableItem> itemsSet = items.keySet();
        double total = 0;
        for (IAvailableItem i : itemsSet) {
            total += i.getItemPrice() * items.get(i);
        }
        return total;
    }
}
