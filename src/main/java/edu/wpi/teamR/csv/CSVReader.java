package edu.wpi.teamR.csv;

import edu.wpi.teamR.mapdb.MapData;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVReader<T, K> {
    private BufferedReader reader;
    private String[] columns;
    private String[] currentValues;

    public CSVReader(String fileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        columns = reader.readLine().split(",");
    }

    public <T extends MapData> ArrayList<T> parseCSV(Constructor<T> constructor) throws NoSuchMethodException {
        ArrayList<T> data = new ArrayList<>();
        constructor.
        while (reader.)
        return data;
    }
}
