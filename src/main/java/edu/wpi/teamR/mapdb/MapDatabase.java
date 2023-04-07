package edu.wpi.teamR.mapdb;

import java.util.ArrayList;
import java.util.Queue;

public class MapDatabase {
    private MapDatabase instance;

    private MapDatabase(String connectionURL, String username, String schemaName, String tableName) {

    }

    public MapDatabase createInstance(String connectionURL, String username, String schemaName, String tableName) {
        if (instance != null)
            instance = new MapDatabase(connectionURL, username, schemaName, tableName);
        return instance;
    }

    public MapDatabase getInstance() {
        return instance;
    }

    void submitUpdates(Queue<ArrayList<Pair<?, EditType>>> updates) {

    }

    public ArrayList<Node> getNodes() {
        return null;
    }

    public Node getNodeByID(int nodeID) {
        return null;
    }

    public ArrayList<Node> getNodesByFloor(String floor) {
        return null;
    }

    public ArrayList<Node> getNodesByType(String type) {
        return null;
    }

    public Node addNode(int xCoord, int yCoord, String floorNum) {
        return null;
    }

    public Node modifyCoords(int nodeID, int newXCoord, int newYCoord) {
        return null;
    }

    public void deleteNode(int nodeID) {

    }
}
