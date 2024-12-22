package maze.maze.tagger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import maze.maze.*;

public class TaggerView {
    private TaggerModel taggerModel;

    public TaggerView(TaggerModel taggerModel) {
      this.taggerModel = taggerModel;
    }

    public void draw(Graphics g) {
      g.setColor(Color.yellow);
      Graphics2D g2d = (Graphics2D) g;
      g2d.fill(new Ellipse2D.Float(
        taggerModel.getTaggerX() * MazeModel.MAZE_CELL_SIZE,
        taggerModel.getTaggerY() * MazeModel.MAZE_CELL_SIZE,
        MazeModel.MAZE_CELL_SIZE,
        MazeModel.MAZE_CELL_SIZE
      ));
    }
}
