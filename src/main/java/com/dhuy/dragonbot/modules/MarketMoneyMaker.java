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
import com.dhuy.dragonbot.util.MMMCoordinates;
import com.dhuy.dragonbot.util.Mouse;
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
            MMMCoordinates.MARKET_TITLE_X_TOP, MMMCoordinates.MARKET_TITLE_Y_TOP,
            MMMCoordinates.MARKET_TITLE_X_BOTTOM, MMMCoordinates.MARKET_TITLE_Y_BOTTOM));

        if (!isMarketOpened.equals("Market")) {
          isTheFirstNegotiation = true;
          showMessage("A janela do Market está fechada.");
          i--;
          continue;
        }

        if (isTheFirstNegotiation) {
          mouse.clickOn(MMMCoordinates.ANONYMOUS_CHECKBOX_X, MMMCoordinates.ANONYMOUS_CHECKBOX_Y,
              store.getWindowsTitleBarHeight());
          mouse.clickOn(MMMCoordinates.BUY_RADIO_X, MMMCoordinates.BUY_RADIO_Y,
              store.getWindowsTitleBarHeight());

          isTheFirstNegotiation = false;
        }

        buyItem(items.get(i));
      }

      mouse.clickOn(MMMCoordinates.CLOSE_MARKET_X, MMMCoordinates.CLOSE_MARKET_Y,
          store.getWindowsTitleBarHeight());
      System.exit(0);
    } catch (Exception e) {
      log.getLogger().log(Level.SEVERE, log.getMessage(this, null), e);
    }
  }

  private void buyItem(Item item)
      throws InterruptedException, AWTException, IOException, TesseractException {
    mouse.clickOn(MMMCoordinates.SEARCH_BOX_X, MMMCoordinates.SEARCH_BOX_Y,
        store.getWindowsTitleBarHeight());
    delay(250);

    keyboard.selectAllTextAndDelete();
    delay(250);

    keyboard.writeWord(item.getName());
    delay(250);

    mouse.clickOn(MMMCoordinates.FIRST_FOUND_X, MMMCoordinates.FIRST_FOUND_Y,
        store.getWindowsTitleBarHeight());
    delay(250);

    BufferedImage hoursImage = ocrHelper.getImageFromCoordinates(
        MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP, MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
        MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
        MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM);
    BufferedImage minutesImage =
        ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
            MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
            MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
            MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM);
    BufferedImage secondsImage =
        ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
            MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
            MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
            MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM);
    BufferedImage idImage =
        combineImages(new BufferedImage[] {hoursImage, minutesImage, secondsImage});

    String id =
        normalizeOCREntries.newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(idImage, 5)));

    log.getLogger().info(log.getMessage(this, "End At value read " + id));

    if (id.equals(item.getId())) {
      /**
       * Se já existir uma oferta.
       */
      log.getLogger().info(log.getMessage(this,
          "Não comprou " + item.getName() + ". Você já possui uma oferta corrente."));
    } else {
      /**
       * Se não existir nenhuma oferta:
       */
      if (id.equals("")) {
        int firstOffer = Math.round((item.getPrice() / 2));

        mouse.clickOn(MMMCoordinates.PIECE_PRICE_X, MMMCoordinates.PIECE_PRICE_Y,
            store.getWindowsTitleBarHeight());
        delay(250);

        keyboard.selectAllTextAndDelete();
        delay(500);

        keyboard.writeWord(String.valueOf(firstOffer));
        delay(250);

        for (int i = 1; i < item.getBuy(); i++) {
          mouse.clickOn(MMMCoordinates.ITEM_QUANTITY_X, MMMCoordinates.ITEM_QUANTITY_Y,
              store.getWindowsTitleBarHeight());
          delay(25);
        }

        mouse.clickOn(MMMCoordinates.CREATE_OFFER_X, MMMCoordinates.CREATE_OFFER_Y,
            store.getWindowsTitleBarHeight());
        delay(500);

        BufferedImage createdHoursImage =
            ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP,
                MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
                MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
                MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM);
        BufferedImage createdMinutesImage =
            ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
                MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
                MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
                MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM);
        BufferedImage createdSecondsImage =
            ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
                MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
                MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
                MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM);
        BufferedImage createdIdImage = combineImages(
            new BufferedImage[] {createdHoursImage, createdMinutesImage, createdSecondsImage});

        String createdId = normalizeOCREntries
            .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdIdImage, 5)));

        xml.updateItemId(createdId, item.getName());

        log.getLogger().info(
            log.getMessage(this, "Primeiro ao comprar " + item.getName() + " por: " + firstOffer));
      } else {
        /**
         * Se já existir alguma oferta: Verificar se há alguma oferta obsoleta.
         */
        int numberOfOffersToCheck = 0;
        boolean foundObsoleteOfferId = false;
        boolean foundObsoleteOfferRow = false;
        BufferedImage currentScreenshot = screenshotModule.execute(this);

        for (int d = 0; d < MMMCoordinates.NUMBER_OF_OFFERS_TO_CHECK; d++) {
          String monsterLifeBarPixelHex = imageProcessor.getHexFromColor(new Color(
              currentScreenshot.getSubimage(MMMCoordinates.CHECK_EXISTING_OFFER_BUY_OFFERS_X,
                  MMMCoordinates.CHECK_EXISTING_OFFER_BUY_OFFERS_Y + (numberOfOffersToCheck * 17),
                  1, 1).getRGB(0, 0)));

          if (!monsterLifeBarPixelHex.equals(Store.BATTLE_PIXEL_HEX_WITHOUT_OFFER_VISIBLE_COLOR)) {
            numberOfOffersToCheck += 1;
          }
        }

        for (int f = 0; f < numberOfOffersToCheck; f++) {
          if (!foundObsoleteOfferId) {
            BufferedImage currentRowHoursImage =
                ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP,
                    MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
                    MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
                    MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM, f);
            BufferedImage currentRowMinutesImage =
                ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
                    MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
                    MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
                    MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM, f);
            BufferedImage currentRowSecondsImage =
                ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
                    MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
                    MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
                    MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM, f);
            BufferedImage currentRowIdImage = combineImages(new BufferedImage[] {
                currentRowHoursImage, currentRowMinutesImage, currentRowSecondsImage});

            String currentRowId = normalizeOCREntries
                .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(currentRowIdImage, 5)));

            log.getLogger()
                .info(log.getMessage(this, "Janela Market Linha Atual: " + currentRowId));

            if (!currentRowId.equals("")) {
              if (item.getId().equals(currentRowId)) {
                log.getLogger().info(log.getMessage(this,
                    "Cancelar a oferta obsoleta que está na linha " + (f + 1) + "."));

                foundObsoleteOfferId = true;

                mouse.clickOn(MMMCoordinates.MY_OFFERS_X, MMMCoordinates.MY_OFFERS_Y,
                    store.getWindowsTitleBarHeight());
                delay(1000);

                int totalOfBuyOffers = Integer.parseInt(normalizeOCREntries.normalizeId(
                    ocrHelper.getTextFromImage(MMMCoordinates.NUMBER_OF_BUY_OFFERS_X_TOP,
                        MMMCoordinates.NUMBER_OF_BUY_OFFERS_Y_TOP,
                        MMMCoordinates.NUMBER_OF_BUY_OFFERS_X_BOTTOM,
                        MMMCoordinates.NUMBER_OF_BUY_OFFERS_Y_BOTTOM)));

                log.getLogger()
                    .info(log.getMessage(this, "Total of buy orders: " + totalOfBuyOffers));

                int numberOfHiddenBuyOffers =
                    totalOfBuyOffers - MMMCoordinates.NUMBER_OF_VISIBLE_BUY_OFFERS;

                mouse.clickOn(MMMCoordinates.FIRST_BUY_OFFER_X, MMMCoordinates.FIRST_BUY_OFFER_Y,
                    store.getWindowsTitleBarHeight());

                String currentBuyOfferId;
                for (int g = 0; g < MMMCoordinates.NUMBER_OF_VISIBLE_BUY_OFFERS; g++) {
                  if (!foundObsoleteOfferRow) {
                    BufferedImage currentBuyOfferHoursImage = ocrHelper.getImageFromCoordinates(
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_END_AT_HOURS_X_TOP,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_END_AT_HOURS_Y_TOP,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_HOURS_X_BOTTOM,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_HOURS_Y_BOTTOM, g);
                    BufferedImage currentBuyOfferMinutesImage = ocrHelper.getImageFromCoordinates(
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_X_TOP,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_Y_TOP,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_X_BOTTOM,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_MINUTES_Y_BOTTOM, g);
                    BufferedImage currentBuyOfferSecondsImage = ocrHelper.getImageFromCoordinates(
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_X_TOP,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_Y_TOP,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_X_BOTTOM,
                        MMMCoordinates.MY_OFFERS_FIRST_ROW_AT_SECONDS_Y_BOTTOM, g);
                    BufferedImage currentBuyOfferIdImage =
                        combineImages(new BufferedImage[] {currentBuyOfferHoursImage,
                            currentBuyOfferMinutesImage, currentBuyOfferSecondsImage});

                    currentBuyOfferId = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferIdImage, 5)));

                    log.getLogger().info(log.getMessage(this,
                        "Janela Buy Offers Linha Atual: " + currentBuyOfferId));

                    if (currentBuyOfferId.equals("\"?\\it;“A")
                        || currentBuyOfferId.equals("\"\"?\\it;“A\"")) {
                      foundObsoleteOfferRow = true;

                      mouse.clickOn(MMMCoordinates.BACK_TO_MARKET_X,
                          MMMCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                      delay(1000);

                      break;
                    } else {
                      if (item.getId().equals(currentBuyOfferId)) {
                        mouse.clickOn(MMMCoordinates.CANCEL_OFFER_X, MMMCoordinates.CANCEL_OFFER_Y,
                            store.getWindowsTitleBarHeight());
                        delay(500);

                        foundObsoleteOfferRow = true;

                        mouse.clickOn(MMMCoordinates.BACK_TO_MARKET_X,
                            MMMCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                        delay(1000);

                        log.getLogger().info(log.getMessage(this, "Oferta do item " + item.getName()
                            + " com o id: " + currentBuyOfferId + ". Cancelada com sucesso."));

                        break;
                      } else {
                        keyboard.type("DOWN", 10);
                      }
                    }
                  }
                }

                if (!foundObsoleteOfferRow) {
                  for (int h = 0; h < numberOfHiddenBuyOffers; h++) {
                    BufferedImage currentBuyOfferHoursImage = ocrHelper.getImageFromCoordinates(
                        MMMCoordinates.MY_OFFERS_LAST_ROW_END_AT_HOURS_X_TOP,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_END_AT_HOURS_Y_TOP,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_HOURS_X_BOTTOM,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_HOURS_Y_BOTTOM);
                    BufferedImage currentBuyOfferMinutesImage = ocrHelper.getImageFromCoordinates(
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_X_TOP,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_Y_TOP,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_X_BOTTOM,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_MINUTES_Y_BOTTOM);
                    BufferedImage currentBuyOfferSecondsImage = ocrHelper.getImageFromCoordinates(
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_X_TOP,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_Y_TOP,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_X_BOTTOM,
                        MMMCoordinates.MY_OFFERS_LAST_ROW_AT_SECONDS_Y_BOTTOM);
                    BufferedImage currentBuyOfferIdImage =
                        combineImages(new BufferedImage[] {currentBuyOfferHoursImage,
                            currentBuyOfferMinutesImage, currentBuyOfferSecondsImage});

                    currentBuyOfferId = normalizeOCREntries.newNormalizeId(
                        ocrHelper.getText(ocrHelper.resizeImage(currentBuyOfferIdImage, 5)));

                    log.getLogger().info(log.getMessage(this,
                        "Janela Buy Offers Linha Atual: " + currentBuyOfferId));

                    if (currentBuyOfferId.equals("\"?\\it;“A")
                        || currentBuyOfferId.equals("\"\"?\\it;“A\"")) {
                      foundObsoleteOfferRow = true;

                      mouse.clickOn(MMMCoordinates.BACK_TO_MARKET_X,
                          MMMCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                      delay(1000);

                      break;
                    } else {
                      if (item.getId().equals(currentBuyOfferId)) {
                        mouse.clickOn(MMMCoordinates.CANCEL_OFFER_X, MMMCoordinates.CANCEL_OFFER_Y,
                            store.getWindowsTitleBarHeight());
                        delay(500);

                        foundObsoleteOfferRow = true;

                        mouse.clickOn(MMMCoordinates.BACK_TO_MARKET_X,
                            MMMCoordinates.BACK_TO_MARKET_Y, store.getWindowsTitleBarHeight());
                        delay(1000);

                        log.getLogger().info(log.getMessage(this, "Oferta do item " + item.getName()
                            + " com o id: " + currentBuyOfferId + ". Cancelada com sucesso."));

                        break;
                      } else {
                        keyboard.type("DOWN", 25);
                      }
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
            MMMCoordinates.PIECE_PRICE_X_TOP, MMMCoordinates.PIECE_PRICE_Y_TOP,
            MMMCoordinates.PIECE_PRICE_X_BOTTOM, MMMCoordinates.PIECE_PRICE_Y_BOTTOM)));

        price = (price + 1);

        log.getLogger().info(log.getMessage(this, "Piece price " + price + "."));

        /**
         * Se o maior preço ofertado for menor que o preço do item:
         */
        if (price < item.getPrice()) {
          mouse.clickOn(MMMCoordinates.PIECE_PRICE_X, MMMCoordinates.PIECE_PRICE_Y,
              store.getWindowsTitleBarHeight());
          delay(250);

          keyboard.selectAllTextAndDelete();
          delay(500);

          keyboard.writeWord(String.valueOf(price));
          delay(250);

          for (int i = 1; i < item.getBuy(); i++) {
            mouse.clickOn(MMMCoordinates.ITEM_QUANTITY_X, MMMCoordinates.ITEM_QUANTITY_Y,
                store.getWindowsTitleBarHeight());
            delay(25);
          }

          mouse.clickOn(MMMCoordinates.CREATE_OFFER_X, MMMCoordinates.CREATE_OFFER_Y,
              store.getWindowsTitleBarHeight());
          delay(500);

          BufferedImage createdHoursImage =
              ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_TOP,
                  MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_TOP,
                  MMMCoordinates.BUY_OFFERS_END_AT_HOURS_X_BOTTOM,
                  MMMCoordinates.BUY_OFFERS_END_AT_HOURS_Y_BOTTOM);
          BufferedImage createdMinutesImage =
              ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_TOP,
                  MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_TOP,
                  MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_X_BOTTOM,
                  MMMCoordinates.BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM);
          BufferedImage createdSecondsImage =
              ocrHelper.getImageFromCoordinates(MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_TOP,
                  MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_TOP,
                  MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_X_BOTTOM,
                  MMMCoordinates.BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM);
          BufferedImage createdIdImage = combineImages(
              new BufferedImage[] {createdHoursImage, createdMinutesImage, createdSecondsImage});

          String createdId = normalizeOCREntries
              .newNormalizeId(ocrHelper.getText(ocrHelper.resizeImage(createdIdImage, 5)));

          xml.updateItemId(createdId, item.getName());

          System.out.println("Comprando " + item.getName() + " por: " + price + " gps.");
        } else {
          System.out.println("Não comprou " + item.getName() + ". Caro demais.");
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
