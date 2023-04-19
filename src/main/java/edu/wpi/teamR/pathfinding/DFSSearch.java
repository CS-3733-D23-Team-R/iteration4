package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class DFSSearch extends SearchAlgorithm{
    DFSSearch(MapDatabase mapDatabase) {
        super(mapDatabase);
    }

    @Override
    public Path getPath(int startID, int endID, boolean accessible) throws SQLException, ItemNotFoundException {
        Path path = new Path();
        HashMap<Integer, Integer> cameFrom = new HashMap<>();
        cameFrom.put(startID, null);
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(startID);
        Integer currentNode;
        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            if (currentNode == endID) {
                break;
            }
            ArrayList<Integer> neighbors = mapDatabase.getAdjacentNodeIDsByNodeID(currentNode);
            for (Integer neighbor : neighbors) {
                if(accessible && mapDatabase.getNodeTypeByNodeID(currentNode).equals("STAI") && mapDatabase.getNodeTypeByNodeID(neighbor).equals("STAI")) {
                    continue;
                }
                if (!cameFrom.containsKey(neighbor)) {
                    queue.addFirst(neighbor);
                    cameFrom.put(neighbor, currentNode);
                }
            }
        }

        currentNode = endID;
        while (currentNode != startID) {
            path.add(currentNode);
            currentNode = cameFrom.get(currentNode);
        }
        path.add(currentNode);

        return path;
    }
}
