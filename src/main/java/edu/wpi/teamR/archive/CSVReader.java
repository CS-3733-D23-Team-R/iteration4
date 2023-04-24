package edu.wpi.teamR.archive;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVReader {
    public <T extends Archivable> ArrayList<T> parseCSV(Class<T> _class, String filename) throws IOException, CSVParameterException, IndexOutOfBoundsException {
        InputStream in = new FileInputStream(filename);
        return parseCSV(_class, in);
    }

    public <T extends Archivable> ArrayList<T> parseCSV(Class<T> _class, InputStream in) throws CSVParameterException, IOException, IndexOutOfBoundsException {
        //System.out.println("Parsing");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ArrayList<T> data = new ArrayList<>();
        if (reader.readLine() == null)
            return data;
        Constructor<T> c;
        try {
            c = _class.getDeclaredConstructor(String[].class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        c.setAccessible(true);
        String line;
        String[] args;
        try {
            while ((line = reader.readLine()) != null) {
                args = line.split(",");
                //System.out.println(line);
                data.add(c.newInstance((Object) args));
            }
            c.setAccessible(false);
        } catch(IndexOutOfBoundsException e) {
            c.setAccessible(false);
            throw new CSVParameterException();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e ) {
            throw new RuntimeException(e);
        }
        reader.close();
        return data;
    }
}
