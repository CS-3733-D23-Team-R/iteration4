package edu.wpi.teamR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Configuration {
    private static final String connectionURL = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";
    private static final String username = "teamr";
    private static final String password = "teamr150";
    private static Connection connection;
    private static String schemaName = "iteration1";
    private static final String nodeTableName = "node";
    private static final String edgeTableName = "edge";
    private static final String moveTableName = "move";
    private static final String locationNameTableName = "locationname";
    private static final String mealRequestTableName = "mealrequestview";
    private static final String furnitureRequestTableName = "furniturerequestview";
    private static final String roomRequestTableName = "roomrequestview";
    private static final String flowerRequestTableName = "flowerrequestview";
    private static final String authenticationTableName = "authentication";
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection!=null && !connection.isClosed())
            return Configuration.connection;
        Class.forName("org.postgresql.Driver");
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
    public static String getMealRequestSchemaNameTableName(){
        return schemaName+"."+mealRequestTableName;
    }
    public static String getFurnitureRequestSchemaNameTableName(){
        return schemaName+"."+furnitureRequestTableName;
    }
    public static String getRoomRequestSchemaNameTableName(){
        return schemaName+"."+roomRequestTableName;
    }
    public static String getFlowerRequestSchemaNameTableName() {return schemaName+"."+flowerRequestTableName; }
    public static String getAuthenticationTableName(){ return schemaName+"."+authenticationTableName;}
}
