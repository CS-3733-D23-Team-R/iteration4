package edu.wpi.teamR.mapdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Move {

    private int nodeID;
    private String longName;
    private Date moveDate;
    public Move(int nodeID, String longName, Date moveDate){
        this.nodeID = nodeID;
        this.longName = longName;
        this.moveDate = moveDate;
    }
}
