package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.csv.CSVWritable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Edge implements MapData, CSVWritable {
    private int startNode, endNode;

    Edge(int startNode, int endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    private Edge(String[] args) throws ArrayIndexOutOfBoundsException {
        this(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }

    @Override
    public String toCSVEntry() {
        return startNode + "," + endNode;
    }

    @Override
    public String getCSVColumns() {
        return "startNode,endNode";
    }
}
