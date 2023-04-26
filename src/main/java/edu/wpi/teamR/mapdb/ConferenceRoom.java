package edu.wpi.teamR.mapdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConferenceRoom {
    private String longName;
    private int capacity;
    private boolean isAccessible, hasOutlets, hasScreen;

    public ConferenceRoom(String longName, int capacity, boolean isAccessible, boolean hasOutlets, boolean hasScreen){
        this.longName = longName;
        this.capacity = capacity;
        this.isAccessible = isAccessible;
        this.hasOutlets = hasOutlets;
        this.hasScreen = hasScreen;
    }

    private ConferenceRoom(String[] args){
        //TODO: WHATEVER BEN NEEDS
    }
}
