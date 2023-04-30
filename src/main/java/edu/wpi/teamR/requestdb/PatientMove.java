package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter @Setter
public class PatientMove implements Archivable {
    private int patientID;
    private String longName, staffUsername;
    private Timestamp time;

    public PatientMove(int patientID, Timestamp time, String longName, String staffUsername) {
        this.patientID = patientID;
        this.longName = longName;
        this.staffUsername = staffUsername;
        this.time = time;
    }

    private PatientMove(String[] args) {
        this(Integer.parseInt(args[0]), Timestamp.valueOf(args[1]), args[2], args[3]);
    }

    @Override
    public String[] toCSVEntry() {
        return new String[]{String.valueOf(patientID), time.toString(), longName, staffUsername};
    }

    @Override
    public String getCSVColumns() {
        return "patientID,time,longName,staffUsername";
    }
}
