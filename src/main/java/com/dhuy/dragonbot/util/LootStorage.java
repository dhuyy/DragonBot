package com.dhuy.dragonbot.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.modules.Screenshot;

public class LootStorage {
  // It works only on 1920x1080 screens
  private final Rectangle lootQuantityArea = new Rectangle(1770, 520, 19, 15);
  public static final String PROTECTIVE_CHARM = "protectiveCharm";
  public static final String MEDICINE_POUCH = "medicinePouch";
  public static final String HONEYCOMB = "honeycomb";

  private Log log;
  private ImageProcessor imageProcessor;
  private Screenshot screenshotModule;

  private BufferedImage protectiveCharmCacheImage = null;
  private BufferedImage medicinePouchCacheImage = null;
  private BufferedImage honeycombCacheImage = null;

  public LootStorage() {
    log = Log.getInstance();
    imageProcessor = new ImageProcessor();
    screenshotModule = new Screenshot();
  }

  public boolean hasReachedLootLimit(String loot) {
    BufferedImage systemLootLimit = null;

    try {
      switch (loot) {
        case PROTECTIVE_CHARM:
          if (protectiveCharmCacheImage != null) {
            systemLootLimit = ImageIO.read(new File(Store.PROTECTIVE_CHARM_LIMIT_PATH));
            protectiveCharmCacheImage = systemLootLimit;
          } else {
            systemLootLimit = protectiveCharmCacheImage;
          }
          break;

        case MEDICINE_POUCH:
          if (medicinePouchCacheImage != null) {
            systemLootLimit = ImageIO.read(new File(Store.MEDICINE_POUCH_LIMIT_PATH));
            medicinePouchCacheImage = systemLootLimit;
          } else {
            systemLootLimit = medicinePouchCacheImage;
          }
          break;

        case HONEYCOMB:
          if (honeycombCacheImage != null) {
            systemLootLimit = ImageIO.read(new File(Store.HONEYCOMB_LIMIT_PATH));
            honeycombCacheImage = systemLootLimit;
          } else {
            systemLootLimit = honeycombCacheImage;
          }
          break;
      }
    } catch (IOException e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }

    BufferedImage currentScreenshot = screenshotModule.execute(this);
    BufferedImage rawLootQuantityImage = currentScreenshot.getSubimage(lootQuantityArea.x,
        lootQuantityArea.y, lootQuantityArea.width, lootQuantityArea.height);

    int[] currentLootQuantityCoord =
        imageProcessor.findSubimage(rawLootQuantityImage, systemLootLimit);
    BufferedImage currentLootQuantity =
        rawLootQuantityImage.getSubimage(currentLootQuantityCoord[0], currentLootQuantityCoord[1],
            Store.CREATURE_PRODUCT_LIMIT_BLOCK_IMAGE_WIDTH,
            Store.CREATURE_PRODUCT_LIMIT_BLOCK_IMAGE_HEIGHT);

    double isCurrentLootQuantityReachedLimit =
        imageProcessor.compareImages(currentLootQuantity, systemLootLimit);

    System.out.println(isCurrentLootQuantityReachedLimit);

    return isCurrentLootQuantityReachedLimit == 0;
  }
}
