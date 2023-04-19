package edu.wpi.teamR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Configuration {
    private static final String connectionURL = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";
    private static final String username = "teamr";
    private static final String password = "teamr150";
    private static Connection connection;
    private static String schemaName = "iteration2";
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
    private static final String userTableName = "user";
    public static Connection getConnection() throws SQLException {
        if (connection!=null && !connection.isClosed())
            return Configuration.connection;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(connectionURL, username, password);
    }
    public static void changeSchemaName(String schemaName) { Configuration.schemaName = schemaName; }
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
}
