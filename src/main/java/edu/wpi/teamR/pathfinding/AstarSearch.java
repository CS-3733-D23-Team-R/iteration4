package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

class AstarSearch extends SearchTemplate {

    AstarSearch(MapDatabase mapDatabase) {
        super(mapDatabase);
    }

    protected int heuristic(int nodeID, int endID) throws SQLException, ItemNotFoundException {
        //returns A* hueristic for node
        return nodeDist(nodeID, endID, 200);
    }
}
