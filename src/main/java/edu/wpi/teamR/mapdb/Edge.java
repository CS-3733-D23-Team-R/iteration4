package edu.wpi.teamR.mapdb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Edge implements MapData {
    private int startNode, endNode;

    Edge(int startNode, int endNode){
        this.startNode = startNode;
        this.endNode = endNode;
    }

    Edge(String startNode, String endNode) {
        this(Integer.parseInt(startNode), Integer.parseInt(endNode));
    }


    @Override
    public Constructor<?> getStringConstructor() throws NoSuchMethodException {
        return this.getClass().getDeclaredConstructor(String.class, String.class);
    }
}
