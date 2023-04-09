package edu.wpi.teamR.mapdb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

public interface MapData {
    // Class<?>[] dataTypes() throws NoSuchMethodException;
    Constructor<?> getStringConstructor() throws NoSuchMethodException;
}
