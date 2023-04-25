package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import edu.wpi.teamR.mapdb.Node;
import lombok.Getter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class PathToText {
//    private ArrayList<Integer> path;

    @Getter
    private ArrayList<String> textualPath;

    /**
     * Construct class using Path object
     * get text directions using getTextualPath()
     * @param path
     */
    public PathToText(Path path) throws SQLException, ItemNotFoundException {
        ArrayList<Integer> newPath = path.getPath();

        this.textualPath = getTextualDirections(newPath);

    }

    private ArrayList<String> getTextualDirections(ArrayList<Integer> newPath) throws SQLException, ItemNotFoundException {
        MapDatabase mapDatabase = new MapDatabase();
        ArrayList<Node> nodeList = new ArrayList<>();
//        for (Integer num : newPath){ nodeList.add(mapDatabase.getNodeByID(num)); }
        ArrayList <String> textList = new ArrayList<>();
        Node lastNode = mapDatabase.getNodeByID(newPath.get(0));
        textList.add("Start at " + "mapDatabase.getNameByNodeID");
//
//        if(mapDatabase.getNodeTypeByNodeID(newPath.get(0)).equals("STAI") && 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(1)).equals("STAI")){
//            textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(1)).getFloorNum().toString());
//            lastNode = mapDatabase.getNodeByID(1);
//        } else if(mapDatabase.getNodeTypeByNodeID(newPath.get(0)).equals("ELEV") && 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(1)).equals("ELEV")){
//            textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(1)).getFloorNum().toString());
//            lastNode = mapDatabase.getNodeByID(1);
//        }

        //Break path into segments that need directions
        for (int i = 1; i < newPath.size(); i++){
            Node node = mapDatabase.getNodeByID(i);
            if(mapDatabase.getNodeTypeByNodeID(newPath.get(i)).equals("STAI") && i + 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(i + 1)).equals("STAI")){
                textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(i+1)).getFloorNum().toString());
                lastNode = node;
            } else if(mapDatabase.getNodeTypeByNodeID(newPath.get(i)).equals("ELEV") && i + 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(i + 1)).equals("ELEV")){
                textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(i+1)).getFloorNum().toString());
                lastNode = node;
            }
//
//            if(mapDatabase.getNodeTypeByNodeID(newPath.get(i)) != "HALL"){
//                textList.add("Walk forwards until you reach the " + "SHORTNAME");
//                lastNode = node;
//            }

        }
        return textList;
    }

//    private String directionText(int firstNode, int secondNode){
//
//    }
}
