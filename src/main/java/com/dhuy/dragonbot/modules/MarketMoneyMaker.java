package com.dhuy.dragonbot.modules;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics2D;
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

  public MarketMoneyMaker() throws AWTException {
    log = Log.getInstance();
    xml = new XMLHelper();
    mouse = new Mouse();
    keyboard = new Keyboard();
    normalizeOCREntries = new NormalizeOCREntries();
    imageProcessor = new ImageProcessor();
    ocrHelper = new OCRHelper();
    appWindow = new ApplicationWindow();
    screenshotModule = new Screenshot();
  }

  // ImageIO.write(images[0], "png", new File("images\\sample.png"));

  private BufferedImage combineImages(BufferedImage[] images) {
    int widthCurr = 0;
    BufferedImage concatImage = new BufferedImage(48, 16, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = concatImage.createGraphics();
    for (BufferedImage image : images) {
      g2d.drawImage(image, widthCurr, 0, null);
      widthCurr += image.getWidth();
    }
    g2d.dispose();

    return concatImage;
  }

  public void execute() {
    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int titleBarHeight = appWindow.getWindowsTitleBarHeight(currentScreenshot.getHeight(),
        appWindow.getWindowsTaskbarHeight());

    store.setWindowsTitleBarHeight(titleBarHeight);

    try {
      items = xml.getItemsList();

      boolean isTheFirstNegotiation = true;
      for (int i = 0; i < items.size(); i++) {
        String isMarketOpened = normalizeOCREntries.normalizeId(ocrHelper.getTextFromImage(
            MouseCoordinates.MARKET_TITLE_X_TOP, MouseCoordinates.MARKET_TITLE_Y_TOP,
            MouseCoordinates.MARKET_TITLE_X_BOTTOM, MouseCoordinates.MARKET_TITLE_Y_BOTTOM));

        if (!isMarketOpened.equals("Market")) {
          isTheFirstNegotiation = true;
          showMessage("A janela do Market está fechada.");
          i--;
          continue;
        }

        if (isTheFirstNegotiation) {
          mouse.clickOn(MouseCoordinates.ANONYMOUS_CHECKBOX_X,
              MouseCoordinates.ANONYMOUS_CHECKBOX_Y, store.getWindowsTitleBarHeight());
          mouse.clickOn(MouseCoordinates.BUY_RADIO_X, MouseCoordinates.BUY_RADIO_Y,
              store.getWindowsTitleBarHeight());

          isTheFirstNegotiation = false;
        }

        buyItem(items.get(i));
      }

      mouse.clickOn(MouseCoordinates.CLOSE_MARKET_X, MouseCoordinates.CLOSE_MARKET_Y,
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

    mouse.clickOn(MouseCoordinates.SEARCH_BOX_X, MouseCoordinates.SEARCH_BOX_Y,
        store.getWindowsTitleBarHeight());
    delay(100);

    mouse.clickOn(MouseCoordinates.CLEAR_MARKET_ITEM_NAME_X,
        MouseCoordinates.CLEAR_MARKET_ITEM_NAME_Y, store.getWindowsTitleBarHeight());
    delay(100);

    keyboard.writeWord(item.getName());
    delay(250);

    mouse.clickOn(MouseCoordinates.FIRST_FOUND_X, MouseCoordinates.FIRST_FOUND_Y,
        store.getWindowsTitleBarHeight());
    delay(250);

    BufferedImage currentScreenshot = screenshotModule.execute(this);

    BufferedImage hoursImage =
        ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP,
            MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
            MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
            MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM, currentScreenshot);
    BufferedImage minutesImage =
        ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
            MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
            MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
            MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM, currentScreenshot);
    BufferedImage secondsImage =
        ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
            MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
            MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
            MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM, currentScreenshot);
    BufferedImage idImage =
        combineImages(new BufferedImage[] {hoursImage, minutesImage, secondsImage});

    String id =
        normalizeOCREntries.newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(idImage, 5)));

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

        mouse.clickOn(MouseCoordinates.PIECE_PRICE_X, MouseCoordinates.PIECE_PRICE_Y,
            store.getWindowsTitleBarHeight());
        delay(100);

        keyboard.selectAllTextAndDelete();
        delay(250);

        keyboard.writeWord(String.valueOf(firstOffer));
        delay(100);

        for (int i = 1; i < item.getBuy(); i++) {
          mouse.clickOn(MouseCoordinates.ITEM_QUANTITY_X, MouseCoordinates.ITEM_QUANTITY_Y,
              store.getWindowsTitleBarHeight());
          delay(25);
        }

        mouse.clickOn(MouseCoordinates.CREATE_OFFER_X, MouseCoordinates.CREATE_OFFER_Y,
            store.getWindowsTitleBarHeight());
        delay(750);

        BufferedImage currentScreenshot_2 = screenshotModule.execute(this);

        BufferedImage createdHoursImage =
            ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP,
                MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
                MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
                MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM, currentScreenshot_2);
        BufferedImage createdMinutesImage =
            ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
                MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
                MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
                MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM, currentScreenshot_2);
        BufferedImage createdSecondsImage =
            ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
                MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
                MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
                MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM, currentScreenshot_2);
        BufferedImage createdIdImage = combineImages(
            new BufferedImage[] {createdHoursImage, createdMinutesImage, createdSecondsImage});

        String createdId = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdIdImage, 5)));

        xml.updateItemId(createdId, item.getName());

        log.getLogger()
            .info(log.getMessage(this,
                "Oferta criada em um item que não havia nenhuma outra oferta de terceiros. Item: "
                    + item.getName()));
      } else {
        /**
         * Se já existir alguma oferta: Verificar se há alguma oferta obsoleta.
         */
        int numberOfOffersToCheck = 0;
        boolean foundObsoleteOfferId = false;
        boolean foundObsoleteOfferRow = false;
        BufferedImage currentScreenshot_7 = screenshotModule.execute(this);

        for (int d = 0; d < MouseCoordinates.NUMBER_OF_OFFERS_TO_CHECK; d++) {
          String battlePixelWithoutOffer = imageProcessor.getHexFromColor(new Color(
              currentScreenshot_7.getSubimage(MouseCoordinates.CHECK_EXISTING_OFFER_BUY_OFFERS_X,
                  MouseCoordinates.CHECK_EXISTING_OFFER_BUY_OFFERS_Y + (numberOfOffersToCheck * 17),
                  1, 1).getRGB(0, 0)));

          if (!battlePixelWithoutOffer.equals(Store.BATTLE_PIXEL_HEX_WITHOUT_OFFER_VISIBLE_COLOR)) {
            numberOfOffersToCheck += 1;
          }
        }

        for (int f = 0; f < numberOfOffersToCheck; f++) {
          if (!foundObsoleteOfferId) {
            BufferedImage currentScreenshot_3 = screenshotModule.execute(this);

            BufferedImage currentRowHoursImage =
                ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP,
                    MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
                    MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
                    MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM, f, currentScreenshot_3);
            BufferedImage currentRowMinutesImage =
                ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
                    MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
                    MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
                    MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM, f, currentScreenshot_3);
            BufferedImage currentRowSecondsImage =
                ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
                    MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
                    MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
                    MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM, f, currentScreenshot_3);
            BufferedImage currentRowIdImage = combineImages(new BufferedImage[] {
                currentRowHoursImage, currentRowMinutesImage, currentRowSecondsImage});

            String currentRowId = normalizeOCREntries
                .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(currentRowIdImage, 5)));

            log.getLogger().info(log.getMessage(this,
                "Lendo ID das buy offers existentes. ID lido: " + currentRowId));

            if (!currentRowId.equals("")) {
              if (item.getId().equals(currentRowId)) {
                log.getLogger().info(log.getMessage(this,
                    "ID da oferta obsoleta achado. Cancelando ID: " + currentRowId));

                foundObsoleteOfferId = true;

                mouse.clickOn(MouseCoordinates.MY_OFFERS_X, MouseCoordinates.MY_OFFERS_Y,
                    store.getWindowsTitleBarHeight());
                delay(1000);

                mouse.clickOn(MouseCoordinates.FIRST_BUY_OFFER_X,
                    MouseCoordinates.FIRST_BUY_OFFER_Y, store.getWindowsTitleBarHeight());

                String currentBuyOfferId;
                for (int g = 0; g < MouseCoordinates.NUMBER_OF_VISIBLE_BUY_OFFERS; g++) {
                  if (!foundObsoleteOfferRow) {
                    BufferedImage currentScreenshot_4 = screenshotModule.execute(this);

                    BufferedImage currentBuyOfferHoursImage = ocrHelper.getImageFromCoordinates(
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_HOURS_X_TOP,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_END_AT_HOURS_Y_TOP,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_HOURS_X_BOTTOM,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_HOURS_Y_BOTTOM, g,
                        currentScreenshot_4);
                    BufferedImage currentBuyOfferMinutesImage = ocrHelper.getImageFromCoordinates(
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_X_TOP,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_Y_TOP,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_X_BOTTOM,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_Y_BOTTOM, g,
                        currentScreenshot_4);
                    BufferedImage currentBuyOfferSecondsImage = ocrHelper.getImageFromCoordinates(
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_X_TOP,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_Y_TOP,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_X_BOTTOM,
                        MouseCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_Y_BOTTOM, g,
                        currentScreenshot_4);
                    BufferedImage currentBuyOfferIdImage =
                        combineImages(new BufferedImage[] {currentBuyOfferHoursImage,
                            currentBuyOfferMinutesImage, currentBuyOfferSecondsImage});

                    currentBuyOfferId = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferIdImage, 5)));

                    log.getLogger().info(
                        log.getMessage(this, "Buscando por ofertas feitas com o ID: " + item.getId()
                            + ". Item: " + item.getName() + ". ID lido: " + currentBuyOfferId));

                    if (item.getId().equals(currentBuyOfferId)) {
                      mouse.clickOn(MouseCoordinates.CANCEL_OFFER_X,
                          MouseCoordinates.CANCEL_OFFER_Y, store.getWindowsTitleBarHeight());
                      delay(500);

                      foundObsoleteOfferRow = true;

                      mouse.clickOn(MouseCoordinates.BACK_TO_MARKET_X,
                          MouseCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                      delay(1000);

                      log.getLogger().info(log.getMessage(this, "Oferta do item " + item.getName()
                          + " com o ID: " + item.getId() + ". Cancelada com sucesso."));

                      break;
                    } else {
                      keyboard.type("DOWN", 10);
                    }
                  }
                }

                if (!foundObsoleteOfferRow) {
                  boolean scrollHasReachedBottom = false;
                  while (!scrollHasReachedBottom) {
                    BufferedImage currentScreenshot_5 = screenshotModule.execute(this);

                    BufferedImage currentBuyOfferHoursImage = ocrHelper.getImageFromCoordinates(
                        MouseCoordinates.MY_OFFERS_LAST_ROW_END_AT_HOURS_X_TOP,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_END_AT_HOURS_Y_TOP,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_HOURS_X_BOTTOM,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_HOURS_Y_BOTTOM, currentScreenshot_5);
                    BufferedImage currentBuyOfferMinutesImage = ocrHelper.getImageFromCoordinates(
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_X_TOP,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_Y_TOP,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_X_BOTTOM,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_Y_BOTTOM,
                        currentScreenshot_5);
                    BufferedImage currentBuyOfferSecondsImage = ocrHelper.getImageFromCoordinates(
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_X_TOP,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_Y_TOP,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_X_BOTTOM,
                        MouseCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_Y_BOTTOM,
                        currentScreenshot_5);
                    BufferedImage currentBuyOfferIdImage =
                        combineImages(new BufferedImage[] {currentBuyOfferHoursImage,
                            currentBuyOfferMinutesImage, currentBuyOfferSecondsImage});

                    currentBuyOfferId = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferIdImage, 5)));

                    log.getLogger().info(
                        log.getMessage(this, "Buscando por ofertas feitas com o ID: " + item.getId()
                            + ". Item: " + item.getName() + ". ID lido: " + currentBuyOfferId));

                    if (item.getId().equals(currentBuyOfferId)) {
                      mouse.clickOn(MouseCoordinates.CANCEL_OFFER_X,
                          MouseCoordinates.CANCEL_OFFER_Y, store.getWindowsTitleBarHeight());
                      delay(500);

                      mouse.clickOn(MouseCoordinates.BACK_TO_MARKET_X,
                          MouseCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                      delay(1000);

                      log.getLogger().info(log.getMessage(this, "Oferta do item " + item.getName()
                          + " com o id: " + currentBuyOfferId + ". Cancelada com sucesso."));

                      break;
                    } else {
                      String scrollHasReachedBottomPixelHex =
                          imageProcessor.getHexFromColor(new Color(currentScreenshot_5
                              .getSubimage(MouseCoordinates.PIXEL_SCROLL_HAS_REACHED_BOTTOM_X,
                                  MouseCoordinates.PIXEL_SCROLL_HAS_REACHED_BOTTOM_Y, 1, 1)
                              .getRGB(0, 0)));

                      if (scrollHasReachedBottomPixelHex
                          .equals(Store.MY_OFFERS_SCROLL_HAS_REACHED_BOTTOM_PIXEL_COLOR)) {
                        scrollHasReachedBottom = true;

                        mouse.clickOn(MouseCoordinates.BACK_TO_MARKET_X,
                            MouseCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                        delay(1000);

                        log.getLogger().info(log.getMessage(this, "[ATENÇÃO] A oferta com o ID: "
                            + item.getName() + " não foi cancelada pois não foi encontrada."));
                      }

                      keyboard.type("DOWN", 10);
                    }
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
            MouseCoordinates.PIECE_PRICE_X_TOP, MouseCoordinates.PIECE_PRICE_Y_TOP,
            MouseCoordinates.PIECE_PRICE_X_BOTTOM, MouseCoordinates.PIECE_PRICE_Y_BOTTOM)));

        price = (price + 1);

        /**
         * Se o maior preço ofertado for menor que o preço do item:
         */
        if (price < item.getPrice()) {
          mouse.clickOn(MouseCoordinates.PIECE_PRICE_X, MouseCoordinates.PIECE_PRICE_Y,
              store.getWindowsTitleBarHeight());
          delay(100);

          keyboard.selectAllTextAndDelete();
          delay(250);

          keyboard.writeWord(String.valueOf(price));
          delay(100);

          for (int i = 1; i < item.getBuy(); i++) {
            mouse.clickOn(MouseCoordinates.ITEM_QUANTITY_X, MouseCoordinates.ITEM_QUANTITY_Y,
                store.getWindowsTitleBarHeight());
            delay(25);
          }

          mouse.clickOn(MouseCoordinates.CREATE_OFFER_X, MouseCoordinates.CREATE_OFFER_Y,
              store.getWindowsTitleBarHeight());
          delay(750);

          BufferedImage currentScreenshot_6 = screenshotModule.execute(this);

          BufferedImage createdHoursImage =
              ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP,
                  MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
                  MouseCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
                  MouseCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM, currentScreenshot_6);
          BufferedImage createdMinutesImage =
              ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
                  MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
                  MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
                  MouseCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM, currentScreenshot_6);
          BufferedImage createdSecondsImage =
              ocrHelper.getImageFromCoordinates(MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
                  MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
                  MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
                  MouseCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM, currentScreenshot_6);
          BufferedImage createdIdImage = combineImages(
              new BufferedImage[] {createdHoursImage, createdMinutesImage, createdSecondsImage});

          String createdId = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdIdImage, 5)));

          xml.updateItemId(createdId, item.getName());

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
