package com.dhuy.dragonbot.util;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;

public class Notification {
  public Notification() {
    Notify.IMAGE_PATH = "images";
    Notify.TITLE_TEXT_FONT = "Source Code Pro BOLD 14";
    Notify.MAIN_TEXT_FONT = "Source Code Pro BOLD 12";
    Notify.MOVE_DURATION = 0.3F;
  }

  public void instantlyShow(String message) {
    Notify.create().title("DragonBot").text(message).darkStyle().position(Pos.TOP_LEFT)
        .showInformation();
  }

  public void show(String message, int hideAfter) {
    Notify.create().title("DragonBot").text(message).hideAfter(hideAfter).darkStyle()
        .position(Pos.TOP_LEFT).showInformation();
  }
}
