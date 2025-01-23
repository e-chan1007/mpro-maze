package maze.maze.element;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import maze.asset.ImageManager;

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
    g.drawImage(wallType.sprite, x, y, size, size, null);
  }

  public enum WallType {
    LEFT_EDGE(0, 1),
    RIGHT_EDGE(5, 1),
    TOP_EDGE(1, 0),
    BOTTOM_EDGE(1, 4),
    LEFT_TOP_CORNER(0, 0),
    RIGHT_TOP_CORNER(5, 0),
    LEFT_BOTTOM_CORNER(0, 4),
    RIGHT_BOTTOM_CORNER(5, 4),
    TOP_LEFT_CORNER_2(4, 5),
    TOP_RIGHT_CORNER_2(5, 5),
    BOTTOM_LEFT_CORNER_2(1, 0),
    BOTTOM_RIGHT_CORNER_2(1, 0),
    NO_WALL(8, 7);

    public final BufferedImage sprite;

    WallType(int spriteX, int spriteY) {
      this.sprite = ImageManager.DUNGEON_SPRITE.getImageAt(spriteX, spriteY);
    }
  }
}
