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
import maze.assets.ImageManager;

public class PlayerView {
  private PlayerModel playerModel;
  private MazeView mazeView;
  private List<BufferedImage> playerIdleSprite;
  private int currentFrame;
  private Timer idleAnimeTimer;

  public PlayerView(PlayerModel playerModel, MazeView mazeView) {
    this.playerModel = playerModel;
    this.mazeView = mazeView;

    playerIdleSprite = new ArrayList<>();
    playerIdleSprite.add(ImageManager.PLAYER_IDLE_SPRITE.getImageAt(0, 0));
    for (int i = 0; i < 8; i++) {
      playerIdleSprite.add(ImageManager.PLAYER_IDLE_SPRITE.getImageAt(i, 0));
    }

    this.currentFrame = 0;

    this.idleAnimeTimer = new Timer(1000 / 10, e -> {
      if (playerModel.mazeModel.isPaused()) {
        return;
      }
      currentFrame = (currentFrame + 1) % playerIdleSprite.size();
      mazeView.repaint();
    });
    this.idleAnimeTimer.start();
  }

  public void draw(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    double RATIO = 2.0;

    if (playerModel.isIdle()) {
      BufferedImage playerIdle = playerIdleSprite.get(currentFrame);
      int imageWidth = playerIdle.getWidth();
      int imageHeight = playerIdle.getHeight();
      int cellSize = mazeView.getMazeCellSize();

      // * 縦横比維持 */
      double scale = Math.min((double) cellSize / imageWidth, (double) cellSize / imageHeight) * RATIO;

      int scaleWidth = (int) (imageWidth * scale);
      int scaleHeight = (int) (imageHeight * scale);

      int xOffset = (cellSize - scaleWidth) / 2;
      int yOffset = (cellSize - scaleHeight) / 2;

      int mazeX = (int) ((playerModel.mazeModel.getMazeWidth() - 0.5f) * mazeView.getMazeCellSize() / 2);
      int mazeY = (int) ((playerModel.mazeModel.getMazeHeight() - 0.5f) * mazeView.getMazeCellSize() / 2);

      g2d.drawImage(playerIdle,
          mazeX + xOffset,
          mazeY + yOffset,
          scaleWidth,
          scaleHeight,
          null);
    } else {
      g2d.fill(new Ellipse2D.Float(
          (playerModel.mazeModel.getMazeWidth() - 0.5f) * mazeView.getMazeCellSize() / 2,
          (playerModel.mazeModel.getMazeHeight() - 0.5f) * mazeView.getMazeCellSize() / 2,
          mazeView.getMazeCellSize(),
          mazeView.getMazeCellSize()));
    }
    // g2d.fill(new Ellipse2D.Float(playerModel.getPlayerX() *
    // mazeView.getMazeCellSize(),
    // playerModel.getPlayerY() * mazeView.getMazeCellSize(),
    // mazeView.getMazeCellSize(),
    // mazeView.getMazeCellSize()));
  }
}
