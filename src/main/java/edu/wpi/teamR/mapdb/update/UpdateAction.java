package edu.wpi.teamR.mapdb.update;

import edu.wpi.teamR.mapdb.MapData;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public class UpdateAction {
    private final Deque<UpdateData> updates;
    private final Deque<UndoData> undos;

    UpdateAction() {
        updates = new ArrayDeque<>();
        undos = new ArrayDeque<>();
    }

    void addUpdate(Method update, Object[] updateArgs, List<? extends MapData> data) {
        updates.add(new UpdateData(update, updateArgs));
        for (MapData d : data) {
            undos.addFirst(new UndoData(d, EditType.DELETION));
        }
    }

    void addUpdate(Method update, Object[] updateArgs, MapData data, EditType editType) {
        updates.addFirst(new UpdateData(update, updateArgs));
        undos.addFirst(new UndoData(data, editType));
    }

    List<UpdateData> getUpdates() {
        return new ArrayList<>(updates);
    }

    List<UndoData> getUndoData() {
        return new ArrayList<>(undos);
    }

}
