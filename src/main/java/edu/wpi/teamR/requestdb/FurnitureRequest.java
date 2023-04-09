package edu.wpi.teamR.requestdb;

import java.sql.Timestamp;

public class FurnitureRequest extends ItemRequest{
    public FurnitureRequest(int requestID, String requestorName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus) {
        super(requestID, requestorName, location, staffMember, additionalNotes, requestDate, requestStatus);
    }
}
