package com.dhuy.dragonbot;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import com.dhuy.dragonbot.global.DBConnection;
import com.dhuy.dragonbot.global.Database;
import com.dhuy.dragonbot.global.KeyboardHook;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.modules.CavebotTask;
import com.dhuy.dragonbot.modules.Setup;

public class App {
  private static final String DRAGONBOT_SCRIPT_NAME = "thais";
  private static final String TIBIA_SCREENSHOT_RELATIVE_DIRECTORY = "Django Enforced";

  public static void main(String[] args) {
    Object[] options = new Object[] {"Criar Script", "Executar Bot"};
    int modeChosen = JOptionPane.showOptionDialog(null, "Em qual modo deseja iniciar?", "",
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    DBConnection.getInstance().open(DRAGONBOT_SCRIPT_NAME);

    Store store = Store.getInstance();
    store.setScriptName(DRAGONBOT_SCRIPT_NAME);
    store.setCharacterName(TIBIA_SCREENSHOT_RELATIVE_DIRECTORY);

    try {
      store.setMinimapCross(ImageIO.read(new File(Store.MINIMAP_CROSS_ZOOM_4X_PATH)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    KeyboardHook keyboardHook = KeyboardHook.getInstance();
    keyboardHook.register(keyboardHook.getPauseAppHook());

    if (modeChosen == 0) {
      Database.getInstance().createDatabase(DRAGONBOT_SCRIPT_NAME);

      new Setup().execute();
    } else if (modeChosen == 1) {
      Database.getInstance().createDatabase(DRAGONBOT_SCRIPT_NAME);

      new Setup().execute();

      ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
      threadPoolExecutor.submit(new CavebotTask());
      threadPoolExecutor.shutdown();
    } else {
      System.exit(0);
    }
  }
}
