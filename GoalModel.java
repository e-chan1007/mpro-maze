import java.awt.Color;
import java.awt.Graphics;

public class GoalModel extends MazeElement {
  @Override
  public boolean canEnter() {
    return true;
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(Color.BLUE);
    g.fillRect(x, y, size, size);
  }
  
}
