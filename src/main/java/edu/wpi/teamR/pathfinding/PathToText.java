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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;

public class PathToText {
//    private ArrayList<Integer> path;

    @Getter @Deprecated
    private ArrayList<String> textualPath;

    @Getter
    private ArrayList<TextByFloor> textualPathByFloor;

    private MapDatabase mapDatabase;

    /**
     * Construct class using Path object
     * get text directions using getTextualPath()
     * @param path
     */
    public PathToText(Path path, Date date) throws SQLException, ItemNotFoundException {
        this.mapDatabase = new MapDatabase();
        ArrayList<Integer> newPath = path.getPath();
        this.textualPathByFloor = splitPathByFloor(newPath, date);
        System.out.print("text path size: ");
        System.out.println(textualPathByFloor.size());
        for (int i = 0; i < this.textualPathByFloor.size(); i++){
            TextByFloor textByFloorList = this.textualPathByFloor.get(i);
            String nextFloor = null;
            if(this.textualPathByFloor.size() > i + 1){nextFloor = this.textualPathByFloor.get(i + 1).floorNum;}
            textByFloorList.floorText = getTextualDirectionsByFloor(textByFloorList.getNodeIDs(), date, newPath.get(newPath.size() - 1), nextFloor);
        }
//        this.textualPath = getTextualDirections(newPath, date); //deprecate this
        //testing only prints first part
        this.textualPath = getTextualDirectionsByFloor(textualPathByFloor.get(0).getNodeIDs(), date, newPath.get(newPath.size() - 1), textualPathByFloor.get(0).floorNum);
    }

    private ArrayList<TextByFloor> splitPathByFloor(ArrayList<Integer> newPath, Date date) throws SQLException, ItemNotFoundException {
        ArrayList<TextByFloor> textByFloorList = new ArrayList<>();
        ArrayList<Integer> floorInts = new ArrayList<>();
        String currentFloor = this.mapDatabase.getNodeByID(newPath.get(0)).getFloorNum();
        for (Integer nodeID : newPath){
            if(!this.mapDatabase.getNodeByID(nodeID).getFloorNum().equals(currentFloor)){
                TextByFloor thisFloorText = new TextByFloor(floorInts, currentFloor);
                textByFloorList.add(thisFloorText);
                floorInts = new ArrayList<>();
                currentFloor = this.mapDatabase.getNodeByID(nodeID).getFloorNum();
            }
            floorInts.add(nodeID);
        }
        TextByFloor thisFloorText = new TextByFloor(floorInts, currentFloor);
        textByFloorList.add(thisFloorText);
        return textByFloorList;
    }

    private ArrayList<String> getTextualDirectionsByFloor(ArrayList<Integer> floorNodes, Date date, int lastNodeID, String nextFloor) throws SQLException, ItemNotFoundException {
        ArrayList <String> floorTextList = new ArrayList<>();
        BFSSearch bfsSearch = new BFSSearch(mapDatabase);
        Node lastNode = null; //keeps track of last junction we came from
        Node currentNode = null;
        Node previousNode = null;
        Node secondPreviousNode = null;
        Node nextNode = null;
        Node nextNextNode = null;
        int floorNodesSize = floorNodes.size();
        for (int i = 0; i < floorNodesSize; i++) {
            secondPreviousNode = previousNode; //will be null until i = 2
            previousNode = currentNode; //will be null until i = 1
            currentNode = this.mapDatabase.getNodeByID(floorNodes.get(i)); //current node
            if (i + 1 < floorNodesSize) {
                nextNode = this.mapDatabase.getNodeByID(floorNodes.get(i + 1));
            } else {
                nextNode = null;
            }
            if (i + 2 < floorNodesSize) {
                nextNextNode = this.mapDatabase.getNodeByID(floorNodes.get(i + 2));
            } else {
                nextNextNode = null;
            }

            if (i == 0 && floorNodes.get(0) != lastNodeID) { //start of floor
                if (floorNodesSize == 2) {
                    floorTextList.add("Walk " + cardinalText(currentNode, nextNode) +
                            " for " + nodeDiffToFeet(currentNode, nextNode) + " feet");
                } else {
                    String initialText = "";
                    if (getNodeTypeByNodeID(floorNodes.get(i), date).equals("ELEV")) {
                        initialText = initialText + "Exit the elevator and " + turnText(currentNode, nextNode, nextNextNode);
//                        floorTextList.add("testing current node id: " + currentNode.getNodeID());
//                        floorTextList.add("testing next node id: " + nextNode.getNodeID());
//                        floorTextList.add("testing nextNext node id: " + nextNextNode.getNodeID());
                        lastNode = nextNode;
                    } else if (getNodeTypeByNodeID(floorNodes.get(i), date).equals("STAI")) {
                        initialText = initialText + "Exit the stairs and " + turnText(currentNode, nextNode, nextNextNode);
                        lastNode = nextNode;
                    } else {
                        initialText = initialText + "Start at " + this.mapDatabase.getLocationNamesByNodeIDAtDate(floorNodes.get(0), date).get(0).getLongName()
                                + " and turn to face " + cardinalText(currentNode, nextNode);
                        lastNode = currentNode;
                    }
                    floorTextList.add(initialText);
                }

            } else if (i < floorNodesSize - 1 && currentNode.getNodeID() == lastNode.getNodeID()){ //after leaving an elevator assuming it's not the end of the path
            } else { //anything intermediate or at the end
                if(detectTurn(currentNode, lastNode, nextNode)){
                    String intermediateText = "";
//                    intermediateText = "Continue for " + nodeDiffToFeet(lastNode, currentNode) + " feet until you reach the hallway junction";
                    intermediateText = "Continue for " + nodeDiffToFeet(lastNode, currentNode) + " feet";
                    Node nearestNode = bfsSearch.getNearest(floorNodes.get(i), date);
                    intermediateText = intermediateText + " until you are near the " + mapDatabase.getLocationNamesByNodeIDAtDate(nearestNode.getNodeID(), date).get(0).getLongName();
                    floorTextList.add(intermediateText);
                    floorTextList.add(turnText(lastNode, currentNode, nextNode));
                    lastNode = currentNode;
                }

                if (i == floorNodesSize - 1) { //end of floor
                    String endingText = "";
                    if (floorNodes.get(i) == lastNodeID) {
                        endingText = endingText + "You have arrived at " + this.mapDatabase.getLocationNamesByNodeIDAtDate(floorNodes.get(i), date).get(0).getLongName();
                    } else if (getNodeTypeByNodeID(floorNodes.get(i), date).equals("ELEV")) {
                        endingText = endingText + "Take the elevator to floor " + nextFloor;
                    } else if (getNodeTypeByNodeID(floorNodes.get(i), date).equals("STAI")) {
                        endingText = endingText + "Take the stairs to floor " + nextFloor;
                    } else {
                        endingText = "End of route"; //error - should never occur
                    }
                    floorTextList.add(endingText);
                }
            }
        }
        return floorTextList;
    }

