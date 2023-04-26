package edu.wpi.teamR.requestdb;

public interface IAvailableItem {
    String getItemName();
    String getImageReference();
    String getDescription();
    Double getItemPrice();
    RequestType getRequestType();
}
