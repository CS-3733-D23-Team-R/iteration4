package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.Node;

import java.sql.*;
import java.util.ArrayList;

public class ItemRequestDAO {
    public  ItemRequestDAO(){}
    ItemRequest addItemRequest(RequestType requestType, RequestStatus requestStatus, String longname, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate) throws SQLException, ClassNotFoundException {
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
            requestID = resultSet.getInt("nodeID");
        return new ItemRequest(requestID, requestType, requestStatus, longname, staffUsername, itemType, requesterName, additionalNotes, requestDate);
    }

    void deleteItemRequest(int requestID) throws SQLException, ClassNotFoundException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getServiceRequestSchemaNameTableName()+" WHERE requestID=?;");
        preparedStatement.setInt(1, requestID);
        int numRows = preparedStatement.executeUpdate();
        if (numRows==0)
            throw new ItemNotFoundException();
    }
    void deleteAllItemRequests() throws SQLException, ClassNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getServiceRequestSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }
    ArrayList<ItemRequest> getItemRequests(){
        Connection connection == Configuration.getConnection()
    }

~ getItemRequestByAttributes(searchList, SearchList): ArrayList<ItemRequest>
}
