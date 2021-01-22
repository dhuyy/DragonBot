package com.dhuy.dragonbot;

import java.awt.AWTException;
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
import com.dhuy.dragonbot.modules.AntiLogoutThread;
import com.dhuy.dragonbot.modules.CollectItemsToSell;
import com.dhuy.dragonbot.modules.CollectItemsToSellVM;
import com.dhuy.dragonbot.modules.FoodThread;
import com.dhuy.dragonbot.modules.HealingThread;
import com.dhuy.dragonbot.modules.Hunting;
import com.dhuy.dragonbot.modules.MarketMoneyMaker;
import com.dhuy.dragonbot.modules.MarketMoneyMakerVM;
import com.dhuy.dragonbot.modules.OptimizeItemList;
import com.dhuy.dragonbot.modules.Setup;
import com.dhuy.dragonbot.modules.SpellCasterThread;
import com.dhuy.dragonbot.util.ApplicationWindow;
import com.dhuy.dragonbot.util.FileSystem;
import com.dhuy.dragonbot.util.LoggerConfigurator;
import com.dhuy.dragonbot.util.MouseCoordinates;

public class AppInitialization {
  private FileSystem fileSystem;
  private ApplicationWindow appWindow;

  private String[] modes;
  private String[] scripts;

  public AppInitialization() {
    fileSystem = new FileSystem();
    appWindow = new ApplicationWindow();

    modes = new String[] {"Create Cavebot Script", "Run Cavebot", "Run Spell Caster",
        "Market Money Maker", "Sell Items", "Optimize Item List"};
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
          Document document = builder.parse(new File(Store.CONFIGURATION_FILE_PATH));
          document.getDocumentElement().normalize();

          characterName = document.getElementsByTagName("characterName").item(0).getTextContent();
          characterLevel = Integer
              .parseInt(document.getElementsByTagName("characterLevel").item(0).getTextContent());
        } catch (Exception e) {
          log.getLogger().info(log.getMessage(this, "Leitura do arquivo de configuração falhou."));
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
        hunting.execute(true);
      }

