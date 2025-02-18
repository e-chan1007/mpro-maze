package maze.maze.player;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.EnumMap;

import javax.swing.Timer;

import maze.asset.ImageManager;
import maze.asset.Sprite;
import maze.enums.Direction;
import maze.maze.MazeView;

public class PlayerView {
  // スプライトのフレーム数
  private static final int SPRITE_FRAME_COUNT = 8;
  // スプライトの幅と高さ
  private static final int SPRITE_WIDTH = 48;
  private static final int SPRITE_HEIGHT = 64;
  // 拡大率
  private static final double MAGNIFICATION = 2.0;
  // アニメーションのFPSと遅延
  private static final int ANIMATION_FPS = 10;
  private static final int ANIMATION_DELAY = 1000 / ANIMATION_FPS;

  private PlayerModel playerModel;
  private MazeView mazeView;

  // スプライトのマップ
  private Map<Direction, List<BufferedImage>> idleSprites;
  private Map<Direction, List<BufferedImage>> walkSprites;

  // スプライトの読み込み
  private final Sprite PLAYER_IDLE_UP_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_up.png", SPRITE_WIDTH, SPRITE_HEIGHT);
  private final Sprite PLAYER_IDLE_DOWN_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_down.png", SPRITE_WIDTH, SPRITE_HEIGHT);
  private final Sprite PLAYER_IDLE_LEFT_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_left.png", SPRITE_WIDTH, SPRITE_HEIGHT);
  private final Sprite PLAYER_IDLE_RIGHT_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_right.png", SPRITE_WIDTH, SPRITE_HEIGHT);

  private final Sprite PLAYER_WALKUP_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_up.png", SPRITE_WIDTH, SPRITE_HEIGHT);
  private final Sprite PLAYER_WALKDOWN_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_down.png", SPRITE_WIDTH, SPRITE_HEIGHT);
  private final Sprite PLAYER_WALKLEFT_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_left.png", SPRITE_WIDTH, SPRITE_HEIGHT);
  private final Sprite PLAYER_WALKRIGHT_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_right.png", SPRITE_WIDTH, SPRITE_HEIGHT);

  // 現在のフレーム
  private int currentFrame;
  // アニメーション用のタイマー
  private final Timer animationTimer;

  // コンストラクタ
  public PlayerView(PlayerModel playerModel, MazeView mazeView) {
    this.playerModel = playerModel;
    this.mazeView = mazeView;

    // スプライトのマップの初期化
    idleSprites = new EnumMap<>(Direction.class);
    walkSprites = new EnumMap<>(Direction.class);

    // スプライトのマップにスプライトを追加
    for (Direction direction : Direction.values()) {
      idleSprites.put(direction, new ArrayList<>());
      walkSprites.put(direction, new ArrayList<>());
    }
    // スプライトの読み込み
    loadSprites();

    this.currentFrame = 0;

    // アニメーション用のタイマー
    this.animationTimer = new Timer(ANIMATION_DELAY, e -> {
      if (playerModel.mazeModel.isPaused()) {
        return;
      }
      updateAnimationFrame();
      mazeView.repaint();
    });
    this.animationTimer.start();
  }

  // スプライトの読み込み
  private void loadSprites() {
    for (int i = 0; i < SPRITE_FRAME_COUNT; i++) {
      idleSprites.get(Direction.UP).add(PLAYER_IDLE_UP_SPRITE.getImageAt(i, 0));
      idleSprites.get(Direction.DOWN).add(PLAYER_IDLE_DOWN_SPRITE.getImageAt(i, 0));
      idleSprites.get(Direction.LEFT).add(PLAYER_IDLE_LEFT_SPRITE.getImageAt(i, 0));
      idleSprites.get(Direction.RIGHT).add(PLAYER_IDLE_RIGHT_SPRITE.getImageAt(i, 0));

      walkSprites.get(Direction.UP).add(PLAYER_WALKUP_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.DOWN).add(PLAYER_WALKDOWN_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.LEFT).add(PLAYER_WALKLEFT_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.RIGHT).add(PLAYER_WALKRIGHT_SPRITE.getImageAt(i, 0));
    }
  }

  // アニメーションフレームの更新
  private void updateAnimationFrame() {
    Direction direction = playerModel.getCurrentDirection();
    if (playerModel.isIdle()) {
      currentFrame = (currentFrame + 1) % idleSprites.get(direction).size();
    } else {
      currentFrame = (currentFrame + 1) % walkSprites.get(direction).size();
    }
  }

  // プレイヤーの描画処理
  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    int cellSize = mazeView.getMazeCellSize();

    BufferedImage playerSprite = null;
    if (playerModel.isIdle()) {
      playerSprite = idleSprites.get(playerModel.getCurrentDirection()).get(currentFrame);
    } else {
      playerSprite = walkSprites.get(playerModel.getCurrentDirection()).get(currentFrame);
    }

    if (playerSprite != null) {
      int imageWidth = playerSprite.getWidth();
      int imageHeight = playerSprite.getHeight();

      // 縦横比を維持しながら、画像の倍率調整 */
      double scale = Math.min((double) cellSize / imageWidth, (double) cellSize / imageHeight) * MAGNIFICATION;
      int scaleWidth = (int) (imageWidth * scale);
      int scaleHeight = (int) (imageHeight * scale);
      int xOffset = (cellSize - scaleWidth) / 2;
      int yOffset = (cellSize - scaleHeight) / 2;

      int mazeX = (int) ((playerModel.getMazeWidth() - 1) * mazeView.getMazeCellSize() / 2);
      int mazeY = (int) ((playerModel.getMazeHeight() - 1) * mazeView.getMazeCellSize() / 2);

      g2d.drawImage(playerSprite,
          mazeX + xOffset,
          mazeY + yOffset,
          scaleWidth,
          scaleHeight,
          null);
    }
  }
}
