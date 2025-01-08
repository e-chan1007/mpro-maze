package maze.maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class MazeFogView {
  public static void draw(Graphics g, int width, int height) {
    Graphics2D g2d = (Graphics2D) g;

    Area outer = new Area(g.getClipBounds());
    int x = (width / 4);
    int y = (height / 4);
    Ellipse2D.Float inner = new Ellipse2D.Float(x, y, width / 2f, height / 2f);
    outer.subtract(new Area(inner));// remove the ellipse from the original area

    g2d.setColor(Color.BLACK);
    g2d.fill(outer);
  }
}
