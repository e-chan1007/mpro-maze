package maze.maze.element;

import java.awt.Graphics;

import maze.maze.MazeModel;
import maze.maze.player.PlayerModel;

public class CollectTaskModel extends TaskElement {
  public CollectTaskModel(MazeModel mazeModel, PlayerModel playerModel) {
    super(mazeModel, playerModel);
  }

  @Override
  public void onEnter() {
    if (!this.isTaskCompleted()) {
      this.setTaskCompleted(true);
    }
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    if (this.isTaskCompleted()) {
      g.setColor(java.awt.Color.GREEN);
    } else {
      g.setColor(java.awt.Color.ORANGE);
    }
    g.fillOval(x, y, size, size);
  }
}
