package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;

public class LocationNameDAO {
    private Connection aConnection;
    private LocationNameDAO(Connection connection) throws SQLException, ClassNotFoundException {
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
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        aConnection.close();
        return aLocationName;
    }
    LocationName modifyLocationNameType(String longName, String newType) throws SQLException {
        Statement statement = aConnection.createStatement();
        PreparedStatement preparedStatement = aConnection.prepareStatement("UPDATE ? SET nodeType = ? WHERE longname = ?;");
        preparedStatement.setString(1, Configuration.getLocationNameSchemaNameTableName());
        preparedStatement.setString(2, newType);
        preparedStatement.setString(3, longName);
        preparedStatement.execute();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = "+longName+";");
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        aConnection.close();
        return aLocationName;
    }
    LocationName modifyLocationNameShortName(String longName, String shortName) throws SQLException {
        Statement statement = aConnection.createStatement();
        PreparedStatement preparedStatement = aConnection.prepareStatement("UPDATE ? SET shortname = ? WHERE longname = ?;");
        preparedStatement.setString(1, Configuration.getLocationNameSchemaNameTableName());
        preparedStatement.setString(2, shortName);
        preparedStatement.setString(3, longName);
        preparedStatement.execute();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = "+longName+";");
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        aConnection.close();
        return aLocationName;
    }
}
