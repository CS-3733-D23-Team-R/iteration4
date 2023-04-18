package edu.wpi.teamR.requestdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
public class RoomRequest {
    private int roomRequestID;
    private String longname;
    private String staffUsername;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;

    public RoomRequest(int roomRequestID, String longname, String staffUsername, Timestamp startTime, Timestamp endTime) {
        this.roomRequestID = roomRequestID;
        this.longname = longname;
        this.staffUsername = staffUsername;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
