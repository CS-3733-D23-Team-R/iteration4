package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
public class ItemRequest implements Archivable {

    private int requestID;
    private RequestType requestType;
    private RequestStatus requestStatus;
    private String longName, staffUsername, itemType, requesterName, additionalNotes;
    private Timestamp requestDate;
    private int quantity;

    public ItemRequest(int requestID, RequestType requestType, RequestStatus requestStatus, String longName, String staffUsername, String itemType, String requesterName, String additionalNotes, Timestamp requestDate, int quantity){
        this.requestID=requestID;
        this.requestType = requestType;
        this.requestStatus = requestStatus;
        this.longName = longName;
        this.staffUsername = staffUsername;
        this.itemType = itemType;
        this.requesterName = requesterName;
        this.additionalNotes = additionalNotes;
        this.requestDate = requestDate;
        this.quantity = quantity;
    }

    private ItemRequest(String[] args) throws IndexOutOfBoundsException {
        this(Integer.parseInt(args[0]), RequestType.valueOf(args[1]), RequestStatus.valueOf(args[2]), args[3], args[4], args[5], args[6], args[7], Timestamp.valueOf(args[8]), Integer.parseInt(args[9]));
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public void setStaffMember(String staffMember) {
        this.staffUsername = staffMember;
    }

    @Override
    public String[] toCSVEntry() {
        return new String[]{String.valueOf(requestID), requestType.toString(), requestStatus.toString(), longName, staffUsername, itemType, requesterName, additionalNotes, requestDate.toString(), String.valueOf(quantity)};
    }

    @Override
    public String getCSVColumns() {
        return "requestID,requestType,requestStatus,longName,staffUsername,itemType,requesterName,additionalNotes,requestDate,quantity";
    }
}
