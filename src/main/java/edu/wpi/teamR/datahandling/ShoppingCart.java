package edu.wpi.teamR.datahandling;

import edu.wpi.teamR.controllers.ServiceRequestCartController;
import edu.wpi.teamR.requestdb.AvailableItem;
import edu.wpi.teamR.requestdb.IAvailableItem;
import edu.wpi.teamR.requestdb.ItemRequest;
import edu.wpi.teamR.requestdb.RequestType;
import javafx.scene.text.Text;

import java.util.*;

public class ShoppingCart {
    private List<CartObserver> observers = new ArrayList<CartObserver>();
    public HashMap<IAvailableItem, Integer> items = new HashMap<IAvailableItem, Integer>();

    private static ShoppingCart instance;

    private ShoppingCart() {}

    public static ShoppingCart getInstance(){
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void attach(CartObserver observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (CartObserver observer : observers) {
            observer.update();
        }
    }

    public void deleteCartInstance(){
        instance = null;
    }

    public void addItem(IAvailableItem item, int quantity){
        if(!itemsContainsItem(item)){
            items.putIfAbsent(item, quantity);
        } else {incrementItem(itemInList(item));}
        notifyAllObservers();
    }

    /**
     * only call on item already in cart
     * @param item
     */
    public void incrementItem(IAvailableItem item){
        if(item != null) {
            items.replace(itemInList(item), items.get(itemInList(item)) + 1);
            notifyAllObservers();
        }
    }
    public void deleteItem(IAvailableItem item){
        if(item != null) {
            items.remove(itemInList(item));
            notifyAllObservers();
        }
    }
    public void decrementItem(IAvailableItem item) {
        if(item != null) {
            items.replace(itemInList(item), items.get(itemInList(item)) - 1);
            notifyAllObservers();
        }
    }
    public void clearCart(){
        items.clear();
        notifyAllObservers();
    }

    public int getItemQuantity(IAvailableItem item){
        if(item != null) {
            return items.get(itemInList(item));
        } else return 0;
    }


    //returns the total price of all items in the items shopping cart
    public double calculateTotal() {
        Set<IAvailableItem> itemsSet = items.keySet();
        double total = 0;
        for (IAvailableItem i : itemsSet) {
            if (!i.getRequestType().equals(RequestType.Furniture) && i.getItemPrice() != null){
                total += i.getItemPrice() * items.get(i);
            }
        }
        return total;
    }

    private boolean itemsContainsItem(IAvailableItem item){
        if(item != null) {
            String itemName = item.getItemName();
            RequestType type = item.getRequestType();
            for (IAvailableItem itemInList : items.keySet()) {
                if (itemName.equals(itemInList.getItemName()) && type == item.getRequestType()) return true;
            }
        }
        return false;
    }

    public IAvailableItem itemInList(IAvailableItem item){
        if(item != null) {
            String itemName = item.getItemName();
            RequestType type = item.getRequestType();
            for (IAvailableItem itemInList : items.keySet()) {
                if (itemName.equals(itemInList.getItemName()) && type == item.getRequestType()) return itemInList;
            }
        }
        return null;
    }
}
