package com.dhuy.dragonbot.global;

import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class MouseHook {
  private static final MouseHook instance = new MouseHook();

  private GlobalMouseHook globalMouseHook;

  private MouseHook() {
    globalMouseHook = new GlobalMouseHook();
  }

  public static MouseHook getInstance() {
    return instance;
  }

  public void register(GlobalMouseAdapter mouseAdapter) {
    globalMouseHook.addMouseListener(mouseAdapter);
  }

  public void unregister(GlobalMouseAdapter mouseAdapter) {
    globalMouseHook.removeMouseListener(mouseAdapter);
  }

  public GlobalMouseAdapter getPrintCoordinatesHook() {
    return new GlobalMouseAdapter() {
      @Override
      public void mousePressed(GlobalMouseEvent event) {
        if (event.getButtons() == GlobalMouseEvent.BUTTON_LEFT) {
          System.out.println(event.getX());
          System.out.println(event.getY());
        }
      }
    };
  }
}
