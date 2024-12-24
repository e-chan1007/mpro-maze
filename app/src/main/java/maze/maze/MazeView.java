package maze.maze;

import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import maze.maze.element.MazeElement;
import maze.maze.player.PlayerModel;
import maze.maze.player.PlayerView;
import maze.util.Observable;
import maze.util.Observer;
import maze.window.AppWindow;

/**
 * 迷路の盤面を描画するView
 */
public class MazeView extends JPanel implements Observer {
  protected MazeModel mazeModel;
  protected PlayerModel playerModel;
  protected PlayerView playerView;

  public MazeView(MazeModel mazeModel, PlayerModel playerModel) {
    this.mazeModel = mazeModel;
    this.playerModel = playerModel;
    this.playerView = new PlayerView(playerModel, this);

    this.setFocusable(true);
    this.requestFocusInWindow();
    mazeModel.addObserver(this);
    mazeModel.setView(this);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateSize();
      }
    });
    updateSize();
  }

  private void updateSize() {
    int cellSize = getMazeCellSize();
    setBounds(AppWindow.getInnerWidth() / 2 - cellSize * mazeModel.getMazeWidth() / 2,
        AppWindow.getInnerHeight() / 2 - cellSize * mazeModel.getMazeHeight() / 2,
        cellSize * mazeModel.getMazeWidth(), cellSize * mazeModel.getMazeHeight());
  }

  public int getMazeCellSize() {
    int x = Math.clamp(AppWindow.getInnerWidth() / mazeModel.getMazeWidth(), 0, 128);
    int y = Math.clamp(AppWindow.getInnerHeight() / mazeModel.getMazeHeight(), 0, 128);
    return Math.min(x, y);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // * 迷路を描画 */
    MazeElement elements[][] = mazeModel.getElements();
    for (int x = 0; x < mazeModel.getMazeWidth(); x++) {
      for (int y = 0; y < mazeModel.getMazeHeight(); y++) {
        elements[x][y].draw(g, x * getMazeCellSize(), y * getMazeCellSize(), getMazeCellSize());
      }
    }

    playerView.draw(g);
  }

  @Override
  public void update(Observable o, Object arg) {
    repaint();
  }
}
