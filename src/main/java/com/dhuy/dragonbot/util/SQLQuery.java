package com.dhuy.dragonbot.util;

public class SQLQuery {
  public SQLQuery() {}

  public String getCreateDatabaseQuery() {
    return "CREATE TABLE IF NOT EXISTS waypoints(id INT PRIMARY KEY AUTO_INCREMENT, "
        + "type ENUM('WALK', 'ROPE', 'SHOVEL', 'LADDER', 'HOLE', 'TALK', 'SEQUENTIAL_CLICKS'), "
        + "direction ENUM('NO_DIRECTION', 'NORTH', 'EAST', 'SOUTH', 'WEST'), base_image BLOB, "
        + "goal_image BLOB, phrase VARCHAR(255))";
  }

  public String getInsertWaypointQuery() {
    return "INSERT INTO waypoints(type, direction, base_image, goal_image, phrase) VALUES(?, ?, ?, ?, ?)";
  }

  public String getSelectAllWaypointsQuery() {
    return "SELECT * FROM waypoints";
    // return "SELECT * FROM waypoints WHERE ID > 51";
  }
}
