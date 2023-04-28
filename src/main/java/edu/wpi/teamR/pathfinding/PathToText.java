package edu.wpi.teamR.pathfinding;

import edu.wpi.teamR.ItemNotFoundException;
import edu.wpi.teamR.mapdb.LocationName;
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
        textList.add("Start at " + mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(0), date).get(0).getLongName());

        if(mapDatabase.getNodeTypeByNodeID(newPath.get(0)).equals("STAI") && 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(1)).equals("STAI")){
            textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(1)).getFloorNum().toString());
            lastNode = mapDatabase.getNodeByID(newPath.get(1));
        } else if(mapDatabase.getNodeTypeByNodeID(newPath.get(0)).equals("ELEV") && 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(1)).equals("ELEV")){
            textList.add("Take the elevator to floor " + mapDatabase.getNodeByID(newPath.get(1)).getFloorNum().toString());
            lastNode = mapDatabase.getNodeByID(newPath.get(1));
        }

        //Break path into segments that need directions
        for (int i = 1; i < newPath.size(); i++){
            if(i == newPath.size() - 1){
                textList.add("You have arrived at " + mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(i), date).get(0).getLongName());
                break;
            }

            Node node = mapDatabase.getNodeByID(newPath.get(i));
            LocationName locationName = mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(i), date).get(0);
            if(mapDatabase.getNodeTypeByNodeID(newPath.get(i)).equals("STAI") && i + 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(i + 1)).equals("STAI")){
                textList.add("Take the staircase to floor " + mapDatabase.getNodeByID(newPath.get(i+1)).getFloorNum().toString());
                lastNode = node;
            } else if(mapDatabase.getNodeTypeByNodeID(newPath.get(i)).equals("ELEV") && i + 1 < newPath.size() && mapDatabase.getNodeTypeByNodeID(newPath.get(i + 1)).equals("ELEV")){
                textList.add("Take the elevator to floor " + mapDatabase.getNodeByID(newPath.get(i+1)).getFloorNum().toString());
                lastNode = node;
            }


            if(abs(node.getXCoord() - lastNode.getXCoord()) > 100 && abs(node.getYCoord() - lastNode.getYCoord()) > 100){
                String forwardText = "Error";
                if(!locationName.getNodeType().equals("HALL")) forwardText = "Walk forwards until you reach " + locationName.getShortName();
                else forwardText = "Continue for " + nodeDiffToFeet(lastNode, mapDatabase.getNodeByID(newPath.get(i))) + " feet until you reach the hallway junction";

                textList.add(forwardText);
                textList.add(turnText(lastNode, mapDatabase.getNodeByID(newPath.get(i)), mapDatabase.getNodeByID(newPath.get(i + 1))));
                lastNode = node;
            }

        }
        return textList;
    }

    private int nodeDiffToFeet(Node firstNode, Node secondNode){
        int diff = abs(firstNode.getXCoord() - secondNode.getXCoord()) + abs(firstNode.getYCoord() - secondNode.getYCoord());
        return diff / 3;
    }
    private String turnText(Node firstNode, Node middleNode, Node secondNode){
        double firstSlope = (middleNode.getYCoord() - firstNode.getYCoord() * 1.0)/(middleNode.getXCoord() - firstNode.getXCoord());
        double secondSlope = (secondNode.getYCoord() - middleNode.getYCoord() * 1.0)/(secondNode.getXCoord() - middleNode.getXCoord());
        double difference = secondSlope - firstSlope;
        if (difference > 0) return "Turn Right";
        else return "Turn Left";
    }
}
