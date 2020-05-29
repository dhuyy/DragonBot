package com.dhuy.dragonbot;

import java.util.logging.Logger;
import javax.swing.JOptionPane;
import com.dhuy.dragonbot.global.DBConnection;
import com.dhuy.dragonbot.global.Database;
import com.dhuy.dragonbot.global.KeyboardHook;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.modules.Hunting;
import com.dhuy.dragonbot.modules.Setup;
import com.dhuy.dragonbot.util.FileSystem;
import com.dhuy.dragonbot.util.LoggerConfigurator;

public class AppInitialization {
  private FileSystem fileSystem;

  private String[] modes;
  private String[] scripts;

  public AppInitialization() {
    fileSystem = new FileSystem();

    modes = new String[] {"Criar Script", "Executar Cavebot"};
    scripts = fileSystem.getScriptFiles();
  }

  public void execute() {
    LoggerConfigurator loggerConfigurator = new LoggerConfigurator();
    loggerConfigurator.initialize();

    Setup setup = new Setup();
    Log log = Log.getInstance();
    Store store = Store.getInstance();

    KeyboardHook keyboardHook = KeyboardHook.getInstance();
    keyboardHook.register(keyboardHook.getExitAppHook());

    int chosenMode = JOptionPane.showOptionDialog(null, "Em qual modo deseja iniciar?", "",
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modes, null);

    if (chosenMode == 0) {
      /**
       * [BEGIN] CREATE SCRIPT MODE
       */

      String scriptName = JOptionPane.showInputDialog("Qual o título do script?");
      if (scriptName == null) {
        Logger.getLogger("global")
            .info(log.getMessage(this, "Não escolheu o título do script. Fechando bot..."));
        System.exit(0);
      }

      String characterName = JOptionPane.showInputDialog("Qual o nome do personagem?");
      if (characterName == null) {
        log.getLogger().info(log.getMessage(this, "Nome de personagem inválido. Fechando bot..."));
        System.exit(0);
      }

      Database.getInstance().createDatabase(scriptName);

      store.setScriptName(scriptName);
      store.setCharacterName(characterName);
      log.getLogger()
          .info(log.getMessage(this, "Nome do personagem: '".concat(characterName + "'")));
      log.getLogger().info(log.getMessage(this, "Carregando o script ".concat(scriptName + "...")));

      setup.execute();

      /**
       * [END] CREATE SCRIPT MODE
       */
    } else if (chosenMode == 1) {
      /**
       * [BEGIN] EXECUTE CAVEBOT MODE
       */

      int chosenScript = JOptionPane.showOptionDialog(null, "Escolha o script:", "",
          JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, scripts, null);
      if (chosenScript == -1) {
        log.getLogger().info(log.getMessage(this, "Não escolheu o script. Fechando bot..."));
        System.exit(0);
      }

      String characterName = JOptionPane.showInputDialog("Qual o nome do personagem?");
      if (characterName == null) {
        log.getLogger().info(log.getMessage(this, "Nome de personagem inválido. Fechando bot..."));
        System.exit(0);
      }

      DBConnection.getInstance().open(scripts[chosenScript]);

      store.setScriptName(scripts[chosenScript]);
      store.setCharacterName(characterName);
      log.getLogger()
          .info(log.getMessage(this, "Nome do personagem: '".concat(characterName + "'")));
      log.getLogger()
          .info(log.getMessage(this, "Carregando o script ".concat(scripts[chosenScript] + "...")));

      setup.storeWaypointsInMemory();

      DBConnection.getInstance().close();

      setup.execute();

      Hunting hunting = new Hunting();
      while (true) {
        hunting.execute();
      }

      /**
       * [END] EXECUTE CAVEBOT MODE
       */
    } else {
      log.getLogger().info(log.getMessage(this, "Não escolheu o modo. Fechando bot..."));
      System.exit(0);
    }
  }
}
