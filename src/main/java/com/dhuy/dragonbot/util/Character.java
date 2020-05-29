package com.dhuy.dragonbot.util;

import com.dhuy.dragonbot.global.Store;

public class Character {
  public Character() {}

  public long getWalkingSpeedInMilliseconds(int tileSpeedWalking, double distanceToReachWaypoint) {
    double characterBaseSpeed =
        Store.CHARACTER_SPEED_BASE + (2 * (Store.getInstance().getCharacterLevel() - 1));
    double sqmPerSecond = tileSpeedWalking / characterBaseSpeed;
    double walkingSpeed = sqmPerSecond * distanceToReachWaypoint;

    return (long) (walkingSpeed * 1000);
  }
}
