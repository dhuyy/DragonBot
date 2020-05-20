package com.dhuy.dragonbot.util;

public class SQLQuery {
  public SQLQuery() {}

  public String getCreateDatabaseQuery() {
    return "CREATE TABLE IF NOT EXISTS waypoints"
        + "(id INT PRIMARY KEY AUTO_INCREMENT, base_image BLOB, goal_image BLOB)";
  }

  public String getInsertWaypointQuery() {
    return "INSERT INTO waypoints(base_image, goal_image) VALUES(?, ?)";
  }

  public String getSelectAllWaypointsQuery() {
    return "SELECT * FROM waypoints";
  }
}
