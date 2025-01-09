package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;

public class WallModel extends MazeElement {
  private final WallType wallType;

  public WallModel(WallType wallType) {
    this.wallType = wallType;
  }

  @Override
  public boolean canEnter() {
    return false;
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(wallType.color);
    g.fillRect(x, y, size, size);
  }

  public enum WallType {
    LEFT_EDGE(Color.PINK),
    RIGHT_EDGE(Color.RED),
    TOP_EDGE(Color.CYAN),
    BOTTOM_EDGE(Color.BLUE),
    LEFT_TOP_CORNER(Color.GRAY),
    RIGHT_TOP_CORNER(Color.GRAY),
    LEFT_BOTTOM_CORNER(Color.GRAY),
    RIGHT_BOTTOM_CORNER(Color.GRAY);

    public final Color color; // TODO: Color→画像パスとかにする

    WallType(Color color) {
      this.color = color;
    }
  }
}
