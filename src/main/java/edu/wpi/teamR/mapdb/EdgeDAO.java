package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;

public class EdgeDAO {
    private Connection connection;

    EdgeDAO(Connection connection){
        this.connection = connection;
    }
    ArrayList<Edge> getEdges() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+ Configuration.getEdgeSchemaNameTableName()+";");
        ArrayList<Edge> edges = new ArrayList<>();

        while (resultSet.next()){
            int startNode = resultSet.getInt("startnode");
            int endNode = resultSet.getInt("endnode");

            edges.add(new Edge(startNode, endNode));
        }
        return edges;
    }

    ArrayList<Edge> getEdgesByNode(int nodeID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+ Configuration.getEdgeSchemaNameTableName()+" WHERE startnode=? OR endnode=?;");
        statement.setInt(1, nodeID);
        statement.setInt(2, nodeID);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Edge> edges = new ArrayList<>();
        while (resultSet.next()){
            int startNode = resultSet.getInt("startnode");
            int endNode = resultSet.getInt("endnode");

            edges.add(new Edge(startNode, endNode));
        }
        return edges;
    }

    void deleteEdge(int startNodeID, int endNodeID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getEdgeSchemaNameTableName()+" WHERE startnode=? AND endnode=?");
        preparedStatement.setInt(1, startNodeID);
        preparedStatement.setInt(2, endNodeID);
        preparedStatement.executeUpdate();
    }
    void deleteEdgesByNode(int nodeID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getEdgeSchemaNameTableName()+" WHERE startnode=? OR endnode=?");
        preparedStatement.setInt(1, nodeID);
        preparedStatement.setInt(2, nodeID);
        preparedStatement.executeUpdate();
    }

    Edge addEdge(int startnode, int endnode) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getEdgeSchemaNameTableName()+"(startnode, endnode) VALUES(?, ?);");
        preparedStatement.setInt(1, startnode);
        preparedStatement.setInt(2, endnode);
        preparedStatement.executeUpdate();
        return new Edge(startnode, endnode);
    }

    void deleteAllEdges() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getEdgeSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    public ArrayList<Integer> getAdjacentNodeIDsByNodeID(int nodeID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT startnode,endnode FROM "+Configuration.getEdgeSchemaNameTableName()+" WHERE startnode=? OR endnode=?;");
        preparedStatement.setInt(1, nodeID);
        preparedStatement.setInt(2, nodeID);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Integer> nodeIDs = new ArrayList<>();

        while (resultSet.next()){
            int startNode = resultSet.getInt("startnode");
            int endNode = resultSet.getInt("endnode");

            if (startNode != nodeID)
                nodeIDs.add(startNode);

            if (endNode != nodeID)
                nodeIDs.add(endNode);
        }
        return nodeIDs;
    }
}
