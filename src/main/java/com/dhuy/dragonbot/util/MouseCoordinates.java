package com.dhuy.dragonbot.util;

public class MouseCoordinates {
  private int isRunnninInVM;

  public int NUMBER_OF_OFFERS_TO_CHECK;
  public int NUMBER_OF_VISIBLE_BUY_OFFERS;
  public int NUMBER_OF_TRANSACTIONS_REQUIRED;

  public int CLOSE_MARKET_X;
  public int CLOSE_MARKET_Y;

  public int MARKET_TITLE_X_TOP;
  public int MARKET_TITLE_Y_TOP;
  public int MARKET_TITLE_X_BOTTOM;
  public int MARKET_TITLE_Y_BOTTOM;

  public int ANONYMOUS_CHECKBOX_X;
  public int ANONYMOUS_CHECKBOX_Y;

  public int BUY_RADIO_X;
  public int BUY_RADIO_Y;

  public int SEARCH_BOX_X;
  public int SEARCH_BOX_Y;

  public int FIRST_FOUND_X;
  public int FIRST_FOUND_Y;

  public int PIECE_PRICE_X;
  public int PIECE_PRICE_Y;

  public int ITEM_QUANTITY_X;
  public int ITEM_QUANTITY_Y;

  public int CREATE_OFFER_X;
  public int CREATE_OFFER_Y;

  public int MY_OFFERS_X;
  public int MY_OFFERS_Y;

  public int FIRST_BUY_OFFER_X;
  public int FIRST_BUY_OFFER_Y;

  public int BACK_TO_MARKET_X;
  public int BACK_TO_MARKET_Y;

  public int CANCEL_OFFER_X;
  public int CANCEL_OFFER_Y;

  public int PIECE_PRICE_X_TOP;
  public int PIECE_PRICE_Y_TOP;
  public int PIECE_PRICE_X_BOTTOM;
  public int PIECE_PRICE_Y_BOTTOM;

  public int CHECK_EXISTING_OFFER_BUY_OFFERS_X;
  public int CHECK_EXISTING_OFFER_BUY_OFFERS_Y;

  public int SEARCH_ITEMS_BUTTON_X;
  public int SEARCH_ITEMS_BUTTON_Y;

  public int CLEAR_FOUND_ITEM_NAME_X;
  public int CLEAR_FOUND_ITEM_NAME_Y;

  public int FIRST_FOUND_ITEM_X;
  public int FIRST_FOUND_ITEM_Y;

  public int SEARCH_FOR_ITEMS_X;
  public int SEARCH_FOR_ITEMS_Y;

  public int CHECK_EXISTING_ITEMS_X;
  public int CHECK_EXISTING_ITEMS_Y;

  public int GO_BACK_SEARCH_ITEMS_X;
  public int GO_BACK_SEARCH_ITEMS_Y;

  public int RETRIEVE_ITEMS_X;
  public int RETRIEVE_ITEMS_Y;

  public int NOT_ENOUGH_CAPACITY_INFO_MESSAGE_X;
  public int NOT_ENOUGH_CAPACITY_INFO_MESSAGE_Y;

  public int PIXEL_NOT_ENOUGH_CAPACITY_X;
  public int PIXEL_NOT_ENOUGH_CAPACITY_Y;

  public int AMOUNT_BUY_OFFERS_IN_SERVER_X_TOP;
  public int AMOUNT_BUY_OFFERS_IN_SERVER_Y_TOP;
  public int AMOUNT_BUY_OFFERS_IN_SERVER_X_BOTTOM;
  public int AMOUNT_BUY_OFFERS_IN_SERVER_Y_BOTTOM;

  public int DETAIL_BUTTON_MARKET_X;
  public int DETAIL_BUTTON_MARKET_Y;

  public int CLEAR_MARKET_ITEM_NAME_X;
  public int CLEAR_MARKET_ITEM_NAME_Y;

  public int PIXEL_SCROLL_HAS_REACHED_BOTTOM_X;
  public int PIXEL_SCROLL_HAS_REACHED_BOTTOM_Y;

  public int[] BUY_OFFERS_END_AT_DAY;
  public int[] BUY_OFFERS_END_AT_HOURS_1;
  public int[] BUY_OFFERS_END_AT_HOURS_2;
  public int[] BUY_OFFERS_END_AT_MINUTES_1;
  public int[] BUY_OFFERS_END_AT_MINUTES_2;
  public int[] BUY_OFFERS_END_AT_SECONDS_1;
  public int[] BUY_OFFERS_END_AT_SECONDS_2;

  public int[] MY_OFFERS_FIRST_ROW_END_AT_DAY;
  public int[] MY_OFFERS_FIRST_ROW_END_AT_HOURS_1;
  public int[] MY_OFFERS_FIRST_ROW_END_AT_HOURS_2;
  public int[] MY_OFFERS_FIRST_ROW_END_AT_MINUTES_1;
  public int[] MY_OFFERS_FIRST_ROW_END_AT_MINUTES_2;
  public int[] MY_OFFERS_FIRST_ROW_END_AT_SECONDS_1;
  public int[] MY_OFFERS_FIRST_ROW_END_AT_SECONDS_2;

