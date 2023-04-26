package edu.wpi.teamR.archive;

public class CSVParameterException extends Exception {
    CSVParameterException() {
        super("File does not contain enough rows for the constructor");
    }
}
