package maze.maze.tagger;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.swing.Timer;

import maze.asset.ImageManager;
import maze.asset.Sprite;
import maze.maze.MazeView;
import maze.enums.Direction;

public class TaggerView {
  private static final int FRAME_COUNT = 10;
  private static final int ANIMATION_FPS = 10;
  private static final int ANIMAEION_DELAY = 1500 / ANIMATION_FPS;
  private static final double MAGNIFICATION = 0.8;

  private final TaggerModel taggerModel;
  private MazeView mazeView;

  // スプライトの読み込み
  public static final Sprite TAGGER_WALKLEFT_SPRITE = ImageManager.loadImageAsSprite("/tagger/WispLeft.png", 32, 32);
  public static final Sprite TAGGER_WALKRIGHT_SPRITE = ImageManager.loadImageAsSprite("/tagger/WispRight.png", 32, 32);

  private final EnumMap<Direction, List<BufferedImage>> walkSprites;
  private int currentFrame;
  private final Timer animationTimer;

  // コンストラクタ
  public TaggerView(TaggerModel taggerModel, MazeView mazeView) {
    this.taggerModel = taggerModel;
    this.mazeView = mazeView;
    this.currentFrame = 0;

    walkSprites = new EnumMap<>(Direction.class);
    walkSprites.put(Direction.LEFT, new ArrayList<>());
    walkSprites.put(Direction.RIGHT, new ArrayList<>());
    walkSprites.put(Direction.UP, new ArrayList<>());
    walkSprites.put(Direction.DOWN, new ArrayList<>());

    // スプライトの読み込み *上下のスプライトは読み込まない
    for (int i = 0; i < FRAME_COUNT; i++) {
      walkSprites.get(Direction.LEFT).add(TAGGER_WALKLEFT_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.RIGHT).add(TAGGER_WALKRIGHT_SPRITE.getImageAt(i, 0));
    }

    this.animationTimer = new Timer(ANIMAEION_DELAY, e -> {
      if (taggerModel.getMazeModel().isPaused()) {
        return;
      }
      Direction direction = taggerModel.getCurrentDirection();
      currentFrame = (currentFrame + 1) % walkSprites.get(direction).size();
      mazeView.repaint();
    });
    this.animationTimer.start();
  }

  // 鬼の描画処理  
  public void draw(Graphics g, int anchorX, int anchorY) {
    Graphics2D g2d = (Graphics2D) g;
    Direction direction = taggerModel.getCurrentDirection();
    BufferedImage taggerSprite = walkSprites.get(direction).get(currentFrame);
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
