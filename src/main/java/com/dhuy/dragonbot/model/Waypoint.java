package com.dhuy.dragonbot.model;

import java.awt.image.BufferedImage;

public class Waypoint {
  private int id;
  /**
   * WALK: 0 | ROPE: 1 | SHOVEL: 2 | LADDER: 3 | HOLE_RAMP: 4 | TALK: 5 | SEQUENTIAL_CLICKS: 6
   */
  private int type;
  /**
   * NO_DIRECTION: 0 | NORTH: 1 | EAST: 2 | SOUTH: 3 | WEST: 4
   */
  private int direction;
  private BufferedImage baseImage;
  private BufferedImage goalImage;
  private String phrase;

  public Waypoint(int id, int type, int direction, BufferedImage baseImage, BufferedImage goalImage,
      String phrase) {
    this.id = id;
    this.type = type;
    this.direction = direction;
    this.baseImage = baseImage;
    this.goalImage = goalImage;
    this.phrase = phrase;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getDirection() {
    return direction;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public BufferedImage getBaseImage() {
    return baseImage;
  }

  public void setBaseImage(BufferedImage baseImage) {
    this.baseImage = baseImage;
  }

  public BufferedImage getGoalImage() {
    return goalImage;
  }

  public void setGoalImage(BufferedImage goalImage) {
    this.goalImage = goalImage;
  }

  public String getPhrase() {
    return phrase;
  }

  public void setPhrase(String phrase) {
    this.phrase = phrase;
  }
}
