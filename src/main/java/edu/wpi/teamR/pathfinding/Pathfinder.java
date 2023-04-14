package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Node;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

public class Pathfinder {
    private MapDatabase mapDatabase;
    public Pathfinder(MapDatabase mapDatabase) {
        this.mapDatabase = mapDatabase;
    }

    //if accessible is True, the algorithm will not suggest staircases
    public Path aStarPath(int startID, int endID, boolean accessible) throws Exception, ItemNotFoundException {
        Path path = new Path();
        HashMap<Integer, Integer> cameFrom = new HashMap<>();
        HashMap<Integer, Integer> costSoFar = new HashMap<>();
        PriorityQueue<QueueNode> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueNode(startID, 0));
        int currentNode;
        costSoFar.put(startID, 0);
        while(!pQueue.isEmpty()){
            currentNode = pQueue.remove().getNodeID();

            if(currentNode == endID){ break; }

            ArrayList<Integer> neighbors = mapDatabase.getAdjacentNodeIDsByNodeID(currentNode);
            for (int neighbor : neighbors) {
                //remove stair nodes if accessible is checked
                if(accessible && mapDatabase.getNodeTypeByNodeID(currentNode).equals("STAI") && mapDatabase.getNodeTypeByNodeID(neighbor).equals("STAI")) {
                    continue;
                }
                int newCost = costSoFar.get(currentNode) + nodeDist(currentNode, neighbor);
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)){
                    costSoFar.put(neighbor, newCost);
                    int priority = newCost + heuristic(neighbor, endID);
                    pQueue.add(new QueueNode(neighbor, priority));
                    cameFrom.put(neighbor, currentNode);
                }
            }
        }

        currentNode = endID;
        while (currentNode != startID) {
            path.add(currentNode);
            currentNode = cameFrom.get(currentNode);
        }
        path.add(startID);

        return path;
    }

    private int heuristic(int nodeID, int endID) throws SQLException, ItemNotFoundException {
        //returns A* hueristic for node
        return nodeDist(nodeID, endID, 200);
    }

    private ArrayList<Integer> findNeighboringNodes(int nodeID, int endID) throws SQLException, ItemNotFoundException {
        // ArrayList<Node> neighbors = new ArrayList<Node>();
        ArrayList<Integer> neighbors = mapDatabase.getAdjacentNodeIDsByNodeID(nodeID);
        for (Integer neighbor : neighbors) {
            if (neighbor.equals(endID)) {
                continue;
            }
            String type = mapDatabase.getNodeTypeByNodeID(neighbor);
            if (!type.equals("HALL") && !type.equals("ELEV") && !type.equals("STAI")) {
                neighbors.remove(neighbor);
            }
        }

        return neighbors;
    }

    private int nodeDist(int currentNodeID, int nextNodeID) throws SQLException, ItemNotFoundException {
        return nodeDist(currentNodeID, nextNodeID, 100);
    }

    private int nodeDist(int currentNodeID, int nextNodeID, int zDifMultiplier) throws SQLException, ItemNotFoundException {
        //finds difference in x,y
        Node currNode = mapDatabase.getNodeByID(currentNodeID);
        Node nextNode = mapDatabase.getNodeByID(nextNodeID);

        int xDif = abs(currNode.getXCoord() - nextNode.getXCoord());
        int yDif = abs(currNode.getYCoord() - nextNode.getYCoord());
        int zDif = abs(floorNumAsInt(currNode.getFloorNum()) - floorNumAsInt(nextNode.getFloorNum()));

        if (mapDatabase.getNodeTypeByNodeID(currentNodeID).equals("STAI") && mapDatabase.getNodeTypeByNodeID(nextNodeID).equals("STAI")) {
            zDif = zDif * zDifMultiplier * 2;
        } else {
            zDif = zDif * zDifMultiplier;
        }

        return xDif + yDif + zDif; //returns distance
    }

    //outputs the floor number 0 indexed from the lowest floor (L2)
    private int floorNumAsInt(String floorNum){
        return switch (floorNum) {
            case "L1" -> 1;
            case "L2" -> 0;
            default -> Integer.parseInt(floorNum) + 1;
        };
    }
}