package edu.wpi.teamR.mapdb;

import java.lang.reflect.Method;
import java.util.*;

public class UpdateAction {
    private final List<Pair<Method, Object[]>> updates;
    private final Deque<UndoData> undos;

    UpdateAction() {
        updates = new ArrayList<>();
        undos = new ArrayDeque<>();
    }

    void addUpdate(Method update, Object[] updateArgs, List<? extends MapData> data) {
        updates.add(new Pair<>(update, updateArgs));
        for (MapData d : data) {
            undos.addFirst(new UndoData(d, EditType.DELETION));
        }
    }

    void addUpdate(Method update, Object[] updateArgs, MapData data, EditType editType) {
        updates.add(new Pair<>(update, updateArgs));
        undos.addFirst(new UndoData(data, editType));
    }

    List<Pair<Method, Object[]>> getUpdates() {
        return updates;
    }

    Deque<UndoData> getUndoData() {
        return undos;
    }

}
