package edu.wpi.teamR.mapdb;

import lombok.Getter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Getter
public class UpdateAction {
    private ArrayList<Pair<MapData, EditType>> updates;

    UpdateAction() {
        updates = new ArrayList<>();
    }

    void addUpdate(MapData data, EditType editType) {
        updates.add(new Pair<>(data, editType));
    }

    void addUpdates(Collection<? extends MapData> data, EditType editType) {
        for (MapData d : data) {
            updates.add(new Pair<>(d, editType));
        }
    }

    Collection<Pair<MapData, EditType>> getUpdates() {
        return updates;
    }
}
