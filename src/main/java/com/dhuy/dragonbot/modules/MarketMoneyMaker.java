package com.dhuy.dragonbot.modules;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import com.dhuy.dragonbot.global.Log;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.model.Item;
import com.dhuy.dragonbot.util.ApplicationWindow;
import com.dhuy.dragonbot.util.ImageProcessor;
import com.dhuy.dragonbot.util.Keyboard;
import com.dhuy.dragonbot.util.Mouse;
import com.dhuy.dragonbot.util.MouseCoordinates;
import com.dhuy.dragonbot.util.NormalizeOCREntries;
import com.dhuy.dragonbot.util.OCRHelper;
import com.dhuy.dragonbot.util.XMLHelper;
import net.sourceforge.tess4j.TesseractException;

public class MarketMoneyMaker {
  Store store = Store.getInstance();

  private List<Item> items;

  private Log log;
  private XMLHelper xml;
  private Mouse mouse;
  private Keyboard keyboard;
  private NormalizeOCREntries normalizeOCREntries;
  private ImageProcessor imageProcessor;
  private OCRHelper ocrHelper;
  private ApplicationWindow appWindow;
  private Screenshot screenshotModule;
  private MouseCoordinates mouseCoordinates;

  public MarketMoneyMaker(boolean isRunningInVM) throws AWTException {
    log = Log.getInstance();

    if (store.getCharacterName().equals("Dhuyzin")) {
      xml = new XMLHelper("xml\\items_green.xml");
    } else {
      xml = new XMLHelper("xml\\items_blue.xml");
    }

    mouse = new Mouse();
    keyboard = new Keyboard();
    normalizeOCREntries = new NormalizeOCREntries();
    imageProcessor = new ImageProcessor();
    ocrHelper = new OCRHelper();
    appWindow = new ApplicationWindow();
    screenshotModule = new Screenshot();
    mouseCoordinates = new MouseCoordinates(isRunningInVM);
  }

  // ImageIO.write(images[0], "png", new File("images\\sample.png"));

