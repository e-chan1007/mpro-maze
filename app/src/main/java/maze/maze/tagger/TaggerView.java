package maze.maze.tagger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class TaggerView {
  private TaggerModel taggerModel;

  public TaggerView(TaggerModel taggerModel) {
    this.taggerModel = taggerModel;
  }

  public void draw(Graphics g) {
    g.setColor(Color.yellow);
    Graphics2D g2d = (Graphics2D) g;
    int mazeCellSize = taggerModel.getMazeModel().getView().getMazeCellSize();
    g2d.fill(new Ellipse2D.Float(
        taggerModel.getTaggerX() * mazeCellSize,
        taggerModel.getTaggerY() * mazeCellSize,
        mazeCellSize,
        mazeCellSize));
  }
}
