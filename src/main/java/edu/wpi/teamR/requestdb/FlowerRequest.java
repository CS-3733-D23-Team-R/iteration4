package edu.wpi.teamR.requestdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
public class FlowerRequest extends ItemRequest{
    public FlowerRequest(int requestID, String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String flowerType) {
        super(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus, flowerType);
    }
}
