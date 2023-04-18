package edu.wpi.teamR.mapdb.update;

import edu.wpi.teamR.mapdb.MapData;

public record UndoData(MapData data, EditType editType) {
}
