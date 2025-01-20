package maze.maze.tagger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.Timer;

import maze.assets.ImageManager;
import maze.maze.MazeView;
import maze.maze.tagger.TaggerModel.Direction;;

public class TaggerView {
  private TaggerModel taggerModel;
  private MazeView mazeView;

  private Map<Direction, List<BufferedImage>> walkSprites;

  private int currentFrame;
  private Timer animationTimer;

  public TaggerView(TaggerModel taggerModel, MazeView mazeView) {
    this.taggerModel = taggerModel;
    this.mazeView = mazeView;

    walkSprites = new HashMap<>();

    walkSprites.put(Direction.LEFT, new ArrayList<>());
    walkSprites.put(Direction.RIGHT, new ArrayList<>());

    for (int i = 0; i < 8; i++) {
      walkSprites.get(Direction.LEFT).add(ImageManager.TAGGER_WALK_SPRITE.getImageAt(i, 1));
      walkSprites.get(Direction.RIGHT).add(ImageManager.TAGGER_WALK_SPRITE.getImageAt(i, 1));
    }

    this.currentFrame = 0;

    this.animationTimer = new Timer(1500 / 10, e -> {
      if (taggerModel.getMazeModel().isPaused()) {
        return;
      }

      Direction direction = taggerModel.getCurrentDirection();
      currentFrame = (currentFrame + 1) % walkSprites.get(direction).size();
      mazeView.repaint();
    });
    this.animationTimer.start();
  }

  public void draw(Graphics g, int anchorX, int anchorY) {
    Graphics2D g2d = (Graphics2D) g;
    double MAGNIFICATION = 1.5;

    BufferedImage taggerSprite = null;
    Direction direction = taggerModel.getCurrentDirection();

    taggerSprite = walkSprites.get(direction).get(currentFrame);

    if (taggerSprite != null) {
      int imageWidth = taggerSprite.getWidth();
      int imageHeight = taggerSprite.getHeight();
      int cellSize = mazeView.getMazeCellSize();

      double scale = Math.min((double) cellSize / imageWidth, (double) cellSize / imageHeight) * MAGNIFICATION;

      int scaleWidth = (int) (imageWidth * scale);
      int scaleHeight = (int) (imageHeight * scale);

      int xOffset = (cellSize - scaleWidth) / 2;
      int yOffset = (cellSize - scaleHeight) / 2;

      g2d.drawImage(taggerSprite,
          anchorX + (int) (taggerModel.getTaggerX() * cellSize) + xOffset,
          anchorY + (int) (taggerModel.getTaggerY() * cellSize) + yOffset,
          scaleWidth,
          scaleHeight,
          null);

    }
  }
}
