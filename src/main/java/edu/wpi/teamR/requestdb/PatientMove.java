package edu.wpi.teamR.requestdb;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class PatientMove {
    private int patientID;
    private String longName, staffUsername;
    private Timestamp time;

    public PatientMove(int patientID, Timestamp time, String longName, String staffUsername) {
        this.patientID = patientID;
        this.longName = longName;
        this.staffUsername = staffUsername;
        this.time = time;
    }
}
