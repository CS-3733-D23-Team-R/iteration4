package edu.wpi.teamR.mapdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Node {
    private int nodeID;
    private int xCoord;
    private int yCoord;
    private String floorNum, building;

    public Node(int nodeID, int xCoord, int yCoord, String floorNum, String building){
        this.nodeID = nodeID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floorNum = floorNum;
        this.building = building;
    }

}
