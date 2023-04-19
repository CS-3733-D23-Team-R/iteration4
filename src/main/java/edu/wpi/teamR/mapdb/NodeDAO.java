package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NodeDAO {

    ArrayList<Node> getNodes() throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getNodeSchemaNameTableName()+";");
        return parseNodes(resultSet);
    }

    static ArrayList<Node> parseNodes(ResultSet resultSet) throws SQLException {
        ArrayList<Node> nodes = new ArrayList<>();
        while (resultSet.next()){
            int nodeID = resultSet.getInt("nodeid");
            int xCoord = resultSet.getInt("xCoord");
            int yCoord = resultSet.getInt("yCoord");
            String building = resultSet.getString("building");
            String floor = resultSet.getString("floor");

            nodes.add(new Node(nodeID, xCoord, yCoord, floor, building));
        }
        return nodes;
    }

    Node getNodeByID(int nodeID) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getNodeSchemaNameTableName()+" WHERE nodeID="+nodeID+";");
        resultSet.next();
        int xCoord = resultSet.getInt("xCoord");
        int yCoord = resultSet.getInt("yCoord");
        String building = resultSet.getString("building");
        String floor = resultSet.getString("floor");
        return new Node(nodeID, xCoord, yCoord, floor, building);
    }

    ArrayList<Node> getNodesByFloor(String floor) throws SQLException {
        Connection connection = Configuration.getConnection();
        ArrayList<Node> nodes = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getNodeSchemaNameTableName()+" WHERE floor='"+floor+"';");
        while (resultSet.next()){
            int nodeID = resultSet.getInt("nodeid");
            int xCoord = resultSet.getInt("xCoord");
            int yCoord = resultSet.getInt("yCoord");
            String building = resultSet.getString("building");
            nodes.add(new Node(nodeID, xCoord, yCoord, building, floor));
        }
        return nodes;
    }

    Node addNode(int xCoord, int yCoord, String floorNum, String building) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getNodeSchemaNameTableName()+"(xCoord, yCoord, floor, building) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, xCoord);
        preparedStatement.setInt(2, yCoord);
        preparedStatement.setString(3, floorNum);
        preparedStatement.setString(4, building);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int nodeID = 0;
        if (resultSet.next())
            nodeID = resultSet.getInt("nodeID");
        return new Node(nodeID, xCoord, yCoord, floorNum, building);
    }

    Node modifyCoords(int nodeID, int xCoord, int yCoord) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getNodeSchemaNameTableName()+" SET xCoord=?, yCoord=? WHERE nodeID=?;");
        preparedStatement.setInt(1, xCoord);
        preparedStatement.setInt(2, yCoord);
        preparedStatement.setInt(3, nodeID);
        preparedStatement.executeUpdate();
        return getNodeByID(nodeID);
    }

    void deleteNode(int nodeID) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getNodeSchemaNameTableName()+" WHERE nodeID=?");
        preparedStatement.setInt(1, nodeID);
        preparedStatement.executeUpdate();
    }

    void deleteAllNodes() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getNodeSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }


    void addNodes(List<Node> nodes) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getNodeSchemaNameTableName()+"(nodeID, xCoord, yCoord, floor, building) VALUES (?,?,?,?,?)");
        for (Node n : nodes) {
            preparedStatement.setInt(1, n.getNodeID());
            preparedStatement.setInt(2, n.getXCoord());
            preparedStatement.setInt(3, n.getYCoord());
            preparedStatement.setString(4, n.getFloorNum());
            preparedStatement.setString(5, n.getBuilding());
            preparedStatement.executeUpdate();
        }
    }
}
