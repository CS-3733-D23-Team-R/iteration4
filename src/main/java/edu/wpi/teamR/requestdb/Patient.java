package edu.wpi.teamR.requestdb;

import edu.wpi.teamR.archive.Archivable;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Patient implements Archivable {
    private int patientID;
    private String patientName;

    public Patient(int patientID, String patientName) {
        this.patientID = patientID;
        this.patientName = patientName;
    }

    private Patient(String[] args) {
        this(Integer.parseInt(args[0]), args[1]);
    }

    @Override
    public String[] toCSVEntry() {
        return new String[]{String.valueOf(patientID), patientName};
    }

    @Override
    public String getCSVColumns() {
        return "patientID,patientName";
    }
}
