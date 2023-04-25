package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.SQLException;

public class DijkstraSearch extends SearchTemplate {
    DijkstraSearch(MapDatabase mapDatabase) {
        super(mapDatabase);
    }

    @Override
    protected int heuristic(int node1, int node2) {
        return 0;
    }
}
