package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Node;

import java.sql.SQLException;

import static java.lang.Math.abs;

abstract class SearchAlgorithm implements SearchInterface {

    MapDatabase mapDatabase;
    SearchAlgorithm(MapDatabase mapDatabase) {
        this.mapDatabase = mapDatabase;
    }
    abstract public Path getPath(int startID, int endID, boolean accessible) throws SQLException, ItemNotFoundException;
    int nodeDist(int currentNodeID, int nextNodeID) throws SQLException, ItemNotFoundException {
        return nodeDist(currentNodeID, nextNodeID, 100);
    }

    protected int nodeDist(int currentNodeID, int nextNodeID, int zDifMultiplier) throws SQLException, ItemNotFoundException {
        //finds difference in x,y
        Node currNode = mapDatabase.getNodeByID(currentNodeID);
        Node nextNode = mapDatabase.getNodeByID(nextNodeID);

        int xDif = abs(currNode.getXCoord() - nextNode.getXCoord());
        int yDif = abs(currNode.getYCoord() - nextNode.getYCoord());
        int zDif = abs(floorNumAsInt(currNode.getFloorNum()) - floorNumAsInt(nextNode.getFloorNum()));

        if (nodeTypeEquals(currentNodeID, "STAI") && nodeTypeEquals(nextNodeID, "STAI")) {
            zDif = zDif * zDifMultiplier * 2;
        } else {
            zDif = zDif * zDifMultiplier;
        }

        return xDif + yDif + zDif; //returns distance
    }

    //outputs the floor number 0 indexed from the lowest floor (L2)
    protected int floorNumAsInt(String floorNum) {
        return switch (floorNum) {
            case "L1" -> 1;
            case "L2" -> 0;
            default -> Integer.parseInt(floorNum) + 1;
        };
    }

    protected boolean nodeTypeEquals(int nodeID, String type) throws SQLException{
        try {
            return mapDatabase.getNodeTypeByNodeID(nodeID).equals(type);
        } catch (ItemNotFoundException e){
            return false;
        }
    }
}