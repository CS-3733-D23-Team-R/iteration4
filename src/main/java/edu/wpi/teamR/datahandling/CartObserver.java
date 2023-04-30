package edu.wpi.teamR.datahandling;

abstract public class CartObserver {
    protected ShoppingCart cartInstance = ShoppingCart.getInstance();
    public abstract void update();
}
