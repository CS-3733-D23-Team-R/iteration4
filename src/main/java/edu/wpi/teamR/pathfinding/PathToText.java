package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;
import lombok.Getter;

import java.sql.SQLException;
import java.util.ArrayList;

public class PathToText {
//    private ArrayList<Integer> path;
    @Getter
    private ArrayList<String> pathText;

    /**
     * Construct class using Path object
     * get text directions using getTextualDirections()
     * @param path
     */
    public PathToText(Path path) {
        ArrayList<Integer> newPath = path.getPath();
        this.pathText = getTextualDirections(newPath);
    }

    private ArrayList<String> getTextualDirections(ArrayList<Integer> newPath){
        ArrayList <String> textList = new ArrayList<>();
        textList.add("Go Left");
        textList.add("Go Right");
        return textList;
    }

//    private ArrayList<Integer> filteredPath(ArrayList<Integer> newPath) throws SQLException, ClassNotFoundException, ItemNotFoundException {
//        MapDatabase mapDatabase = new MapDatabase();
//        for(Integer i : newPath){
//            String nodeType = mapDatabase.getNodeTypeByNodeID(i);
//            if(unimportantNodes.contains(nodeType)) {newPath.remove(i)}
//        }
//        return newPath;
//    }
}
