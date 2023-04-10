package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Queue;

public class MapDatabase {
//    private MapDatabase instance;
    private Connection connection;
    private NodeDAO nodeDao;
    private EdgeDAO edgeDao;
    private MoveDAO moveDao;
    private LocationNameDAO locationNameDao;

    public MapDatabase() throws SQLException, ClassNotFoundException {
        this.connection = Configuration.getConnection();
        this.nodeDao = new NodeDAO(connection);
        this.edgeDao = new EdgeDAO(connection);
        this.moveDao = new MoveDAO(connection);
        this.locationNameDao = new LocationNameDAO(connection);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    // old code for making mapDatabase singleton
//    public MapDatabase getInstance() {
//        if (instance != null)
//            instance = new MapDatabase();
//        return instance;
//    }

    void submitUpdates(Queue<ArrayList<Pair<?, EditType>>> updates) {

    }

    public ArrayList<Node> getNodes() throws SQLException {
        return nodeDao.getNodes();
    }

    public Node getNodeByID(int nodeID) throws SQLException {
        return nodeDao.getNodeByID(nodeID);
    }

    public ArrayList<Node> getNodesByFloor(String floor) throws SQLException {
        return nodeDao.getNodesByFloor(floor);
    }

    /*TODO public ArrayList<Node> getNodesByType(String type) throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        NodeDAO nodeDao = new NodeDAO(connection);
        ArrayList<Node> output = nodeDao.getNodesByType(type);
        connection.close();
        return(output);
    }*/

    public Node addNode(int xCoord, int yCoord, String floorNum, String building) throws SQLException {
        return nodeDao.addNode(xCoord, yCoord, floorNum, building);
    }

    public Node modifyCoords(int nodeID, int newXCoord, int newYCoord) throws SQLException {
        return nodeDao.modifyCoords(nodeID, newXCoord, newYCoord);
    }

    public void deleteNode(int nodeID) throws SQLException {
        nodeDao.deleteNode(nodeID);
    }

    public ArrayList<Edge> getEdges() throws SQLException {
        return edgeDao.getEdges();
    }

    public ArrayList<Edge> getEdgesByNode(int nodeID) throws SQLException {
        return edgeDao.getEdgesByNode(nodeID);
    }

    public Edge addEdge(int startNodeID, int endNodeID) throws SQLException {
        return edgeDao.addEdge(startNodeID, endNodeID);
    }

    public void deleteEdge(int startNodeID, int endNodeID) throws SQLException {
        edgeDao.deleteEdge(startNodeID, endNodeID);
    }

    public void deleteEdgesByNode(int nodeID) throws SQLException {
        edgeDao.deleteEdgesByNode(nodeID);
    }

    public ArrayList<Move> getMoves() throws SQLException {
        return moveDao.getMoves();
    }

    public ArrayList<Move> getMovesByNode(int nodeID) throws SQLException {
        return moveDao.getMovesByNodeID(nodeID);
    }

    /*TODO public Node getLatestMoveByLocationName(String longName) {
        return nodeDao.get;
    }*/

    public Move addMove(int nodeID, String longName, Date moveDate) throws SQLException {
        return moveDao.addMove(nodeID, longName, moveDate);
    }

    public void deleteMovesByNode(int nodeID) throws SQLException {
        moveDao.deleteMovesByNode(nodeID);
    }

    public void deleteMovesByLocationName(String longName) throws SQLException {
        moveDao.deleteMovesByLongName(longName);
    }

    public ArrayList<LocationName> getLocationNames() throws SQLException {
        return locationNameDao.getLocations();
    }

    public ArrayList<LocationName> getLocationNamesByNodeType(String nodeType) throws SQLException {
        return locationNameDao.getLocationsByNodeType(nodeType);
    }


    public LocationName getLocationNameByLongName(String longName) throws SQLException, ItemNotFoundException {
        return locationNameDao.getLocationByLongName(longName);
    }


    public LocationName modifyLocationNameType(String longName, String newType) throws SQLException {
        return locationNameDao.modifyLocationNameType(longName, newType);
    }

    public LocationName modifyLocationNameShortName(String longName, String newShortName) throws SQLException {
        return locationNameDao.modifyLocationNameShortName(longName, newShortName);
    }


    /*TODO public ArrayList<MapLocation> getMapLocationsByFloor(String floor) {
        return locationNameDao.getMap;
    }*/

}
