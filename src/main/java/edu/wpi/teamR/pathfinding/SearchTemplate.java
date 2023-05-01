package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public abstract class SearchTemplate extends SearchAlgorithm {
    SearchTemplate(MapDatabase mapDatabase) {
        super(mapDatabase);
    }

    //if accessible is True, the algorithm will not suggest staircases
    @Override
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
                if(accessible && nodeTypeEquals(currentNode, "STAI") && nodeTypeEquals(neighbor, "STAI")) {
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

    protected abstract int heuristic(int node1, int node2) throws SQLException, ItemNotFoundException;
}
