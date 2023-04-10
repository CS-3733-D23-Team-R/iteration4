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
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE date=(select max(date) FROM "+Configuration.getMoveSchemaNameTableName()+".move WHERE nodeID = "+nodeID+") AND nodeID = "+nodeID+";");
        resultSet.next();
        Move temp = new Move(resultSet.getInt("nodeid"), resultSet.getString("longname"), resultSet.getDate("date"));
        aConnection.close();
        return temp;
    }
    Move addMove(int nodeID, String longName, java.sql.Date moveDate) throws SQLException {
        Statement statement = aConnection.createStatement();
        statement.executeUpdate("INSERT INTO "+Configuration.getMoveSchemaNameTableName()+" WHERE nodeID = "+nodeID+" and longname = '"+longName+"' and date = '"+moveDate.toString()+"';");
        aConnection.close();
        return new Move(nodeID, longName, moveDate);
    }
    void deleteMovesByNode(int nodeID) throws SQLException {
        Statement statement = aConnection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE nodeid = "+nodeID+";");
        statement.close();
    }
    void deleteMovesByLongName(String longName) throws SQLException {
        Statement statement = aConnection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE longname = '"+longName+"';");
        statement.close();
    }

    void deleteAllMoves() throws SQLException {
        PreparedStatement preparedStatement = aConnection.prepareStatement("DELETE FROM "+Configuration.getMoveSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }
}
