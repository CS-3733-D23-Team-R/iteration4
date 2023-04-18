package edu.wpi.teamR.mapdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConferenceRoom {
    private String longname;
    private int capacity;
    private boolean isAccessible, hasOutlets, hasScreen;

    public ConferenceRoom(String longname, int capacity, boolean isAccessible, boolean hasOutlets, boolean hasScreen){
        this.longname = longname;
        this.capacity = capacity;
        this.isAccessible = isAccessible;
        this.hasOutlets = hasOutlets;
        this.hasScreen = hasScreen;
    }

    private ConferenceRoom(String[] args){
        //TODO: WHATEVER BEN NEEDS
    }
}
