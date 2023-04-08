package edu.wpi.teamR.mapdb;

import javafx.util.Pair;

import java.sql.Date;
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

    public ArrayList<Edge> getEdges() {
        return null;
    }

    public ArrayList<Edge> getEdgesByNode(int nodeID) {
        return null;
    }

    public Edge addEdge(int startNodeID, int endNodeID) {
        return null;
    }

    public void deleteEdge(int startNodeID, int endNodeID) {

    }

    public void deleteEdgesByNode(int nodeID) {

    }

    public ArrayList<Move> getMoves() {
        return null;
    }

    public ArrayList<Move> getMovesByNode(int nodeID) {
        return null;
    }

    public Node getLatestMoveByLocationName(String longName) {
        return null;
    }

    public Move addMove(int nodeID, String longName, Date moveDate) {
        return null;
    }

    public void deleteMovesByNode(int nodeID) {

    }

    public void deleteMovesByLocationName(String longName) {

    }

    public ArrayList<LocationName> getLocationNames() {
        return null;
    }

    public LocationName getLocationNameByLongName(String longName) {
        return null;
    }

    public LocationName modifyLocationNameType(String longName, String newType) {
        return null;
    }

    public LocationName modifyLocationNameShortName(String longName, String newShortName) {
        return null;
    }

    public ArrayList<MapLocation> getMapLocationsByFloor(String floor) {
        return null;
    }
}
