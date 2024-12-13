import java.awt.Color;
import java.awt.Graphics;

public class PathModel extends MazeElement {
  private final boolean canEnter = true;

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(Color.GREEN);
    g.drawRect(x, y, size, size);
  }
  
}
