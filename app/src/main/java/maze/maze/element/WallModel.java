package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;

import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;

public class WallModel extends MazeElement {
  public WallModel(MazeModel mazeModel, PlayerModel playerModel) {
    super(mazeModel, playerModel);
  }

  @Override
  public boolean canEnter() {
    return false;
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(Color.BLACK);
    g.fillRect(x, y, size, size);
  }

}
