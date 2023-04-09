package edu.wpi.teamR.requestdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
public class FurnitureRequest extends ItemRequest{
    private String furnitureType;
    public FurnitureRequest(int requestID, String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String furnitureType) {
        super(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus);
        this.furnitureType = furnitureType;
    }
}
