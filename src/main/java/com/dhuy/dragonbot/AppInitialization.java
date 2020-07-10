package com.dhuy.dragonbot;

import java.io.File;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import com.dhuy.dragonbot.global.DBConnection;
import com.dhuy.dragonbot.global.Database;
import com.dhuy.dragonbot.global.KeyboardHook;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.modules.FoodThread;
import com.dhuy.dragonbot.modules.HealingThread;
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
        log.getLogger()
            .info(log.getMessage(this, "Não escolheu o título do script. Fechando bot..."));
        System.exit(0);
      }

      String characterName = JOptionPane.showInputDialog("Qual o nome do personagem?");
      if (characterName == null) {
        log.getLogger().info(log.getMessage(this, "Nome de personagem inválido. Fechando bot..."));
        System.exit(0);
      }

      // String scriptName = "TEST";
      // String characterName = "Raul Porcino";

      Database.getInstance().createDatabase(scriptName);

      store.setScriptName(scriptName);
      store.setCharacterName(characterName);
      log.getLogger()
          .info(log.getMessage(this, "Nome do personagem: '".concat(characterName + "'")));
      log.getLogger().info(log.getMessage(this, "Carregando o script ".concat(scriptName + "...")));

      setup.execute(chosenMode);

      /**
       * [END] CREATE SCRIPT MODE
       */
    } else if (chosenMode == 1) {
      /**
       * [BEGIN] EXECUTE CAVEBOT MODE
       */

      String[] questions = new String[] {"Manual", "Auto"};
      int chosenSettings =
          JOptionPane.showOptionDialog(null, "Em qual modo de configuração deseja executar?", "",
              JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, questions, null);

      store.setChosenSettings(chosenSettings);

      String characterName = "";
      int characterLevel = 0;

      int chosenScript = 0;
      chosenScript = JOptionPane.showOptionDialog(null, "Escolha o script:", "",
          JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, scripts, null);

      if (chosenScript == -1) {
        log.getLogger().info(log.getMessage(this, "Não escolheu o script. Fechando bot..."));
        System.exit(0);
      }

      if (chosenSettings == 0) {
        characterName = JOptionPane.showInputDialog("Qual o nome do personagem?");
        if (characterName == null) {
          log.getLogger()
              .info(log.getMessage(this, "Não escolheu o nome do personagem. Fechando bot..."));
          System.exit(0);
        }

        try {
          characterLevel =
              Integer.parseInt(JOptionPane.showInputDialog("Qual o level do personagem?"));

          store.setCharacterLevel(characterLevel);
        } catch (NumberFormatException e) {
          log.getLogger()
              .info(log.getMessage(this, "Level do personagem inválido. Fechando bot..."));
          System.exit(0);
        }
      } else if (chosenSettings == 1) {
        try {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          Document document = builder.parse(new File("character.xml"));
          document.getDocumentElement().normalize();

          characterName = document.getElementsByTagName("name").item(0).getTextContent();
          characterLevel =
              Integer.parseInt(document.getElementsByTagName("level").item(0).getTextContent());
        } catch (Exception e) {
          log.getLogger().info(log.getMessage(this, "Leitura do XML falhou."));
          System.exit(0);
        }
      } else {
        log.getLogger()
            .info(log.getMessage(this, "Não escolheu o título do script. Fechando bot..."));
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

      setup.execute(chosenMode);

      HealingThread healingThread = new HealingThread();
      FoodThread foodThread = new FoodThread();
      Hunting hunting = new Hunting();

      healingThread.start();

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
      }

      foodThread.start();

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
