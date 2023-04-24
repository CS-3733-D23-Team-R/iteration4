package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;

public class MoveDAO {
    ArrayList<Move> getMoves() throws SQLException {
        Connection connection = Configuration.getConnection();
        ArrayList<Move> temp = new ArrayList<Move>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+";");
        return parseMoves(temp, resultSet);
    }

    private ArrayList<Move> parseMoves(ArrayList<Move> temp, ResultSet resultSet) throws SQLException {
        while(resultSet.next()){
            Date aDate = resultSet.getDate("date");
            String aLongName = resultSet.getString("longname");
            int aNodeID = resultSet.getInt("nodeID");
            Move aMove = new Move(aNodeID, aLongName, aDate);
            temp.add(aMove);
        }
        return temp;
    }

    ArrayList<Move> getMovesByNodeID(int nodeID) throws SQLException {
        Connection connection = Configuration.getConnection();
        ArrayList<Move> temp = new ArrayList<Move>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE nodeID = "+nodeID+";");
        return parseMoves(temp, resultSet);
    }

    ArrayList<Move> getMovesForDate(Date date) throws SQLException {
        Connection connection = Configuration.getConnection();
        ArrayList<Move> temp = new ArrayList<Move>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" NATURAL JOIN (SELECT longname, MAX(date) as date from "+Configuration.getMoveSchemaNameTableName()+" WHERE date<? group by longname) as foo;");
        preparedStatement.setDate(1, date);
        ResultSet resultSet = preparedStatement.executeQuery();
        return parseMoves(temp, resultSet);
    }

    Move getLatestMoveByNodeID(int nodeID) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE date=(select max(date) FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE nodeID = "+nodeID+" AND date<now()) AND nodeID = "+nodeID+";");
        resultSet.next();
        Move temp = new Move(resultSet.getInt("nodeid"), resultSet.getString("longname"), resultSet.getDate("date"));
        return temp;
    }
    Move addMove(int nodeID, String longName, java.sql.Date moveDate) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO "+Configuration.getMoveSchemaNameTableName()+"(nodeID, longName, date) VALUES("+nodeID+", '"+longName+"', '"+moveDate.toString()+"');");
        return new Move(nodeID, longName, moveDate);
    }
    void deleteMovesByNode(int nodeID) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE nodeid = "+nodeID+";");
    }
    void deleteMovesByLongName(String longName) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE longname = '"+longName+"';");
    }
    void deleteMove(int nodeID, String longName, java.sql.Date moveDate) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM "+Configuration.getMoveSchemaNameTableName()+" WHERE longname = '"+longName+"' and nodeID = "+nodeID+" and date = '"+moveDate.toString()+"';");
    }

    void deleteAllMoves() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getMoveSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    public void addMoves(ArrayList<Move> moves) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getMoveSchemaNameTableName()+"(nodeID, longName, date) VALUES(?, ?, ?);");
        for (Move m : moves) {
            preparedStatement.setInt(1, m.getNodeID());
            preparedStatement.setString(2, m.getLongName());
            preparedStatement.setDate(3, m.getMoveDate());
            preparedStatement.executeUpdate();
        }
    }
}
