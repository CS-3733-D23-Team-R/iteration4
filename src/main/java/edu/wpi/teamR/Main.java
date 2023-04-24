package edu.wpi.teamR;

import edu.wpi.teamR.archive.Archiver;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;

import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    RequestDatabase requestDatabase = new RequestDatabase();
    MapDatabase mapDatabase = new MapDatabase();
    Archiver archiver = new Archiver(mapDatabase, requestDatabase);
    archiver.createArchive("archive.zip");
    //App.launch(App.class, args);
    // shortcut: psvm
  }
}
