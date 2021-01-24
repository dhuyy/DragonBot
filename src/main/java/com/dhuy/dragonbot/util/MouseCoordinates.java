package com.dhuy.dragonbot.util;

public class MouseCoordinates {
  private int isRunnninInVM;

  public int DEPOT_BOX_X = 865;
  public int DEPOT_BOX_Y = 0;

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

  public int NUMBER_OF_OFFERS_TO_CHECK = 7;
  public int NUMBER_OF_VISIBLE_BUY_OFFERS = 11;
  public int NUMBER_OF_TRANSACTIONS_REQUIRED = 50;

  public int CLOSE_MARKET_X = 1280;
  public int CLOSE_MARKET_Y = 760 - isRunnninInVM;

  public int MARKET_TITLE_X_TOP = 935;
  public int MARKET_TITLE_Y_TOP = 242 - isRunnninInVM;
  public int MARKET_TITLE_X_BOTTOM = 984;
  public int MARKET_TITLE_Y_BOTTOM = 257 - isRunnninInVM;

  public int ANONYMOUS_CHECKBOX_X = 1185;
  public int ANONYMOUS_CHECKBOX_Y = 718 - isRunnninInVM;

  public int BUY_RADIO_X = 776;
  public int BUY_RADIO_Y = 672 - isRunnninInVM;

  public int SEARCH_BOX_X = 690;
  public int SEARCH_BOX_Y = 720 - isRunnninInVM;

  public int FIRST_FOUND_X = 618;
  public int FIRST_FOUND_Y = 417 - isRunnninInVM;

  public int PIECE_PRICE_X = 1030;
  public int PIECE_PRICE_Y = 690 - isRunnninInVM;

  public int ITEM_QUANTITY_X = 1095;
  public int ITEM_QUANTITY_Y = 673 - isRunnninInVM;

  public int CREATE_OFFER_X = 1300;
  public int CREATE_OFFER_Y = 720 - isRunnninInVM;

  public int MY_OFFERS_X = 1205;
  public int MY_OFFERS_Y = 760 - isRunnninInVM;

  public int FIRST_BUY_OFFER_X = 960;
  public int FIRST_BUY_OFFER_Y = 560 - isRunnninInVM;

  public int BACK_TO_MARKET_X = 1280;
  public int BACK_TO_MARKET_Y = 760 - isRunnninInVM;

  public int CANCEL_OFFER_X = 1285;
  public int CANCEL_OFFER_Y = 514 - isRunnninInVM;

  public int PIECE_PRICE_X_TOP = 963;
  public int PIECE_PRICE_Y_TOP = 508 - isRunnninInVM;
  public int PIECE_PRICE_X_BOTTOM = 1043;
  public int PIECE_PRICE_Y_BOTTOM = 520 - isRunnninInVM;

  public int CHECK_EXISTING_OFFER_BUY_OFFERS_X = 1235;
  public int CHECK_EXISTING_OFFER_BUY_OFFERS_Y = 512 - isRunnninInVM;

  public int SEARCH_ITEMS_BUTTON_X = 1880;
  public int SEARCH_ITEMS_BUTTON_Y = 745;

  public int CLEAR_FOUND_ITEM_NAME_X = 1729;
  public int CLEAR_FOUND_ITEM_NAME_Y = 319;

  public int FIRST_FOUND_ITEM_X = 1590;
  public int FIRST_FOUND_ITEM_Y = 163;

  public int SEARCH_FOR_ITEMS_X = 1727;
  public int SEARCH_FOR_ITEMS_Y = 341;

  public int CHECK_EXISTING_ITEMS_X = 1717;
  public int CHECK_EXISTING_ITEMS_Y = 68;

  public int GO_BACK_SEARCH_ITEMS_X = 1710;
  public int GO_BACK_SEARCH_ITEMS_Y = 7;

  public int RETRIEVE_ITEMS_X = 1660;
  public int RETRIEVE_ITEMS_Y = 342;

  public int NOT_ENOUGH_CAPACITY_INFO_MESSAGE_X = 1111;
  public int NOT_ENOUGH_CAPACITY_INFO_MESSAGE_Y = 547 - isRunnninInVM;

  public int PIXEL_NOT_ENOUGH_CAPACITY_X = 900;
  public int PIXEL_NOT_ENOUGH_CAPACITY_Y = 480 - isRunnninInVM;

  public int AMOUNT_BUY_OFFERS_IN_SERVER_X_TOP = 948;
  public int AMOUNT_BUY_OFFERS_IN_SERVER_Y_TOP = 490 - isRunnninInVM;
  public int AMOUNT_BUY_OFFERS_IN_SERVER_X_BOTTOM = 979;
  public int AMOUNT_BUY_OFFERS_IN_SERVER_Y_BOTTOM = 500 - isRunnninInVM;

