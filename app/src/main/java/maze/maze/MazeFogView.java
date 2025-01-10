package maze.maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class MazeFogView {
  public static void draw(Graphics g, int width, int height) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // int cx = (int) (width / 2);
    // int cy = (int) (height / 2);
    // for(float radiusFactor = 0.3f; radiusFactor <= 1.0f; radiusFactor += 0.1f) {      
    //   Area outer = new Area(g.getClipBounds());
    //   int radiusX = (int) (width * radiusFactor / 2);
    //   int radiusY = (int) (height * radiusFactor / 2);
    //   Ellipse2D.Float circle = new Ellipse2D.Float(cx - radiusX, cy - radiusY, radiusX * 2, radiusY * 2);
    //   outer.subtract(new Area(circle));// remove the circle from the original area

    //   Color fogColor = new Color(0, 0, 0, (int) (255 * (1 - radiusFactor)));
    //   g2d.setColor(fogColor);
    //   g2d.fill(outer);
    // }
    int x = (width / 4);
    int y = (height / 4);
    Area outer = new Area(g.getClipBounds());
    Ellipse2D.Float inner = new Ellipse2D.Float(x, y, width / 2f, height / 2f);
    outer.subtract(new Area(inner));// remove the ellipse from the original area
    Color fogColor = new Color(0, 0, 0, 0.8f);
    g2d.setColor(fogColor);
    g2d.fill(outer);
  }
}
