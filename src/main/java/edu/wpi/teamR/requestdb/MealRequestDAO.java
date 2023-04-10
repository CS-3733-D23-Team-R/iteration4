package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.Configuration;

import java.sql.*;
import java.util.ArrayList;

public class MealRequestDAO {
    private Connection connection;
    MealRequestDAO(Connection connection)  {
        this.connection = connection;
    }

    MealRequest addMealRequest(String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String mealType) throws SQLException {
        PreparedStatement sqlInsert = connection.prepareStatement("INSERT INTO "+ Configuration.getMealRequestSchemaNameTableName()
                +"(requesterName,location,staffMember,additionalNotes,requestDate,requestStatus,mealType)"
                +" VALUES (?,?,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
        sqlInsert.setString(1, requesterName);
        sqlInsert.setString(2, location);
        sqlInsert.setString(3, staffMember);
        sqlInsert.setString(4, additionalNotes);
        sqlInsert.setTimestamp(5, requestDate);
        sqlInsert.setString(6, requestStatus.toString());
        sqlInsert.setString(7, mealType);
        sqlInsert.executeUpdate();
        ResultSet rs = sqlInsert.getGeneratedKeys();
        int requestID = 0;
        if (rs.next()) {
            requestID = rs.getInt("requestID");
        }
        return new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType);
    }

    void deleteMealRequest(Integer requestID) throws SQLException {
        PreparedStatement sqlDelete = connection.prepareStatement("DELETE FROM"+ Configuration.getMealRequestSchemaNameTableName() + "WHERE requestID = " + requestID +";");
    }

    ArrayList<MealRequest> getMealRequests() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+";");
        ArrayList<MealRequest> meals = new ArrayList<MealRequest>();
        while (resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
            String mealType = resultSet.getString("mealType");

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }
        return meals;
    }

    MealRequest getMealRequestByID(Integer requestID) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE requestID="+requestID+";");
        resultSet.next();
        String requesterName = resultSet.getString("requesterName");
        String location = resultSet.getString("location");
        String staffMember = resultSet.getString("staffMember");
        String additionalNotes = resultSet.getString("additionalNotes");
        Timestamp requestDate = resultSet.getTimestamp("requestDate");
        RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
        String mealType = resultSet.getString("mealType");

        return new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType);
    }

    ArrayList<MealRequest> getMealRequestsByRequesterName(String requesterName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE requesterName="+requesterName+";");
        ArrayList<MealRequest> meals = new ArrayList<MealRequest>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
            String mealType = resultSet.getString("mealType");

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }
        return meals;
    }

    ArrayList<MealRequest> getMealRequestsByLocation(String location) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE location="+location+";");
        ArrayList<MealRequest> meals = new ArrayList<MealRequest>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
            String mealType = resultSet.getString("mealType");

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }
        return meals;
    }

    ArrayList<MealRequest> getMealRequestByStaffMember(String staffMember) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE staffMember="+staffMember+";");
        ArrayList<MealRequest> meals = new ArrayList<MealRequest>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );
            String mealType = resultSet.getString("mealType");

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }
        return meals;
    }

    ArrayList<MealRequest> getMealRequestByMealType(String mealType) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE mealType="+mealType+";");
        ArrayList<MealRequest> meals = new ArrayList<MealRequest>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }
        return meals;
    }

    ArrayList<MealRequest> getMealRequestByRequestStatus(RequestStatus requestStatus) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE requestStatus="+requestStatus+";");
        ArrayList<MealRequest> meals = new ArrayList<MealRequest>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String mealType = resultSet.getString("mealType");

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }
        return meals;
    }

    ArrayList<MealRequest> getMealRequestsAfterTime(Timestamp time) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE starttime>"+time+";");
        ArrayList<MealRequest> meals = new ArrayList<>();
        while(resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String mealType = resultSet.getString("mealType");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }

        return meals;
    }

    ArrayList<MealRequest> getMealRequestsBeforeTime(Timestamp time) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE starttime<"+time+";");
        ArrayList<MealRequest> meals = new ArrayList<>();
        while(resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String mealType = resultSet.getString("mealType");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }

        return meals;
    }

    ArrayList<MealRequest> getMealRequestsBetweenTimes(Timestamp firstTime, Timestamp secondTime) throws SQLException { //TODO: Finish Implementation
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+Configuration.getMealRequestSchemaNameTableName()+" WHERE starttime>"+ firstTime +"AND starttime<" + secondTime + ";");
        ArrayList<MealRequest> meals = new ArrayList<>();
        while(resultSet.next()){
            int requestID = resultSet.getInt("requestID");
            String requesterName = resultSet.getString("requesterName");
            String location = resultSet.getString("location");
            String staffMember = resultSet.getString("staffMember");
            String additionalNotes = resultSet.getString("additionalNotes");
            Timestamp requestDate = resultSet.getTimestamp("requestDate");
            String mealType = resultSet.getString("mealType");
            RequestStatus requestStatus = RequestStatus.valueOf(resultSet.getString("requestStatus") );

            meals.add(new MealRequest(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, mealType));
        }

        return meals;
    }
}
