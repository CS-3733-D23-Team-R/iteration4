package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

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
        return parseLocationNames(temp, resultSet);
    }

    private ArrayList<LocationName> parseLocationNames(ArrayList<LocationName> temp, ResultSet resultSet) throws SQLException {
        while(resultSet.next()){
            String aLongName = resultSet.getString("longname");
            String aShortName = resultSet.getString("shortname");
            String aNodeType = resultSet.getString("nodetype");
            LocationName aLocationName = new LocationName(aLongName, aShortName, aNodeType);
            temp.add(aLocationName);
        }
        return temp;
    }

    ArrayList<LocationName> getLocationsByNodeType(String nodeType) throws SQLException {
        ArrayList<LocationName> temp = new ArrayList<LocationName>();
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE nodeType = '"+nodeType+"';");
        return parseLocationNames(temp, resultSet);
    }
    LocationName getLocationByLongName(String longName) throws SQLException, ItemNotFoundException {
        Statement statement = aConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = '"+longName+"';");
        if (resultSet.next()) {
            LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
            return aLocationName;
        } else{
            throw new ItemNotFoundException();
        }

    }
    LocationName modifyLocationNameType(String longName, String newType) throws SQLException {
        Statement statement = aConnection.createStatement();
        statement.executeUpdate("UPDATE "+Configuration.getLocationNameSchemaNameTableName()+" SET nodeType = '"+newType+"' WHERE longname = '"+longName+"';");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = '"+longName+"';");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        return aLocationName;
    }
    LocationName modifyLocationNameShortName(String longName, String newShortName) throws SQLException {
        Statement statement = aConnection.createStatement();
        statement.executeUpdate("UPDATE "+Configuration.getLocationNameSchemaNameTableName()+" SET shortname = '"+newShortName+"' WHERE longname = '"+longName+"';");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = '"+longName+"';");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));

        return aLocationName;
    }

    LocationName addLocationName(String longName, String shortName, String nodeType) throws SQLException {
        PreparedStatement preparedStatement = aConnection.prepareStatement("INSERT INTO "+Configuration.getLocationNameSchemaNameTableName()+"(longname, shortname, nodetype) VALUES(?,?,?);");
        preparedStatement.setString(1, longName);
        preparedStatement.setString(2, shortName);
        preparedStatement.setString(3, nodeType);
        preparedStatement.executeUpdate();
        return new LocationName(longName, shortName, nodeType);
    }

    void deleteAllLocationNames() throws SQLException {
        PreparedStatement preparedStatement = aConnection.prepareStatement("DELETE FROM "+Configuration.getLocationNameSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    void deleteLocationName(String longname) throws SQLException {
        PreparedStatement preparedStatement = aConnection.prepareStatement("DELETE FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname=?;");
        preparedStatement.setString(1, longname);
        preparedStatement.executeUpdate();
    }
}
