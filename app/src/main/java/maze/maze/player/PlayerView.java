package maze.maze.player;

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
import maze.enums.Direction;
import maze.maze.MazeView;

public class PlayerView {
  private PlayerModel playerModel;
  private MazeView mazeView;

  private Map<Direction, List<BufferedImage>> idleSprites;
  private Map<Direction, List<BufferedImage>> walkSprites;

  private final Sprite PLAYER_IDLE_UP_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_up.png", 48,
      64);
  private final Sprite PLAYER_IDLE_DOWN_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_down.png", 48,
      64);
  private final Sprite PLAYER_IDLE_LEFT_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_left.png", 48,
      64);
  private final Sprite PLAYER_IDLE_RIGHT_SPRITE = ImageManager.loadImageAsSprite("/player/idle/idle_right.png",
      48, 64);
  private final Sprite PLAYER_WALKUP_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_up.png", 48,
      64);
  private final Sprite PLAYER_WALKDOWN_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_down.png", 48,
      64);
  private final Sprite PLAYER_WALKLEFT_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_left.png", 48,
      64);
  private final Sprite PLAYER_WALKRIGHT_SPRITE = ImageManager.loadImageAsSprite("/player/walk/walk_right.png",
      48, 64);

  private int currentFrame;
  private final Timer animationTimer;

  public PlayerView(PlayerModel playerModel, MazeView mazeView) {
    this.playerModel = playerModel;
    this.mazeView = mazeView;

    idleSprites = new HashMap<>();
    walkSprites = new HashMap<>();

    for (Direction direction : Direction.values()) {
      idleSprites.put(direction, new ArrayList<>());
      walkSprites.put(direction, new ArrayList<>());
    }

    for (int i = 0; i < 8; i++) {
      idleSprites.get(Direction.UP).add(PLAYER_IDLE_UP_SPRITE.getImageAt(i, 0));
      idleSprites.get(Direction.DOWN).add(PLAYER_IDLE_DOWN_SPRITE.getImageAt(i, 0));
      idleSprites.get(Direction.LEFT).add(PLAYER_IDLE_LEFT_SPRITE.getImageAt(i, 0));
      idleSprites.get(Direction.RIGHT).add(PLAYER_IDLE_RIGHT_SPRITE.getImageAt(i, 0));

      walkSprites.get(Direction.UP).add(PLAYER_WALKUP_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.DOWN).add(PLAYER_WALKDOWN_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.LEFT).add(PLAYER_WALKLEFT_SPRITE.getImageAt(i, 0));
      walkSprites.get(Direction.RIGHT).add(PLAYER_WALKRIGHT_SPRITE.getImageAt(i, 0));
    }

    this.currentFrame = 0;

    this.animationTimer = new Timer(1000 / 10, e -> {
      if (playerModel.mazeModel.isPaused()) {
        return;
      }
      Direction direction = playerModel.getCurrentDirection();
      if (playerModel.isIdle()) {
        currentFrame = (currentFrame + 1) % idleSprites.get(direction).size();
      } else {
        currentFrame = (currentFrame + 1) % walkSprites.get(direction).size();
      }
    });
    this.animationTimer.start();
  }

  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    double MAGNIFICATION = 2.0;

    BufferedImage playerSprite = null;
    Direction direction = playerModel.getCurrentDirection();

    if (playerModel.isIdle()) {
      playerSprite = idleSprites.get(direction).get(currentFrame);
    } else {
      playerSprite = walkSprites.get(direction).get(currentFrame);
    }

    if (playerSprite != null) {
      int imageWidth = playerSprite.getWidth();
      int imageHeight = playerSprite.getHeight();
      int cellSize = mazeView.getMazeCellSize();

      // * 縦横比維持 */
      double scale = Math.min((double) cellSize / imageWidth, (double) cellSize / imageHeight) * MAGNIFICATION;

      int scaleWidth = (int) (imageWidth * scale);
      int scaleHeight = (int) (imageHeight * scale);

      int xOffset = (cellSize - scaleWidth) / 2;
      int yOffset = (cellSize - scaleHeight) / 2;

      int mazeX = (int) ((playerModel.mazeModel.getMazeWidth() - 1) * mazeView.getMazeCellSize() / 2);
      int mazeY = (int) ((playerModel.mazeModel.getMazeHeight() - 1) * mazeView.getMazeCellSize() / 2);

      g2d.drawImage(playerSprite,
          mazeX + xOffset,
          mazeY + yOffset,
          scaleWidth,
          scaleHeight,
          null);
    }
  }
}
