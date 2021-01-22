package com.dhuy.dragonbot.modules;

import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;

public class Cavebot {
  private Store store;
  private Log log;

  private CavebotActions cavebotActions;

  public Cavebot() {
    store = Store.getInstance();
    log = Log.getInstance();

    cavebotActions = new CavebotActions();
  }

  public void execute(boolean enableLooting) {
    int type = store.getWaypointList().get(store.getCurrentWaypointIndex()).getType();
    int direction = store.getWaypointList().get(store.getCurrentWaypointIndex()).getDirection();
    String phrase = store.getWaypointList().get(store.getCurrentWaypointIndex()).getPhrase();

    switch (type) {
      case 0:
        log.getLogger().info(log.getMessage(this, "Executando waypoint do tipo: WALK"));
        cavebotActions.executeWalkAction(direction, enableLooting);
        break;
      case 1:
        log.getLogger().info(log.getMessage(this, "Executando waypoint do tipo: ROPE"));
        cavebotActions.executeRopeAction();
        break;
      case 2:
        log.getLogger().info(log.getMessage(this, "Executando waypoint do tipo: SHOVEL"));
        cavebotActions.executeShovelAction(direction);
        break;
      case 3:
        log.getLogger().info(log.getMessage(this, "Executando waypoint do tipo: LADDER"));
        cavebotActions.executeLadderAction(direction);
        break;
      case 4:
        log.getLogger().info(log.getMessage(this, "Executando waypoint do tipo: HOLE/RAMP"));
        cavebotActions.executeHoleRampAction(direction);
        break;
      case 5:
        log.getLogger().info(log.getMessage(this, "Executando waypoint do tipo: TALK"));
        cavebotActions.executeTalkAction(phrase);
        break;
      case 6:
        log.getLogger()
            .info(log.getMessage(this, "Executando waypoint do tipo: SEQUENTIAL CLICKS"));
        String sequence = phrase;
        cavebotActions.executeSequencialClicksAction(sequence);
        break;
    }
  }
}