  public void execute() {
    mouseCoordinates.init();

    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int titleBarHeight = appWindow.getWindowsTitleBarHeight(currentScreenshot.getHeight(),
        appWindow.getWindowsTaskbarHeight());

    store.setWindowsTitleBarHeight(titleBarHeight);

    try {
      items = xml.getItemsList();

      boolean isTheFirstNegotiation = true;
      for (int i = 0; i < items.size(); i++) {
        String isMarketOpened = normalizeOCREntries.normalizeId(ocrHelper.getTextFromImage(
            mouseCoordinates.MARKET_TITLE_X_TOP, mouseCoordinates.MARKET_TITLE_Y_TOP,
            mouseCoordinates.MARKET_TITLE_X_BOTTOM, mouseCoordinates.MARKET_TITLE_Y_BOTTOM));

        if (!isMarketOpened.equals("Market")) {
          isTheFirstNegotiation = true;
          showMessage("A janela do Market está fechada.");
          i--;
          continue;
        }

        if (isTheFirstNegotiation) {
          mouse.clickOn(mouseCoordinates.ANONYMOUS_CHECKBOX_X,
              mouseCoordinates.ANONYMOUS_CHECKBOX_Y, store.getWindowsTitleBarHeight());
          mouse.clickOn(mouseCoordinates.BUY_RADIO_X, mouseCoordinates.BUY_RADIO_Y,
              store.getWindowsTitleBarHeight());

          isTheFirstNegotiation = false;
        }

        buyItem(items.get(i));
      }

      mouse.clickOn(mouseCoordinates.CLOSE_MARKET_X, mouseCoordinates.CLOSE_MARKET_Y,
          store.getWindowsTitleBarHeight());
      System.exit(0);
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }

  private void buyItem(Item item)
      throws InterruptedException, AWTException, IOException, TesseractException {
    log.getLogger()
        .info(log.getMessage(this, "Em processo de compra do item: " + item.getName() + "."));

    mouse.clickOn(mouseCoordinates.SEARCH_BOX_X, mouseCoordinates.SEARCH_BOX_Y,
        store.getWindowsTitleBarHeight());
    delay(100);

    mouse.clickOn(mouseCoordinates.CLEAR_MARKET_ITEM_NAME_X,
        mouseCoordinates.CLEAR_MARKET_ITEM_NAME_Y, store.getWindowsTitleBarHeight());
    delay(100);

    keyboard.writeWord(item.getName());
    delay(250);

    mouse.clickOn(mouseCoordinates.FIRST_FOUND_X, mouseCoordinates.FIRST_FOUND_Y,
        store.getWindowsTitleBarHeight());
    delay(250);

    BufferedImage currentScreenshot = screenshotModule.execute(this);

    BufferedImage dayImage = ocrHelper
        .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_DAY, currentScreenshot);
    BufferedImage hours_1Image = ocrHelper
        .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_HOURS_1, currentScreenshot);
    BufferedImage hours_2Image = ocrHelper
        .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_HOURS_2, currentScreenshot);
    BufferedImage minutes_1Image = ocrHelper
        .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_1, currentScreenshot);
    BufferedImage minutes_2Image = ocrHelper
        .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_2, currentScreenshot);
    BufferedImage seconds_1Image = ocrHelper
        .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_1, currentScreenshot);
    BufferedImage seconds_2Image = ocrHelper
        .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_2, currentScreenshot);

    String day =
        normalizeOCREntries.newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(dayImage, 5)));
    String hours_1 = normalizeOCREntries
        .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(hours_1Image, 5)));
    String hours_2 = normalizeOCREntries
        .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(hours_2Image, 5)));
    String minutes_1 = normalizeOCREntries
        .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(minutes_1Image, 5)));
    String minutes_2 = normalizeOCREntries
        .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(minutes_2Image, 5)));
    String seconds_1 = normalizeOCREntries
        .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(seconds_1Image, 5)));
    String seconds_2 = normalizeOCREntries
        .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(seconds_2Image, 5)));

    String id = "" + day + "" + hours_1 + "" + hours_2 + "" + minutes_1 + "" + minutes_2 + ""
        + seconds_1 + "" + seconds_2;

    log.getLogger().info(
        log.getMessage(this, "ID atual da compra mais cara: " + id + " (" + item.getName() + ")"));

    if (id.equals(item.getId())) {
      /**
       * Se já existir uma oferta.
       */
      log.getLogger().info(
          log.getMessage(this, "Não comprou " + item.getName() + ". A oferta mais cara é a nossa"));
    } else {
      /**
       * Se não existir nenhuma oferta:
       */
      if (id.equals("")) {
        int firstOffer = Math.round((item.getPrice() / 2));

        mouse.clickOn(mouseCoordinates.PIECE_PRICE_X, mouseCoordinates.PIECE_PRICE_Y,
            store.getWindowsTitleBarHeight());
        delay(100);

        keyboard.selectAllTextAndDelete();
        delay(250);

        keyboard.writeWord(String.valueOf(firstOffer));
        delay(100);

        for (int i = 1; i < item.getBuy(); i++) {
          mouse.clickOn(mouseCoordinates.ITEM_QUANTITY_X, mouseCoordinates.ITEM_QUANTITY_Y,
              store.getWindowsTitleBarHeight());
          delay(25);
        }

        mouse.clickOn(mouseCoordinates.CREATE_OFFER_X, mouseCoordinates.CREATE_OFFER_Y,
            store.getWindowsTitleBarHeight());
        delay(750);

        BufferedImage createdDayImage = ocrHelper
            .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_DAY, currentScreenshot);
        BufferedImage createdHours_1Image = ocrHelper
            .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_HOURS_1, currentScreenshot);
        BufferedImage createdHours_2Image = ocrHelper
            .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_HOURS_2, currentScreenshot);
        BufferedImage createdMinutes_1Image = ocrHelper.getImageFromCoordinates(
            mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_1, currentScreenshot);
        BufferedImage createdMinutes_2Image = ocrHelper.getImageFromCoordinates(
            mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_2, currentScreenshot);
        BufferedImage createdSeconds_1Image = ocrHelper.getImageFromCoordinates(
            mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_1, currentScreenshot);
        BufferedImage createdSeconds_2Image = ocrHelper.getImageFromCoordinates(
            mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_2, currentScreenshot);

        String createdDay = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdDayImage, 5)));
        String createdHours_1 = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdHours_1Image, 5)));
        String createdHours_2 = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdHours_2Image, 5)));
        String createdMinutes_1 = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdMinutes_1Image, 5)));
        String createdMinutes_2 = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdMinutes_2Image, 5)));
        String createdSeconds_1 = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdSeconds_1Image, 5)));
        String createdSeconds_2 = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdSeconds_2Image, 5)));

        String createdId =
            "" + createdDay + "" + createdHours_1 + "" + createdHours_2 + "" + createdMinutes_1 + ""
                + createdMinutes_2 + "" + createdSeconds_1 + "" + createdSeconds_2;

        if (createdId.matches("^[0-9]{7}$")) {
          xml.updateItemId(createdId, item.getName());
        } else {
          xml.updateItemId("0000000", item.getName());
        }

        log.getLogger()
            .info(log.getMessage(this,
                "Oferta criada em um item que não havia nenhuma outra oferta de terceiros. Item: "
                    + item.getName()));
      } else {
        /**
         * Se já existir alguma oferta: Verificar se há alguma oferta obsoleta.
         */
        int numberOfBuyOffersToCheck = 0;
        boolean foundObsoleteOfferId = false;
        boolean foundObsoleteMyOffer = false;
        BufferedImage currentScreenshot_7 = screenshotModule.execute(this);

        for (int d = 0; d < mouseCoordinates.NUMBER_OF_OFFERS_TO_CHECK; d++) {
          String battlePixelWithoutOffer = imageProcessor.getHexFromColor(new Color(
              currentScreenshot_7.getSubimage(mouseCoordinates.CHECK_EXISTING_OFFER_BUY_OFFERS_X,
                  mouseCoordinates.CHECK_EXISTING_OFFER_BUY_OFFERS_Y
                      + (numberOfBuyOffersToCheck * 17),
                  1, 1).getRGB(0, 0)));

          if (!battlePixelWithoutOffer.equals(Store.BATTLE_PIXEL_HEX_WITHOUT_OFFER_VISIBLE_COLOR)) {
            numberOfBuyOffersToCheck += 1;
          }
        }

        for (int f = 0; f < numberOfBuyOffersToCheck; f++) {
          if (!foundObsoleteOfferId) {
            BufferedImage currentRowDayImage = ocrHelper.getImageFromCoordinates(
                mouseCoordinates.BUY_OFFERS_END_AT_DAY, f, currentScreenshot);
            BufferedImage currentRowHours_1Image = ocrHelper.getImageFromCoordinates(
                mouseCoordinates.BUY_OFFERS_END_AT_HOURS_1, f, currentScreenshot);
            BufferedImage currentRowHours_2Image = ocrHelper.getImageFromCoordinates(
                mouseCoordinates.BUY_OFFERS_END_AT_HOURS_2, f, currentScreenshot);
            BufferedImage currentRowMinutes_1Image = ocrHelper.getImageFromCoordinates(
                mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_1, f, currentScreenshot);
            BufferedImage currentRowMinutes_2Image = ocrHelper.getImageFromCoordinates(
                mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_2, f, currentScreenshot);
            BufferedImage currentRowSeconds_1Image = ocrHelper.getImageFromCoordinates(
                mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_1, f, currentScreenshot);
            BufferedImage currentRowSeconds_2Image = ocrHelper.getImageFromCoordinates(
                mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_2, f, currentScreenshot);

            String currentRowDay = normalizeOCREntries
                .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(currentRowDayImage, 5)));
            String currentRowHours_1 = normalizeOCREntries.newNormalizeId(
                ocrHelper.getText(ocrHelper.resizeImage(currentRowHours_1Image, 5)));
            String currentRowHours_2 = normalizeOCREntries.newNormalizeId(
                ocrHelper.getText(ocrHelper.resizeImage(currentRowHours_2Image, 5)));
            String currentRowMinutes_1 = normalizeOCREntries.newNormalizeId(
                ocrHelper.getText(ocrHelper.resizeImage(currentRowMinutes_1Image, 5)));
            String currentRowMinutes_2 = normalizeOCREntries.newNormalizeId(
                ocrHelper.getText(ocrHelper.resizeImage(currentRowMinutes_2Image, 5)));
            String currentRowSeconds_1 = normalizeOCREntries.newNormalizeId(
                ocrHelper.getText(ocrHelper.resizeImage(currentRowSeconds_1Image, 5)));
            String currentRowSeconds_2 = normalizeOCREntries.newNormalizeId(
                ocrHelper.getText(ocrHelper.resizeImage(currentRowSeconds_2Image, 5)));

            String currentBuyOfferId = "" + currentRowDay + "" + currentRowHours_1 + ""
                + currentRowHours_2 + "" + currentRowMinutes_1 + "" + currentRowMinutes_2 + ""
                + currentRowSeconds_1 + "" + currentRowSeconds_2;

            log.getLogger().info(log.getMessage(this,
                "Lendo ID das buy offers existentes. ID lido: " + currentBuyOfferId));

            if (!currentBuyOfferId.equals("")) {
              if (item.getId().equals(currentBuyOfferId)) {
                log.getLogger().info(log.getMessage(this,
                    "ID da oferta obsoleta achado. Cancelando ID: " + currentBuyOfferId));

                foundObsoleteOfferId = true;

                mouse.clickOn(mouseCoordinates.MY_OFFERS_X, mouseCoordinates.MY_OFFERS_Y,
                    store.getWindowsTitleBarHeight());
                delay(1000);

                mouse.clickOn(mouseCoordinates.FIRST_BUY_OFFER_X,
                    mouseCoordinates.FIRST_BUY_OFFER_Y, store.getWindowsTitleBarHeight());
                delay(250);

                String currentMyOfferId;
                boolean scrollHasReachedBottom = false;
                while (!scrollHasReachedBottom && !foundObsoleteMyOffer) {
                  for (int g = 0; g < 10; g++) {
                    keyboard.type("DOWN", 10);
                  }

                  BufferedImage currentScreenshot_2 = screenshotModule.execute(this);

                  for (int g = 0; g < mouseCoordinates.NUMBER_OF_VISIBLE_BUY_OFFERS; g++) {
                    BufferedImage currentBuyOfferDayImage = ocrHelper.getImageFromCoordinates(
                        mouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_DAY, g, currentScreenshot_2);
                    BufferedImage currentBuyOfferHours_1Image = ocrHelper.getImageFromCoordinates(
                        mouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_HOURS_1, g,
                        currentScreenshot_2);
                    BufferedImage currentBuyOfferHours_2Image = ocrHelper.getImageFromCoordinates(
                        mouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_HOURS_2, g,
                        currentScreenshot_2);
                    BufferedImage currentBuyOfferMinutes_1Image = ocrHelper.getImageFromCoordinates(
                        mouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_MINUTES_1, g,
                        currentScreenshot_2);
                    BufferedImage currentBuyOfferMinutes_2Image = ocrHelper.getImageFromCoordinates(
                        mouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_MINUTES_2, g,
                        currentScreenshot_2);
                    BufferedImage currentBuyOfferSeconds_1Image = ocrHelper.getImageFromCoordinates(
                        mouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_SECONDS_1, g,
                        currentScreenshot_2);
                    BufferedImage currentBuyOfferSeconds_2Image = ocrHelper.getImageFromCoordinates(
                        mouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_SECONDS_2, g,
                        currentScreenshot_2);

                    String currentBuyOfferDay = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferDayImage, 5)));
                    String currentBuyOfferHours_1 = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferHours_1Image, 5)));
                    String currentBuyOfferHours_2 = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferHours_2Image, 5)));
                    String currentBuyOfferMinutes_1 = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferMinutes_1Image, 5)));
                    String currentBuyOfferMinutes_2 = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferMinutes_2Image, 5)));
                    String currentBuyOfferSeconds_1 = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferSeconds_1Image, 5)));
                    String currentBuyOfferSeconds_2 = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferSeconds_2Image, 5)));

                    currentMyOfferId = "" + currentBuyOfferDay + "" + currentBuyOfferHours_1 + ""
                        + currentBuyOfferHours_2 + "" + currentBuyOfferMinutes_1 + ""
                        + currentBuyOfferMinutes_2 + "" + currentBuyOfferSeconds_1 + ""
                        + currentBuyOfferSeconds_2;

                    log.getLogger().info(
                        log.getMessage(this, "Buscando por ofertas feitas com o ID: " + item.getId()
                            + ". Item: " + item.getName() + ". ID lido: " + currentMyOfferId));

                    if (currentBuyOfferId.equals(currentMyOfferId)) {
                      if (g == 0) {
                        mouse.clickOn(mouseCoordinates.FIRST_BUY_OFFER_X,
                            mouseCoordinates.FIRST_BUY_OFFER_Y, store.getWindowsTitleBarHeight());
                        delay(100);
                      } else {
                        mouse.clickOn(mouseCoordinates.FIRST_BUY_OFFER_X,
                            (mouseCoordinates.FIRST_BUY_OFFER_Y + (16 * g)),
                            store.getWindowsTitleBarHeight());
                        delay(100);
                      }

                      mouse.clickOn(mouseCoordinates.CANCEL_OFFER_X,
                          mouseCoordinates.CANCEL_OFFER_Y, store.getWindowsTitleBarHeight());
                      delay(500);

                      mouse.clickOn(mouseCoordinates.BACK_TO_MARKET_X,
                          mouseCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                      delay(1000);

                      log.getLogger().info(log.getMessage(this, "Oferta do item " + item.getName()
                          + " com o id: " + currentMyOfferId + ". Cancelada com sucesso."));

                      foundObsoleteMyOffer = true;

                      break;
                    }
                  }

                  String scrollHasReachedBottomPixelHex =
                      imageProcessor.getHexFromColor(new Color(currentScreenshot_2
                          .getSubimage(mouseCoordinates.PIXEL_SCROLL_HAS_REACHED_BOTTOM_X,
                              mouseCoordinates.PIXEL_SCROLL_HAS_REACHED_BOTTOM_Y, 1, 1)
                          .getRGB(0, 0)));

                  if (scrollHasReachedBottomPixelHex
                      .equals(Store.MY_OFFERS_SCROLL_HAS_REACHED_BOTTOM_PIXEL_COLOR)) {

                    mouse.clickOn(mouseCoordinates.BACK_TO_MARKET_X,
                        mouseCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                    delay(1000);

                    log.getLogger().info(log.getMessage(this, "[ATENÇÃO] A oferta com o ID: "
                        + item.getName() + " não foi cancelada pois não foi encontrada."));

                    scrollHasReachedBottom = true;
                  }
                }
              }
            }
          }
        }

        /**
         * Iniciar o processo de compra
         */
        int price = Integer.parseInt(normalizeOCREntries.normalizePrice(ocrHelper.getTextFromImage(
            mouseCoordinates.PIECE_PRICE_X_TOP, mouseCoordinates.PIECE_PRICE_Y_TOP,
            mouseCoordinates.PIECE_PRICE_X_BOTTOM, mouseCoordinates.PIECE_PRICE_Y_BOTTOM)));

        price = (price + 1);

        /**
         * Se o maior preço ofertado for menor que o preço do item:
         */
        if (price < item.getPrice()) {
          mouse.clickOn(mouseCoordinates.PIECE_PRICE_X, mouseCoordinates.PIECE_PRICE_Y,
              store.getWindowsTitleBarHeight());
          delay(100);

          keyboard.selectAllTextAndDelete();
          delay(250);

          keyboard.writeWord(String.valueOf(price));
          delay(100);

          for (int i = 1; i < item.getBuy(); i++) {
            mouse.clickOn(mouseCoordinates.ITEM_QUANTITY_X, mouseCoordinates.ITEM_QUANTITY_Y,
                store.getWindowsTitleBarHeight());
            delay(25);
          }

          mouse.clickOn(mouseCoordinates.CREATE_OFFER_X, mouseCoordinates.CREATE_OFFER_Y,
              store.getWindowsTitleBarHeight());
          delay(750);

          BufferedImage currentScreenshot_3 = screenshotModule.execute(this);

          BufferedImage createdDayImage = ocrHelper
              .getImageFromCoordinates(mouseCoordinates.BUY_OFFERS_END_AT_DAY, currentScreenshot_3);
          BufferedImage createdHours_1Image = ocrHelper.getImageFromCoordinates(
              mouseCoordinates.BUY_OFFERS_END_AT_HOURS_1, currentScreenshot_3);
          BufferedImage createdHours_2Image = ocrHelper.getImageFromCoordinates(
              mouseCoordinates.BUY_OFFERS_END_AT_HOURS_2, currentScreenshot_3);
          BufferedImage createdMinutes_1Image = ocrHelper.getImageFromCoordinates(
              mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_1, currentScreenshot_3);
          BufferedImage createdMinutes_2Image = ocrHelper.getImageFromCoordinates(
              mouseCoordinates.BUY_OFFERS_END_AT_MINUTES_2, currentScreenshot_3);
          BufferedImage createdSeconds_1Image = ocrHelper.getImageFromCoordinates(
              mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_1, currentScreenshot_3);
          BufferedImage createdSeconds_2Image = ocrHelper.getImageFromCoordinates(
              mouseCoordinates.BUY_OFFERS_END_AT_SECONDS_2, currentScreenshot_3);

          String createdDay = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdDayImage, 5)));
          String createdHours_1 = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdHours_1Image, 5)));
          String createdHours_2 = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdHours_2Image, 5)));
          String createdMinutes_1 = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdMinutes_1Image, 5)));
          String createdMinutes_2 = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdMinutes_2Image, 5)));
          String createdSeconds_1 = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdSeconds_1Image, 5)));
          String createdSeconds_2 = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdSeconds_2Image, 5)));

          String createdId =
              "" + createdDay + "" + createdHours_1 + "" + createdHours_2 + "" + createdMinutes_1
                  + "" + createdMinutes_2 + "" + createdSeconds_1 + "" + createdSeconds_2;

          if (createdId.matches("^[0-9]{7}$")) {
            xml.updateItemId(createdId, item.getName());
          } else {
            xml.updateItemId("0000000", item.getName());
          }

          log.getLogger().info(log.getMessage(this,
              "Comprando " + item.getBuy() + " " + item.getName() + "s por " + price + "gp cada."));
        } else {
          log.getLogger().info(log.getMessage(this, "Não comprou " + item.getName()
              + ". O valor da oferta iria ser maior que o valor do item."));
        }
      }
    }

    delay(500);
  }

  private void delay(int milliseconds) throws InterruptedException {
    Thread.sleep(milliseconds);
  }

  private void showMessage(String message) {
    JOptionPane jOptionPane = new JOptionPane();
    jOptionPane.setMessage(message);

    JDialog dialog = jOptionPane.createDialog(null);
    dialog.setAlwaysOnTop(true);
    dialog.setVisible(true);
  }
}