    private int nodeDiffToFeet(Node firstNode, Node secondNode){
        int diff = nodeDiffPixels(firstNode, secondNode);
        return diff / 3;
    }

    private int nodeDiffPixels(Node firstNode, Node secondNode){
        int diff = abs(firstNode.getXCoord() - secondNode.getXCoord()) + abs(firstNode.getYCoord() - secondNode.getYCoord());
        return diff;
    }

    private boolean detectTurn(Node currentNode, Node lastNode, Node nextNode){
        String turnDirection = turnDirection(lastNode, currentNode, nextNode);
        if(turnDirection == null) return false;
        else if(turnDirection.equals("go forwards facing ")) return false;
        else return true;
    }

    private String turnDirection(Node firstNode, Node middleNode, Node secondNode){
        double turnThreshold = 0.4;
        if(middleNode != null && firstNode != null && secondNode != null) {
            double firstX = middleNode.getXCoord() - firstNode.getXCoord() * 1.0;
            double firstY = -(middleNode.getYCoord() - firstNode.getYCoord() * 1.0); //invert Ys because image
//            double firstOrientation = Math.atan2(firstY, firstX)
            double secondX = secondNode.getXCoord() - middleNode.getXCoord() * 1.0;
            double secondY = -(secondNode.getYCoord() - middleNode.getYCoord() * 1.0);
            double firstTheta = atan2(firstY, firstX);
            if(firstTheta < 0) firstTheta += 2*Math.PI;
            double secondTheta = atan2(secondY, secondX);
            if(secondTheta < 0) secondTheta += 2*Math.PI;
//            double difference = secondTheta - firstTheta;
            double headingDiff = Math.atan2(Math.sin(secondTheta - firstTheta), Math.cos(secondTheta - firstTheta));
            if (abs(headingDiff) < turnThreshold) return "go forwards facing ";
            else if(headingDiff < turnThreshold) return "turn right to face ";
            else return "turn left to face "; //difference < turnThreshold
        } else return null;
    }

    private String turnText(Node firstNode, Node middleNode, Node secondNode){
        String outputText = turnDirection(firstNode, middleNode, secondNode) + cardinalText(middleNode, secondNode);
        return outputText;
    }

    //returns cardinal direction between two nodes
    private String cardinalText(Node firstNode, Node secondNode){
        int NESW = firstNode.getYCoord() - secondNode.getYCoord(); //images are indexed from top left corner
        int SENW = secondNode.getXCoord() - firstNode.getXCoord();
        int nodeDiff = nodeDiffPixels(firstNode, secondNode);
//        if(abs(NESW) < nodeDiff/2) NESW = 0; //account for nodes that are more or less straight
//        if(abs(SENW) < nodeDiff/2) SENW = 0;
        if(NESW == 0){
            if(SENW == 0){
                System.out.println("Nodes on top of each other? PathToText Error");
            } else if (SENW > 0) {
                return "Southeast";
            } else if (SENW < 0) {
                return "Northwest";
            }
        } else if (NESW > 0) {
            if(SENW == 0){
                return "Northeast";
            } else if (SENW > 0){
                return "East";
            } else if (SENW < 0){
                return "North";
            }
        } else if (NESW < 0) {
            if(SENW == 0){
                return "Southwest";
            } else if (SENW > 0){
                return "South";
            } else if (SENW < 0){
                return "West";
            }
        }
        return "Turn error";
    }

    private String getNodeTypeByNodeID(int nodeID, Date date) throws SQLException {
        ArrayList<LocationName> locationNames = this.mapDatabase.getLocationNamesByNodeIDAtDate(nodeID, date);
        if(locationNames.size() > 0) return locationNames.get(0).getNodeType();
        else
            return "";
    }
}
