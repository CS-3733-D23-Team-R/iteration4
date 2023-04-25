package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvailableFlowersDAO {

    AvailableFlowers addAvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAvailableFlowersTableSchemaNameTableName()+"(itemName,imageReference,description,itemPrice,isBouqet,hasCard) VALUES(?,?,?,?,?,?);");
        preparedStatement.setString(1, itemName);
        preparedStatement.setString(2, imageReference);
        preparedStatement.setString(3, description);
        preparedStatement.setDouble(4, itemPrice);
        preparedStatement.setBoolean(5, isBouqet);
        preparedStatement.setBoolean(6, hasCard);
        preparedStatement.executeUpdate();
        return new AvailableFlowers(itemName, imageReference, description, itemPrice, isBouqet, hasCard);
    }

    void addAvailableFlowers(List<AvailableFlowers> availableFlowers) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAvailableFlowersTableSchemaNameTableName()+"(itemName,imageReference,description,itemPrice,isBouqet,hasCard) VALUES(?,?,?,?,?,?);");
        for (AvailableFlowers a : availableFlowers) {
            preparedStatement.setString(1, a.getItemName());
            preparedStatement.setString(2, a.getImageReference());
            preparedStatement.setString(3, a.getDescription());
            preparedStatement.setDouble(4, a.getItemPrice());
            preparedStatement.setBoolean(5, a.isBouqet());
            preparedStatement.setBoolean(6, a.isHasCard());
            preparedStatement.executeUpdate();
        }
    }

    void deleteAvailableFlowers(String itemName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableFlowersTableSchemaNameTableName()+" WHERE itemName=?;");
        preparedStatement.setString(1, itemName);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
    }

    void deleteAllAvailableFlowers() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableFlowersTableSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    AvailableFlowers modifyAvailableFlowers(String itemName, String imageReference, String description, double itemPrice, boolean isBouqet, boolean hasCard) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getAvailableFlowersTableSchemaNameTableName()+" SET imageReference=?,description=?,itemPrice=?,isBouqet=?,hasCard=? WHERE itemName=?;");
        preparedStatement.setString(1, imageReference);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, itemPrice);
        preparedStatement.setBoolean(4, isBouqet);
        preparedStatement.setBoolean(5, hasCard);
        preparedStatement.setString(6, itemName);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
        return new AvailableFlowers(itemName, imageReference, description, itemPrice, isBouqet, hasCard);
    }

    ArrayList<AvailableFlowers> getAvailableFlowers() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getAvailableFlowersTableSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AvailableFlowers> availableFlowers = new ArrayList<>();

        while (resultSet.next()){
            String itemName = resultSet.getString("itemName");
            String imageReference = resultSet.getString("imageReference");
            String description = resultSet.getString("description");
            double itemPrice = resultSet.getDouble("itemPrice");
            boolean isBouqet = resultSet.getBoolean("isBouqet");
            boolean hasCard = resultSet.getBoolean("hasCard");

            availableFlowers.add(new AvailableFlowers(itemName, imageReference, description, itemPrice, isBouqet, hasCard));
        }

        return availableFlowers;
    }

    ArrayList<AvailableFlowers> getAvailableFlowersByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isBouqet, Boolean hasCard, SortOrder sortOrder) throws SQLException {
        Connection connection = Configuration.getConnection();

        //Construct the query
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM "+Configuration.getAvailableFlowersTableSchemaNameTableName()+" WHERE 1=1 ");

        if (itemName!=null)
            stringBuilder.append("AND itemName='"+itemName+"' ");

        if (imageReference!=null)
            stringBuilder.append("AND imageReference='"+imageReference+"' ");

        if (description!=null)
            stringBuilder.append("AND description='"+description+"' ");

        if (itemPrice!=null)
            stringBuilder.append("AND itemPrice="+itemPrice+" ");

        if (isBouqet!=null)
            stringBuilder.append("AND isBouqet="+isBouqet+" ");

        if (hasCard!=null)
            stringBuilder.append("AND hasCard="+hasCard+" ");

        switch (sortOrder){
            case lowToHigh -> stringBuilder.append("ORDER BY itemprice asc;");
            case highToLow -> stringBuilder.append("ORDER BY itemprice desc;");
            case unsorted -> stringBuilder.append(";");
        }
;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
        ArrayList<AvailableFlowers> availableFlowers = new ArrayList<>();

        while (resultSet.next()){
            String itemNameVal = resultSet.getString("itemName");
            String imageReferenceVal = resultSet.getString("imageReference");
            String descriptionVal = resultSet.getString("description");
            double itemPriceVal = resultSet.getDouble("itemPrice");
            boolean isBouqetVal = resultSet.getBoolean("isBouqet");
            boolean hasCardVal = resultSet.getBoolean("hasCard");

            availableFlowers.add(new AvailableFlowers(itemNameVal, imageReferenceVal, descriptionVal, itemPriceVal, isBouqetVal, hasCardVal));
        }

        return availableFlowers;
    }
}
