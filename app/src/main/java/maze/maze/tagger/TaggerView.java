package maze.maze.tagger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import maze.asset.ImageManager;
import maze.asset.Sprite;
import maze.maze.MazeView;
import maze.maze.tagger.TaggerModel.Direction;;

public class TaggerView {
  private final TaggerModel taggerModel;
  private MazeView mazeView;

  public static final Sprite TAGGER_WALKLEFT_SPRITE = ImageManager.loadImageAsSprite("/tagger/WispLeft.png", 32, 32);
  public static final Sprite TAGGER_WALKRIGHT_SPRITE = ImageManager.loadImageAsSprite("/tagger/WispRight.png", 32, 32);

  private Map<Direction, List<BufferedImage>> walkSprites;

  private int currentFrame;
  private Timer animationTimer;

  public TaggerView(TaggerModel taggerModel, MazeView mazeView) {
    this.taggerModel = taggerModel;
    this.mazeView = mazeView;

    walkSprites = new HashMap<>();

    walkSprites.put(Direction.LEFT, new ArrayList<>());
    walkSprites.put(Direction.RIGHT, new ArrayList<>());
    walkSprites.put(Direction.UP, new ArrayList<>());
    walkSprites.put(Direction.DOWN, new ArrayList<>());

    for (int i = 0; i < 10; i++) {
      walkSprites.get(Direction.LEFT).add(TAGGER_WALKLEFT_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.RIGHT).add(TAGGER_WALKRIGHT_SPRITE.getImageAt(i, 0));
      // walkSprites.get(Direction.UP).add(TAGGER_WALK_SPRITE.getImageAt(i, 0));
      // walkSprites.get(Direction.DOWN).add(TAGGER_WALK_SPRITE.getImageAt(i, 0));
    }

    this.currentFrame = 0;

    this.animationTimer = new Timer(1500 / 10, e -> {
      if (taggerModel.getMazeModel().isPaused()) {
        return;
      }

      Direction direction = taggerModel.getCurrentDirection();
      currentFrame = (currentFrame + 1) % walkSprites.get(direction).size();
    });
    this.animationTimer.start();
  }

  public void draw(Graphics g, int anchorX, int anchorY) {
    Graphics2D g2d = (Graphics2D) g;
    double MAGNIFICATION = 0.8;

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
