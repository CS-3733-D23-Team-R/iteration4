package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

class AstarSearch extends SearchAlgorithm {

    AstarSearch(MapDatabase mapDatabase) {
        super(mapDatabase);
    }

    //if accessible is True, the algorithm will not suggest staircases
    public Path getPath(int startID, int endID, boolean accessible) throws SQLException, ItemNotFoundException {
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
}
