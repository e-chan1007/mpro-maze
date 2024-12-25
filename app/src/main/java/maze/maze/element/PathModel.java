package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;

import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;

public class PathModel extends MazeElement {
  public PathModel(MazeModel mazeModel, PlayerModel playerModel) {
    super(mazeModel, playerModel);
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(Color.GREEN);
    g.drawRect(x, y, size, size);
  }

}
