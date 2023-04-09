package edu.wpi.teamR.requestdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
public class RoomRequest {
    private int requestID;
    private String location;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;
    private String requesterName;
    private String requestReason;

    public RoomRequest(int requestID, String location, Timestamp startTime, Timestamp endTime, String requesterName, String requestReason) {
        this.requestID = requestID;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requesterName = requesterName;
        this.requestReason = requestReason;
    }
}
