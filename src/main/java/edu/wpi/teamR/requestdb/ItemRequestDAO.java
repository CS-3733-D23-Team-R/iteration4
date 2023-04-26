package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.Node;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRequestDAO {
    public  ItemRequestDAO(){}
    ItemRequest addItemRequest(RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ Configuration.getServiceRequestSchemaNameTableName()+"(requestType,requestStatus,longname,staffUsername,itemType,requesterName,additionalNotes,requestDate) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, requestType.toString());
        preparedStatement.setString(2, requestStatus.toString());
        preparedStatement.setString(3, longname);
        preparedStatement.setString(4, staffUsername);
        preparedStatement.setString(5, itemType);
        preparedStatement.setString(6, requesterName);
        preparedStatement.setString(7, additionalNotes);
        preparedStatement.setTimestamp(8, requestDate);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        int requestID = 0;
        if (resultSet.next())
            requestID = resultSet.getInt("requestID");
        return new ItemRequest(requestID, requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate);
    }

    void addItemRequests(List<ItemRequest> itemRequests) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ Configuration.getServiceRequestSchemaNameTableName()+" VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        for (ItemRequest i : itemRequests) {
            preparedStatement.setInt(1, i.getRequestID());
            preparedStatement.setString(2, i.getRequestType().toString());
            preparedStatement.setString(3, i.getRequestStatus().toString());
            preparedStatement.setString(4, i.getLongName());
            preparedStatement.setString(5, i.getStaffUsername());
            preparedStatement.setString(6, i.getItemType());
            preparedStatement.setString(7, i.getRequesterName());
            preparedStatement.setString(8, i.getAdditionalNotes());
            preparedStatement.setTimestamp(9, i.getRequestDate());
            preparedStatement.executeUpdate();
        }
    }

    void deleteItemRequest(int requestID) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getServiceRequestSchemaNameTableName()+" WHERE requestID=?;");
        preparedStatement.setInt(1, requestID);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
    }

    ItemRequest modifyItemRequestByID(int requestID, RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+Configuration.getServiceRequestSchemaNameTableName()+" SET requestType=?, requestStatus=?, longname=?, staffUsername=?, itemType=?, requesterName=?, additionalNotes=?, requestDate=? WHERE requestID=?");
        preparedStatement.setString(1, requestType.toString());
        preparedStatement.setString(2, requestStatus.toString());
        preparedStatement.setString(3, longname);
        preparedStatement.setString(4, staffUsername);
        preparedStatement.setString(5, itemType);
        preparedStatement.setString(6, requesterName);
        preparedStatement.setString(7, additionalNotes);
        preparedStatement.setTimestamp(8, requestDate);
        preparedStatement.setInt(9, requestID);
        int rows = preparedStatement.executeUpdate();
        if (rows==0)
            throw new ItemNotFoundException();
        return new ItemRequest(requestID, requestType, requestStatus, longname, staffUsername, requesterName, additionalNotes, additionalNotes, requestDate);
    }

    public void deleteAllItemRequests() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getServiceRequestSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    void deleteItemRequestsByUser(String staffUsername) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getServiceRequestSchemaNameTableName()+" WHERE staffusername=?;");
        preparedStatement.setString(1, staffUsername);
        preparedStatement.executeUpdate();
    }


    ArrayList<ItemRequest> getItemRequests() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getServiceRequestSchemaNameTableName()+";");
        return getItemRequests(preparedStatement);
    }
    ArrayList<ItemRequest> getItemRequestByAttributes(SearchList searchList) throws SQLException {
        //separate the query items into the filter and order requirements because the
        // filter requirements need to be applied first
        ArrayList<Triple<RequestAttribute, Operation, Object>> fullRequirements = searchList.getSearchRequirements();
        ArrayList<Triple<RequestAttribute, Operation, Object>> filterRequirements = new ArrayList<>();
        ArrayList<Triple<RequestAttribute, Operation, Object>> orderRequirements = new ArrayList<>();

        //Split different operation into different lists
        for (Triple<RequestAttribute, Operation, Object> triple : fullRequirements){
            Operation operation = triple.getMiddle();
            boolean isFilter = operation==Operation.lessThan || operation==Operation.equalTo || operation==Operation.greaterThan;
            if (isFilter)
                filterRequirements.add(triple);
            else
                orderRequirements.add(triple);
        }

        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM "+Configuration.getServiceRequestSchemaNameTableName()+" WHERE 1=1 ");

        //Add filter segment of statement
        for (Triple<RequestAttribute, Operation, Object> triple : filterRequirements){
            String operationString = "";

            if (triple.getRight() == null)
                operationString = " IS ";
            else{
                switch (triple.getMiddle()){
                    case lessThan -> operationString = "<";
                    case equalTo -> operationString = "=";
                    case greaterThan -> operationString = ">";
                }
            }

            stringBuilder.append("AND ").append(triple.getLeft().toString()).append(operationString).append("? ");
        }

        //Add order by segment of statement
        if (orderRequirements.size() > 0)
            stringBuilder.append("ORDER BY "); //if there is any order by clause

        for (Triple<RequestAttribute, Operation, Object> triple : orderRequirements){
            // Object will be null for these
            String orderDirectionString = "";

            switch (triple.getMiddle()){
                case orderByDesc -> orderDirectionString = " desc";
                case orderByAsc -> orderDirectionString = " asc";
            }

            stringBuilder.append(triple.getLeft().toString()).append(orderDirectionString).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1); //remove the last ,
        stringBuilder.append(";");

        PreparedStatement preparedStatement = Configuration.getConnection().prepareStatement(stringBuilder.toString());

        //Insert the values into the statement
        for (int j = 0; j < filterRequirements.size(); j++){
            Object compareValue = filterRequirements.get(j).getRight();

            if (compareValue instanceof Integer)
                preparedStatement.setInt(j+1, (Integer) compareValue);
            if (compareValue instanceof RequestType)
                preparedStatement.setString(j+1, ((RequestType) compareValue).toString());
            if (compareValue instanceof RequestStatus)
                preparedStatement.setString(j+1, ((RequestStatus) compareValue).toString());
            if (compareValue instanceof String)
                preparedStatement.setString(j+1, (String) compareValue);
            if (compareValue instanceof Timestamp)
                preparedStatement.setTimestamp(j+1, (Timestamp) compareValue);
            if (compareValue==null)
                preparedStatement.setString(j+1, null);
        }

        return getItemRequests(preparedStatement);
    }

    @NotNull
    private ArrayList<ItemRequest> getItemRequests(PreparedStatement preparedStatement) throws SQLException {
        Statement statement = Configuration.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(preparedStatement.toString()); //preparedStatement.executeQuery();
        ArrayList<ItemRequest> itemRequests = new ArrayList<>();
        while (resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            RequestType requestType = RequestType.valueOf(resultSet.getString("requestType"));
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus"));
            String longname = resultSet.getString("longname");
            String staffUsername = resultSet.getString("staffUsername");
            String itemType = resultSet.getString("itemType");
            String requesterName = resultSet.getString("requesterName");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");

            itemRequests.add(new ItemRequest(requestID, requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate));
        }
        return itemRequests;
    }
}
