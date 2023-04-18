package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class AvailableItemDAO {
    AvailableItemDAO(){}

    ArrayList<AvailableItem> getAvailableItemsByTypeWithinRangeSortedByPrice(RequestType requestType, Double upperBound, Double lowerBound, SortOrder sortOrder) throws SQLException, ClassNotFoundException {
        String searchTableString = "";
        if (requestType == RequestType.Meal)
            searchTableString = Configuration.getAvailableMealsTableSchemaNameTableName();
        if (requestType == RequestType.Furniture)
            searchTableString = Configuration.getAvailableFurnitureTableSchemaNameTableName();
        if (requestType == RequestType.Flower)
            searchTableString = Configuration.getAvailableFlowersTableSchemaNameTableName();
        if (requestType == RequestType.Supplies)
            searchTableString = Configuration.getAvailableSuppliesTableSchemaNameTableName();

        String upperBoundString = "1=1"; //only remove if upperBound is a number
        if (upperBound!=null)
            upperBoundString = "itemPrice<"+upperBound;

        String lowerBoundString = "1=1"; //only remove if lowerBound is a number
        if (lowerBound!=null)
            lowerBoundString = "itemPrice<"+lowerBound;

        String sortOrderString = "";
        if (sortOrder == SortOrder.unsorted)
            sortOrderString = "";
        if (sortOrder == SortOrder.lowToHigh)
            sortOrderString = " ORDER BY itemPrice ASC";
        if (sortOrder == SortOrder.highToLow)
            sortOrderString = " ORDER BY itemPrice DESC";

        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+searchTableString+" WHERE "+upperBoundString+" AND "+lowerBoundString+sortOrderString+";");

        ArrayList<AvailableItem> availableItems = new ArrayList<AvailableItem>();
        while (resultSet.next()){
            String itemName = resultSet.getString("itemName");
            double itemPrice = resultSet.getDouble("itemPrice");
            String imageReference = resultSet.getString("imageReference");

            availableItems.add(new AvailableItem(itemName, requestType, itemPrice, imageReference));
        }
        return availableItems;
    }
}
