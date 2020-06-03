package com.dhuy.dragonbot.util;

public class SQLQuery {
  public SQLQuery() {}

  public String getCreateDatabaseQuery() {
    return "CREATE TABLE IF NOT EXISTS waypoints(id INT PRIMARY KEY AUTO_INCREMENT, "
        + "type ENUM('WALK', 'ROPE', 'SHOVEL', 'LADDER', 'HOLE'), "
        + "direction ENUM('NO_DIRECTION', 'NORTH', 'EAST', 'SOUTH', 'WEST'), base_image BLOB, "
        + "goal_image BLOB)";
  }

  public String getInsertWaypointQuery() {
    return "INSERT INTO waypoints(type, direction, base_image, goal_image) VALUES(?, ?, ?, ?)";
  }

  public String getSelectAllWaypointsQuery() {
    return "SELECT * FROM waypoints";
  }
}
