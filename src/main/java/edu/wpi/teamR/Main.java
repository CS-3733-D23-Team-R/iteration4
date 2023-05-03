package edu.wpi.teamR;

import edu.wpi.teamR.login.AccessLevel;
import edu.wpi.teamR.login.UserDatabase;

import java.sql.Date;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    try {
      new UserDatabase().getUserByUsername("admin");
    } catch (ItemNotFoundException e) {
      new UserDatabase().addUser("admin", "admin", "","","","1234567890", new Date(System.currentTimeMillis()), AccessLevel.Admin, "", 1);
    }
//    Configuration.swapDatabase();
    App.launch(App.class, args);
    // shortcut: psvm
  }
}
