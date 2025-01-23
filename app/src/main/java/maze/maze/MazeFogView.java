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

    int x = (width / 4 + 75);
    int y = (height / 4 + 75);
    Area outer = new Area(g.getClipBounds());
    Ellipse2D.Float inner = new Ellipse2D.Float(x, y, width / 2f - 150, height / 2f - 150);
    outer.subtract(new Area(inner));// remove the ellipse from the original area
    Color fogColor = new Color(0, 0, 0, 0.9f);
    g2d.setColor(fogColor);
    g2d.fill(outer);

    int x2 = (width / 4 + 150);
    int y2 = (height / 4 + 150);
    Area outer2 = new Area(g.getClipBounds());
    Ellipse2D.Float inner2 = new Ellipse2D.Float(x2, y2, width / 2f - 300, height / 2f - 300);
    outer2.subtract(new Area(inner2));// remove the ellipse from the original area
    Color fogColor2 = new Color(0, 0, 0, 0.7f);
    g2d.setColor(fogColor2);
    g2d.fill(outer2);

    int x3 = (width / 4 + 225);
    int y3 = (height / 4 + 225);
    Area outer3 = new Area(g.getClipBounds());
    Ellipse2D.Float inner3 = new Ellipse2D.Float(x3, y3, width / 2f - 450, height / 2f - 450);
    outer3.subtract(new Area(inner3));// remove the ellipse from the original area
    Color fogColor3 = new Color(0, 0, 0, 0.5f);
    g2d.setColor(fogColor3);
    g2d.fill(outer3);

    int x4 = (width / 4 + 300);
    int y4 = (height / 4 + 300);
    Area outer4 = new Area(g.getClipBounds());
    Ellipse2D.Float inner4 = new Ellipse2D.Float(x4, y4, width / 2f - 600, height / 2f - 600);
    outer4.subtract(new Area(inner4));// remove the ellipse from the original area
    Color fogColor4 = new Color(0, 0, 0, 0.3f);
    g2d.setColor(fogColor4);
    g2d.fill(outer4);

    int x5 = (width / 4 + 375);
    int y5 = (height / 4 + 375);
    Area outer5 = new Area(g.getClipBounds());
    Ellipse2D.Float inner5 = new Ellipse2D.Float(x5, y5, width / 2f - 750, height / 2f - 750);
    outer5.subtract(new Area(inner5));// remove the ellipse from the original area
    Color fogColor5 = new Color(0, 0, 0, 0.1f);
    g2d.setColor(fogColor5);
    g2d.fill(outer5);
  }
}
