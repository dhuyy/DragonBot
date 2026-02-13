package com.dhuy.dragonbot.modules;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.Keyboard;

public class Hunting {
  private Store store = Store.getInstance();

  private Cavebot cavebot;
  private ImageProcessor imageProcessor;
  private Keyboard keyboard;
  private Screenshot screenshotModule;
  private Looting looting;
  private MovementDetector movementDetector;
  private Log log;
  private boolean wasAttackingMonster = false;
  private long monsterEngageSince = 0;
  private static final long STUCK_MONSTER_TIMEOUT = 20000;

  public Hunting() {
    cavebot = new Cavebot();
    imageProcessor = new ImageProcessor();
    keyboard = new Keyboard();
    screenshotModule = new Screenshot();
    looting = new Looting();
    movementDetector = new MovementDetector();
    store.setMovementDetector(movementDetector);
    log = Log.getInstance();
  }

  public void execute(boolean enableLooting) {
    try {
      Rectangle battleWindowArea = store.getBattleWindowArea();

      BufferedImage currentScreenshot = screenshotModule.execute(this);

      BufferedImage currentBattleWindow = currentScreenshot.getSubimage(battleWindowArea.x,
          battleWindowArea.y, battleWindowArea.width, battleWindowArea.height);
      BufferedImage battleListCrop = store.getBattleListCrop();

      int[] battleWindowTitleCoord =
          imageProcessor.findSubimage(currentBattleWindow, battleListCrop);

      int[] monsterLifeBar =
          {battleWindowTitleCoord[0] + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_LIFE_BAR_X,
              battleWindowTitleCoord[1] + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_LIFE_BAR_Y};

      String monsterLifeBarPixelHex = imageProcessor.getHexFromColor(new Color(currentBattleWindow
          .getSubimage(monsterLifeBar[0], monsterLifeBar[1], 1, 1).getRGB(0, 0)));

      boolean noMonstersVisible =
          monsterLifeBarPixelHex.equals(Store.BATTLE_PIXEL_HEX_WITHOUT_MONSTER_VISIBLE_COLOR);

      if (noMonstersVisible) {
        if (wasAttackingMonster && enableLooting) {
          log.getLogger().info(log.getMessage(this, "Monstro morreu, lootando..."));
          looting.execute();
        }
        wasAttackingMonster = false;
        monsterEngageSince = 0;

        log.getLogger().info(log.getMessage(this, "Não tem monstro no battle list"));

        if (movementDetector.hasCharacterStopped(currentScreenshot)) {
          cavebot.execute(enableLooting);
        }
      } else {
        if (monsterEngageSince == 0) {
          monsterEngageSince = System.currentTimeMillis();
        }

        long timeEngaged = System.currentTimeMillis() - monsterEngageSince;
        boolean stuckMonster = timeEngaged > STUCK_MONSTER_TIMEOUT;

        if (stuckMonster) {
          log.getLogger().info(log.getMessage(this,
              "Monstro preso no battle, ignorando e continuando waypoints..."));
          wasAttackingMonster = false;
          monsterEngageSince = System.currentTimeMillis();

          if (movementDetector.hasCharacterStopped(currentScreenshot)) {
            cavebot.execute(enableLooting);
          }
        } else {
          log.getLogger().info(log.getMessage(this, "Tem monstro no battle list"));
          movementDetector.resetForNewWalk();

          if (!isThereAnyMonsterBeingAttacked(currentBattleWindow, battleWindowTitleCoord[0],
              battleWindowTitleCoord[1])) {
            if (wasAttackingMonster && enableLooting) {
              log.getLogger().info(log.getMessage(this, "Monstro morreu, lootando..."));
              looting.execute();
              wasAttackingMonster = false;
              monsterEngageSince = System.currentTimeMillis();
              return;
            }

            log.getLogger().info(log.getMessage(this, "Atacando monstro..."));

            keyboard.type("ESC");
            Thread.sleep(100);
            keyboard.type("SPACE");
          } else {
            wasAttackingMonster = true;
          }
        }
      }
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, e.getMessage()), e);
    }
  }

  private boolean isThereAnyMonsterBeingAttacked(BufferedImage currentBattleWindow,
      int battleWindowBaseX, int battleWindowBaseY) {
    boolean hasMonsterBeingAttacked = false;

    for (int i = 0; i < Store.AMOUNT_MONSTERS_VISIBLE_IN_BATTLE; i++) {
      int[] monsterBeingAttacked =
          {battleWindowBaseX + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_X,
              battleWindowBaseY + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_Y
                  + (Store.DISTANCE_BETWEEN_MONSTER_BATTLE_SQUARE * i)};

      String monsterSquarePixelHex = imageProcessor.getHexFromColor(new Color(currentBattleWindow
          .getSubimage(monsterBeingAttacked[0], monsterBeingAttacked[1], 1, 1).getRGB(0, 0)));

      if (monsterSquarePixelHex.equals(Store.BATTLE_PIXEL_HEX_MONSTER_BEING_ATTACKED_COLOR_1)
          || monsterSquarePixelHex.equals(Store.BATTLE_PIXEL_HEX_MONSTER_BEING_ATTACKED_COLOR_2)) {
        hasMonsterBeingAttacked = true;
      }
    }

    if (hasMonsterBeingAttacked) {
      log.getLogger().info(log.getMessage(this, "Há algum monstro sendo atacado..."));
    } else {
      log.getLogger().info(log.getMessage(this, "Não há nenhum monstro sendo atacado..."));
    }

    return hasMonsterBeingAttacked;
  }
}
