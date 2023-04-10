package edu.wpi.teamR.csv;

public interface CSVWritable {
    /**
     *
     * @return String with each of the fields to write, in order that they appear in the constructor
     */
    String toCSVEntry();
    String getCSVColumns();
}