      /**
       * [END] EXECUTE CAVEBOT MODE
       */
    } else if (chosenMode == 2) {
      /**
       * [BEGIN] EXECUTE SPELL CASTER MODE
       */

      boolean enableCavebot = false;

      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(Store.CONFIGURATION_FILE_PATH));
        document.getDocumentElement().normalize();

        store.setCharacterName(
            document.getElementsByTagName("characterName").item(0).getTextContent());
        store.setCharacterLevel((Integer
            .parseInt(document.getElementsByTagName("characterLevel").item(0).getTextContent())));
        store.setSpellCasterHotkey(
            document.getElementsByTagName("spellCasterHotkey").item(0).getTextContent());
        store.setSpellCasterInterval(Integer.parseInt(
            document.getElementsByTagName("spellCasterInterval").item(0).getTextContent()));

        enableCavebot = Boolean.parseBoolean(
            document.getElementsByTagName("spellCasterEnableCavebot").item(0).getTextContent());
      } catch (Exception e) {
        log.getLogger().info(log.getMessage(this, "Leitura do arquivo de configuração falhou."));
        System.exit(0);
      }

      appWindow.restore();

      AntiLogoutThread antiLogoutThread = new AntiLogoutThread();
      antiLogoutThread.start();

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
      }

      FoodThread foodThread = new FoodThread();
      foodThread.start();

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
      }

      SpellCasterThread spellCasterThread = new SpellCasterThread();
      spellCasterThread.start();

      if (enableCavebot) {
        int chosenScript = 0;
        chosenScript = JOptionPane.showOptionDialog(null, "Escolha o script:", "",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, scripts, null);

        if (chosenScript == -1) {
          log.getLogger().info(log.getMessage(this, "Não escolheu o script. Fechando bot..."));
          System.exit(0);
        }

        DBConnection.getInstance().open(scripts[chosenScript]);

        store.setScriptName(scripts[chosenScript]);
        store.setCharacterName(store.getCharacterName());
        log.getLogger().info(
            log.getMessage(this, "Nome do personagem: '".concat(store.getCharacterName() + "'")));
        log.getLogger().info(
            log.getMessage(this, "Carregando o script ".concat(scripts[chosenScript] + "...")));

        setup.storeWaypointsInMemory();
        store.setChosenSettings(1);

        DBConnection.getInstance().close();

        setup.execute(1);

        Hunting hunting = new Hunting();

        while (true) {
          hunting.execute(true);
        }
      }

      /**
       * [END] EXECUTE SPELL CASTER MODE
       */
    } else if (chosenMode == 3) {
      /**
       * [BEGIN] EXECUTE MARKET MONEY MAKER
       */

      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(Store.CONFIGURATION_FILE_PATH));
        document.getDocumentElement().normalize();

        store.setCharacterName(
            document.getElementsByTagName("characterName").item(0).getTextContent());
      } catch (Exception e) {
        log.getLogger().info(log.getMessage(this, "Leitura do arquivo de configuração falhou."));
        System.exit(0);
      }

      appWindow.restore();

      store.setCharacterName(store.getCharacterName());
      log.getLogger().info(
          log.getMessage(this, "Nome do personagem: '".concat(store.getCharacterName() + "'")));

      try {
        MarketMoneyMaker marketMoneyMaker = new MarketMoneyMaker();
        MarketMoneyMakerVM marketMoneyMakerVM = new MarketMoneyMakerVM();

        String[] isVM = new String[] {"Sim", "Não"};
        int chosenIsVM = JOptionPane.showOptionDialog(null, "Vai executar na VM?", "",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, isVM, null);

        if (chosenIsVM == 0) {
          store.setExecutingMMMInVM(true);

          marketMoneyMakerVM.execute();
        } else {
          marketMoneyMaker.execute();
        }
      } catch (AWTException e) {
        log.getLogger().info(log.getMessage(this, "Módulo MMM falhou. Fechando bot..."));
      }

      /**
       * [END] EXECUTE MARKET MONEY MAKER
       */
    } else if (chosenMode == 4) {
      /**
       * [BEGIN] EXECUTE SELL ITEMS
       */

      String[] questions = new String[] {"Manual", "Auto"};
      int chosenSettings =
          JOptionPane.showOptionDialog(null, "Em qual modo de configuração deseja executar?", "",
              JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, questions, null);

      store.setChosenSettings(chosenSettings);

      String[] npcType = new String[] {"Green Djinn", "Flint (Oramond)", "Rashid"};
      int npcChoise = JOptionPane.showOptionDialog(null, "Pra qual NPC deseja vender os items?", "",
          JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, npcType, null);

      if (npcChoise == 0) {
        store.setChosenSellItemsScript("GreenDjinn");
        store.setChosenSellItemsXmlFileName("xml\\ENHANCED\\Green.xml");
        MouseCoordinates.DEPOT_BOX_X = 865;
        MouseCoordinates.DEPOT_BOX_Y = 505;
      } else if (npcChoise == 1) {
        store.setChosenSellItemsScript("Oramond");
        store.setChosenSellItemsXmlFileName("xml\\ENHANCED\\Oramond_Items.xml");
        MouseCoordinates.DEPOT_BOX_X = 865;
        MouseCoordinates.DEPOT_BOX_Y = 505;
      } else if (npcChoise == 2) {
        store.setChosenSellItemsXmlFileName("xml\\ENHANCED\\Rashid.xml");

        String[] weekDay = new String[] {"Tuesday", "Wednesday", "Friday", "Saturday", "Sunday"};
        int chosenWeekDay = JOptionPane.showOptionDialog(null, "Qual o dia da semana?", "",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, weekDay, null);

        if (chosenWeekDay == 0) {
          store.setChosenSellItemsScript("RashidTuesday");
          MouseCoordinates.DEPOT_BOX_X = 865;
          MouseCoordinates.DEPOT_BOX_Y = 360;
        } else if (chosenWeekDay == 1) {
          store.setChosenSellItemsScript("RashidWednesday");
          MouseCoordinates.DEPOT_BOX_X = 800;
          MouseCoordinates.DEPOT_BOX_Y = 440;
        } else if (chosenWeekDay == 2) {
          store.setChosenSellItemsScript("RashidFriday");
          MouseCoordinates.DEPOT_BOX_X = 865;
          MouseCoordinates.DEPOT_BOX_Y = 505;
        } else if (chosenWeekDay == 3) {
          store.setChosenSellItemsScript("RashidSaturday");
          MouseCoordinates.DEPOT_BOX_X = 865;
          MouseCoordinates.DEPOT_BOX_Y = 360;
        } else if (chosenWeekDay == 4) {
          store.setChosenSellItemsScript("RashidSunday");
          MouseCoordinates.DEPOT_BOX_X = 865;
          MouseCoordinates.DEPOT_BOX_Y = 360;
        } else {
          log.getLogger()
              .info(log.getMessage(this, "Não escolheu o dia da semana. Fechando bot..."));
          System.exit(0);
        }
      } else {
        log.getLogger().info(log.getMessage(this, "Não escolheu o tipo do NPC. Fechando bot..."));
        System.exit(0);
      }

      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(Store.CONFIGURATION_FILE_PATH));
        document.getDocumentElement().normalize();

        store.setCharacterName(
            document.getElementsByTagName("characterName").item(0).getTextContent());
        store.setCharacterLevel((Integer
            .parseInt(document.getElementsByTagName("characterLevel").item(0).getTextContent())));
      } catch (Exception e) {
        log.getLogger().info(log.getMessage(this, "Leitura do arquivo de configuração falhou."));
        System.exit(0);
      }

      String currentScript = "";
      for (String script : scripts) {
        if (script.equals(store.getChosenSellItemsScript())) {
          currentScript = script;
        }
      }

      DBConnection.getInstance().open(currentScript);

      store.setScriptName(currentScript);
      log.getLogger().info(
          log.getMessage(this, "Nome do personagem: '".concat(store.getCharacterName() + "'")));
      log.getLogger()
          .info(log.getMessage(this, "Carregando o script ".concat(currentScript + "...")));

      setup.storeWaypointsInMemory();

      DBConnection.getInstance().close();

      setup.execute(chosenMode);

      HealingThread healingThread = new HealingThread();
      healingThread.start();

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
      }

      FoodThread foodThread = new FoodThread();
      foodThread.start();

      try {
        CollectItemsToSell collectItemsToSell = new CollectItemsToSell();
        CollectItemsToSellVM collectItemsToSellVM = new CollectItemsToSellVM();

        String[] isVM = new String[] {"Sim", "Não"};
        int chosenIsVM = JOptionPane.showOptionDialog(null, "Vai executar na VM?", "",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, isVM, null);

        if (chosenIsVM == 0) {
          store.setExecutingMMMInVM(true);

          collectItemsToSellVM.execute();
        } else {
          collectItemsToSell.execute();
        }

      } catch (AWTException e) {
        log.getLogger()
            .info(log.getMessage(this, "Módulo CollectItemsToSell falhou. Fechando bot..."));
      }

      /**
       * [END] EXECUTE SELL ITEMS
       */
    } else if (chosenMode == 5) {
      /**
       * [BEGIN] EXECUTE OPTIMIZE ITEM LIST
       */

      try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(Store.CONFIGURATION_FILE_PATH));
        document.getDocumentElement().normalize();

        store.setCharacterName(
            document.getElementsByTagName("characterName").item(0).getTextContent());
      } catch (Exception e) {
        log.getLogger().info(log.getMessage(this, "Leitura do arquivo de configuração falhou."));
        System.exit(0);
      }

      appWindow.restore();

      store.setCharacterName(store.getCharacterName());
      log.getLogger().info(
          log.getMessage(this, "Nome do personagem: '".concat(store.getCharacterName() + "'")));

      try {
        OptimizeItemList optimizeItemList = new OptimizeItemList();
        optimizeItemList.execute();
      } catch (AWTException e) {
        log.getLogger()
            .info(log.getMessage(this, "Módulo OptimizeItemList falhou. Fechando bot..."));
      }

      /**
       * [END] EXECUTE OPTIMIZE ITEM LIST
       */
    } else {
      log.getLogger().info(log.getMessage(this, "Não escolheu o modo. Fechando bot..."));
      System.exit(0);
    }
  }
}
