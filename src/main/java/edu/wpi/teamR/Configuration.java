package edu.wpi.teamR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Configuration {
    private static final String connectionURL = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";
    private static final String username = "teamr";
    private static final String password = "teamr150";
    private static final String awsConnectionURL = "jdbc:postgresql://teamrdb.cjimrohummbk.us-east-1.rds.amazonaws.com:5432/postgres";
    private static final String awsUsername = "teamr";
    private static final String awsPassword = "teamr151";
    private static boolean isAws = true;
    private static Connection connection;
    private static String schemaName = "iteration4";
    private static String testSchemaName = "iteration4test";
    private static final String nodeTableName = "node";
    private static final String edgeTableName = "edge";
    private static final String moveTableName = "move";
    private static final String locationNameTableName = "locationname";
    private static final String conferenceRoomTableName = "conferenceroom";
    private static final String directionArrowTableName = "directionarrow";
    private static final String serviceRequestTableName = "servicerequest";
    private static final String roomRequestTableName = "roomrequest";
    private static final String availableMealsTableName = "availableMeals";
    private static final String availableFurnitureTableName = "availableFurniture";
    private static final String availableFlowersTableName = "availableFlowers";
    private static final String availableSuppliesTableName = "availableSupplies";
    private static final String alertTableName = "alerts";
    private static final String userTableName = "user";
    private static final String patientTableName = "patient";
    private static final String patientMoveTableName = "patientMove";
    public static Connection getConnection() throws SQLException {
        if (connection!=null && !connection.isClosed())
            return Configuration.connection;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!isAws)
            Configuration.connection = DriverManager.getConnection(connectionURL, username, password);
        else
            Configuration.connection = DriverManager.getConnection(awsConnectionURL, awsUsername, awsPassword);
        return Configuration.connection;
    }

    //returns true if aws after swap and false if WPI
    public static boolean swapDatabase() throws SQLException {
        isAws = !isAws;
        connection.close();
        return isAws;
    }
    public static void changeSchemaToTest() throws SQLException {
        Configuration.schemaName = Configuration.testSchemaName;
        Configuration.deleteEverything();
    }
    public static String getNodeSchemaNameTableName(){
        return schemaName+"."+nodeTableName;
    }
    public static String getEdgeSchemaNameTableName(){
        return schemaName+"."+edgeTableName;
    }
    public static String getMoveSchemaNameTableName(){
        return schemaName+"."+moveTableName;
    }
    public static String getLocationNameSchemaNameTableName(){
        return schemaName+"."+locationNameTableName;
    }
    public static String getDirectionArrowSchemaNameTableName(){
        return schemaName+"."+directionArrowTableName;
    }
    public static String getConferenceRoomSchemaNameTableName(){
        return schemaName+"."+conferenceRoomTableName;
    }
    public static String getServiceRequestSchemaNameTableName(){
        return schemaName+"."+serviceRequestTableName;
    }
    public static String getFlowerRequestSchemaNameTableName() {return "";}
    public static String getMealRequestSchemaNameTableName() {return "";}
    public static String getFurnitureRequestSchemaNameTableName() {return "";}
    public static String getRoomRequestSchemaNameTableName(){
        return schemaName+"."+roomRequestTableName;
    }
    public static String getUserTableSchemaNameTableName(){ return schemaName+"."+userTableName;}
    public static String getAvailableMealsTableSchemaNameTableName(){ return schemaName+"."+availableMealsTableName;}
    public static String getAvailableFurnitureTableSchemaNameTableName(){ return schemaName+"."+availableFurnitureTableName;}
    public static String getAvailableFlowersTableSchemaNameTableName(){ return schemaName+"."+availableFlowersTableName;}
    public static String getAvailableSuppliesTableSchemaNameTableName(){ return schemaName+"."+availableSuppliesTableName;}
    public static String getAlertSchemaNameTableName(){
        return schemaName+"."+alertTableName;
    }
    public static String getPatientSchemaNameTableName(){
        return schemaName+"."+patientTableName;
    }
    public static String getPatientMoveSchemaNameTableName(){
        return schemaName+"."+patientMoveTableName;
    }
    public static void deleteEverything() throws SQLException {
        Connection connection = Configuration.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + Configuration.getDirectionArrowSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getMoveSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getEdgeSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getNodeSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getRoomRequestSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getConferenceRoomSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getServiceRequestSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getPatientMoveSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getPatientSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getUserTableSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getLocationNameSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getAlertSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getAvailableFurnitureTableSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getAvailableMealsTableSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getAvailableSuppliesTableSchemaNameTableName() + ";");
        statement.executeUpdate("DELETE FROM " + Configuration.getAvailableFlowersTableSchemaNameTableName() + ";");
    }
}
