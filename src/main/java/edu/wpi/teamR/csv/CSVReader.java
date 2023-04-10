package edu.wpi.teamR.csv;

import edu.wpi.teamR.mapdb.MapData;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class CSVReader<T extends MapData> {
    private final BufferedReader reader;
    private final Class<T> _class;

    public CSVReader(String fileName, Class<T> _class) throws IOException {
        this._class = _class;
        reader = new BufferedReader(new FileReader(fileName));
        reader.readLine();
    }

    public ArrayList<T> parseCSV() throws CSVParameterException, IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        ArrayList<T> data = new ArrayList<>();
        Constructor<T> c = _class.getDeclaredConstructor(String[].class);
        c.setAccessible(true);
        String line;
        String[] args;
        try {
            while ((line = reader.readLine()) != null) {
                args = line.split(",");
                data.add(c.newInstance((Object) args));
            }
            c.setAccessible(false);
        } catch(ArrayIndexOutOfBoundsException e) {
            c.setAccessible(false);
            throw new CSVParameterException();
        }
        return data;
    }
}
