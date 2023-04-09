package edu.wpi.teamR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Configuration {
    public static final String connectionURL = "jdbc:postgresql://database.cs.wpi.edu:5432/teamrdb";
    public static final String username = "teamr";
    public static final String password = "teamr150";
    public static final String schemaName = "iteration1";
    public static final String nodeTableName = "node";
    public static final String edgeTableName = "edge";
    public static final String moveTableName = "move";
    public static final String locationNameTableName = "locationname";
    public static final String mealRequestTableName = "mealrequestview";
    public static final String furnitureRequestTableName = "furniturerequestview";
    public static final String roomRequestTableName = "roomrequestview";
    public static final String authenticationTableName = "user";
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(connectionURL, username, password);
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
    public static String getMealRequestSchemaNameTableName(){
        return schemaName+"."+mealRequestTableName;
    }
    public static String getFurnitureRequestSchemaNameTableName(){
        return schemaName+"."+furnitureRequestTableName;
    }
    public static String getRoomRequestSchemaNameTableName(){
        return schemaName+"."+roomRequestTableName;
    }
    public static String getAuthenticationTableName(){ return schemaName+"."+authenticationTableName;}
}
