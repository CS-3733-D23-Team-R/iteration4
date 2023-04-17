package edu.wpi.teamR.mapdb.update;

import edu.wpi.teamR.mapdb.MapData;

import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

public class UpdateAction {
    private final List<UpdateData> updates;
    private final List<UndoData> undos;

    UpdateAction() {
        updates = new ArrayList<>();
        undos = new ArrayList<>();
    }

    void addUpdate(Method update, Object[] updateArgs, List<? extends MapData> data) {
        updates.add(new UpdateData(update, updateArgs));
        for (MapData d : data) {
            undos.add(new UndoData(d, EditType.DELETION));
        }
    }

    void addUpdate(Method update, Object[] updateArgs, MapData data, EditType editType) {
        updates.add(new UpdateData(update, updateArgs));
        undos.add(new UndoData(data, editType));
    }

    List<UpdateData> getUpdates() {
        return updates;
    }

    List<UndoData> getUndoData() {
        return undos;
    }

}
