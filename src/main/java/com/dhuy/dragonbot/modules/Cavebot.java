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

  public void execute() {
    int type = store.getWaypointList().get(store.getCurrentWaypointIndex()).getType();
    int direction = store.getWaypointList().get(store.getCurrentWaypointIndex()).getDirection();

    switch (type) {
      case 0:
        log.getLogger().info(log.getMessage(this, "Executando waypoint do tipo: WALK"));
        cavebotActions.executeWalkAction(direction);
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
    }
  }
}
