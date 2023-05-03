package edu.wpi.teamR.datahandling;

import edu.wpi.teamR.requestdb.IAvailableItem;
import edu.wpi.teamR.requestdb.RequestType;

class ItemForeignKey {
    String itemName;
    RequestType type;

    ItemForeignKey(IAvailableItem item) {
        this.itemName = item.getItemName();
        this.type = item.getRequestType();
    }

    boolean isEqualTo(IAvailableItem item){
        return this.itemName.equals(item.getItemName()) && this.type == item.getRequestType();
    }
}
