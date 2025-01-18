package maze.maze.element;

import java.awt.Graphics;

public class CollectTaskModel extends TaskElement {
  @Override
  public void onEnter() {
    if (!this.isCompleted()) {
      this.setCompleted(true);
    }
  }

  @Override
  public void draw(Graphics g, int x, int y, int size) {
    if (this.isCompleted()) {
      g.setColor(java.awt.Color.GREEN);
    } else {
      g.setColor(java.awt.Color.ORANGE);
    }
    g.fillOval(x, y, size, size);
  }
}
