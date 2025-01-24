package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import maze.maze.MazeModel;
import maze.window.AppScreenManager;
import maze.window.screen.MazeGoalScreen;
import maze.asset.ImageManager;

public class GoalModel extends MazeElement {
  private final MazeModel mazeModel;
  private final BufferedImage INITIAL_IMAGE = ImageManager.DUNGEON_SPRITE.getImageAt(7, 7);
  private final BufferedImage DONE_IMAGE = ImageManager.DUNGEON_SPRITE.getImageAt(4, 7);
  private BufferedImage pathSprite;
  private int ovalX, ovalY;

  public GoalModel(MazeModel mazeModel) {
    this.mazeModel = mazeModel;
  }

  @Override
  public void onAllInitiated(MazeModel mazeModel, int x, int y) {
    this.ovalX = x;
    this.ovalY = y;

    boolean isTopWall = mazeModel.getElementAt(x, y - 1) instanceof WallModel;
    boolean isLeftWall = mazeModel.getElementAt(x - 1, y) instanceof WallModel;
    boolean isRightWall = mazeModel.getElementAt(x + 1, y) instanceof WallModel;
    boolean isBottomWall = mazeModel.getElementAt(x, y + 1) instanceof WallModel;

    if (!(isTopWall || isBottomWall || isLeftWall || isRightWall)) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(7, 0, 10, 3);
    } else if (isTopWall) {
      if (isLeftWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 1);
      } else if (isRightWall) {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 1);
      } else {
        this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 1, 3, 1);
      }
    } else if (isBottomWall) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getRandomImage(2, 3, 3, 3);
    } else if (isLeftWall) {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(1, 2);
    } else {
      this.pathSprite = ImageManager.DUNGEON_SPRITE.getImageAt(4, 2);
    }

  }

  @Override
  public void onEnter() {
    if (mazeModel.canGoal()) {
      System.out.println("Goal!");
      AppScreenManager.getInstance().push(new MazeGoalScreen(mazeModel));
    } else {
      System.out.println("Not all tasks are completed.");
    }
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.drawImage(pathSprite, x, y, size, size, null);

    int goalObjectSize = size / 5 * 4;
    int goalObjectX = x + size / 2 - goalObjectSize / 2;
    int goalObjectY = y + size / 2 - goalObjectSize / 2;

    if (mazeModel.canGoal()) {
      g.drawImage(DONE_IMAGE, goalObjectX, goalObjectY, goalObjectSize, goalObjectSize, null);
    } else {
      g.drawImage(INITIAL_IMAGE, goalObjectX, goalObjectY, goalObjectSize, goalObjectSize, null);
    }
  }
}
