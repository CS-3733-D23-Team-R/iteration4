package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConferenceRoom implements Archivable {
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
        this(args[0], Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]),
                Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]));
    }

    @Override
    public String toCSVEntry() {
        return longName + "," + capacity + "," + isAccessible + "," + hasOutlets + "," + hasScreen;
    }

    @Override
    public String getCSVColumns() {
        return "longName,capacity,isAccessible,hasOutlets,hasScreen";
    }
}
