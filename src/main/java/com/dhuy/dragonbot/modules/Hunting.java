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
  private Log log;

  public Hunting() {
    cavebot = new Cavebot();
    imageProcessor = new ImageProcessor();
    keyboard = new Keyboard();
    screenshotModule = new Screenshot();
    looting = new Looting();
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

      if (monsterLifeBarPixelHex.equals(Store.BATTLE_PIXEL_HEX_WITHOUT_MONSTER_VISIBLE_COLOR)) {
        log.getLogger().info(log.getMessage(this, "Não tem monstro no battle list"));

        if (store.getIntervalToReachWaypoint() == -1) {
          cavebot.execute(enableLooting);
        } else {
          long currentTime = (System.currentTimeMillis() - store.getStartTimeWaypoint());

          if (store.getIntervalToReachWaypoint() <= currentTime) {
            cavebot.execute(enableLooting);
          }
        }
      } else {
        log.getLogger().info(log.getMessage(this, "Tem monstro no battle list"));

        if (!isThereAnyMonsterBeingAttacked(currentBattleWindow, battleWindowTitleCoord[0],
            battleWindowTitleCoord[1])) {
          log.getLogger().info(log.getMessage(this, "Atacando monstro..."));

          if (enableLooting) {
            looting.execute();
          }

          keyboard.type("ESC");
          keyboard.type("SPACE");

          store.setIntervalAttackingMonster(System.currentTimeMillis());
        } else {
          long timeAttacking = (System.currentTimeMillis() - store.getIntervalAttackingMonster());

          if (timeAttacking > Store.SECONDS_UNTIL_SKIP_ATTACKING_MONSTER) {
            log.getLogger().info(log.getMessage(this, "Mostro sendo atacado a mais de "
                + (Store.SECONDS_UNTIL_SKIP_ATTACKING_MONSTER / 1000) + " segundos"));

            keyboard.type("SPACE");

            store.setIntervalAttackingMonster(System.currentTimeMillis());
          }
        }
      }
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
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
