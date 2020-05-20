package com.dhuy.dragonbot.global;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.SQLQuery;

public class Database {
  private static Database instance = new Database();

  private SQLQuery sqlQuery;
  private ImageProcessor imageProcessor;
  private DBConnection dbConnection;

  private Database() {
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
    dbConnection.open(databaseName);
    try {
      dbConnection.getStatement().execute(sqlQuery.getCreateDatabaseQuery());
    } catch (SQLException e) {
      e.printStackTrace();
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
      e.printStackTrace();
    }
  }

  public ResultSet getAllWaypoints() {
    try {
      return dbConnection.getStatement().executeQuery(sqlQuery.getSelectAllWaypointsQuery());
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}
