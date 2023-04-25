package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class AvailableFurnitureDAO {

    AvailableFurniture addAvailableFurniture(String itemName, String imageReference, String description, boolean isSeating, boolean isTable, boolean isPillow, boolean isStorage) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAvailableFurnitureTableSchemaNameTableName()+"(itemName,imageReference,description,isSeating,isTable,isPillow,isStorage) VALUES(?,?,?,?,?,?,?);");
        preparedStatement.setString(1, itemName);
        preparedStatement.setString(2, imageReference);
        preparedStatement.setString(3, description);
        preparedStatement.setBoolean(4, isSeating);
        preparedStatement.setBoolean(5, isTable);
        preparedStatement.setBoolean(6, isPillow);
        preparedStatement.setBoolean(7, isStorage);
        preparedStatement.executeUpdate();
        return new AvailableFurniture(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    void deleteAvailableFurniture(String itemName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableFurnitureTableSchemaNameTableName()+" WHERE itemName=?;");
        preparedStatement.setString(1, itemName);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
    }

    void deleteAllAvailableFurniture() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableFurnitureTableSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    AvailableFurniture modifyAvailableFurniture(String itemName, String imageReference, String description, boolean isSeating, boolean isTable, boolean isPillow, boolean isStorage) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getAvailableFurnitureTableSchemaNameTableName()+" SET imageReference=?,description=?,isSeating=?,isTable=?,isPillow=?,isStorage=? WHERE itemName=?;");
        preparedStatement.setString(1, imageReference);
        preparedStatement.setString(2, description);
        preparedStatement.setBoolean(3, isSeating);
        preparedStatement.setBoolean(4, isTable);
        preparedStatement.setBoolean(5, isPillow);
        preparedStatement.setBoolean(6, isStorage);
        preparedStatement.setString(7, itemName);

        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
        return new AvailableFurniture(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage);
    }

    ArrayList<AvailableFurniture> getAvailableFurniture() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getAvailableFurnitureTableSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AvailableFurniture> availableFurniture = new ArrayList<>();

        while (resultSet.next()){
            String itemName = resultSet.getString("itemName");
            String imageReference = resultSet.getString("imageReference");
            String description = resultSet.getString("description");
            boolean isSeating = resultSet.getBoolean("isSeating");
            boolean isTable = resultSet.getBoolean("isTable");
            boolean isPillow = resultSet.getBoolean("isPillow");
            boolean isStorage = resultSet.getBoolean("isStorage");

            availableFurniture.add(new AvailableFurniture(itemName, imageReference, description, isSeating, isTable, isPillow, isStorage));
        }

        return availableFurniture;
    }

    ArrayList<AvailableFurniture> getAvailableFurnitureByAttributes(String itemName, String imageReference, String description, Boolean isSeating, Boolean isTable, Boolean isPillow, Boolean isStorage) throws SQLException {
        Connection connection = Configuration.getConnection();

        //Construct the query
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM "+Configuration.getAvailableFurnitureTableSchemaNameTableName()+" WHERE 1=1 ");

        if (itemName!=null)
            stringBuilder.append("AND itemName='"+itemName+"' ");

        if (imageReference!=null)
            stringBuilder.append("AND imageReference='"+imageReference+"' ");

        if (description!=null)
            stringBuilder.append("AND description='"+description+"' ");

        if (isSeating!=null)
            stringBuilder.append("AND isSeating="+isSeating+" ");

        if (isTable!=null)
            stringBuilder.append("AND isTable="+isTable+" ");

        if (isPillow!=null)
            stringBuilder.append("AND isPillow="+isPillow+" ");

        if (isStorage!=null)
            stringBuilder.append("AND isStorage="+isStorage+" ");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
        ArrayList<AvailableFurniture> availableFurniture = new ArrayList<>();

        while (resultSet.next()){
            String itemNameVal = resultSet.getString("itemName");
            String imageReferenceVal = resultSet.getString("imageReference");
            String descriptionVal = resultSet.getString("description");
            boolean isSeatingVal = resultSet.getBoolean("isSeating");
            boolean isTableVal = resultSet.getBoolean("isTable");
            boolean isPillowVal = resultSet.getBoolean("isPillow");
            boolean isStorageVal = resultSet.getBoolean("isStorage");

            availableFurniture.add(new AvailableFurniture(itemNameVal, imageReferenceVal, descriptionVal, isSeatingVal, isTableVal, isPillowVal, isStorageVal));
        }

        return availableFurniture;
    }
}
