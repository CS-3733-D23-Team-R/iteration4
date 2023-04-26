package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.archive.CSVParameterException;
import edu.wpi.teamR.archive.CSVReader;
import edu.wpi.teamR.requestdb.ItemRequestDAO;
import edu.wpi.teamR.requestdb.RoomRequestDAO;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static edu.wpi.teamR.mapdb.NodeDAO.parseNodes;

public class MapDatabase {
    private final NodeDAO nodeDao;
    private final EdgeDAO edgeDao;
    private final MoveDAO moveDao;
    private final LocationNameDAO locationNameDao;
    private final DirectionArrowDAO directionArrowDAO;
    private final ConferenceRoomDAO conferenceRoomDAO;
    private final ItemRequestDAO itemRequestDAO;
    private final RoomRequestDAO roomRequestDAO;

    public MapDatabase() throws SQLException {
        this.nodeDao = new NodeDAO();
        this.edgeDao = new EdgeDAO();
        this.moveDao = new MoveDAO();
        this.locationNameDao = new LocationNameDAO();
        this.directionArrowDAO = new DirectionArrowDAO();
        this.conferenceRoomDAO = new ConferenceRoomDAO();
        this.itemRequestDAO = new ItemRequestDAO();
        this.roomRequestDAO = new RoomRequestDAO();
    }

    public ArrayList<Node> getNodes() throws SQLException {
        return nodeDao.getNodes();
    }

    public Node getNodeByID(int nodeID) throws SQLException, ItemNotFoundException {
        return nodeDao.getNodeByID(nodeID);
    }

    public ArrayList<Node> getNodesByFloor(String floor) throws SQLException {
        return nodeDao.getNodesByFloor(floor);
    }

