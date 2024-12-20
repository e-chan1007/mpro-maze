package maze.maze.element;

import java.awt.Color;
import java.awt.Graphics;

import maze.maze.*;
import maze.maze.player.*;

public class GoalModel extends MazeElement {
  @Override
  public boolean canEnter() {
    return true;
  }

  @Override
  public void onEnter(MazeModel mazeModel, PlayerModel playerModel) {}

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    g.setColor(Color.BLUE);
    g.fillRect(x, y, size, size);
  }  
}
