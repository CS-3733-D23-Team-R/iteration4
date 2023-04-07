package edu.wpi.teamR.mapdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Location {
    private String longName, shortName, nodeType;

    public Location(String longName, String shortName, String nodeType){
        this.longName = longName;
        this.shortName = shortName;
        this.nodeType = nodeType;
    }
}
