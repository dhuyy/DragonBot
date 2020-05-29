package com.dhuy.dragonbot.global;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.SQLQuery;

public class Database {
  private static Database instance = new Database();

  private Log log;
  private SQLQuery sqlQuery;
  private ImageProcessor imageProcessor;
  private DBConnection dbConnection;

  private Database() {
    log = Log.getInstance();
    sqlQuery = new SQLQuery();
    imageProcessor = new ImageProcessor();

    dbConnection = DBConnection.getInstance();
  }

  public static Database getInstance() {
    if (instance == null) {
      synchronized (Database.class) {
        if (instance == null) {
          instance = new Database();
        }
      }
    }

    return instance;
  }

  public void createDatabase(String databaseName) {
    try {
      dbConnection.open(databaseName);
      dbConnection.getStatement().execute(sqlQuery.getCreateDatabaseQuery());
    } catch (SQLException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }

  public void insertWaypoint(BufferedImage baseImage, BufferedImage goalImage) {
    try {
      PreparedStatement preparedStatement = DBConnection.getInstance().getConnection()
          .prepareStatement(sqlQuery.getInsertWaypointQuery());

      InputStream baseImageBinary = imageProcessor.getInputStreamFromBufferedImage(baseImage);
      InputStream goalImageBinary = imageProcessor.getInputStreamFromBufferedImage(goalImage);

      preparedStatement.setBinaryStream(1, baseImageBinary);
      preparedStatement.setBinaryStream(2, goalImageBinary);

      preparedStatement.execute();
    } catch (SQLException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }

  public ResultSet getAllWaypoints() {
    try {
      return dbConnection.getStatement().executeQuery(sqlQuery.getSelectAllWaypointsQuery());
    } catch (SQLException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }

    return null;
  }
}
