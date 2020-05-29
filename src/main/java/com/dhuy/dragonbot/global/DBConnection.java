package com.dhuy.dragonbot.global;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class DBConnection {
  private static DBConnection instance = new DBConnection();

  private static final String JDBC_DRIVER = "org.h2.Driver";
  private static final String DB_URL = "jdbc:h2:./scripts/";

  private static final String USER = "dragonbot";
  private static final String PASS = "";

  private static final String TRACE_LEVEL_FILE = ";TRACE_LEVEL_FILE=0";

  private Log log;
  private Connection connection = null;
  private Statement statement = null;

  private DBConnection() {
    log = Log.getInstance();

    try {
      Class.forName(JDBC_DRIVER);
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }

  public static DBConnection getInstance() {
    if (instance == null) {
      synchronized (DBConnection.class) {
        if (instance == null) {
          instance = new DBConnection();
        }
      }
    }

    return instance;
  }

  public void open(String databaseName) {
    try {
      log.getLogger().info(log.getMessage(this, "Connecting to database..."));
      connection = DriverManager.getConnection(DB_URL.concat(databaseName).concat(TRACE_LEVEL_FILE),
          USER, PASS);

      log.getLogger().info(log.getMessage(this, "Creating database statement..."));
      statement = connection.createStatement();
    } catch (SQLException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }

  public void close() {
    try {
      log.getLogger().info(log.getMessage(this, "Closing database statement..."));
      statement.close();

      log.getLogger().info(log.getMessage(this, "Closing database connection..."));
      connection.close();
    } catch (SQLException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public Statement getStatement() {
    return statement;
  }
}
