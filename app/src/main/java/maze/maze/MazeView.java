package maze.maze;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import maze.util.*;
import maze.window.AppWindow;
import maze.maze.element.*;
import maze.maze.player.*;

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

    this.setBackground(Color.yellow);
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
    setSize(new Dimension(AppWindow.getScreenWidth(), AppWindow.getScreenHeight()));
    setPreferredSize(getSize());
    revalidate();
  }

  public int getMazeCellSize() {
    int x = Math.clamp(AppWindow.getScreenWidth() / mazeModel.getMazeWidth(), 0, 128);
    int y = Math.clamp(AppWindow.getScreenHeight() / mazeModel.getMazeHeight(), 0, 128);
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

  public void update(Observable o, Object arg) {
    repaint();
  }
}
