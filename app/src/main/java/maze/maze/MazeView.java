package maze.maze;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import maze.maze.element.MazeElement;
import maze.maze.player.PlayerModel;
import maze.maze.player.PlayerView;
import maze.maze.tagger.TaggerModel;
import maze.maze.tagger.TaggerView;
import maze.util.Observable;
import maze.util.Observer;
import maze.window.AppWindow;

/**
 * 迷路の盤面を描画するView
 */
public class MazeView extends JPanel implements Observer {
  private final MazeModel mazeModel;
  private final PlayerModel playerModel;
  private final PlayerView playerView;
  private final TaggerModel taggerModel;
  private final TaggerView taggerView;

  private int anchorX = 0;
  private int anchorY = 0;

  public MazeView(MazeModel mazeModel, PlayerModel playerModel, TaggerModel taggerModel) {
    this.mazeModel = mazeModel;

    this.playerModel = playerModel;
    this.mazeModel.readFile("/test.txt");
    this.playerView = new PlayerView(playerModel, this);
    this.taggerModel = taggerModel;
    this.taggerView = new TaggerView(taggerModel, this);
    setBackground(new Color(0x25, 0x13, 0x1A));

    this.setFocusable(true);
    this.requestFocusInWindow();
    mazeModel.addObserver(this);
    playerModel.addObserver(this);
    mazeModel.setView(this);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateSize();
      }
    });
    updateSize();
    updateAnchor();
  }

  private void updateSize() {
    int cellSize = getMazeCellSize();
    setBounds(AppWindow.getInnerWidth() / 2 - cellSize * mazeModel.getMazeWidth() / 2,
        AppWindow.getInnerHeight() / 2 - cellSize * mazeModel.getMazeHeight() / 2,
        cellSize * mazeModel.getMazeWidth(), cellSize * mazeModel.getMazeHeight());
  }

  public int getMazeCellSize() {
    int fovSize = 8;
    int x = Math.clamp(AppWindow.getInnerWidth() / fovSize, 0, 256);
    int y = Math.clamp(AppWindow.getInnerHeight() / fovSize, 0, 256);
    return Math.min(x, y);
  }

  private void drawHitPoints(Graphics g) {
    int cellSize = getMazeCellSize();
    int x = (int)(playerModel.getPlayerX() * cellSize + anchorX);
    int y = (int)(playerModel.getPlayerY() * cellSize + anchorY - cellSize * 0.75);
    
    g.setColor(Color.WHITE);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // * 迷路を描画 */
    MazeElement elements[][] = mazeModel.getElements();
    for (int x = 0; x < mazeModel.getMazeWidth(); x++) {
      for (int y = 0; y < mazeModel.getMazeHeight(); y++) {
        elements[x][y].draw(g, anchorX + x * getMazeCellSize(), anchorY + y * getMazeCellSize(), getMazeCellSize());
      }
    }

    playerView.draw(g);
    taggerView.draw(g, anchorX, anchorY);
    drawHitPoints(g);

    MazeFogView.draw(g, getWidth(), getHeight());
  }

  private void updateAnchor() {
    anchorX = (int) (((mazeModel.getMazeWidth() - 1) * getMazeCellSize()) / 2
        - playerModel.getPlayerX() * getMazeCellSize());
    anchorY = (int) (((mazeModel.getMazeHeight() - 1) * getMazeCellSize()) / 2
        - playerModel.getPlayerY() * getMazeCellSize());
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o == playerModel) {
      updateAnchor();
      repaint();
    }
  }
}
