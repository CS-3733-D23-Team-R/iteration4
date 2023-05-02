package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.LocationName;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Node;

import javax.xml.stream.Location;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

public class BFSSearch extends SearchAlgorithm{
    BFSSearch(MapDatabase mapDatabase) {
        super(mapDatabase);
    }

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
                if(accessible && nodeTypeEquals(currentNode, "STAI") && nodeTypeEquals(neighbor, "STAI")) {
                    continue;
                }
                if (!cameFrom.containsKey(neighbor)) {
                    queue.addLast(neighbor);
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

    Node getNearest(int startID, Date date) throws SQLException, ItemNotFoundException {
        HashMap<Integer, Integer> cameFrom = new HashMap<>();
        cameFrom.put(startID, null);
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(startID);
        String floorNum = mapDatabase.getNodeByID(startID).getFloorNum();
        Integer currentNode;
        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            ArrayList<LocationName> locationNames = mapDatabase.getLocationNamesByNodeIDAtDate(currentNode, date);
            if (locationNames.size() > 0) {
                LocationName locationName = locationNames.get(0);
                if(!locationName.getNodeType().equals("HALL")) return mapDatabase.getNodeByID(currentNode);
            }
            ArrayList<Integer> neighbors = mapDatabase.getAdjacentNodeIDsByNodeID(currentNode);
            for (Integer neighbor : neighbors) {
                if(!mapDatabase.getNodeByID(neighbor).getFloorNum().equals(floorNum)) continue; //only on this floor
                if (!cameFrom.containsKey(neighbor)) {
                    queue.addLast(neighbor);
                    cameFrom.put(neighbor, currentNode);
                }
            }
        }
        return null;
    }
}
