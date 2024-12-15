import java.awt.*;
// import java.util.logging.Logger;
import javax.swing.*;

/**
 * 迷路の盤面を描画するView
 */
public class MazeView extends JPanel implements Observer {
  protected MazeModel mazeModel;
  protected PlayerModel playerModel;

  public MazeView(MazeModel mazeModel, PlayerModel playerModel) {
    this.mazeModel = mazeModel;
    this.playerModel = playerModel;

    this.setBackground(Color.white);
    this.setPreferredSize(
      new Dimension(
        MazeModel.MAZE_CELL_SIZE * mazeModel.getMazeWidth(),
        MazeModel.MAZE_CELL_SIZE * mazeModel.getMazeHeight()
      )
    );
    this.setFocusable(true);
    // model = m;
    mazeModel.addObserver(this);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    //* 迷路を描画 */
    MazeElement elements[][] = mazeModel.getElements();
    for (int x = 0; x < mazeModel.getMazeWidth(); x++) {
      for (int y = 0; y < mazeModel.getMazeHeight(); y++) {
        elements[x][y].draw(g, x * MazeModel.MAZE_CELL_SIZE, y * MazeModel.MAZE_CELL_SIZE, MazeModel.MAZE_CELL_SIZE);
        //* 重いので一旦無効化 */
        // Logger.getAnonymousLogger().info("" + elements[x][y].canEnter());
      }
    }


    //* Playerを描画(仮) */
    g.setColor(Color.RED);
    g.fillOval(
      playerModel.getPlayerX() * MazeModel.MAZE_CELL_SIZE,
      playerModel.getPlayerY() * MazeModel.MAZE_CELL_SIZE,
      MazeModel.MAZE_CELL_SIZE,
      MazeModel.MAZE_CELL_SIZE
      );
  }

  public void update(Observable o, Object arg) {
    repaint();
  }
}
