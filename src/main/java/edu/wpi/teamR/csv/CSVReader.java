package edu.wpi.teamR.csv;

import java.io.*;
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

    public <T extends MapData> ArrayList<?> getValues(Class<T> _class) throws NoSuchMethodException {
        _class.getMethod("constructorData").invoke();
        ArrayList<Field> fields = new ArrayList<>(Arrays.asList(_class.getFields()));
        fields.
    }
}