  public int DETAIL_BUTTON_MARKET_X = 1123;
  public int DETAIL_BUTTON_MARKET_Y = 760;

  public int CLEAR_MARKET_ITEM_NAME_X = 752;
  public int CLEAR_MARKET_ITEM_NAME_Y = 720;

  public int PIXEL_SCROLL_HAS_REACHED_BOTTOM_X = 1313;
  public int PIXEL_SCROLL_HAS_REACHED_BOTTOM_Y = 714;

  public int BUY_OFFERS_END_AT_HOURS_X_TOP = 1218;
  public int BUY_OFFERS_END_AT_HOURS_Y_TOP = 506 - isRunnninInVM;
  public int BUY_OFFERS_END_AT_HOURS_X_BOTTOM = 1234;
  public int BUY_OFFERS_END_AT_HOURS_Y_BOTTOM = 522 - isRunnninInVM;

  public int BUY_OFFERS_END_AT_MINUTES_X_TOP = 1238;
  public int BUY_OFFERS_END_AT_MINUTES_Y_TOP = 506 - isRunnninInVM;
  public int BUY_OFFERS_END_AT_MINUTES_X_BOTTOM = 1254;
  public int BUY_OFFERS_END_AT_MINUTES_Y_BOTTOM = 522 - isRunnninInVM;

  public int BUY_OFFERS_END_AT_SECONDS_X_TOP = 1258;
  public int BUY_OFFERS_END_AT_SECONDS_Y_TOP = 506 - isRunnninInVM;
  public int BUY_OFFERS_END_AT_SECONDS_X_BOTTOM = 1274;
  public int BUY_OFFERS_END_AT_SECONDS_Y_BOTTOM = 522 - isRunnninInVM;

  public int MY_OFFERS_FIRST_ROW_END_AT_HOURS_X_TOP = 1118;
  public int MY_OFFERS_FIRST_ROW_END_AT_HOURS_Y_TOP = 551 - isRunnninInVM;
  public int MY_OFFERS_FIRST_ROW_AT_HOURS_X_BOTTOM = 1134;
  public int MY_OFFERS_FIRST_ROW_AT_HOURS_Y_BOTTOM = 567 - isRunnninInVM;

  public int MY_OFFERS_FIRST_ROW_AT_MINUTES_X_TOP = 1138;
  public int MY_OFFERS_FIRST_ROW_AT_MINUTES_Y_TOP = 551 - isRunnninInVM;
  public int MY_OFFERS_FIRST_ROW_AT_MINUTES_X_BOTTOM = 1154;
  public int MY_OFFERS_FIRST_ROW_AT_MINUTES_Y_BOTTOM = 567 - isRunnninInVM;

  public int MY_OFFERS_FIRST_ROW_AT_SECONDS_X_TOP = 1158;
  public int MY_OFFERS_FIRST_ROW_AT_SECONDS_Y_TOP = 551 - isRunnninInVM;
  public int MY_OFFERS_FIRST_ROW_AT_SECONDS_X_BOTTOM = 1174;
  public int MY_OFFERS_FIRST_ROW_AT_SECONDS_Y_BOTTOM = 567 - isRunnninInVM;

  public int MY_OFFERS_LAST_ROW_END_AT_HOURS_X_TOP = 1118;
  public int MY_OFFERS_LAST_ROW_END_AT_HOURS_Y_TOP = 711 - isRunnninInVM;
  public int MY_OFFERS_LAST_ROW_AT_HOURS_X_BOTTOM = 1134;
  public int MY_OFFERS_LAST_ROW_AT_HOURS_Y_BOTTOM = 727 - isRunnninInVM;

  public int MY_OFFERS_LAST_ROW_AT_MINUTES_X_TOP = 1138;
  public int MY_OFFERS_LAST_ROW_AT_MINUTES_Y_TOP = 711 - isRunnninInVM;
  public int MY_OFFERS_LAST_ROW_AT_MINUTES_X_BOTTOM = 1154;
  public int MY_OFFERS_LAST_ROW_AT_MINUTES_Y_BOTTOM = 727 - isRunnninInVM;

  public int MY_OFFERS_LAST_ROW_AT_SECONDS_X_TOP = 1158;
  public int MY_OFFERS_LAST_ROW_AT_SECONDS_Y_TOP = 711 - isRunnninInVM;
  public int MY_OFFERS_LAST_ROW_AT_SECONDS_X_BOTTOM = 1174;
  public int MY_OFFERS_LAST_ROW_AT_SECONDS_Y_BOTTOM = 727 - isRunnninInVM;
}
