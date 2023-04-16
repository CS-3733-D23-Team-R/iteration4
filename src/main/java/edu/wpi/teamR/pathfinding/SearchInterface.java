package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;

import java.sql.SQLException;
interface SearchInterface{
    Path getPath(int startID, int endID, boolean accessible) throws SQLException, ItemNotFoundException;
}
