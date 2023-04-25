package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
public class RoomRequest implements Archivable {
    private int roomRequestID;
    private String longName;
    private String staffUsername;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;

    public RoomRequest(int roomRequestID, String longName, String staffUsername, Timestamp startTime, Timestamp endTime) {
        this.roomRequestID = roomRequestID;
        this.longName = longName;
        this.staffUsername = staffUsername;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private RoomRequest(String[] args) throws IndexOutOfBoundsException {
        this(Integer.parseInt(args[0]), args[1], args[2], Timestamp.valueOf(args[3]), Timestamp.valueOf(args[4]));
    }

    @Override
    public String toCSVEntry() {
        return roomRequestID + "," + longName + "," + staffUsername + "," + startTime.toString() + "," + endTime.toString();
    }

    @Override
    public String getCSVColumns() {
        return "roomRequestID,longName,staffUsername,startTime,endTime";
    }
}
