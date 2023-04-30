package edu.wpi.teamR.archive;

import java.io.*;
import java.util.List;

public class CSVWriter {

    public void writeCSV(String filename, List<? extends Archivable> data) throws IOException {
        OutputStream out = new FileOutputStream(filename);
        writeCSV(out, data, "|");
        out.close();
    }

    public void writeCSV(OutputStream out, List<? extends Archivable> data) throws IOException {
        writeCSV(out, data, "|");
    }

    public void writeCSV(OutputStream out, List<? extends Archivable> data, String delimiter) throws IOException {
        if (data.size() == 0)
            return;
        Writer writer = new BufferedWriter(new OutputStreamWriter(out));
        String columns = data.get(0).getCSVColumns();
        if (!delimiter.equals(","))
            columns = String.join(delimiter, columns.split(","));
        writer.write(columns);
        writer.write("\n");
        for (Archivable d : data) {
            String[] entry = d.toCSVEntry();
            writer.write(String.join(delimiter, entry));
            writer.write("\n");
        }
        writer.flush();
    }
}
