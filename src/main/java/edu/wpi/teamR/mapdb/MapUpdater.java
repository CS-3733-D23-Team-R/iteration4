package edu.wpi.teamR.mapdb;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

public class MapUpdater {
    ArrayDeque<UpdateAction> actionQueue;
    UpdateAction currentAction;
    MapDatabase mapdb;


    public MapUpdater(MapDatabase mapDatabase) {
        actionQueue = new ArrayDeque<>();
        mapdb = mapDatabase;
    }

    public void startAction() {
        if (currentAction != null)
            actionQueue.addFirst(currentAction);
        currentAction = new UpdateAction();
    }

    public void endAction() {
        if (currentAction != null)
            actionQueue.addFirst(currentAction);
        currentAction = null;
    }

    public void submitUpdates() throws SQLException {
        if (currentAction != null) actionQueue.addFirst(currentAction);
        Collection<Pair<MapData, EditType>> action;
        while(!actionQueue.isEmpty()) {
            action = actionQueue.removeLast().getUpdates();
            for (Pair<MapData, EditType> p : action) {
                switch (p.second) {
                    case ADDITION -> {
                        if (p.first instanceof Node n) {
                            mapdb.addNode(n.getXCoord(), n.getYCoord(), n.getFloorNum(), n.getBuilding());
                        } else if (p.first instanceof Edge e) {
                            mapdb.addEdge(e.getStartNode(), e.getEndNode());
                        } else if (p.first instanceof Move m) {
                            mapdb.addMove(m.getNodeID(), m.getLongName(), m.getMoveDate());
                        } else if (p.first instanceof LocationName l) {
                            mapdb.addLocationName(l.getLongName(), l.getShortName(), l.getNodeType());
                        }
                    }
                    case MODIFICATION -> {
                        if (p.first instanceof Node n) {
                            mapdb.modifyCoords(n.getNodeID(), n.getXCoord(), n.getYCoord());
                        } else if (p.first instanceof LocationName l) {
                            mapdb.deleteLocationName(l.getLongName());
                            mapdb.addLocationName(l.getLongName(), l.getShortName(), l.getNodeType());
                        }
                    }
                    case DELETION -> {
                        if (p.first instanceof Node n) {
                            mapdb.deleteNode(n.getNodeID());
                        } else if (p.first instanceof Edge e) {
                            mapdb.deleteEdge(e.getStartNode(), e.getEndNode());
                        } else if (p.first instanceof Move m) {
                            mapdb.deleteMove(m.getNodeID(), m.getLongName(), m.getMoveDate());
                        } else if (p.first instanceof LocationName l) {
                            mapdb.deleteLocationName(l.getLongName());
                        }
                    }
                }
            }

        }
    }

    public UpdateAction undo() {
        return actionQueue.removeFirst();
    }


    public Node addNode(int xCoord, int yCoord, String floorNum, String building) {
        if (currentAction == null) currentAction = new UpdateAction();
        Node n = new Node(-1, xCoord, yCoord, floorNum, building);
        currentAction.addUpdate(n, EditType.ADDITION);
        return n;
    }

    public Node modifyCoords(int nodeID, int xCoord, int yCoord) throws SQLException {
        if (currentAction == null) currentAction = new UpdateAction();
        Node n = mapdb.getNodeByID(nodeID);
        n.setXCoord(xCoord);
        n.setYCoord(yCoord);
        currentAction.addUpdate(n, EditType.MODIFICATION);
        return n;
    }

    public void deleteNode(int nodeID) throws SQLException {
        if (currentAction == null) currentAction = new UpdateAction();
        currentAction.addUpdate(mapdb.getNodeByID(nodeID), EditType.DELETION);
    }


    public Edge addEdge(int startNode, int endNode) {
        if (currentAction == null) currentAction = new UpdateAction();
        Edge e = new Edge(startNode, endNode);
        currentAction.addUpdate(e, EditType.ADDITION);
        return e;
    }

    public void deleteEdge(int startNode, int endNode) {
        if (currentAction == null) currentAction = new UpdateAction();
        Edge e = new Edge(startNode, endNode);
        currentAction.addUpdate(e, EditType.DELETION);
    }

    public void deleteEdgesByNode(int nodeID) throws SQLException {
        if (currentAction == null) currentAction = new UpdateAction();
        ArrayList<Edge> edges = mapdb.getEdgesByNode(nodeID);
        currentAction.addUpdates(edges, EditType.DELETION);
    }


    public Move addMove(int nodeID, String longName, Date moveDate) {
        if (currentAction == null) currentAction = new UpdateAction();
        Move m = new Move(nodeID, longName, moveDate);
        currentAction.addUpdate(m, EditType.ADDITION);
        return m;
    }

    public void deleteMovesByNode(int nodeID) throws SQLException {
        if (currentAction == null) currentAction = new UpdateAction();
        ArrayList<Move> moves = mapdb.getMovesByNode(nodeID);
        currentAction.addUpdates(moves, EditType.DELETION);
    }

    public void deleteMovesByLocationName(String longName) throws SQLException, ItemNotFoundException {
        if (currentAction == null) currentAction = new UpdateAction();
        Move m = mapdb.getLatestMoveByLocationName(longName);
        currentAction.addUpdate(m, EditType.DELETION);
    }


    public LocationName modifyLocationNameType(String longName, String newType) throws SQLException, ItemNotFoundException {
        if (currentAction == null) currentAction = new UpdateAction();
        LocationName l = mapdb.getLocationNameByLongName(longName);
        l.setNodeType(newType);
        currentAction.addUpdate(l, EditType.MODIFICATION);
        return l;
    }

    public LocationName modifyLocationNameShortName(String longName, String newShortName) throws SQLException, ItemNotFoundException {
        if (currentAction == null) currentAction = new UpdateAction();
        LocationName l = mapdb.getLocationNameByLongName(longName);
        l.setShortName(newShortName);
        currentAction.addUpdate(l, EditType.MODIFICATION);
        return l;
    }

    public LocationName addLocationName(String longName, String shortName, String nodeType) {
        if (currentAction == null) currentAction = new UpdateAction();
        LocationName l = new LocationName(longName, shortName, nodeType);
        currentAction.addUpdate(l, EditType.ADDITION);
        return l;
    }
}
