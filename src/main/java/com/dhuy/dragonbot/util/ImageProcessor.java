package com.dhuy.dragonbot.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageProcessor {
  public ImageProcessor() {}

  public InputStream getInputStreamFromBufferedImage(BufferedImage original) {
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ImageIO.write(original, "png", outputStream);

      return new ByteArrayInputStream(outputStream.toByteArray());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public BufferedImage getBufferedImageFromByteArray(byte[] original) {
    ImageIcon imageIcon = new ImageIcon(original);
    Image image = imageIcon.getImage();

    BufferedImage bufferedImage =
        new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

    Graphics g = bufferedImage.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();

    return bufferedImage;
  }

  /**
   * Finds the a region in one image that best matches another, smaller, image.
   */
  public int[] findSubimage(BufferedImage im1, BufferedImage im2) {
    int w1 = im1.getWidth();
    int h1 = im1.getHeight();
    int w2 = im2.getWidth();
    int h2 = im2.getHeight();
    assert w2 <= w1 && h2 <= h1;
    // will keep track of best position found
    int bestX = 0;
    int bestY = 0;
    double lowestDiff = Double.POSITIVE_INFINITY;
    // brute-force search through whole image (slow...)
    for (int x = 0; x < w1 - w2; x++) {
      for (int y = 0; y < h1 - h2; y++) {
        double comp = compareImages(im1.getSubimage(x, y, w2, h2), im2);
        if (comp < lowestDiff) {
          bestX = x;
          bestY = y;
          lowestDiff = comp;
        }
      }
    }
    // return best location
    return new int[] {bestX, bestY};
  }

  /**
   * Determines how different two identically sized regions are.
   */
  public double compareImages(BufferedImage im1, BufferedImage im2) {
    assert im1.getHeight() == im2.getHeight() && im1.getWidth() == im2.getWidth();
    double variation = 0.0;
    for (int x = 0; x < im1.getWidth(); x++) {
      for (int y = 0; y < im1.getHeight(); y++) {
        variation += compareARGB(im1.getRGB(x, y), im2.getRGB(x, y)) / Math.sqrt(3);
      }
    }
    return variation / (im1.getWidth() * im1.getHeight());
  }

  /**
   * Calculates the difference between two ARGB colors (BufferedImage.TYPE_INT_ARGB).
   */
  private double compareARGB(int rgb1, int rgb2) {
    double r1 = (rgb1 >> 16 & 0xFF) / 255.0;
    double r2 = (rgb2 >> 16 & 0xFF) / 255.0;
    double g1 = (rgb1 >> 8 & 0xFF) / 255.0;
    double g2 = (rgb2 >> 8 & 0xFF) / 255.0;
    double b1 = (rgb1 & 0xFF) / 255.0;
    double b2 = (rgb2 & 0xFF) / 255.0;
    double a1 = (rgb1 >> 24 & 0xFF) / 255.0;
    double a2 = (rgb2 >> 24 & 0xFF) / 255.0;
    // if there is transparency, the alpha values will make difference smaller
    return a1 * a2
        * Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2));
  }
}
