package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import oracle.ucp.proxy.annotation.Pre;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

public class MoveDAO {
    private Connection aConnection;
    MoveDAO(Connection connection) throws SQLException, ClassNotFoundException {
        aConnection = connection;
    }
    ArrayList<Move> getMoves() throws SQLException {
        ArrayList<Move> temp = new ArrayList<Move>();
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+";");
        while(resultSet.next()){
            java.sql.Date aDate = resultSet.getDate("date");
            String aLongName = resultSet.getString("longname");
            int aNodeID = resultSet.getInt("nodeID");
            Move aMove = new Move(aNodeID, aLongName, aDate);
            temp.add(aMove);
        }
        aConnection.close();
        return temp;
    }
    ArrayList<Move> getMovesByNodeID(int nodeID) throws SQLException {
        ArrayList<Move> temp = new ArrayList<Move>();
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE nodeID = "+nodeID+";");
        while(resultSet.next()){
            java.sql.Date aDate = resultSet.getDate("date");
            String aLongName = resultSet.getString("longname");
            int aNodeID = resultSet.getInt("nodeID");
            Move aMove = new Move(aNodeID, aLongName, aDate);
            temp.add(aMove);
        }
        aConnection.close();
        return temp;
    }
    Move getLatestMoveByNodeID(int nodeID) throws SQLException {
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE date=(select max(date) FROM iteration1test.move WHERE nodeID = "+nodeID+") AND nodeID = "+nodeID+";");
        Move temp = new Move(resultSet.getInt("nodeid"), resultSet.getString("longname"), resultSet.getDate("date"));
        aConnection.close();
        return temp;
    }
    Move addMove(int nodeID, String longName, java.sql.Date moveDate) throws SQLException {
        PreparedStatement statement = aConnection.prepareStatement("INSERT INTO ? WHERE nodeID = ? and longname = ? and date = ?;");
        statement.setString(1, Configuration.getMoveSchemaNameTableName());
        statement.setInt(2, nodeID);
        statement.setString(3, longName);
        statement.setDate(4, moveDate);
        statement.execute();
        aConnection.close();
        return new Move(nodeID, longName, moveDate);
    }
    void deleteMovesByNode(int nodeID) throws SQLException {
        PreparedStatement statement = aConnection.prepareStatement("DELETE FROM ? WHERE nodeid = ?;");
        statement.setString(1, Configuration.getMoveSchemaNameTableName());
        statement.setInt(2, nodeID);
        statement.execute();
        statement.close();
    }
    void deleteMovesByLongName(String longName) throws SQLException {
        PreparedStatement statement = aConnection.prepareStatement("DELETE FROM ? WHERE longname = ?;");
        statement.setString(1, Configuration.getMoveSchemaNameTableName());
        statement.setString(2, longName);
        statement.execute();
        statement.close();
    }
}