  public int[] MY_OFFERS_LAST_ROW_END_AT_DAY;
  public int[] MY_OFFERS_LAST_ROW_END_AT_HOURS_1;
  public int[] MY_OFFERS_LAST_ROW_END_AT_HOURS_2;
  public int[] MY_OFFERS_LAST_ROW_END_AT_MINUTES_1;
  public int[] MY_OFFERS_LAST_ROW_END_AT_MINUTES_2;
  public int[] MY_OFFERS_LAST_ROW_END_AT_SECONDS_1;
  public int[] MY_OFFERS_LAST_ROW_END_AT_SECONDS_2;

  public int DEPOT_BOX_X;
  public int DEPOT_BOX_Y;

  public MouseCoordinates(boolean isVM) {
    if (isVM) {
      isRunnninInVM = 5;
    } else {
      isRunnninInVM = 0;
    }
  }

  public MouseCoordinates(boolean isVM, String depotDirection) {
    if (isVM) {
      isRunnninInVM = 5;
    } else {
      isRunnninInVM = 0;
    }

    switch (depotDirection) {
      case "NORTH":
        DEPOT_BOX_Y = 360;
        break;
      case "SOUTH":
        DEPOT_BOX_Y = 505;
        break;
    }
  }

  public void init() {
    DEPOT_BOX_X = 865;

    NUMBER_OF_OFFERS_TO_CHECK = 7;
    NUMBER_OF_VISIBLE_BUY_OFFERS = 11;
    NUMBER_OF_TRANSACTIONS_REQUIRED = 50;

    CLOSE_MARKET_X = 1280;
    CLOSE_MARKET_Y = 760 - isRunnninInVM;

    MARKET_TITLE_X_TOP = 935;
    MARKET_TITLE_Y_TOP = 242 - isRunnninInVM;
    MARKET_TITLE_X_BOTTOM = 984;
    MARKET_TITLE_Y_BOTTOM = 257 - isRunnninInVM;

    ANONYMOUS_CHECKBOX_X = 1185;
    ANONYMOUS_CHECKBOX_Y = 718 - isRunnninInVM;

    BUY_RADIO_X = 776;
    BUY_RADIO_Y = 672 - isRunnninInVM;

    SEARCH_BOX_X = 690;
    SEARCH_BOX_Y = 720 - isRunnninInVM;

    FIRST_FOUND_X = 618;
    FIRST_FOUND_Y = 417 - isRunnninInVM;

    PIECE_PRICE_X = 1030;
    PIECE_PRICE_Y = 690 - isRunnninInVM;

    ITEM_QUANTITY_X = 1095;
    ITEM_QUANTITY_Y = 673 - isRunnninInVM;

    CREATE_OFFER_X = 1300;
    CREATE_OFFER_Y = 720 - isRunnninInVM;

    MY_OFFERS_X = 1205;
    MY_OFFERS_Y = 760 - isRunnninInVM;

    FIRST_BUY_OFFER_X = 960;
    FIRST_BUY_OFFER_Y = 560 - isRunnninInVM;

    BACK_TO_MARKET_X = 1280;
    BACK_TO_MARKET_Y = 760 - isRunnninInVM;

    CANCEL_OFFER_X = 1285;
    CANCEL_OFFER_Y = 514 - isRunnninInVM;

    PIECE_PRICE_X_TOP = 963;
    PIECE_PRICE_Y_TOP = 508 - isRunnninInVM;
    PIECE_PRICE_X_BOTTOM = 1043;
    PIECE_PRICE_Y_BOTTOM = 520 - isRunnninInVM;

    CHECK_EXISTING_OFFER_BUY_OFFERS_X = 1235;
    CHECK_EXISTING_OFFER_BUY_OFFERS_Y = 512 - isRunnninInVM;

    SEARCH_ITEMS_BUTTON_X = 1880;
    SEARCH_ITEMS_BUTTON_Y = 745;

    CLEAR_FOUND_ITEM_NAME_X = 1729;
    CLEAR_FOUND_ITEM_NAME_Y = 319;

    FIRST_FOUND_ITEM_X = 1590;
    FIRST_FOUND_ITEM_Y = 163;

    SEARCH_FOR_ITEMS_X = 1727;
    SEARCH_FOR_ITEMS_Y = 341;

    CHECK_EXISTING_ITEMS_X = 1717;
    CHECK_EXISTING_ITEMS_Y = 68;

    GO_BACK_SEARCH_ITEMS_X = 1710;
    GO_BACK_SEARCH_ITEMS_Y = 7;

    RETRIEVE_ITEMS_X = 1660;
    RETRIEVE_ITEMS_Y = 342;

    NOT_ENOUGH_CAPACITY_INFO_MESSAGE_X = 1111;
    NOT_ENOUGH_CAPACITY_INFO_MESSAGE_Y = 547 - isRunnninInVM;

    PIXEL_NOT_ENOUGH_CAPACITY_X = 900;
    PIXEL_NOT_ENOUGH_CAPACITY_Y = 480 - isRunnninInVM;

    AMOUNT_BUY_OFFERS_IN_SERVER_X_TOP = 948;
    AMOUNT_BUY_OFFERS_IN_SERVER_Y_TOP = 490 - isRunnninInVM;
    AMOUNT_BUY_OFFERS_IN_SERVER_X_BOTTOM = 979;
    AMOUNT_BUY_OFFERS_IN_SERVER_Y_BOTTOM = 500 - isRunnninInVM;

    DETAIL_BUTTON_MARKET_X = 1123;
    DETAIL_BUTTON_MARKET_Y = 760;

    CLEAR_MARKET_ITEM_NAME_X = 752;
    CLEAR_MARKET_ITEM_NAME_Y = 720;

    PIXEL_SCROLL_HAS_REACHED_BOTTOM_X = 1313;
    PIXEL_SCROLL_HAS_REACHED_BOTTOM_Y = 714;

    BUY_OFFERS_END_AT_DAY = new int[] {1202, 506 - isRunnninInVM, 1210, 522 - isRunnninInVM};
    BUY_OFFERS_END_AT_HOURS_1 = new int[] {1218, 506 - isRunnninInVM, 1226, 522 - isRunnninInVM};
    BUY_OFFERS_END_AT_HOURS_2 = new int[] {1226, 506 - isRunnninInVM, 1234, 522 - isRunnninInVM};
    BUY_OFFERS_END_AT_MINUTES_1 = new int[] {1238, 506 - isRunnninInVM, 1246, 522 - isRunnninInVM};
    BUY_OFFERS_END_AT_MINUTES_2 = new int[] {1246, 506 - isRunnninInVM, 1254, 522 - isRunnninInVM};
    BUY_OFFERS_END_AT_SECONDS_1 = new int[] {1258, 506 - isRunnninInVM, 1266, 522 - isRunnninInVM};
    BUY_OFFERS_END_AT_SECONDS_2 = new int[] {1266, 506 - isRunnninInVM, 1274, 522 - isRunnninInVM};

    MY_OFFERS_FIRST_ROW_END_AT_DAY =
        new int[] {1102, 551 - isRunnninInVM, 1110, 567 - isRunnninInVM};
    MY_OFFERS_FIRST_ROW_END_AT_HOURS_1 =
        new int[] {1118, 551 - isRunnninInVM, 1126, 567 - isRunnninInVM};
    MY_OFFERS_FIRST_ROW_END_AT_HOURS_2 =
        new int[] {1126, 551 - isRunnninInVM, 1134, 567 - isRunnninInVM};
    MY_OFFERS_FIRST_ROW_END_AT_MINUTES_1 =
        new int[] {1138, 551 - isRunnninInVM, 1146, 567 - isRunnninInVM};
    MY_OFFERS_FIRST_ROW_END_AT_MINUTES_2 =
        new int[] {1146, 551 - isRunnninInVM, 1154, 567 - isRunnninInVM};
    MY_OFFERS_FIRST_ROW_END_AT_SECONDS_1 =
        new int[] {1158, 551 - isRunnninInVM, 1166, 567 - isRunnninInVM};
    MY_OFFERS_FIRST_ROW_END_AT_SECONDS_2 =
        new int[] {1166, 551 - isRunnninInVM, 1174, 567 - isRunnninInVM};

    MY_OFFERS_LAST_ROW_END_AT_DAY =
        new int[] {1102, 711 - isRunnninInVM, 1110, 727 - isRunnninInVM};
    MY_OFFERS_LAST_ROW_END_AT_HOURS_1 =
        new int[] {1118, 711 - isRunnninInVM, 1126, 727 - isRunnninInVM};
    MY_OFFERS_LAST_ROW_END_AT_HOURS_2 =
        new int[] {1126, 711 - isRunnninInVM, 1134, 727 - isRunnninInVM};
    MY_OFFERS_LAST_ROW_END_AT_MINUTES_1 =
        new int[] {1138, 711 - isRunnninInVM, 1146, 727 - isRunnninInVM};
    MY_OFFERS_LAST_ROW_END_AT_MINUTES_2 =
        new int[] {1146, 711 - isRunnninInVM, 1154, 727 - isRunnninInVM};
    MY_OFFERS_LAST_ROW_END_AT_SECONDS_1 =
        new int[] {1158, 711 - isRunnninInVM, 1166, 727 - isRunnninInVM};
    MY_OFFERS_LAST_ROW_END_AT_SECONDS_2 =
        new int[] {1166, 711 - isRunnninInVM, 1174, 727 - isRunnninInVM};
  }
}
