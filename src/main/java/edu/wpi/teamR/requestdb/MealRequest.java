package edu.wpi.teamR.requestdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter(AccessLevel.PACKAGE)
public class MealRequest extends ItemRequest {
    private String mealType;
    public MealRequest(int requestID, String requesterName, String location, String staffMember, String additionalNotes, Timestamp requestDate, RequestStatus requestStatus, String mealType) {
        super(requestID, requesterName, location, staffMember, additionalNotes, requestDate, requestStatus);
        this.mealType = mealType;
    }
}
