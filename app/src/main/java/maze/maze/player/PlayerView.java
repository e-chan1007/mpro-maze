package maze.maze.player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.List;
import java.util.ArrayList;
import javax.swing.Timer;

import maze.maze.MazeView;
import maze.maze.player.PlayerModel.Direction;
import maze.assets.ImageManager;

public class PlayerView {
  private PlayerModel playerModel;
  private MazeView mazeView;

  private List<BufferedImage> playerIdleUpSprite;
  private List<BufferedImage> playerIdleBackSprite;
  private List<BufferedImage> playerIdleLeftSprite;
  private List<BufferedImage> playerIdleRightSprite;
  private List<BufferedImage> playerWalkUp;
  private List<BufferedImage> playerWalkDown;
  private List<BufferedImage> playerWalkLeft;
  private List<BufferedImage> playerWalkRight;

  private int currentFrame;
  private Timer AnimationTimer;

  public PlayerView(PlayerModel playerModel, MazeView mazeView) {
    this.playerModel = playerModel;
    this.mazeView = mazeView;

    playerIdleUpSprite = new ArrayList<>();
    playerIdleBackSprite = new ArrayList<>();
    playerIdleLeftSprite = new ArrayList<>();
    playerIdleRightSprite = new ArrayList<>();
    playerWalkUp = new ArrayList<>();
    playerWalkDown = new ArrayList<>();
    playerWalkLeft = new ArrayList<>();
    playerWalkRight = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      playerIdleUpSprite.add(ImageManager.PLAYER_IDLE_UP_SPRITE.getImageAt(i, 0));
      playerIdleBackSprite.add(ImageManager.PLAYER_IDLE_DOWN_SPRITE.getImageAt(i, 0));
      playerIdleLeftSprite.add(ImageManager.PLAYER_IDLE_LEFT_SPRITE.getImageAt(i, 0));
      playerIdleRightSprite.add(ImageManager.PLAYER_IDLE_RIGHT_SPRITE.getImageAt(i, 0));
      playerWalkUp.add(ImageManager.PLAYER_WALKUP_SPRITE.getImageAt(i, 0));
      playerWalkDown.add(ImageManager.PLAYER_WALKDOWN_SPRITE.getImageAt(i, 0));
      playerWalkLeft.add(ImageManager.PLAYER_WALKLEFT_SPRITE.getImageAt(i, 0));
      playerWalkRight.add(ImageManager.PLAYER_WALKRIGHT_SPRITE.getImageAt(i, 0));
    }

    this.currentFrame = 0;

    this.AnimationTimer = new Timer(1000 / 10, e -> {
      if (playerModel.mazeModel.isPaused()) {
        return;
      }
      if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.FOWARD) {
        currentFrame = (currentFrame + 1) % playerIdleUpSprite.size();
      } else if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.BACK) {
        currentFrame = (currentFrame + 1) % playerIdleBackSprite.size();
      } else if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.LEFT) {
        currentFrame = (currentFrame + 1) % playerIdleLeftSprite.size();
      } else if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.RIGHT) {
        currentFrame = (currentFrame + 1) % playerIdleRightSprite.size();
      } else if (playerModel.isWalkingUp()) {
        currentFrame = (currentFrame + 1) % playerWalkUp.size();
      } else if (playerModel.isWalkingDown()) {
        currentFrame = (currentFrame + 1) % playerWalkDown.size();
      } else if (playerModel.isWalkingLeft()) {
        currentFrame = (currentFrame + 1) % playerWalkLeft.size();
      } else if (playerModel.isWalkingRight()) {
        currentFrame = (currentFrame + 1) % playerWalkRight.size();
      }
      mazeView.repaint();
    });
    this.AnimationTimer.start();
  }

  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    double RATIO = 2.0;

    BufferedImage playerSprite = null;

    if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.FOWARD) {
      playerSprite = playerIdleUpSprite.get(currentFrame);
    } else if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.BACK) {
      playerSprite = playerIdleBackSprite.get(currentFrame);
    } else if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.LEFT) {
      playerSprite = playerIdleLeftSprite.get(currentFrame);
    } else if (playerModel.isIdle() && playerModel.getCurrentDirection() == Direction.RIGHT) {
      playerSprite = playerIdleRightSprite.get(currentFrame);
    } else if (playerModel.isWalkingUp()) {
      playerSprite = playerWalkUp.get(currentFrame);
    } else if (playerModel.isWalkingDown()) {
      playerSprite = playerWalkDown.get(currentFrame);
    } else if (playerModel.isWalkingLeft()) {
      playerSprite = playerWalkLeft.get(currentFrame);
    } else if (playerModel.isWalkingRight()) {
      playerSprite = playerWalkRight.get(currentFrame);
    }

    if (playerSprite != null) {
      int imageWidth = playerSprite.getWidth();
      int imageHeight = playerSprite.getHeight();
      int cellSize = mazeView.getMazeCellSize();

      // * 縦横比維持 */
      double scale = Math.min((double) cellSize / imageWidth, (double) cellSize / imageHeight) * RATIO;

      int scaleWidth = (int) (imageWidth * scale);
      int scaleHeight = (int) (imageHeight * scale);

      int xOffset = (cellSize - scaleWidth) / 2;
      int yOffset = (cellSize - scaleHeight) / 2;

      int mazeX = (int) ((playerModel.mazeModel.getMazeWidth() - 0.5f) * mazeView.getMazeCellSize() / 2);
      int mazeY = (int) ((playerModel.mazeModel.getMazeHeight() - 0.5f) * mazeView.getMazeCellSize() / 2);

      g2d.drawImage(playerSprite,
          mazeX + xOffset,
          mazeY + yOffset,
          scaleWidth,
          scaleHeight,
          null);
    }
  }
}
