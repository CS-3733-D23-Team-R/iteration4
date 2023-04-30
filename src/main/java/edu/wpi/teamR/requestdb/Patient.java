package edu.wpi.teamR.requestdb;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Patient {
    private int patientID;
    private String patientName;

    public Patient(int patientID, String patientName) {
        this.patientID = patientID;
        this.patientName = patientName;
    }
}
