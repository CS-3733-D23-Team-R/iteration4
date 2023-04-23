package edu.wpi.teamR.archive;

import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;

public class Archiver {
    private MapDatabase mapdb;
    private RequestDatabase requestdb;

    public Archiver(MapDatabase mapDatabase, RequestDatabase requestDatabase) {
        mapdb = mapDatabase;
        requestdb = requestDatabase;
    }

    public void createArchive(String archivePath) {

    }

    public void restoreArchive(String archivePath) {

    }
}
