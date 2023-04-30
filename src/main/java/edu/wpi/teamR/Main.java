package edu.wpi.teamR;

import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.UserDatabase;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
    new UserDatabase().addUser("admin", "admin", "","","","1234567890", new Date(System.currentTimeMillis()), AccessLevel.Admin, "", 1);
    App.launch(App.class, args);
    // shortcut: psvm
  }
}
