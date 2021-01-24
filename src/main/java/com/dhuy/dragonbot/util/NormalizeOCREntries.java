package com.dhuy.dragonbot.util;

import java.util.regex.Pattern;

public class NormalizeOCREntries {
  public String normalizeNumber(String number) {
    return number.trim().replaceAll(" ", "").replaceAll(",", ", ").replaceAll("l", "1")
        .replaceAll("S", "5").replaceAll("z", ":").replaceAll("O", "0");
  }

  public String normalizeId(String id) {
    return id.trim().replaceAll(" ", "").replaceAll(",", ", ").replaceAll("l", "1")
        .replaceAll("S", "5").replaceAll("z", ":").replaceAll("O", "0")
        .replaceAll(Pattern.quote("''"), "").replaceAll(Pattern.quote("'"), "")
        .replaceAll(Pattern.quote("?"), "7").replaceAll(Pattern.quote("]"), "1")
        .replaceAll(Pattern.quote("111"), "11").replaceAll(Pattern.quote("411"), "41")
        .replaceAll(Pattern.quote("~"), "-").replaceAll(Pattern.quote("—"), "-")
        .replaceAll(Pattern.quote("202141"), "2021-0").replaceAll(Pattern.quote("5771e"), "57:10")
        .replaceAll(Pattern.quote("\""), "").replaceAll(Pattern.quote("’"), "");
  }

  public String newNormalizeId(String id) {
    return id.trim().replaceAll(Pattern.quote("'"), "").replaceAll("[.]", "").replaceAll("l", "1")
        .replaceAll("S", "5").replaceAll("O", "0").replaceAll(Pattern.quote("?"), "7")
        .replaceAll(Pattern.quote("]"), "1");
  }

  public String normalizePrice(String price) {
    return price.trim().replaceAll(Pattern.quote("'"), "").replaceAll("[.]", "").replaceAll(" ", "")
        .replaceAll(",", "").replaceAll("l", "1").replaceAll("O", "0")
        .replaceAll(Pattern.quote("e"), "6").replaceAll(Pattern.quote("?"), "7")
        .replaceAll("S", "5").replaceAll(Pattern.quote("]"), "1")
        .replaceAll(Pattern.quote("’"), "");
  }
}
