package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.ImageProcessor;

public class MovementDetector {
  private static final double MOVEMENT_THRESHOLD = 0.005;
  private static final long STOPPED_TIMEOUT_MS = 1000;

  private Store store = Store.getInstance();
  private Log log = Log.getInstance();
  private ImageProcessor imageProcessor = new ImageProcessor();

  private BufferedImage previousSample;
  private long lastMovementTime;
  private boolean isFirstCall;

  public MovementDetector() {
    previousSample = null;
    lastMovementTime = System.currentTimeMillis();
    isFirstCall = true;
  }

  public boolean hasCharacterStopped(BufferedImage currentScreenshot) {
    if (isFirstCall) {
      isFirstCall = false;
      previousSample = extractMinimapSample(currentScreenshot);
      return true;
    }

    BufferedImage currentSample = extractMinimapSample(currentScreenshot);

    if (previousSample == null) {
      previousSample = currentSample;
      lastMovementTime = System.currentTimeMillis();
      return false;
    }

    double difference = imageProcessor.compareImages(previousSample, currentSample);
    previousSample = currentSample;

    if (difference > MOVEMENT_THRESHOLD) {
      lastMovementTime = System.currentTimeMillis();
      log.getLogger().info(log.getMessage(this, "Character is moving (diff: " + difference + ")"));
      return false;
    }

    long elapsed = System.currentTimeMillis() - lastMovementTime;

    if (elapsed >= STOPPED_TIMEOUT_MS) {
      log.getLogger().info(log.getMessage(this, "Character has stopped (no movement for " + elapsed + "ms)"));
      return true;
    }

    return false;
  }

  public void resetForNewWalk() {
    previousSample = null;
    lastMovementTime = System.currentTimeMillis();
  }

  private BufferedImage extractMinimapSample(BufferedImage screenshot) {
    Rectangle minimapArea = store.getMinimapArea();

    int sampleX = minimapArea.x + Store.MOVEMENT_SAMPLE_X;
    int sampleY = minimapArea.y + Store.MOVEMENT_SAMPLE_Y;

    return screenshot.getSubimage(sampleX, sampleY,
        Store.MOVEMENT_SAMPLE_SIZE, Store.MOVEMENT_SAMPLE_SIZE);
  }
}
