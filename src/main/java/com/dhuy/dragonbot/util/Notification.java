package com.dhuy.dragonbot.util;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.logging.Level;
import com.dhuy.dragonbot.global.Log;

public class Notification {
  private Log log;

  public Notification() {
    log = Log.getInstance();
  }

  public void displayTray(String title, String message) {
    if (SystemTray.isSupported()) {
      try {
        SystemTray tray = SystemTray.getSystemTray();
        // Alternative (if the icon is on the classpath):
        // Image image =
        // Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

        TrayIcon trayIcon = new TrayIcon(image, "Tray Notification");
        // Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        // Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon");
        tray.add(trayIcon);

        trayIcon.displayMessage(title, message, MessageType.INFO);
      } catch (AWTException e) {
        log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e.getStackTrace());
      }
    } else {
      log.getLogger().info(log.getMessage(this, "SystemTray não é suportado"));
    }
  }
}
