import java.awt.Color;
import java.awt.Graphics;

public class WallModel extends MazeElement {

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(Color.BLACK);
    g.fillRect(x, y, size, size);
  }
  
}
