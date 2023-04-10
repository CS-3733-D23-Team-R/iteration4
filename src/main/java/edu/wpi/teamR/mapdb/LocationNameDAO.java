package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;

public class LocationNameDAO {
    private Connection aConnection;
    LocationNameDAO(Connection connection) throws SQLException, ClassNotFoundException {
        aConnection = connection;
    }
    ArrayList<LocationName> getLocations() throws SQLException {
        ArrayList<LocationName> temp = new ArrayList<LocationName>();
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+";");
        while(resultSet.next()){
            String aLongName = resultSet.getString("longname");
            String aShortName = resultSet.getString("shortname");
            String aNodeType = resultSet.getString("nodetype");
            LocationName aLocationName = new LocationName(aLongName, aShortName, aNodeType);
            temp.add(aLocationName);
        }
        aConnection.close();
        return temp;
    }
    LocationName getLocationByNodeID(int nodeId) throws SQLException {
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE nodeID = "+nodeId+";");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        aConnection.close();
        return aLocationName;
    }
    ArrayList<LocationName> getLocationsByNodeType(String nodeType) throws SQLException {
        ArrayList<LocationName> temp = new ArrayList<LocationName>();
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE nodeType = "+nodeType+";");
        while(resultSet.next()){
            String aLongName = resultSet.getString("longname");
            String aShortName = resultSet.getString("shortname");
            String aNodeType = resultSet.getString("nodetype");
            LocationName aLocationName = new LocationName(aLongName, aShortName, aNodeType);
            temp.add(aLocationName);
        }
        aConnection.close();
        return temp;
    }
    LocationName getLocationByLongName(String longName) throws SQLException {
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = "+longName+";");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        aConnection.close();
        return aLocationName;
    }
    LocationName modifyLocationNameType(String longName, String newType) throws SQLException {
        Statement statement = aConnection.createStatement();
        statement.executeUpdate("UPDATE "+Configuration.getLocationNameSchemaNameTableName()+" SET nodeType = '"+newType+"' WHERE longname = '"+longName+"';");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = "+longName+";");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        aConnection.close();
        return aLocationName;
    }
    LocationName modifyLocationNameShortName(String longName, String newShortName) throws SQLException {
        Statement statement = aConnection.createStatement();
        statement.executeUpdate("UPDATE "+Configuration.getLocationNameSchemaNameTableName()+" SET shortname = '"+newShortName+"' WHERE longname = '"+longName+"';");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = "+longName+";");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        aConnection.close();
        return aLocationName;
    }

    void deleteAllLocationNames() throws SQLException {
        PreparedStatement preparedStatement = aConnection.prepareStatement("DELETE FROM "+Configuration.getLocationNameSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }
}
