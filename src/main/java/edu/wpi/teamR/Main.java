package edu.wpi.teamR;

import java.sql.SQLException;

public class Main {
  public Main() throws SQLException, ClassNotFoundException {
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    App.launch(App.class, args);
    // shortcut: psvm
  }
}
