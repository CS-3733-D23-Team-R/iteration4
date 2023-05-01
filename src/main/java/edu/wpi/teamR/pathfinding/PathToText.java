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
import java.util.Map;

import static java.lang.Math.abs;

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
        this.textualPath = getTextualDirectionsByFloor(textualPathByFloor.get(0).getNodeIDs(), date, newPath.get(newPath.size() - 1), textualPathByFloor.get(1).floorNum);
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
        Node lastNode = null; //keeps track of last junction we came from
        Node currentNode = null;
        Node previousNode = null;
        Node secondPreviousNode = null;
        Node nextNode = null;
        Node nextNextNode = null;
        int floorNodesSize = floorNodes.size();
        for (int i = 0; i < floorNodesSize; i++){
            secondPreviousNode = previousNode; //will be null until i = 2
            previousNode = currentNode; //will be null until i = 1
            currentNode = this.mapDatabase.getNodeByID(floorNodes.get(i)); //current node
            if(i + 1 < floorNodesSize){nextNode = this.mapDatabase.getNodeByID(floorNodes.get(i + 1));}
            else {nextNode = null;}
            if(i + 2 < floorNodesSize){nextNextNode = this.mapDatabase.getNodeByID(floorNodes.get(i + 2));}
            else {nextNextNode = null;}

            if(i == 0 && floorNodes.get(0) != lastNodeID){ //start of floor
                String initialText = "";
                if(getNodeTypeByNodeID(floorNodes.get(i), date).equals("ELEV")){
                    initialText = initialText + "Exit the elevator and turn " + turnDirection(currentNode, nextNode, nextNextNode)
                            + " to face " + cardinalText(nextNode, nextNextNode);
                    lastNode = nextNode;
                } else if (getNodeTypeByNodeID(floorNodes.get(i), date).equals("STAI")){
                    initialText = initialText + "Exit the stairs and turn " + turnDirection(currentNode, nextNode, nextNextNode)
                            + " to face " + cardinalText(nextNode, nextNextNode);
                    lastNode = nextNode;
                } else {
                    initialText = initialText + "Start at " + this.mapDatabase.getLocationNamesByNodeIDAtDate(floorNodes.get(0), date).get(0).getLongName()
                            + "\n      and turn to face " + cardinalText(currentNode, nextNode);
                    lastNode = currentNode;
                }
                floorTextList.add(initialText);

            } else { //anything intermediate or at the end
                if(detectTurn(currentNode, lastNode, nextNode)){
                    String intermediateText = "";
                    intermediateText = "Continue for " + nodeDiffToFeet(lastNode, currentNode) + " feet until you reach the hallway junction";
                    floorTextList.add(intermediateText);
                    turnText(lastNode, currentNode, nextNode, floorTextList); //sets list by reference
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



    @Deprecated
    private ArrayList<String> getTextualDirections(ArrayList<Integer> newPath, Date date) throws SQLException, ItemNotFoundException {
//        ArrayList<Node> nodeList = new ArrayList<>();
        ArrayList <String> textList = new ArrayList<>();
        Node lastNode = this.mapDatabase.getNodeByID(newPath.get(0)); //keeps track of last junction we came from
        Node currentNode = this.mapDatabase.getNodeByID(newPath.get(1));
        Node previousNode = null;
        Node secondPreviousNode = null;
        Node nextNode = null;
        int pathSize = newPath.size();
        textList.add("Start at " + this.mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(0), date).get(0).getLongName()
                + "\n      and turn to face " + cardinalText(lastNode, currentNode));
        checkFloorChange(lastNode, null, null, currentNode, newPath.get(0), newPath.get(1), pathSize, textList);

        //Break path into segments that need directions
        for (int i = 1; i < pathSize; i++){
            secondPreviousNode = previousNode; //will be null until i = 2
            previousNode = currentNode; //will be null until i = 1
            currentNode = this.mapDatabase.getNodeByID(newPath.get(i)); //current node
            if(i + 1 < pathSize){nextNode = this.mapDatabase.getNodeByID(newPath.get(i + 1));};

            LocationName locationName = this.mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(i), date).get(0);

            if(i == newPath.size() - 1){
                textList.add("Continue for " + nodeDiffToFeet(currentNode, lastNode) + " feet");
                textList.add("You have arrived at " + this.mapDatabase.getLocationNamesByNodeIDAtDate(newPath.get(i), date).get(0).getLongName());
                break;

            } else if(checkFloorChange(currentNode, previousNode, secondPreviousNode, nextNode, newPath.get(i), newPath.get(i + 1), pathSize, textList)){
//                lastNode = currentNode;
                lastNode = secondPreviousNode;
                i++;

            } else if(detectTurn(currentNode, lastNode, nextNode)){
                String forwardText = "Error";
                if(!locationName.getNodeType().equals("HALL")) forwardText = "Walk forwards until you reach " + locationName.getShortName(); //change to deal with wayfinding nodes
                else forwardText = "Continue for " + nodeDiffToFeet(lastNode, currentNode) + " feet until you reach the hallway junction";

                textList.add(forwardText);
                turnText(lastNode, currentNode, nextNode, textList);
                lastNode = currentNode;
            }
        }
        return textList;
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
        if(currentNode == null || lastNode == null || nextNode == null) return false;
        int[] vector1 = new int[]{currentNode.getXCoord() - lastNode.getXCoord(), currentNode.getYCoord() - lastNode.getYCoord()};
        int[] vector2 = new int[]{nextNode.getXCoord() - currentNode.getXCoord(), nextNode.getYCoord() - currentNode.getYCoord()};
        int dotProduct = vector1[0] * vector2[0] + vector1[1] * vector2[1];
        double magnitude1 = Math.sqrt(vector1[0]*vector1[1]);
        double magnitude2 = Math.sqrt(vector2[0]*vector2[1]);
        double turnThreshold = 0.2;
        return dotProduct / (magnitude1 * magnitude2) > turnThreshold;
    }
    //updates textual directions list
    private boolean checkFloorChange(Node firstNode, Node previousNode, Node secondPreviousNode, Node secondNode, int firstNodeId, int secondNodeID, int pathSize, ArrayList<String> textList) throws SQLException, ItemNotFoundException {
        String firstNodeType = this.mapDatabase.getNodeTypeByNodeID(firstNodeId);
        String secondNodeType = this.mapDatabase.getNodeTypeByNodeID(secondNodeID);
        String outputString = "Take the ";
        String direction = turnDirection(secondPreviousNode, previousNode, firstNode);
        if(firstNodeType.equals("STAI") && 1 < pathSize && secondNodeType.equals("STAI")){
            outputString += "staircase ";
            if(direction != null){outputString = outputString + " on your " + direction;}
            textList.add(outputString  + " to floor " + secondNode.getFloorNum().toString());
            return true;
        } else if(firstNodeType.equals("ELEV") && 1 < pathSize && secondNodeType.equals("ELEV")){
            outputString += "elevator ";
            if(direction != null){outputString = outputString + " on your " + direction;}
            textList.add(outputString  + " to floor " + secondNode.getFloorNum().toString());
            return true;
        }
        return false;
    }

    private String turnDirection(Node firstNode, Node middleNode, Node secondNode){
        if(middleNode != null && firstNode != null) {
            double firstSlope = (middleNode.getYCoord() - firstNode.getYCoord() * 1.0) / (middleNode.getXCoord() - firstNode.getXCoord());
            double secondSlope = (secondNode.getYCoord() - middleNode.getYCoord() * 1.0) / (secondNode.getXCoord() - middleNode.getXCoord());
            double difference = secondSlope - firstSlope;
            if (difference > 0) return "right";
            else return "left";
        } else {
            return null;
        }
    }

    private void turnText(Node firstNode, Node middleNode, Node secondNode, ArrayList<String> textList){
        String outputText = "Turn " + turnDirection(firstNode, middleNode, secondNode) + " to face " + cardinalText(firstNode, secondNode);
        textList.add(outputText);
    }

    //returns cardinal direction between two nodes
    private String cardinalText(Node firstNode, Node secondNode){
        int NESW = firstNode.getYCoord() - secondNode.getYCoord(); //images are indexed from top left corner
        int SENW = secondNode.getXCoord() - firstNode.getXCoord();
        int nodeDiff = nodeDiffPixels(firstNode, secondNode);
        if(abs(NESW) < nodeDiff/2) NESW = 0; //account for nodes that are more or less straight
        if(abs(SENW) < nodeDiff/2) SENW = 0;
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
                return "SouthWest";
            } else if (SENW > 0){
                return "South";
            } else if (SENW < 0){
                return "West";
            }
        }
        return "Turn error";
    }

    private String getNodeTypeByNodeID(int nodeID, Date date) throws SQLException {
        return this.mapDatabase.getLocationNamesByNodeIDAtDate(nodeID, date).get(0).getNodeType();
    }
}
