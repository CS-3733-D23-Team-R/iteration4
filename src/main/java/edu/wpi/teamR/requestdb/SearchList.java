package edu.wpi.teamR.requestdb;

import java.sql.Timestamp;
import java.util.ArrayList;

public class SearchList {
    private ArrayList<Triple<RequestAttribute, Operation, Object>> searchRequirements;

    public SearchList(){
        this.searchRequirements = new ArrayList<>();
    }

    //TODO: DOCUMENT
    /**
     * Add a comparison to filter down the results of the selection statement. This function can be called more than once
     *
     * @param requestAttribute an Enum containing a value of every attribute of the service request table.
     *                         This will be the attribute that you are comparing to
     * @param operation an Enum containing different operations. For this function you are limited to the Operation.equalTo,
     *                  Operation.lessThan, and Operation.greaterThan values as ordering cant be used for comparison
     * @param compareValue this is an object with the value that needs to be compared with the request attribute. The
     *                     object type is limited based off of the requestAttribute value types. Here are the required types
     *                     for values of requestAttribute:
     *                     RequestAttribute.requestID: Integer
     *                     RequestAttribute.requestType: RequestType
     *                     RequestAttribute.requestStatus: RequestStatus
     *                     RequestAttribute.longName, staffUsername, itemType, requesterName, additionalNotes: String
     *                     RequestAttribute.requestDate: Timestamp
     *
     * @throws SearchException
     */
    public void addComparison(RequestAttribute requestAttribute, Operation operation, Object compareValue) throws SearchException {
        //Check for possible mistakes
        boolean passedOperationIsOrder = operation==Operation.orderByAsc || operation==Operation.orderByDesc;
        if (passedOperationIsOrder)
            throw new SearchException("Invalid Operation for Comparison");

        RequestAttribute rA = requestAttribute; //make the boolean expressions shorter
        boolean shouldBeInteger = rA==RequestAttribute.requestID;
        boolean shouldBeRequestType = rA==RequestAttribute.requestType;
        boolean shouldBeRequestStatus = rA==RequestAttribute.requestStatus;
        boolean shouldBeString = rA==RequestAttribute.longName || rA==RequestAttribute.itemType || rA==RequestAttribute.requesterName;
        boolean shouldBeStringOrNull = rA==RequestAttribute.staffUsername || rA==RequestAttribute.additionalNotes;
        boolean shouldBeTimestamp = rA==RequestAttribute.requestDate;
        boolean isInteger = compareValue instanceof Integer;
        boolean isRequestType = compareValue instanceof RequestType;
        boolean isRequestStatus = compareValue instanceof RequestStatus;
        boolean isString = compareValue instanceof String;
        boolean isTimestamp = compareValue instanceof Timestamp;
        boolean isStringOrNull = compareValue instanceof String || compareValue == null;

        if (shouldBeInteger && !isInteger) {
            assert compareValue != null;
            throw new SearchException(requestAttribute+" needs to be of type Integer but was given as type "+compareValue.getClass().toString());
        }
        if (shouldBeRequestType && !isRequestType) {
            assert compareValue != null;
            throw new SearchException(requestAttribute+" needs to be of type RequestType but was given as type "+compareValue.getClass().toString());
        }
        if (shouldBeRequestStatus && !isRequestStatus) {
            assert compareValue != null;
            throw new SearchException(requestAttribute+" needs to be of type RequestStatus but was given as type "+compareValue.getClass().toString());
        }
        if (shouldBeString && !isString) {
            assert compareValue != null;
            throw new SearchException(requestAttribute+" needs to be of type String but was given as type "+compareValue.getClass().toString());
        }
        if (shouldBeTimestamp && !isTimestamp) {
            assert compareValue != null;
            throw new SearchException(requestAttribute+" needs to be of type Timestamp but was given as type "+compareValue.getClass().toString());
        }
        if (shouldBeStringOrNull && !isStringOrNull)
            throw new SearchException(requestAttribute+" needs to be of type String or null but was given as type ");

        //input is cleared to be added
        searchRequirements.add(new Triple<RequestAttribute, Operation, Object>(requestAttribute, operation, compareValue));
    }

    /**
     * Add something to order off of for the sql query. The first addOrdering method call will define the main order and all
     * following calls will add tiebreakers
     * @param requestAttribute an Enum containing a value of every attribute of the service request table.
     *      *                  This will be the attribute that you are ordering by
     * @param operation an Enum containing different operations. For this function you are limited to the Operation.orderByDesc,
     *                  and Operation.orderByAsc values as comparisons can't be used for ordering
     * @throws SearchException
     */
    public void addOrdering(RequestAttribute requestAttribute, Operation operation) throws SearchException {
        //Check for possible mistakes
        boolean passedOperationIsOrder = operation==Operation.orderByAsc || operation==Operation.orderByDesc;
        if (!passedOperationIsOrder)
            throw new SearchException("Invalid Operation for Ordering");

        for (Triple<RequestAttribute, Operation, Object> triple : searchRequirements){
            boolean listOperationIsOrder = triple.getMiddle()==Operation.orderByAsc || triple.getMiddle()==Operation.orderByDesc;
            boolean requestAttributesEqual = requestAttribute == triple.getLeft();
            if (requestAttributesEqual && listOperationIsOrder)
                throw new SearchException("Attribute Already Exists"); //if the attribute already exists in another ORDER BY clause
        }

        //input is cleared to be added
        searchRequirements.add(new Triple<RequestAttribute, Operation, Object>(requestAttribute, operation, null)); //will ignore 3rd
    }

    ArrayList<Triple<RequestAttribute, Operation, Object>> getSearchRequirements(){
        return searchRequirements;
    }


}
