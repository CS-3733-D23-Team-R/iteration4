package edu.wpi.teamR.mapdb;
import edu.wpi.teamR.Configuration;
import edu.wpi.teamR.ItemNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectionArrowDAO {

    DirectionArrow addDirectionArrow(String longName, int kioskID, Direction direction, Date date) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ Configuration.getDirectionArrowSchemaNameTableName()+"(longName, kioskID, direction, date) VALUES(?, ?, ?, ?);");
        preparedStatement.setString(1, longName);
        preparedStatement.setInt(2, kioskID);
        preparedStatement.setString(3, direction.toString());
        preparedStatement.setDate(4, date);
        preparedStatement.executeUpdate();
        return new DirectionArrow(longName, kioskID, direction, date);
    }

    public void addDirectionArrows(List<DirectionArrow> directionArrows) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO "+ Configuration.getDirectionArrowSchemaNameTableName()+"(longName, kioskID, direction, date) VALUES(?, ?, ?, ?);");
        for (DirectionArrow d : directionArrows) {
            preparedStatement.setString(1, d.getLongName());
            preparedStatement.setInt(2, d.getKioskID());
            preparedStatement.setString(3, d.getDirection().toString());
            preparedStatement.setDate(4, d.getDate());
            preparedStatement.executeUpdate();
        }
    }

    void deleteDirectionArrowByLongName(String longName) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getDirectionArrowSchemaNameTableName()+" WHERE longName=?;");
        preparedStatement.setString(1, longName);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
    }

    void deleteDirectionArrowsByKiosk(int kioskID) throws SQLException, ItemNotFoundException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getDirectionArrowSchemaNameTableName()+" WHERE kioskID=?;");
        preparedStatement.setInt(1, kioskID);
        int rows = preparedStatement.executeUpdate();
        if (rows == 0)
            throw new ItemNotFoundException();
    }

    void deleteAllDirectionArrows() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+Configuration.getDirectionArrowSchemaNameTableName()+";");
        preparedStatement.executeUpdate();
    }

    ArrayList<DirectionArrow> getDirectionArrows() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getDirectionArrowSchemaNameTableName()+";");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<DirectionArrow> directionArrows = new ArrayList<>();
        while (resultSet.next()){
            String longName = resultSet.getString("longName");
            int kioskID = resultSet.getInt("kioskID");
            Direction direction = Direction.valueOf(resultSet.getString("direction"));
            Date date = resultSet.getDate("date");
            directionArrows.add(new DirectionArrow(longName, kioskID, direction, date));
        }
        return directionArrows;
    }

    ArrayList<DirectionArrow> getCurrentDirectionArrows() throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getDirectionArrowSchemaNameTableName()+" WHERE date<CURRENT_DATE;");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<DirectionArrow> directionArrows = new ArrayList<>();
        while (resultSet.next()){
            String longName = resultSet.getString("longName");
            int kioskID = resultSet.getInt("kioskID");
            Direction direction = Direction.valueOf(resultSet.getString("direction"));
            Date date = resultSet.getDate("date");
            directionArrows.add(new DirectionArrow(longName, kioskID, direction, date));
        }
        return directionArrows;
    }

    ArrayList<DirectionArrow> getDirectionArrowsByKiosk(int kioskID) throws SQLException {
        Connection connection = Configuration.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+Configuration.getDirectionArrowSchemaNameTableName()+" WHERE kioskID=?;");
        preparedStatement.setInt(1, kioskID);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<DirectionArrow> directionArrows = new ArrayList<>();
        while (resultSet.next()){
            String longName = resultSet.getString("longName");
            Direction direction = Direction.valueOf(resultSet.getString("direction"));
            Date date = resultSet.getDate("date");
            directionArrows.add(new DirectionArrow(longName, kioskID, direction, date));
        }
        return directionArrows;
    }
}
