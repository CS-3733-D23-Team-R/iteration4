package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;

import java.sql.Date;
import java.sql.SQLException;

public class Pathfinder {
    private static Algorithm currentAlgorithm = Algorithm.Astar;
    private MapDatabase mapDatabase;
    public Pathfinder(MapDatabase mapDatabase) throws ItemNotFoundException {
        this.mapDatabase = mapDatabase;
    }

    public void setAlgorithm(Algorithm newAlgorithm){
        currentAlgorithm = newAlgorithm;
    }

    Algorithm getCurrentAlgorithm(){return currentAlgorithm;}

    public Path findPath(int startID, int endID, boolean accessible) throws ItemNotFoundException, SQLException {
        SearchInterface search = switch (currentAlgorithm) {
            case Astar -> new AstarSearch(mapDatabase);
            case Dijkstra -> new DijkstraSearch(mapDatabase);
            case BFS -> new BFSSearch(mapDatabase);
            case DFS -> new DFSSearch(mapDatabase);
            default -> throw new ItemNotFoundException();
        };
        return search.getPath(startID, endID, accessible);
    }

}