package edu.wpi.teamR;

import edu.wpi.teamR.archive.Archiver;
import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.UserDatabase;
import edu.wpi.teamR.mapdb.MapDatabase;
import edu.wpi.teamR.requestdb.RequestDatabase;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
    Archiver archiver = new Archiver(new MapDatabase(), new UserDatabase(), new RequestDatabase());
    archiver.createArchive();
//    new UserDatabase().addUser("admin", "admin", "","","","1234567890", new Date(System.currentTimeMillis()), AccessLevel.Admin, "", 1);
    App.launch(App.class, args);
    // shortcut: psvm
  }
}
