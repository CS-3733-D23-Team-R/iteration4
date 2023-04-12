package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class FlowerRequestDAO {
    private Connection connection;

    FlowerRequestDAO(Connection connection) {
        this.connection = connection;
    }

    FlowerRequest addFlowerRequest(String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String flowerType) throws SQLException {
        PreparedStatement sqlInsert = connection.prepareStatement("INSERT INTO "+ Configuration.getFlowerRequestSchemaNameTableName()
                +"(requesterName,location,staffMember,additionalNotes,requestDate,requestStatus,flowerType)"
                +" VALUES (?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
        sqlInsert.setString(1, requesterName);
        sqlInsert.setString(2, location);
        sqlInsert.setString(3, staffMember);
        sqlInsert.setString(4, additionalNotes);
        sqlInsert.setTimestamp(5, requestDate);
        sqlInsert.setString(6, requestStatus.toString());
        sqlInsert.setString(7, flowerType);

        sqlInsert.executeUpdate();
        ResultSet rs = sqlInsert.getGeneratedKeys();
        int requestID = 0;
        if (rs.next()) {
            requestID = rs.getInt("requestID");
        }
        return new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType);
    }

    void deleteFlowerRequest(Integer requestID) throws SQLException{
        PreparedStatement sqlDelete = connection.prepareStatement("DELETE FROM"+ Configuration.getFlowerRequestSchemaNameTableName() + "WHERE requestID = " + requestID +";");
        sqlDelete.executeUpdate();
    }

    ArrayList<FlowerRequest> getFlowerRequests() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+";");
        ArrayList<FlowerRequest> flowers = new ArrayList<FlowerRequest>();
        while (resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus")) ;
            String flowerType = resultSet.getString("flowerType");

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }
        return flowers;
    }

    FlowerRequest getFlowerRequestByID(Integer requestID) throws SQLException, ItemNotFoundException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE requestID="+requestID+";");
        if(!resultSet.next()){
            throw new ItemNotFoundException();
        }
        String requesterName = resultSet.getString("requesterName");
        String location = resultSet.getString("location");
        String staffMember = resultSet.getString("staffMember");
        String additionalNotes = resultSet.getString("additionalNotes");
        Timestamp requestDate = resultSet.getTimestamp("requestDate");
        RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
        String flowerType = resultSet.getString("flowerType");

        return new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType);
    }

    ArrayList<FlowerRequest> getFlowerRequestsByRequesterName(String requesterName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE requesterName='"+requesterName+"';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
            String flowerType = resultSet.getString("flowerType");

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }
        return flowers;
    }

    ArrayList<FlowerRequest> getFlowerRequestsByLocation(String location) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE location='"+location+"';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
            String flowerType = resultSet.getString("flowerType");

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }
        return flowers;
    }

    ArrayList<FlowerRequest> getFlowerRequestsByStaffMember(String staffMember) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE staffMember='"+staffMember+"';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
            String flowerType = resultSet.getString("flowerType");

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }
        return flowers;
    }

    ArrayList<FlowerRequest> getFlowerRequestsByFlowerType(String flowerType) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE flowerType='"+flowerType+"';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String additionalNotes = resultSet.getString("additionalNotes");
            String staffMember = resultSet.getString("staffMember");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );


            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }
        return flowers;
    }

    ArrayList<FlowerRequest> getFlowerRequestsByRequestStatus(RequestStatus requestStatus) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE requestStatus='"+requestStatus+"';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String flowerType = resultSet.getString("flowerType");

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }
        return flowers;
    }

    ArrayList<FlowerRequest> getFlowerRequestsAfterTime(Timestamp time) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE requestDate>'"+time+"';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while(resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String flowerType = resultSet.getString("flowerType");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }

        return flowers;
    }

    ArrayList<FlowerRequest> getFlowerRequestsBeforeTime(Timestamp time) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE requestDate<'"+time+"';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while(resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String flowerType = resultSet.getString("flowerType");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }

        return flowers;
    }

    ArrayList<FlowerRequest> getFlowerRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getFlowerRequestSchemaNameTableName()+" WHERE requestDate>'"+ firstTime +"'AND requestDate<'" + secondTime + "';");
        ArrayList<FlowerRequest> flowers = new ArrayList<>();
        while(resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String flowerType = resultSet.getString("flowerType");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );

            flowers.add(new FlowerRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType));
        }

        return flowers;
    }
}
