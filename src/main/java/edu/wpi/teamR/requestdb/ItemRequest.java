package edu.wpi.teamR.requestdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
abstract class ItemRequest {

    private int requestID;
    private String requesterName;
    private String location;
    private String staffMember;
    private String additionalNotes;
    private Timestamp requestDate;
    private RequestStatus requestStatus;

    public ItemRequest(int requestID, String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus) {
        this.requestID = requestID;
        this.requesterName = requesterName;
        this.location = location;
        this.staffMember = staffMember;
        this.additionalNotes = additionalNotes;
        this.requestDate = requestDate;
        this.requestStatus = requestStatus;
    }

}
