package com.dhuy.dragonbot.util;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.dhuy.dragonbot.modules.Screenshot;
import net.coobird.thumbnailator.Thumbnails;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRHelper {
  private final String TESSERACT_DATA_PATH = "C:/Tesseract";

  private Tesseract tesseract;
  private Screenshot screenshotModule;

  public OCRHelper() throws AWTException {
    tesseract = new Tesseract();
    screenshotModule = new Screenshot();

    setup(TESSERACT_DATA_PATH);
  }

  private void setup(String dataPath) {
    tesseract.setDatapath(dataPath);
  }

  private BufferedImage resize(BufferedImage image, int newWidth, int newHeight)
      throws IOException {
    return Thumbnails.of(image).forceSize(newWidth, newHeight).asBufferedImage();
  }

  private int[] getSizeFromTwoPoints(int topX, int topY, int bottomX, int bottomY) {
    int[] size = new int[2];

    size[0] = (bottomX - topX);
    size[1] = (bottomY - topY);

    return size;
  }

  public String getText(BufferedImage image) throws TesseractException {
    return tesseract.doOCR(image);
  }

  public BufferedImage resizeImage(BufferedImage image, int factor) throws IOException {
    return resize(image, image.getWidth() * factor, image.getHeight() * factor);
  }

  public BufferedImage getImageFromCoordinates(int topX, int topY, int bottomX, int bottomY)
      throws IOException, TesseractException {
    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int[] rectangleSize = getSizeFromTwoPoints(topX, topY, bottomX, bottomY);

    return currentScreenshot.getSubimage(topX, topY, rectangleSize[0], rectangleSize[1]);
  }

  public BufferedImage getImageFromCoordinates(int topX, int topY, int bottomX, int bottomY,
      BufferedImage currentScreenshot) throws IOException, TesseractException {
    int[] rectangleSize = getSizeFromTwoPoints(topX, topY, bottomX, bottomY);

    return currentScreenshot.getSubimage(topX, topY, rectangleSize[0], rectangleSize[1]);
  }

  public BufferedImage getImageFromCoordinates(int[] coordinates, BufferedImage currentScreenshot)
      throws IOException, TesseractException {
    int[] rectangleSize =
        getSizeFromTwoPoints(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);

    return currentScreenshot.getSubimage(coordinates[0], coordinates[1], rectangleSize[0],
        rectangleSize[1]);
  }

  public BufferedImage getImageFromCoordinates(int topX, int topY, int bottomX, int bottomY,
      int currentRow) throws IOException, TesseractException {
    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int recalculatedTopY;
    int[] rectangleSize = getSizeFromTwoPoints(topX, topY, bottomX, bottomY);

    if (currentRow == 0) {
      recalculatedTopY = topY;
    } else {
      recalculatedTopY = topY + (rectangleSize[1] * currentRow);
    }

    return currentScreenshot.getSubimage(topX, recalculatedTopY, rectangleSize[0],
        rectangleSize[1]);
  }

  public BufferedImage getImageFromCoordinates(int topX, int topY, int bottomX, int bottomY,
      int currentRow, BufferedImage currentScreenshot) throws IOException, TesseractException {
    int recalculatedTopY;
    int[] rectangleSize = getSizeFromTwoPoints(topX, topY, bottomX, bottomY);

    if (currentRow == 0) {
      recalculatedTopY = topY;
    } else {
      recalculatedTopY = topY + (rectangleSize[1] * currentRow);
    }

    return currentScreenshot.getSubimage(topX, recalculatedTopY, rectangleSize[0],
        rectangleSize[1]);
  }

  public BufferedImage getImageFromCoordinates(int[] coordinates, int currentRow,
      BufferedImage currentScreenshot) throws IOException, TesseractException {
    int recalculatedTopY;
    int[] rectangleSize =
        getSizeFromTwoPoints(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);

    if (currentRow == 0) {
      recalculatedTopY = coordinates[1];
    } else {
      recalculatedTopY = coordinates[1] + (rectangleSize[1] * currentRow);
    }

    return currentScreenshot.getSubimage(coordinates[0], recalculatedTopY, rectangleSize[0],
        rectangleSize[1]);
  }

  public String getTextFromImage(int topX, int topY, int bottomX, int bottomY,
      BufferedImage currentScreenshot) throws IOException, TesseractException {
    int rateToResizeImage = 5;
    int[] rectangleSize = getSizeFromTwoPoints(topX, topY, bottomX, bottomY);

    BufferedImage screenFullImage =
        resize(currentScreenshot.getSubimage(topX, topY, rectangleSize[0], rectangleSize[1]),
            rectangleSize[0] * rateToResizeImage, rectangleSize[1] * rateToResizeImage);

    return getText(screenFullImage);
  }

  public String getTextFromImage(int topX, int topY, int bottomX, int bottomY)
      throws IOException, TesseractException {
    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int rateToResizeImage = 5;
    int[] rectangleSize = getSizeFromTwoPoints(topX, topY, bottomX, bottomY);

    BufferedImage screenFullImage =
        resize(currentScreenshot.getSubimage(topX, topY, rectangleSize[0], rectangleSize[1]),
            rectangleSize[0] * rateToResizeImage, rectangleSize[1] * rateToResizeImage);

    return getText(screenFullImage);
  }

  public String getTextFromImage(int topX, int topY, int bottomX, int bottomY, int currentRow)
      throws IOException, TesseractException {
    BufferedImage currentScreenshot = screenshotModule.execute(this);

    int recalculatedTopY;
    int rateToResizeImage = 5;
    int[] rectangleSize = getSizeFromTwoPoints(topX, topY, bottomX, bottomY);

    if (currentRow == 0) {
      recalculatedTopY = topY;
    } else {
      recalculatedTopY = topY + (rectangleSize[1] * currentRow);
    }

    BufferedImage screenFullImage = resize(
        currentScreenshot.getSubimage(topX, recalculatedTopY, rectangleSize[0], rectangleSize[1]),
        rectangleSize[0] * rateToResizeImage, rectangleSize[1] * rateToResizeImage);

    return getText(screenFullImage);
  }
}
