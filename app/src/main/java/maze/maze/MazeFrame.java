package maze.maze;

import java.awt.*;
import javax.swing.*;

import maze.maze.player.*;

/**
 * 迷路の盤面を表示するJFrame
 * @see MazeView
 */
public class MazeFrame extends JFrame {
  public MazeFrame() {
    MazeModel mazeModel = new MazeModel();
    PlayerModel playerModel = new PlayerModel(mazeModel);

    MazeView mazeView = new MazeView(mazeModel, playerModel);
    PlayerController playerController = new PlayerController(playerModel);

    playerModel.addObserver(mazeView);

    mazeView.addKeyListener(playerController);
    mazeView.setFocusable(true);
    mazeView.requestFocusInWindow();

    this.setBackground(Color.black);
    this.setTitle("Maze");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);

    this.add(mazeView);
    this.pack();
    this.setVisible(true);
  }
}