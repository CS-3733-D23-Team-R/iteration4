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
    private HashMap<ItemForeignKey, IAvailableItem> itemNames = new HashMap<>(); //check that nothing of the same foreign key i in cart

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
        if(itemNames.isEmpty()){
            itemNames.putIfAbsent(new ItemForeignKey(item), item);
            items.putIfAbsent(item, quantity);
            notifyAllObservers();
            return;
        }
        ItemForeignKey itemKey = new ItemForeignKey(item);
        for(ItemForeignKey keyList : itemNames.keySet()){
            if(keyList.isEqualTo(item)) {
                itemNames.putIfAbsent(itemKey, item);
                incrementItem(itemKey);
                notifyAllObservers();
                return;
            }
        }
        itemNames.putIfAbsent(new ItemForeignKey(item), item); //not empty but not in keyset
        items.putIfAbsent(item, quantity);
        notifyAllObservers();
//        incrementItem(itemKey); //only if wasn't in key list
        notifyAllObservers();
    }

    private IAvailableItem findItemByKey(ItemForeignKey key){
        for(IAvailableItem item : items.keySet()){
            if(key.isEqualTo(item)) return item;
        }
        return null;
    }

    private void incrementItem(ItemForeignKey key){
        incrementItem(findItemByKey(key));
    }

    /**
     * only call on item already in cart
     * @param item
     */
    public void incrementItem(IAvailableItem item){
        items.replace(item, items.get(item) + 1);
        notifyAllObservers();
    }
    public void deleteItem(IAvailableItem item){
        items.remove(item);
        notifyAllObservers();
    }
    public void decrementItem(IAvailableItem item) {
        items.replace(item, items.get(item) - 1);
        notifyAllObservers();
    }
    public void clearCart(){
        items.clear();
        notifyAllObservers();
    }

    public int getItemQuantity(IAvailableItem item){
        return items.get(item);
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
}
