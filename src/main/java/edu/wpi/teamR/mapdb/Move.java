package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.csv.CSVWritable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Move implements MapData, CSVWritable {

    private int nodeID;
    private String longName;
    private Date moveDate;
    Move(int nodeID, String longName, Date moveDate){
        this.nodeID = nodeID;
        this.longName = longName;
        this.moveDate = moveDate;
    }

    private Move(String[] args) throws IndexOutOfBoundsException {
        this(Integer.parseInt(args[0]),args[1], Date.valueOf(args[2]));
    }

    @Override
    public String toCSVEntry() {
        return nodeID + "," + longName + "," + moveDate;
    }

    @Override
    public String getCSVColumns() {
        return "nodeID,longName,moveDate";
    }
}
