package edu.wpi.teamR.pathfinding;

import lombok.Getter;

import java.util.ArrayList;

public class TextByFloor {
    @Getter
    ArrayList<String> floorText;
    @Getter
    ArrayList<Integer> nodeIDs;
    @Getter
    String floorNum;

    TextByFloor(){
    }
    TextByFloor(ArrayList<Integer> nodeIDs, String floorNum) {
        this.nodeIDs = nodeIDs;
        this.floorNum = floorNum;
    }
}
