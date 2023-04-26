package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvailableSuppliesDAO {
    AvailableSupplies addAvailableSupplies(String itemName, String imageReference, String description, double itemPrice, boolean isPaper, boolean isPen, boolean isOrganization, boolean isComputerAccessory) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAvailableSuppliesTableSchemaNameTableName()+"(itemName,imageReference,description,itemPrice,isPaper,isPen,isOrganization,isComputerAccessory) VALUES(?,?,?,?,?,?,?,?);");
        preparedStatement.setString(1, itemName);
        preparedStatement.setString(2, imageReference);
        preparedStatement.setString(3, description);
        preparedStatement.setDouble(4, itemPrice);
        preparedStatement.setBoolean(5, isPaper);
        preparedStatement.setBoolean(6, isPen);
        preparedStatement.setBoolean(7, isOrganization);
        preparedStatement.setBoolean(8, isComputerAccessory);
        preparedStatement.executeUpdate();
        return new AvailableSupplies(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory);
    }

    void addAvailableSupplies(List<AvailableSupplies> availableSupplies) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+Configuration.getAvailableSuppliesTableSchemaNameTableName()+"(itemName,imageReference,description,itemPrice,isPaper,isPen,isOrganization,isComputerAccessory) VALUES(?,?,?,?,?,?,?,?);");
        for (AvailableSupplies a : availableSupplies) {
            preparedStatement.setString(1, a.getItemName());
            preparedStatement.setString(2, a.getImageReference());
            preparedStatement.setString(3, a.getDescription());
            preparedStatement.setDouble(4, a.getItemPrice());
            preparedStatement.setBoolean(5, a.isPaper());
            preparedStatement.setBoolean(6, a.isPen());
            preparedStatement.setBoolean(7, a.isOrganization());
            preparedStatement.setBoolean(8, a.isComputerAccessory());
            preparedStatement.executeUpdate();
        }
    }

    void deleteAvailableSupplies(String itemName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableSuppliesTableSchemaNameTableName()+" WHERE itemName=?;");
        preparedStatement.setString(1, itemName);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
    }

    void deleteAllAvailableSupplies() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getAvailableSuppliesTableSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    AvailableSupplies modifyAvailableSupplies(String itemName, String imageReference, String description, double itemPrice, boolean isPaper, boolean isPen, boolean isOrganization, boolean isComputerAccessory) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getAvailableSuppliesTableSchemaNameTableName()+" SET imageReference=?,description=?,itemPrice=?,isPaper=?,isPen=?,isOrganization=?,isComputerAccessory=? WHERE itemName=?;");
        preparedStatement.setString(1, imageReference);
        preparedStatement.setString(2, description);
        preparedStatement.setDouble(3, itemPrice);
        preparedStatement.setBoolean(4, isPaper);
        preparedStatement.setBoolean(5, isPen);
        preparedStatement.setBoolean(6, isOrganization);
        preparedStatement.setBoolean(7, isComputerAccessory);
        preparedStatement.setString(8, itemName);

        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
        return new AvailableSupplies(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory);
    }

    ArrayList<AvailableSupplies> getAvailableSupplies() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getAvailableSuppliesTableSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<AvailableSupplies> availableSupplies = new ArrayList<>();

        while (resultSet.next()){
            String itemName = resultSet.getString("itemName");
            String imageReference = resultSet.getString("imageReference");
            String description = resultSet.getString("description");
            double itemPrice = resultSet.getDouble("itemPrice");
            boolean isPaper = resultSet.getBoolean("isPaper");
            boolean isPen = resultSet.getBoolean("isPen");
            boolean isOrganization = resultSet.getBoolean("isOrganization");
            boolean isComputerAccessory = resultSet.getBoolean("isComputerAccessory");

            availableSupplies.add(new AvailableSupplies(itemName, imageReference, description, itemPrice, isPaper, isPen, isOrganization, isComputerAccessory));
        }

        return availableSupplies;
    }

    ArrayList<AvailableSupplies> getAvailableSuppliesByAttributes(String itemName, String imageReference, String description, Double itemPrice, Boolean isPaper, Boolean isPen, Boolean isOrganization, Boolean isComputerAccessory, SortOrder sortOrder) throws SQLException {
        Connection connection = Configuration.getConnection();

        //Construct the query
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM "+Configuration.getAvailableSuppliesTableSchemaNameTableName()+" WHERE 1=1 ");

        if (itemName!=null)
            stringBuilder.append("AND itemName='"+itemName+"' ");

        if (imageReference!=null)
            stringBuilder.append("AND imageReference='"+imageReference+"' ");

        if (description!=null)
            stringBuilder.append("AND description='"+description+"' ");

        if (itemPrice!=null)
            stringBuilder.append("AND itemPrice="+itemPrice+" ");

        if (isPaper!=null)
            stringBuilder.append("AND isPaper="+isPaper+" ");

        if (isPen!=null)
            stringBuilder.append("AND isPen="+isPen+" ");

        if (isOrganization!=null)
            stringBuilder.append("AND isOrganization="+isOrganization+" ");

        if (isComputerAccessory!=null)
            stringBuilder.append("AND isComputerAccessory="+isComputerAccessory+" ");

        switch (sortOrder){
            case lowToHigh -> stringBuilder.append("ORDER BY itemprice asc;");
            case highToLow -> stringBuilder.append("ORDER BY itemprice desc;");
            case unsorted -> stringBuilder.append(";");
        }
        ;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(stringBuilder.toString());
        ArrayList<AvailableSupplies> availableSupplies = new ArrayList<>();

        while (resultSet.next()){
            String itemNameVal = resultSet.getString("itemName");
            String imageReferenceVal = resultSet.getString("imageReference");
            String descriptionVal = resultSet.getString("description");
            double itemPriceVal = resultSet.getDouble("itemPrice");
            boolean isPaperVal = resultSet.getBoolean("isPaper");
            boolean isPenVal = resultSet.getBoolean("isPen");
            boolean isOrganizationVal = resultSet.getBoolean("isOrganization");
            boolean isComputerAccessoryVal = resultSet.getBoolean("isComputerAccessory");

            availableSupplies.add(new AvailableSupplies(itemNameVal, imageReferenceVal, descriptionVal, itemPriceVal, isPaperVal, isPenVal, isOrganizationVal, isComputerAccessoryVal));
        }

        return availableSupplies;
    }
}
