package edu.wpi.teamR.csv;

public class CSVParameterException extends Exception {
    CSVParameterException() {
        super("File does not contain enough rows for the constructor");
    }
}
