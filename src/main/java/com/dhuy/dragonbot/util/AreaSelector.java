package com.dhuy.dragonbot.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AreaSelector {
  private Rectangle captureRect;

  public AreaSelector() {}

  public Rectangle getSelectedArea(final BufferedImage screen, String windowTitle) {
    final BufferedImage screenCopy =
        new BufferedImage(screen.getWidth(), screen.getHeight(), screen.getType());
    final JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));
    JScrollPane screenScroll = new JScrollPane(screenLabel);

    screenScroll.setPreferredSize(
        new Dimension((int) (screen.getWidth() / 1.02), (int) (screen.getHeight() / 1.09)));

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(screenScroll, BorderLayout.CENTER);

    repaint(screen, screenCopy);
    screenLabel.repaint();

    screenLabel.addMouseMotionListener(new MouseMotionAdapter() {
      Point start = new Point();

      @Override
      public void mouseMoved(MouseEvent me) {
        start = me.getPoint();
        repaint(screen, screenCopy);
        screenLabel.repaint();
      }

      @Override
      public void mouseDragged(MouseEvent me) {
        Point end = me.getPoint();
        captureRect = new Rectangle(start, new Dimension(end.x - start.x, end.y - start.y));
        repaint(screen, screenCopy);
        screenLabel.repaint();
      }
    });

    JOptionPane.showMessageDialog(null, panel, windowTitle, JOptionPane.PLAIN_MESSAGE);

    return captureRect;
  }

  private void repaint(BufferedImage orig, BufferedImage copy) {
    Graphics2D g = copy.createGraphics();
    g.drawImage(orig, 0, 0, null);

    if (captureRect != null) {
      g.setColor(Color.GREEN);
      g.draw(captureRect);
      g.setColor(new Color(255, 255, 255, 150));
      g.fill(captureRect);
    }

    g.dispose();
  }
}
