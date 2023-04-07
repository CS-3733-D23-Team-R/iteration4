package edu.wpi.teamR.mapdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Edge {
    private int startNode, endNode;

    Edge(int startNode, int endNode){
        this.startNode = startNode;
        this.endNode = endNode;
    }
}
