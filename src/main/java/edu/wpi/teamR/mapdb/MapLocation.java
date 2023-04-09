package edu.wpi.teamR.mapdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Setter(AccessLevel.PACKAGE)
public class MapLocation {
    private Node node;
    private ArrayList<LocationName> locationNames;

    MapLocation(Node node, ArrayList<LocationName> locationNames) {
        this.node = node;
        this.locationNames = locationNames;
    }

    MapLocation(Node node, LocationName... locationNames) {
        this.node = node;
        this.locationNames = new ArrayList<>(Arrays.asList(locationNames));
    }
}
