package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.csv.MapData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Edge extends MapData {
    private int startNode, endNode;

    Edge(int startNode, int endNode){
        this.startNode = startNode;
        this.endNode = endNode;
    }

}
