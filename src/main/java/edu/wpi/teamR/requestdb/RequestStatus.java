package edu.wpi.teamR.requestdb;

public class RequestStatus {
    enum StatusEnum {
        Unstarted,
        Processing,
        Done
    }

    StatusEnum status = StatusEnum.Unstarted;

    public RequestStatus(StatusEnum status) {
        this.status = status;
    }
}
