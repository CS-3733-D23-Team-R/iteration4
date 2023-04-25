package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class AvailableMealsDAO {
    AvailableMeals addAvailableMeals(String itemName, String imageReference, String description, double itemPrice, boolean isVegan, boolean isVegetarian, boolean isDairyFree, boolean isPeanutFree, boolean isGlutenFree) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAvailableMealsTableSchemaNameTableName()+"(itemName,imageReference,description,itemPrice,isVegan,isVegetarian,isDairyFree,isPeanutFree,isGlutenFree) VALUES(?,?,?,?,?,?,?,?,?);");
        preparedStatement.setString(1, itemName);
        preparedStatement.setString(2, imageReference);
        preparedStatement.setString(3, description);
        preparedStatement.setDouble(4, itemPrice);
        preparedStatement.setBoolean(5, isVegan);
        preparedStatement.setBoolean(6, isVegetarian);
        preparedStatement.setBoolean(7, isDairyFree);
        preparedStatement.setBoolean(8, isPeanutFree);
        preparedStatement.setBoolean(9, isGlutenFree);
        preparedStatement.executeUpdate();
        return new AvailableMeals(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree);
    }

    void deleteAvailableMeals(String itemName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableMealsTableSchemaNameTableName()+" WHERE itemName=?;");
        preparedStatement.setString(1, itemName);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
    }

    void deleteAllAvailableMeals() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableMealsTableSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    AvailableMeals modifyAvailableMeals(String itemName, String imageReference, String description, double itemPrice, boolean isVegan, boolean isVegetarian, boolean isDairyFree, boolean isPeanutFree, boolean isGlutenFree) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getAvailableMealsTableSchemaNameTableName()+" SET imageReference=?,description=?,itemPrice=?,isVegan=?,isVegetarian=?,isDairyFree=?,isPeanutFree=?,isGlutenFree=? WHERE itemName=?;");
        preparedStatement.setString(1, imageReference);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, itemPrice);
        preparedStatement.setBoolean(4, isVegan);
        preparedStatement.setBoolean(5, isVegetarian);
        preparedStatement.setBoolean(6, isDairyFree);
        preparedStatement.setBoolean(7, isPeanutFree);
        preparedStatement.setBoolean(8, isGlutenFree);
        preparedStatement.setString(9, itemName);

        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
        return new AvailableMeals(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree);
    }

    ArrayList<AvailableMeals> getAvailableMeals() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getAvailableMealsTableSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AvailableMeals> availableMeals = new ArrayList<>();

        while (resultSet.next()){
            String itemName = resultSet.getString("itemName");
            String imageReference = resultSet.getString("imageReference");
            String description = resultSet.getString("description");
            double itemPrice = resultSet.getDouble("itemPrice");
            boolean isVegan = resultSet.getBoolean("isVegan");
            boolean isVegetarian = resultSet.getBoolean("isVegetarian");
            boolean isDairyFree = resultSet.getBoolean("isDairyFree");
            boolean isPeanutFree = resultSet.getBoolean("isPeanutFree");
            boolean isGlutenFree = resultSet.getBoolean("isGlutenFree");

            availableMeals.add(new AvailableMeals(itemName, imageReference, description, itemPrice, isVegan, isVegetarian, isDairyFree, isPeanutFree, isGlutenFree));
        }

        return availableMeals;
    }

    ArrayList<AvailableMeals> getAvailableMealsByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isVegan, Boolean isVegetarian, Boolean isDairyFree, Boolean isPeanutFree, Boolean isGlutenFree, SortOrder sortOrder) throws SQLException {
        Connection connection = Configuration.getConnection();

        //Construct the query
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM "+Configuration.getAvailableMealsTableSchemaNameTableName()+" WHERE 1=1 ");

        if (itemName!=null)
            stringBuilder.append("AND itemName='"+itemName+"' ");

        if (imageReference!=null)
            stringBuilder.append("AND imageReference='"+imageReference+"' ");

        if (description!=null)
            stringBuilder.append("AND description='"+description+"' ");

        if (itemPrice!=null)
            stringBuilder.append("AND itemPrice="+itemPrice+" ");

        if (isVegan!=null)
            stringBuilder.append("AND isVegan="+isVegan+" ");

        if (isVegetarian!=null)
            stringBuilder.append("AND isVegetarian="+isVegetarian+" ");

        if (isDairyFree!=null)
            stringBuilder.append("AND isDairyFree="+isDairyFree+" ");

        if (isPeanutFree!=null)
            stringBuilder.append("AND isPeanutFree="+isPeanutFree+" ");

        if (isGlutenFree!=null)
            stringBuilder.append("AND isGlutenFree="+isGlutenFree+" ");


        switch (sortOrder){
            case lowToHigh -> stringBuilder.append("ORDER BY itemprice asc;");
            case highToLow -> stringBuilder.append("ORDER BY itemprice desc;");
            case unsorted -> stringBuilder.append(";");
        }
        ;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
        ArrayList<AvailableMeals> availableMeals = new ArrayList<>();

        while (resultSet.next()){
            String itemNameVal = resultSet.getString("itemName");
            String imageReferenceVal = resultSet.getString("imageReference");
            String descriptionVal = resultSet.getString("description");
            double itemPriceVal = resultSet.getDouble("itemPrice");
            boolean isVeganVal = resultSet.getBoolean("isVegan");
            boolean isVegetarianVal = resultSet.getBoolean("isVegetarian");
            boolean isDairyFreeVal = resultSet.getBoolean("isDairyFree");
            boolean isPeanutFreeVal = resultSet.getBoolean("isPeanutFree");
            boolean isGlutenFreeVal = resultSet.getBoolean("isGlutenFree");

            availableMeals.add(new AvailableMeals(itemNameVal, imageReferenceVal, descriptionVal, itemPriceVal, isVeganVal, isVegetarianVal, isDairyFreeVal, isPeanutFreeVal, isGlutenFreeVal));
        }

        return availableMeals;
    }
}
