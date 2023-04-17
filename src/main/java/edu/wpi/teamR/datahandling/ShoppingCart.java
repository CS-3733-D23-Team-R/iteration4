package edu.wpi.teamR.datahandling;

import edu.wpi.teamR.requestdb.ItemRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCart {
    public HashMap<ItemRequest, Integer> items;

    public ShoppingCart(HashMap<ItemRequest, Integer> items) {
        this.items = items;
    }

    public void addItem(ItemRequest item, int quantity){
        items.put(item, quantity);
    }
    public void incrementItem(ItemRequest item){
        items.replace(item, items.get(item) + 1);
    }
    public void deleteItem(ItemRequest item){
        items.remove(item);
    }
    public void decrementItem(ItemRequest item) {
        items.replace(item, items.get(item) - 1);
    }
    public void clearCart(){ items.clear(); }
}
