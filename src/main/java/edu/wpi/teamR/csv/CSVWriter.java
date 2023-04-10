package edu.wpi.teamR.csv;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter<T extends CSVWritable> {

    public void writeCSV(String filename, List<T> data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(data.get(0).getCSVColumns());
        writer.write("\n");
        for (T d : data) {
            writer.write(d.toCSVEntry());
            writer.write("\n");
        }
        writer.close();
    }
}
