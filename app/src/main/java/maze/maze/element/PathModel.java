package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import maze.assets.ImageManager;
import maze.maze.MazeModel;

public class PathModel extends MazeElement {
  private static final Random random = new Random();
  private BufferedImage pathSprite;
  public PathModel() {
    // this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(
    //   6 + random.nextInt(4),
    //   0 + random.nextInt(3)
    // );
  }

  @Override
  public void onAllInitiated(MazeModel mazeModel, int myX, int myY) {
    boolean isTopWall = mazeModel.getElementAt(myX, myY - 1) instanceof WallModel;
    boolean isLeftWall = mazeModel.getElementAt(myX - 1, myY) instanceof WallModel;
    boolean isRightWall = mazeModel.getElementAt(myX + 1, myY) instanceof WallModel;
    boolean isBottomWall = mazeModel.getElementAt(myX, myY + 1) instanceof WallModel;

    if(!(isTopWall || isBottomWall || isLeftWall || isRightWall)) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(7, 0, 10, 3);
    } else if(isTopWall) {
      if(isLeftWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 1);
      } else if(isRightWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 1);
      } else {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 1, 3, 1);
      }
    } else if(isBottomWall) {
      if(isLeftWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 3);
      } else if(isRightWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 3);
      } else {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 3, 3, 3);
      }
    } else if(isLeftWall) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 2);
    } else {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 2);
    }  
  }
  
  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.drawImage(pathSprite, x, y, size, size, null);
  }

}