    public ArrayList<Node> getNodesByType(String type) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getNodeSchemaNameTableName()+" NATURAL JOIN (SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" NATURAL JOIN (SELECT longname, MAX(date) as date from "+Configuration.getMoveSchemaNameTableName()+" WHERE date<now() group by longname) as foo) as foo natural join "+Configuration.getLocationNameSchemaNameTableName()+" WHERE nodetype=? ORDER BY nodeID;");
        preparedStatement.setString(1, type);
        ResultSet resultSet = preparedStatement.executeQuery();
        return parseNodes(resultSet);
    }

    public Node addNode(int xCoord, int yCoord, String floorNum, String building) throws SQLException {
        return nodeDao.addNode(xCoord, yCoord, floorNum, building);
    }

    public void addNode(Node node) throws SQLException {
        List<Node> list = new ArrayList<>();
        list.add(node);
        nodeDao.addNodes(list);
    }

    public void addNodes(List<Node> nodes) throws SQLException {
        nodeDao.addNodes(nodes);
    }

    public Node modifyCoords(int nodeID, int newXCoord, int newYCoord) throws SQLException, ItemNotFoundException {
        return nodeDao.modifyCoords(nodeID, newXCoord, newYCoord);
    }

    public void deleteNode(int nodeID) throws SQLException {
        nodeDao.deleteNode(nodeID);
    }

    public void deleteAllNodes() throws SQLException {
        nodeDao.deleteAllNodes();
    }

    public ArrayList<Edge> getEdges() throws SQLException {
        return edgeDao.getEdges();
    }

    public ArrayList<Edge> getEdgesByNode(int nodeID) throws SQLException {
        return edgeDao.getEdgesByNode(nodeID);
    }

    public ArrayList<Edge> getEdgesByFloor(String floor) throws SQLException {
        Connection connection = Configuration.getConnection();
        ArrayList<Edge> temp = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select startnode,endnode from "+Configuration.getNodeSchemaNameTableName()+" join "+Configuration.getEdgeSchemaNameTableName()+" on node.nodeid = edge.startnode or node.nodeid = edge.endnode where floor = '"+floor+"';");
        while(resultSet.next()){
            Edge aEdge = new Edge(resultSet.getInt("startnode"), resultSet.getInt("endnode"));
            temp.add(aEdge);
        }
        return temp;
    }

    public Edge addEdge(int startNodeID, int endNodeID) throws SQLException {
        return edgeDao.addEdge(startNodeID, endNodeID);
    }

    public void addEdges(List<Edge> edges) throws SQLException {
        edgeDao.addEdges(edges);
    }

    public void deleteEdge(int startNodeID, int endNodeID) throws SQLException {
        edgeDao.deleteEdge(startNodeID, endNodeID);
    }

    public void deleteEdgesByNode(int nodeID) throws SQLException {
        edgeDao.deleteEdgesByNode(nodeID);
    }

    public ArrayList<Integer> getAdjacentNodeIDsByNodeID(int nodeID) throws SQLException {
        return edgeDao.getAdjacentNodeIDsByNodeID(nodeID);
    }
    public void deleteAllEdges() throws SQLException {
        edgeDao.deleteAllEdges();
    }

    public ArrayList<Move> getMoves() throws SQLException {
        return moveDao.getMoves();
    }

    public Move getMoveByLocationAndDate(String longName, Date date) throws SQLException, ItemNotFoundException {
        return moveDao.getMoveByLocationAndDate(longName, date);
    }

    public Move getLatestMoveForLocationByDate(String longName, Date date) throws SQLException, ItemNotFoundException {
        return moveDao.getLatestMoveForLocationByDate(longName, date);
    }

    public ArrayList<Move> getMovesForDate(Date date) throws SQLException {
        return moveDao.getMovesForDate(date);
    }

    public ArrayList<Move> getMovesByNode(int nodeID) throws SQLException {
        return moveDao.getMovesByNodeID(nodeID);
    }

    public Move getLatestMoveByLocationName(String longName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE date=(select max(date) FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE longname = ? AND date<now()) AND longname = ?;");
        preparedStatement.setString(1, longName);
        preparedStatement.setString(2, longName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            int nodeID = resultSet.getInt("nodeID");
            Date date = resultSet.getDate("date");

            return new Move(nodeID, longName, date);
        }
        throw new ItemNotFoundException();
    }

    public Move addMove(int nodeID, String longName, Date moveDate) throws SQLException {
        return moveDao.addMove(nodeID, longName, moveDate);
    }

    public void addMoves(ArrayList<Move> moves) throws SQLException {
        moveDao.addMoves(moves);
    }

    public void deleteMovesByNode(int nodeID) throws SQLException {
        moveDao.deleteMovesByNode(nodeID);
    }

    public void deleteMove(int nodeID, String longname, Date moveDate) throws SQLException {
        moveDao.deleteMove(nodeID, longname, moveDate);
    }

    public void deleteAllMoves() throws SQLException {
        moveDao.deleteAllMoves();
    }

    public LocationName addLocationName(String longname, String shortname, String nodetype) throws SQLException {
        return locationNameDao.addLocationName(longname, shortname, nodetype);
    }

    public void addLocationNames(List<LocationName> locationNames) throws SQLException {
        locationNameDao.addLocationNames(locationNames);
    }

    public void deleteLocationName(String longname) throws SQLException {
        locationNameDao.deleteLocationName(longname);
    }

    public void deleteAllLocationNames() throws SQLException {
        locationNameDao.deleteAllLocationNames();
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

    public ArrayList<LocationName> getLocationNamesByNodeIDAtDate(int nodeID, Date date) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM (SELECT longname FROM (SELECT longname, MAX(date) AS date FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE date<? GROUP BY longname) as foo NATURAL JOIN "+Configuration.getMoveSchemaNameTableName()+" WHERE nodeid=? ORDER BY date desc) as foo NATURAL JOIN "+Configuration.getLocationNameSchemaNameTableName()+";");
        preparedStatement.setDate(1, date);
        preparedStatement.setInt(2, nodeID);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<LocationName> locationNames = new ArrayList<>();

        while(resultSet.next()){
            String aLongName = resultSet.getString("longname");
            String aShortName = resultSet.getString("shortname");
            String aNodeType = resultSet.getString("nodetype");
            LocationName aLocationName = new LocationName(aLongName, aShortName, aNodeType);
            locationNames.add(aLocationName);
        }
        return locationNames;
    }

    public String getNodeTypeByNodeID(int nodeID) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT nodetype from (SELECT * FROM (SELECT longname, MAX(date) as date from "+Configuration.getMoveSchemaNameTableName()+" WHERE date<now() AND nodeid=? group by longname) as foo ORDER BY date desc limit 1) as foo NATURAL JOIN "+Configuration.getLocationNameSchemaNameTableName()+";");
        preparedStatement.setInt(1, nodeID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            return resultSet.getString("nodetype");

        throw new ItemNotFoundException();
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

    public ConferenceRoom addConferenceRoom(String longname, int capacity, boolean isAccessible, boolean hasOutlets, boolean hasScreen) throws SQLException, ClassNotFoundException {
        return conferenceRoomDAO.addConferenceRoom(longname, capacity, isAccessible, hasOutlets, hasScreen);
    }

    public void addConferenceRooms(List<ConferenceRoom> conferenceRooms) throws SQLException {
        conferenceRoomDAO.addConferenceRooms(conferenceRooms);
    }
    public void deleteConferenceRoom(String longname) throws SQLException, ItemNotFoundException {
        conferenceRoomDAO.deleteConferenceRoom(longname);
    }

    public ArrayList<ConferenceRoom> getConferenceRooms() throws SQLException {
        return conferenceRoomDAO.getConferenceRooms();
    }


    public DirectionArrow addDirectionArrow(String longName, int kioskID, Direction direction) throws SQLException {
        return directionArrowDAO.addDirectionArrow(longName, kioskID, direction);
    }

    public void addDirectionArrows(ArrayList<DirectionArrow> directionArrows) throws SQLException {
        directionArrowDAO.addDirectionArrows(directionArrows);
    }

    public void deleteDirectionArrowByLongname(String longname) throws SQLException, ItemNotFoundException {
        directionArrowDAO.deleteDirectionArrowByLongName(longname);
    }

    public void deleteDirectionArrowsByKiosk(int kioskID) throws SQLException, ItemNotFoundException {
        directionArrowDAO.deleteDirectionArrowsByKiosk(kioskID);
    }

    public void deleteAllDirectionArrows() throws SQLException {
        directionArrowDAO.deleteAllDirectionArrows();
    }

    public ArrayList<DirectionArrow> getDirectionArrows() throws SQLException {
        return directionArrowDAO.getDirectionArrows();
    }

    public ArrayList<DirectionArrow> getDirectionArrowsByKiosk(int kioskID) throws SQLException {
        return directionArrowDAO.getDirectionArrowsByKiosk(kioskID);
    }

    public Node getNodeFromLocationName(String locationName) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT nodeID, xCoord, yCoord, Building, floor FROM (SELECT longname, MAX(date) as date from "+Configuration.getMoveSchemaNameTableName()+" WHERE date<now() AND longname=? group by longname) as foo NATURAL JOIN "+Configuration.getMoveSchemaNameTableName()+" NATURAL JOIN "+Configuration.getNodeSchemaNameTableName()+";");
        preparedStatement.setString(1, locationName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int nodeID = resultSet.getInt("nodeid");
            int xCoord = resultSet.getInt("xCoord");
            int yCoord = resultSet.getInt("yCoord");
            String building = resultSet.getString("building");
            String floor = resultSet.getString("floor");
            return new Node(nodeID, xCoord, yCoord, floor, building);
        }
        throw new ItemNotFoundException();
    }

    public ConferenceRoom getConferenceRoomByLongName(String longname) throws SQLException, ItemNotFoundException {
        return conferenceRoomDAO.getConferenceRoomByLongName(longname);
    }

    public void deleteAllConferenceRooms() throws SQLException {
        conferenceRoomDAO.deleteAllConferenceRooms();
    }

    public ArrayList<MapLocation> getMapLocationsByFloor(String floor) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getNodeSchemaNameTableName()+" node LEFT JOIN (SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" NATURAL JOIN (SELECT longname, MAX(date) as date from "+Configuration.getMoveSchemaNameTableName()+" WHERE date<now() group by longname) as foo) as move on node.nodeid=move.nodeid left join "+Configuration.getLocationNameSchemaNameTableName()+" locationname on move.longname=locationname.longname WHERE floor=? ORDER BY node.nodeID desc;");
        preparedStatement.setString(1, floor);
        ResultSet resultSet = preparedStatement.executeQuery();

        return getMapLocations(floor, resultSet);
    }

    public ArrayList<MapLocation> getMapLocationsByFloorForDate(String floor, Date date) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getNodeSchemaNameTableName()+" node LEFT JOIN (SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" NATURAL JOIN (SELECT longname, MAX(date) as date from "+Configuration.getMoveSchemaNameTableName()+" WHERE date<? group by longname) as foo) as move on node.nodeid=move.nodeid left join "+Configuration.getLocationNameSchemaNameTableName()+" locationname on move.longname=locationname.longname WHERE floor=? ORDER BY node.nodeID desc;");
        preparedStatement.setDate(1, date);
        preparedStatement.setString(2, floor);
        ResultSet resultSet = preparedStatement.executeQuery();

        return getMapLocations(floor, resultSet);
    }

    @NotNull
    private ArrayList<MapLocation> getMapLocations(String floor, ResultSet resultSet) throws SQLException {
        ArrayList<MapLocation> mapLocations = new ArrayList<>();
        Node lastNode = new Node(-100, 0, 0, "", "");
        Node currentNode;
        ArrayList<LocationName> locationNames = new ArrayList<>();
        LocationName locationName;
        while (resultSet.next()){
            int nodeID = resultSet.getInt("nodeid");
            int xCoord = resultSet.getInt("xCoord");
            int yCoord = resultSet.getInt("yCoord");
            String building = resultSet.getString("building");
            String longName = resultSet.getString("longName");
            String shortName = resultSet.getString("shortName");
            String nodeType = resultSet.getString("nodeType");

            currentNode = new Node(nodeID, xCoord, yCoord, floor, building);
            locationName = new LocationName(longName, shortName, nodeType);

            boolean noLocationNameForNode = longName==null && shortName==null && nodeType==null;
            boolean continuingLastNode = lastNode.getNodeID()==nodeID;
            boolean noLastNode = lastNode.getNodeID()==-100;
            if (continuingLastNode&&!noLocationNameForNode){
                locationNames.add(locationName);
            } else if (!continuingLastNode){
                if (!noLastNode)
                    mapLocations.add(new MapLocation(lastNode, locationNames));
                locationNames = new ArrayList<LocationName>();
                if (!noLocationNameForNode)
                    locationNames.add(locationName);
            }

            lastNode = currentNode;
        }
        mapLocations.add(new MapLocation(lastNode, locationNames)); //the last locationName will already have been added

        return mapLocations;
    }

    public ArrayList<? extends MapData> readCSV(String path, Class<? extends MapData> _class) throws IOException, CSVParameterException, SQLException, ClassNotFoundException {
        String[] fullName = _class.getName().split("[.]");
        String name = fullName[fullName.length - 1];
        switch (name) {
            case "Node" -> {
                CSVReader reader = new CSVReader();
                ArrayList<Node> nodes = reader.parseCSV(Node.class, path);
                edgeDao.deleteAllEdges();
                moveDao.deleteAllMoves();
                nodeDao.deleteAllNodes();
                nodeDao.addNodes(nodes);
                return nodes;
            }
            case "Edge" -> {
                CSVReader reader = new CSVReader();
                ArrayList<Edge> edges = reader.parseCSV(Edge.class, path);
                edgeDao.deleteAllEdges();
                for (Edge e : edges) {
                    edgeDao.addEdge(e.getStartNode(), e.getEndNode());
                }
                return edges;
            }
            case "Move" -> {
                CSVReader reader = new CSVReader();
                ArrayList<Move> moves = reader.parseCSV(Move.class, path);
                moveDao.deleteAllMoves();
                for (Move m : moves) {
                    moveDao.addMove(m.getNodeID(), m.getLongName(), m.getMoveDate());
                }
                return moves;
            }
            case "LocationName" -> {
                CSVReader reader = new CSVReader();
                ArrayList<LocationName> locs = reader.parseCSV(LocationName.class, path);
                moveDao.deleteAllMoves();
                directionArrowDAO.deleteAllDirectionArrows();
                roomRequestDAO.deleteAllRoomRequests();
                conferenceRoomDAO.deleteAllConferenceRooms();
                itemRequestDAO.deleteAllItemRequests();
                locationNameDao.deleteAllLocationNames();
                for (LocationName l : locs) {
                    locationNameDao.addLocationName(l.getLongName(), l.getShortName(), l.getNodeType());
                }
                return locs;
            }
            default -> throw new IllegalStateException("Unexpected class name: " + _class.getName());
        }
    }



}
