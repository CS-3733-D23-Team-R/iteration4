package edu.wpi.teamR.archive;

import java.io.*;
import java.util.List;

public class CSVWriter<T extends CSVWritable> {

    public void writeCSV(String filename, List<T> data) throws IOException {
        OutputStream out = new FileOutputStream(filename);
        writeCSV(out, data);
    }

    public void writeCSV(OutputStream out, List<T> data) throws IOException {
        Writer writer = new OutputStreamWriter(out);
        writer.write(data.get(0).getCSVColumns());
        writer.write("\n");
        for (T d : data) {
            writer.write(d.toCSVEntry());
            writer.write("\n");
        }
        writer.close();
    }
}
