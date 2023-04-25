package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.mapdb.Move;
import edu.wpi.teamR.mapdb.Node;
import lombok.Getter;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Math.abs;

public class PathToText {
//    private ArrayList<Integer> path;

    @Getter
    private ArrayList<String> textualPath;

    /**
     * Construct class using Path object
     * get text directions using getTextualPath()
     * @param path
     */
    public PathToText(Path path, Date date) throws SQLException, ItemNotFoundException {
        ArrayList<Integer> newPath = path.getPath();

        this.textualPath = getTextualDirections(newPath, date);

    }

    private ArrayList<String> getTextualDirections(ArrayList<Integer> newPath, Date date) throws SQLException, ItemNotFoundException {
        MapDatabase mapDatabase = new MapDatabase();
        ArrayList<Node> nodeList = new ArrayList<>();
//        for (Integer num : newPath){ nodeList.add(mapDatabase.getNodeByID(num)); }
        ArrayList <String> textList = new ArrayList<>();
        Node lastNode = mapDatabase.getNodeByID(newPath.get(0));
        textList.add("Start at " + mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(0), date).get(0).getShortName());

        if(mapDatabase.getNodeTypeByNodeID(newPath.get(0)).equals("STAI") && 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(1)).equals("STAI")){
            textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(1)).getFloorNum().toString());
            lastNode = mapDatabase.getNodeByID(1);
        } else if(mapDatabase.getNodeTypeByNodeID(newPath.get(0)).equals("ELEV") && 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(1)).equals("ELEV")){
            textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(1)).getFloorNum().toString());
            lastNode = mapDatabase.getNodeByID(1);
        }

        //Break path into segments that need directions
        for (int i = 1; i < newPath.size(); i++){
            if(i == newPath.size() - 1){
                textList.add("You have arrived at your destination");
                break;
            }

            Node node = mapDatabase.getNodeByID(i);
            if(mapDatabase.getNodeTypeByNodeID(newPath.get(i)).equals("STAI") && i + 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(i + 1)).equals("STAI")){
                textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(i+1)).getFloorNum().toString());
                lastNode = node;
            } else if(mapDatabase.getNodeTypeByNodeID(newPath.get(i)).equals("ELEV") && i + 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(i + 1)).equals("ELEV")){
                textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(i+1)).getFloorNum().toString());
                lastNode = node;
            }

            if(abs(node.getXCoord() - lastNode.getXCoord()) > 50 || abs(node.getYCoord() - lastNode.getYCoord()) > 50){
                String forwardText = "Walk forwards until you reach " + mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(i), date).get(0).getShortName();
                textList.add(forwardText);
                textList.add(turnText(lastNode, mapDatabase.getNodeByID(i + 1)));
                lastNode = node;
            }

        }
        return textList;
    }

    private String turnText(Node firstNode, Node secondNode){
        return "Turn";
    }
}
