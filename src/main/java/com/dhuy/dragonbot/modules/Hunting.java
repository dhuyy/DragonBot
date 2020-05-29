package com.dhuy.dragonbot.modules;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.Mouse;

public class Hunting {
  private Store store = Store.getInstance();

  private ImageProcessor imageProcessor;
  private Mouse mouse;
  private Screenshot screenshotModule;
  private Looting looting;
  private Log log;

  public Hunting() {
    imageProcessor = new ImageProcessor();
    mouse = new Mouse();
    screenshotModule = new Screenshot();
    looting = new Looting();
    log = Log.getInstance();
  }

  public void execute() {
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

      int monsterLifeBarPixelColor =
          currentBattleWindow.getSubimage(monsterLifeBar[0], monsterLifeBar[1], 1, 1).getRGB(0, 0);

      if (monsterLifeBarPixelColor == Store.BATTLE_PIXEL_RGB_WITHOUT_MONSTER_VISIBLE_COLOR) {
        log.getLogger().info(log.getMessage(this, "Não tem monstro no battle list"));

        if (store.getIntervalToReachWaypoint() == -1) {
          long intervalToReachWaypoint = new Cavebot().execute();

          store.setIntervalToReachWaypoint(intervalToReachWaypoint);
          store.setStartTimeWaypoint(System.currentTimeMillis());
        } else {
          long currentTime = (System.currentTimeMillis() - store.getStartTimeWaypoint());

          if (store.getIntervalToReachWaypoint() <= currentTime) {
            long intervalToReachWaypoint = new Cavebot().execute();

            store.setIntervalToReachWaypoint(intervalToReachWaypoint);
            store.setStartTimeWaypoint(System.currentTimeMillis());
          }
        }
      } else {
        log.getLogger().info(log.getMessage(this, "Tem monstro no battle list"));

        int[] monsterBeingAttacked =
            {battleWindowTitleCoord[0] + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_X,
                battleWindowTitleCoord[1] + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_Y};

        int monsterBeingAttackedPixelColor = currentBattleWindow
            .getSubimage(monsterBeingAttacked[0], monsterBeingAttacked[1], 1, 1).getRGB(0, 0);

        if (monsterBeingAttackedPixelColor != Store.BATTLE_PIXEL_RGB_MONSTER_BEING_ATTACKED_COLOR) {
          log.getLogger().info(log.getMessage(this, "Atacando monstro..."));

          looting.execute();

          // TODO Generate that 359 value based on the user monitor resolution
          mouse.clickOn(
              monsterLifeBar[0] + store.getBattleLeftSpace()
                  + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_X,
              monsterLifeBar[1] + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_Y + 376);

          mouse.move(monsterLifeBar[0] + store.getBattleLeftSpace()
              + Store.BATTLE_MATCH_PIXEL_TO_MONSTER_BEING_ATTACKED_X, monsterLifeBar[1] + 366);
        }
      }
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
    }
  }
}
