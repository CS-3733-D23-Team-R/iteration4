package edu.wpi.teamR.mapdb;

import edu.wpi.teamR.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;

class MapUpdaterTest {
    static MapDatabase mapdb;

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        Configuration.changeSchemaName("test");
        mapdb = new MapDatabase();
    }

    @Test
    void submitUpdates() throws SQLException, ClassNotFoundException {
    }

    @Test
    void undo() {
    }

    @Test
    void addNode() {
        MapUpdater mapUpdater = new MapUpdater(mapdb);
        Node i = mapUpdater.addNode(0, 0, "e", "e2");
        Deque<UndoData> data = mapUpdater.undo();
        UndoData undo = data.getFirst();
        Node f = (Node)undo.data();
        assertSame(i, f);
    }

    @Test
    void modifyCoords() {
    }

    @Test
    void deleteNode() {
    }

    @Test
    void addEdge() {
    }

    @Test
    void deleteEdge() {
    }

    @Test
    void deleteEdgesByNode() {
    }

    @Test
    void addMove() {
    }

    @Test
    void deleteMovesByNode() {
    }

    @Test
    void deleteMovesByLocationName() {
    }

    @Test
    void modifyLocationNameType() {
    }

    @Test
    void modifyLocationNameShortName() {
    }

    @Test
    void addLocationName() {
    }

    @Test
    void deleteLocationName() {
    }
}