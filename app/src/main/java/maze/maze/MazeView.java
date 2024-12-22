package maze.maze;

import java.awt.*;
import javax.swing.*;
import maze.util.*;
import maze.maze.element.*;
import maze.maze.player.*;
import maze.maze.tagger.*;

/**
 * 迷路の盤面を描画するView
 */
public class MazeView extends JPanel implements Observer {
  protected MazeModel mazeModel;
  protected PlayerModel playerModel;
  protected PlayerView playerView;
  protected TaggerModel taggerModel;
  protected TaggerView taggerView;

  public MazeView(MazeModel mazeModel, PlayerModel playerModel, TaggerModel taggerModel) {
    this.mazeModel = mazeModel;

    this.playerModel = playerModel;
    this.playerView = new PlayerView(playerModel);

    this.taggerModel = taggerModel;
    this.taggerView = new TaggerView(taggerModel);

    this.setBackground(Color.white);
    this.setPreferredSize(
        new Dimension(
            MazeModel.MAZE_CELL_SIZE * mazeModel.getMazeWidth(),
            MazeModel.MAZE_CELL_SIZE * mazeModel.getMazeHeight()));
    this.setFocusable(true);
    mazeModel.addObserver(this);
    mazeModel.setView(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // * 迷路を描画 */
    MazeElement elements[][] = mazeModel.getElements();
    for (int x = 0; x < mazeModel.getMazeWidth(); x++) {
      for (int y = 0; y < mazeModel.getMazeHeight(); y++) {
        elements[x][y].draw(g, x * MazeModel.MAZE_CELL_SIZE, y * MazeModel.MAZE_CELL_SIZE, MazeModel.MAZE_CELL_SIZE);
      }
    }

    playerView.draw(g);
    taggerView.draw(g);
  }

  public void update(Observable o, Object arg) {
    repaint();
  }
}
