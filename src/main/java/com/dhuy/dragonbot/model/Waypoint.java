package com.dhuy.dragonbot.model;

import java.awt.image.BufferedImage;

public class Waypoint {
  private int id;
  private BufferedImage baseImage;
  private BufferedImage goalImage;

  public Waypoint(int id, BufferedImage baseImage, BufferedImage goalImage) {
    this.id = id;
    this.baseImage = baseImage;
    this.goalImage = goalImage;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
}
