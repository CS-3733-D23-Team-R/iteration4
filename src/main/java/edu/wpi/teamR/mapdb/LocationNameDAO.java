package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationNameDAO {
    ArrayList<LocationName> getLocations() throws SQLException {
        Connection connection = Configuration.getConnection();
        ArrayList<LocationName> temp = new ArrayList<LocationName>();
        Statement statement = connection.createStatement();
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
        Connection connection = Configuration.getConnection();
        ArrayList<LocationName> temp = new ArrayList<LocationName>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE nodeType = '"+nodeType+"';");
        return parseLocationNames(temp, resultSet);
    }
    LocationName getLocationByLongName(String longName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = '"+longName+"';");
        if (resultSet.next()) {
            return new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        } else{
            throw new ItemNotFoundException();
        }

    }
    LocationName modifyLocationNameType(String longName, String newType) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE "+Configuration.getLocationNameSchemaNameTableName()+" SET nodeType = '"+newType+"' WHERE longname = '"+longName+"';");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = '"+longName+"';");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));
        return aLocationName;
    }
    LocationName modifyLocationNameShortName(String longName, String newShortName) throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE "+Configuration.getLocationNameSchemaNameTableName()+" SET shortname = '"+newShortName+"' WHERE longname = '"+longName+"';");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname = '"+longName+"';");
        resultSet.next();
        LocationName aLocationName = new LocationName(resultSet.getString("longname"), resultSet.getString("shortname"), resultSet.getString("nodetype"));

        return aLocationName;
    }

    LocationName addLocationName(String longName, String shortName, String nodeType) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getLocationNameSchemaNameTableName()+"(longname, shortname, nodetype) VALUES(?,?,?);");
        preparedStatement.setString(1, longName);
        preparedStatement.setString(2, shortName);
        preparedStatement.setString(3, nodeType);
        preparedStatement.executeUpdate();
        return new LocationName(longName, shortName, nodeType);
    }

    void deleteAllLocationNames() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getLocationNameSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    void deleteLocationName(String longName) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getLocationNameSchemaNameTableName()+" WHERE longname=?;");
        preparedStatement.setString(1, longName);
        preparedStatement.executeUpdate();
    }

    public void addLocationNames(List<LocationName> locationNames) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getLocationNameSchemaNameTableName()+"(longname, shortname, nodetype) VALUES(?,?,?);");
        for (LocationName l : locationNames) {
            preparedStatement.setString(1, l.getLongName());
            preparedStatement.setString(2, l.getShortName());
            preparedStatement.setString(3, l.getNodeType());
            preparedStatement.executeUpdate();
        }
    }
}
