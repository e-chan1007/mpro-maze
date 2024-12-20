import java.awt.Color;
import java.awt.Graphics;

public class PathModel extends MazeElement {
  @Override
  public boolean canEnter() { return true; }

  @Override
  public void onEnter(MazeModel mazeModel, PlayerModel playerModel) {}

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(Color.GREEN);
    g.drawRect(x, y, size, size);
  }
  
}
